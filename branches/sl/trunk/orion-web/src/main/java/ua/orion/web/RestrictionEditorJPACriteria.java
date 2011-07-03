package ua.orion.web;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Составитель выражений по правилам javax.persistence.criteria.CriteriaBuilder.
 * Итоговое выражение - javax.persistence.criteria.CriteriaQuery
 * @author Gennadiy Dobrovolsky
 */
// TODO RestrictionEditorHibernateCriteria надо потестировать
public class RestrictionEditorJPACriteria<T> implements RestrictionEditorInterface<CriteriaQuery<T>> {

    /**
     * Выражение в универсальном формате, записанное в обратной бесскобочной форме
     */
    private ArrayList expression;
    /**
     * класс сущности ORM
     */
    private Class<T> forClass;
    private EntityManager em;
    private CriteriaBuilder cb;
    /**
     * Объект, содержащий всю информацию, необходимую для выборки данных
     */
    private CriteriaQuery<T> criteriaQuery;
    private Root<T> criteriaRoot;

    /**
     * Конструктор
     * @param _forClass класс сущности ORM
     * @param entityManager EntityManager
     */
    public RestrictionEditorJPACriteria(Class _forClass, EntityManager entityManager) {
        this.forClass = _forClass;
        this.em = entityManager;
        cb = em.getCriteriaBuilder();
        this.createEmpty();
    }

    /**
     * Создание нового выражения
     */
    @Override
    public void createEmpty() {
        this.expression = new ArrayList();
        this.criteriaQuery = cb.createQuery(forClass);
        criteriaRoot = criteriaQuery.from(forClass);
    }

    //
    // Классы - части выражения
    //
    /**
     * Название атрибута
     */
    private class Attribute {

        public String attributeName;

        Attribute(String _attributeName) {
            this.attributeName = _attributeName;
        }

        @Override
        public String toString() {
            return this.attributeName;
        }
    }

    /**
     * Список констант
     */
    private class ListOfValues {

        public Object[] values;

        ListOfValues(Object... _values) {
            this.values = _values;
        }
    }

    /**
     * Одна константа
     */
    private class OneValue {

        public Object value;

        OneValue(Object _value) {
            this.value = _value;
        }
    }

    // =========================================================================
    // Добавление элементарных частей в выражение - начало
    /**
     * Добавляем название атрибута
     */
    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> constField(String value) {
        if (value.contains(".")) {
            String fld = value.replaceAll("\\.\\w+$", "");
            //TODO persistence-2_0-final-spec.pdf page 274
            //this.criteriaQuery = criteriaQuery.createAlias(fld, fld);
            this.expression.add(new RestrictionEditorJPACriteria.Attribute(value));
        } else {
            this.expression.add(new RestrictionEditorJPACriteria.Attribute(value));
        }
        return this;
    }

