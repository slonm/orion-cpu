package ua.orion.web;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
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
    private boolean isCountQuery;
    private EntityManager em;
    private CriteriaBuilder cb;
    /**
     * Объект, содержащий всю информацию, необходимую для выборки данных
     */
    private CriteriaQuery<T> criteriaQuery;
    private Root<T> root;

    /**
     * Конструктор
     * @param _forClass класс сущности ORM
     * @param _returnClass класс сущности ORM
     * @param entityManager EntityManager
     */
    public RestrictionEditorJPACriteria(Class _forClass, boolean isCountQuery, EntityManager entityManager) {
        this.forClass = _forClass;
        this.isCountQuery = isCountQuery;
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
        if (isCountQuery) {
            this.criteriaQuery = (CriteriaQuery<T>)cb.createQuery(Long.class);
        } else {
            this.criteriaQuery = cb.createQuery(forClass);
        }
        root = criteriaQuery.from(forClass);
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
//            String fld = value.replaceAll("\\.\\w+$", "");
//            select.add(root.get(fld).alias(fld));
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
        Expression<Boolean> op1 = (Expression<Boolean>) this.pop();
        Expression<Boolean> op2 = (Expression<Boolean>) this.pop();
        expression.add(cb.and(op1, op2));
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> or() {
        Expression<Boolean> op1 = (Expression<Boolean>) this.pop();
        Expression<Boolean> op2 = (Expression<Boolean>) this.pop();
        expression.add(cb.or(op1, op2));
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> not() {
        Expression<Boolean> op1 = (Expression<Boolean>) this.pop();
        expression.add(cb.not(op1));
        return this;
    }

    private abstract class NumberComparisionPredicate {

        private Object op1, op2;

        private Expression<? extends Number> toNumberExpr(Object field) {
            return root.get(field.toString());
        }

        private Long toLongValue(Object value) {
            return Long.parseLong(((OneValue) value).value.toString());
        }

        NumberComparisionPredicate() throws RestrictionEditorException {
            op2 = pop();
            op1 = pop();
            //  op1 - атрибут    и  op2 - константа => программируем как op1 > op2
            if (isAttribute(op1) && isOneValue(op2)) {
                expression.add(newPredicate(toNumberExpr(op1), toLongValue(op2)));
            } //  op1 - константа и  op2 - атрибут => программируем как op2 < op1
            else if (isOneValue(op1) && isAttribute(op2)) {
                expression.add(newPredicate(toNumberExpr(op2), toLongValue(op1)));
            } //  op1 - атрибут    и  op2 - атрибут => программируем как op1 > op2
            else if (isAttribute(op1) && isAttribute(op2)) {
                expression.add(newPredicate(toNumberExpr(op1), toNumberExpr(op2)));
            } // случай op1 - константа и op2 - константа  является ошибкой
            else {
                throw new RestrictionEditorException();
            }
        }

        abstract Predicate newPredicate(Expression<? extends Number> expression, Long constant);

        abstract Predicate newPredicate(Long constant, Expression<? extends Number> expression);

        abstract Predicate newPredicate(Expression<? extends Number> expression1, Expression<? extends Number> expression2);
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> gt() throws RestrictionEditorException {

        new NumberComparisionPredicate() {

            @Override
            Predicate newPredicate(Expression<? extends Number> expression, Long constant) {
                return cb.gt(expression, constant);
            }

            @Override
            Predicate newPredicate(Long constant, Expression<? extends Number> expression) {
                return cb.lt(expression, constant);
            }

            @Override
            Predicate newPredicate(Expression<? extends Number> expression1, Expression<? extends Number> expression2) {
                return cb.gt(expression1, expression2);
            }
        };
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> ge() throws RestrictionEditorException {

        new NumberComparisionPredicate() {

            @Override
            Predicate newPredicate(Expression<? extends Number> expression, Long constant) {
                return cb.ge(expression, constant);
            }

            @Override
            Predicate newPredicate(Long constant, Expression<? extends Number> expression) {
                return cb.le(expression, constant);
            }

            @Override
            Predicate newPredicate(Expression<? extends Number> expression1, Expression<? extends Number> expression2) {
                return cb.ge(expression1, expression2);
            }
        };
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> lt() throws RestrictionEditorException {

        new NumberComparisionPredicate() {

            @Override
            Predicate newPredicate(Expression<? extends Number> expression, Long constant) {
                return cb.lt(expression, constant);
            }

            @Override
            Predicate newPredicate(Long constant, Expression<? extends Number> expression) {
                return cb.gt(expression, constant);
            }

            @Override
            Predicate newPredicate(Expression<? extends Number> expression1, Expression<? extends Number> expression2) {
                return cb.lt(expression1, expression2);
            }
        };
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> le() throws RestrictionEditorException {

        new NumberComparisionPredicate() {

            @Override
            Predicate newPredicate(Expression<? extends Number> expression, Long constant) {
                return cb.le(expression, constant);
            }

            @Override
            Predicate newPredicate(Long constant, Expression<? extends Number> expression) {
                return cb.ge(expression, constant);
            }

            @Override
            Predicate newPredicate(Expression<? extends Number> expression1, Expression<? extends Number> expression2) {
                return cb.le(expression1, expression2);
            }
        };
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery<T>> eq() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 != op2
        if (isAttribute(op1) && isOneValue(op2)) {
            expression.add(cb.equal(root.get(op1.toString()), ((OneValue) op2).value));
            return this;
        }
        // op1 - константа  и  op2 - атрибут => программируем как op2 != op1
        if (isOneValue(op1) && isAttribute(op2)) {
            expression.add(cb.equal(root.get(op2.toString()), ((OneValue) op1).value));
            return this;
        }
        // op1 - атрибут и op2 - атрибут=> программируем как op1 == op2
        if (isAttribute(op1) && isAttribute(op2)) {
            expression.add(cb.equal(root.get(op1.toString()), root.get(op2.toString())));
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
            expression.add(cb.notEqual(root.get(op1.toString()), ((OneValue) op2).value));
            return this;
        }
        // op1 - константа  и  op2 - атрибут => программируем как op2 != op1
        if (isOneValue(op1) && isAttribute(op2)) {
            expression.add(cb.notEqual(root.get(op2.toString()), ((OneValue) op1).value));
            return this;
        }
        // op1 - атрибут и op2 - атрибут=> программируем как op1 == op2
        if (isAttribute(op1) && isAttribute(op2)) {
            expression.add(cb.notEqual(root.get(op1.toString()), root.get(op2.toString())));
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
            expression.add(cb.isNull(root.get(op1.toString())));
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
            expression.add(cb.isNotNull(root.get(op1.toString())));
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
            expression.add(cb.like((Expression<String>) (Object) root.get(op1.toString()), ((OneValue) op2).value.toString()));
            return this;
        }
        //  op1 - константа и  op2 - атрибут => программируем как op2 like op1
        if (isOneValue(op1) && isAttribute(op2)) {
            expression.add(cb.like((Expression<String>) (Object) root.get(op2.toString()), ((OneValue) op1).value.toString()));
            return this;
        }
        // op1 - атрибут и op2 - атрибут
        // op1 - константа и op2 - константа  - это ошибка
        throw new RestrictionEditorException();
    }

    @Override
    //TODO Case insensitive
    public RestrictionEditorInterface<CriteriaQuery<T>> ilike() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 like op2
        if (isAttribute(op1) && isOneValue(op2)) {
            expression.add(cb.like((Expression<String>) (Object) root.get(op1.toString()), ((OneValue) op2).value.toString()));
            return this;
        }
        //  op1 - константа и  op2 - атрибут => программируем как op2 like op1
        if (isOneValue(op1) && isAttribute(op2)) {
            expression.add(cb.like((Expression<String>) (Object) root.get(op2.toString()), ((OneValue) op1).value.toString()));
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
            In<Object> in = cb.in(root.get(op1.toString()));
            for (Object v : (((RestrictionEditorJPACriteria.ListOfValues) op2).values)) {
                in.value(v);
            }
            expression.add(in);
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
            expression.add(cb.like((Expression<String>) (Object) root.get(op1.toString()), "%" + ((OneValue) op2).value.toString() + "%"));
            return this;
        }
        //  op1 - константа и  op2 - атрибут => программируем как op2 like op1
        if (isOneValue(op1) && isAttribute(op2)) {
            expression.add(cb.like((Expression<String>) (Object) root.get(op2.toString()), "%" + ((OneValue) op1).value.toString() + "%"));
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
        int size = expression.size();
        Object returnMe = expression.get(size - 1);
        expression.remove(size - 1);
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
        if (expression.size() > 0) {
            this.criteriaQuery.where((Predicate) this.expression.get(0));
        }
        if (isCountQuery) {
            return (CriteriaQuery<T>)((CriteriaQuery<Long>) criteriaQuery).select(cb.count(root));
        } else {
            return this.criteriaQuery.select(root);
        }
    }
    
    public CriteriaQuery<Long> getValueCount() {
        return (CriteriaQuery<Long>)getValue();
    }

    public Root<T> getRoot() {
        return root;
    }
    
    @Override
    public int size() {
        return this.expression.size();
    }
}