    /**
     * Добавляем в выражение константу
     */
    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> constValue(Object value) {
        this.expression.add(new RestrictionEditorJPACriteria.OneValue(value));
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> constValueList(Object... value) {
        this.expression.add(new RestrictionEditorJPACriteria.ListOfValues(value));
        return this;
    }

    // Добавление элементарных частей в выражение - конец
    // =========================================================================
    // =========================================================================
    // определяем операторы - начало
    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> and() {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();
        this.expression.add(Restrictions.and((Criterion) op1, (Criterion) op2));
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> or() {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();
        this.expression.add(Restrictions.or((Criterion) op1, (Criterion) op2));
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> not() {
        Object op1;
        op1 = this.pop();
        this.expression.add(Restrictions.not((Criterion) op1));
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> gt() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 > op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(Restrictions.gt(op1.toString(), ((OneValue) op2).value));
            return this;
        }

        //  op1 - константа и  op2 - атрибут => программируем как op2 < op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.lt(op2.toString(), ((OneValue) op1).value));
            return this;
        }

        //  op1 - атрибут    и  op2 - атрибут => программируем как op1 > op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.gtProperty(op1.toString(), op2.toString()));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> ge() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();


        //  op1 - атрибут    и  op2 - константа => программируем как op1 >= op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(Restrictions.ge(op1.toString(), ((OneValue) op2).value));
            return this;
        }

        //  op1 - константа и  op2 - атрибут => программируем как op2 <= op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.le(op2.toString(), ((OneValue) op1).value));
            return this;
        }
        //  op1 - атрибут    и  op2 - атрибут => программируем как op1 >= op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.geProperty(op1.toString(), op2.toString()));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> lt() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 < op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(Restrictions.lt(op1.toString(), ((OneValue) op2).value));
            return this;
        }
        //  op1 - константа и  op2 - атрибут => программируем как op2 > op1
        if (isOneValue(op1) && isAttribute(op1)) {
            this.expression.add(Restrictions.gt(op2.toString(), ((OneValue) op1).value));
            return this;
        }
        //  op1 - атрибут и op2 - атрибут => программируем как op1 < op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.ltProperty(op1.toString(), op2.toString()));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> le() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        //  op1 - атрибут    и  op2 - константа =>  программируем как op1 <= op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(Restrictions.le(op1.toString(), ((OneValue) op2).value));
            return this;
        }
        // op1 - константа и op2 - атрибут => программируем как op2 >= op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.ge(op2.toString(), ((OneValue) op1).value));
            return this;
        }
        // op1 - атрибут и op2 - атрибут => программируем как op1 <= op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.leProperty(op1.toString(), op2.toString()));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> eq() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        // op1 - атрибут и op2 - константа => программируем как op1 == op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(Restrictions.eq(op1.toString(), ((OneValue) op2).value));
            return this;
        }
        // op1 - константа и op2 - атрибут => программируем как op2 == op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.eq(op2.toString(), ((OneValue) op1).value));
            return this;
        }
        // op1 - атрибут и op2 - атрибут=> программируем как op1 == op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.eqProperty(op1.toString(), op2.toString()));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> neq() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 != op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(Restrictions.ne(op1.toString(), ((OneValue) op2).value));
            return this;
        }
        // op1 - константа  и  op2 - атрибут => программируем как op2 != op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.ne(op2.toString(), ((OneValue) op1).value));
            return this;
        }
        // op1 - атрибут и op2 - атрибут=> программируем как op1 == op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.neProperty(op1.toString(), op2.toString()));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> isNull() throws RestrictionEditorException {
        Object op1;
        op1 = this.pop();
        if (isAttribute(op1)) {
            this.expression.add(Restrictions.isNull(op1.toString()));
        } else {
            throw new RestrictionEditorException();
        }
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> isNotNull() throws RestrictionEditorException {
        Object op1;
        op1 = this.pop();
        if (isAttribute(op1)) {
            this.expression.add(Restrictions.isNotNull(op1.toString()));
        } else {
            throw new RestrictionEditorException();
        }
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> like() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 like op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(Restrictions.like(op1.toString(), ((OneValue) op2).value));
            return this;
        }
        //  op1 - константа и  op2 - атрибут => программируем как op2 like op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.like(op2.toString(), ((OneValue) op1).value));
            return this;
        }
        // op1 - атрибут и op2 - атрибут
        // op1 - константа и op2 - константа  - это ошибка
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> ilike() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 like op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(Restrictions.ilike(op1.toString(), ((OneValue) op2).value));
            return this;
        }
        //  op1 - константа и  op2 - атрибут => программируем как op2 like op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.ilike(op2.toString(), ((OneValue) op1).value));
            return this;
        }
        // op1 - атрибут и op2 - атрибут
        // op1 - константа и op2 - константа  - это ошибка
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> in() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        // op1 - атрибут и op2 - cписок констант =>  программируем как op1 in op2
        if (isAttribute(op1) && isListOfValues(op2)) {
            this.expression.add(Restrictions.in(op1.toString(), ((RestrictionEditorJPACriteria.ListOfValues) op2).values));
            return this;
        }

        // op1 - атрибут и op2 - атрибут
        // op1 - константа и op2 - константа  - это ошибка
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> contains() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        // op1 - атрибут и op2 - константа => программируем как op1 like op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(Restrictions.like(op1.toString(), "%" + ((OneValue) op2).value + "%"));
            return this;
        }
        //  op1 - константа и  op2 - атрибут => программируем как op2 like op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(Restrictions.like(op2.toString(), "%" + ((OneValue) op1).value + "%"));
            return this;
        }
        // op1 - атрибут и op2 - атрибут
        // op1 - константа и op2 - константа  - это ошибка
        throw new RestrictionEditorException();
    }
    // определяем операторы - конец
    // =========================================================================
    // =========================================================================
    // Вспомогательные методы - начало

    /**
     * Возвращает последний элемент выражения и удаляет его из выражения
     */
    private Object pop() {
        int size = this.expression.size();
        Object returnMe = this.expression.get(size - 1);
        this.expression.remove(size - 1);
        return returnMe;
    }

    private boolean isAttribute(Object p) {
        return p instanceof RestrictionEditorJPACriteria.Attribute;
    }

    private boolean isOneValue(Object p) {
        return p instanceof RestrictionEditorJPACriteria.OneValue;
    }

    private boolean isListOfValues(Object p) {
        return p instanceof RestrictionEditorJPACriteria.ListOfValues;
    }

    // Вспомогательные методы - конец
    // =========================================================================
    @Override
    public CriteriaQuery<T> getValue() {
        if (this.expression.size() > 0) {
            this.criteriaQuery.add((Criterion) this.expression.get(0));
        }
        return this.criteriaQuery;
    }

    @Override
    public int size() {
        return this.expression.size();
    }
}
