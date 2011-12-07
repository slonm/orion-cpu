package orion.tapestry.grid.lib.restrictioneditor;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Составитель выражений по правилам JPA Criteria API.
 * Итоговое выражение - просто строка
 * @author Gennadiy Dobrovolsky
 */
public class RestrictionEditorJPACriteria implements RestrictionEditorInterface<CriteriaQuery> {

    /**
     * Выражение в универсальном формате, записанное в обратной бесскобочной форме
     */
    private ArrayList expression;
    /**
     * класс сущности ORM
     */
    private Class forClass;
    /**
     * подключение к базе данных
     */
    private EntityManager entityManager;
    /**
     * редактор запроса
     */
    CriteriaBuilder criteriaBuilder;
    /**
     * запрос
     */
    private CriteriaQuery query;
    /**
     * корневая сущность запроса
     * эквивалент выражения FROM
     * в запросе SQL
     */
    Root from;

    /**
     * Конструктор
     * @param _forClass класс сущности ORM
     * @param _entityManager сессия Hibernate, подключение к базе данных
     */
    public RestrictionEditorJPACriteria(Class _forClass, EntityManager _entityManager) {
        this.forClass = _forClass;
        this.entityManager = _entityManager;
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
        this.createEmpty();
    }

    /**
     * Создание нового выражения
     */
    @Override
    public void createEmpty() {
        this.expression = new ArrayList();

        // запрос
        this.query = this.criteriaBuilder.createQuery(this.forClass);

        // описание сущности
        this.from = this.query.from(this.forClass);

        // запрос типа select
        query.select(from);
    }

    /**
     * Метод вычисляет окончательное выражение
     * @return выражение для фильтра,
     * которое можно напрямую использовать при выборке строк
     * @throws RestrictionEditorException ошибка, возникшая от невозможности составить выражение
     */
    @Override
    public CriteriaQuery getValue() {
        if (this.expression.size() > 0) {
            this.query.where((Predicate) this.expression.get(0));
        }
        return this.query;
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
     * Добавляем в выражение название атрибута
     */
    @Override
    public RestrictionEditorInterface<CriteriaQuery> constField(String value) {
        // TODO добавить условие на связанную сущность
        //if (value.contains(".")) {
        //    String fld = value.replaceAll("\\.\\w+$", "");
        //    this.criteria = this.criteria.createAlias(fld, fld);
        //    this.expression.add(new RestrictionEditorJPACriteria.Attribute(value));
        //} else {
        this.expression.add(new RestrictionEditorJPACriteria.Attribute(value));
        //}
        return this;
    }

    /**
     * Добавляем в выражение константу
     */
    @Override
    public RestrictionEditorInterface<CriteriaQuery> constValue(Object value) {
        this.expression.add(new RestrictionEditorJPACriteria.OneValue(value));
        return this;
    }

    /**
     * Добавляем в выражение список констант
     */
    @Override
    public RestrictionEditorInterface<CriteriaQuery> constValueList(Object... value) {
        this.expression.add(new RestrictionEditorJPACriteria.ListOfValues(value));
        return this;
    }

    // Добавление элементарных частей в выражение - конец
    // =========================================================================
    // =========================================================================
    // определяем операторы - начало
    @Override
    public RestrictionEditorInterface<CriteriaQuery> eq() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        // op1 - атрибут и op2 - константа => программируем как op1 == op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(this.criteriaBuilder.equal(from.get(op1.toString()), ((OneValue) op2).value));
            return this;
        }
        // op1 - константа и op2 - атрибут => программируем как op2 == op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(this.criteriaBuilder.equal(from.get(op2.toString()), ((OneValue) op1).value));
            return this;
        }
        // op1 - атрибут и op2 - атрибут=> программируем как op1 == op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(this.criteriaBuilder.equal(from.get(op1.toString()), from.get(op2.toString())));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> gt() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 > op2
        if (isAttribute(op1) && isOneValue(op2)) {
            if (((OneValue) op2).value instanceof Comparable) {
                this.expression.add(this.criteriaBuilder.greaterThan(from.get(op1.toString()), (Comparable) ((OneValue) op2).value));
                return this;
            } else {
                Object ob = ((OneValue) op2).value;
                throw new RestrictionEditorException(ob.toString() + " (type=" + ob.getClass().getName() + ") should be instance of Comparable");
            }
        }

        //  op1 - константа и  op2 - атрибут => программируем как op2 < op1
        if (isOneValue(op1) && isAttribute(op2)) {
            if (((OneValue) op1).value instanceof Comparable) {
                this.expression.add(this.criteriaBuilder.lessThan(from.get(op2.toString()), (Comparable) ((OneValue) op1).value));
                return this;
            } else {
                Object ob = ((OneValue) op1).value;
                throw new RestrictionEditorException(ob.toString() + " (type=" + ob.getClass().getName() + ") should be instance of Comparable");
            }
        }

        //  op1 - атрибут    и  op2 - атрибут => программируем как op1 > op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(this.criteriaBuilder.greaterThan(from.get(op1.toString()), from.get(op2.toString())));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> ge() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 > op2
        if (isAttribute(op1) && isOneValue(op2)) {
            if (((OneValue) op2).value instanceof Comparable) {
                this.expression.add(this.criteriaBuilder.greaterThanOrEqualTo(from.get(op1.toString()), (Comparable) ((OneValue) op2).value));
                return this;
            } else {
                Object ob = ((OneValue) op2).value;
                throw new RestrictionEditorException(ob.toString() + " (type=" + ob.getClass().getName() + ") should be instance of Comparable");
            }
        }

        //  op1 - константа и  op2 - атрибут => программируем как op2 < op1
        if (isOneValue(op1) && isAttribute(op2)) {
            if (((OneValue) op1).value instanceof Comparable) {
                this.expression.add(this.criteriaBuilder.lessThanOrEqualTo(from.get(op2.toString()), (Comparable) ((OneValue) op1).value));
                return this;
            } else {
                Object ob = ((OneValue) op1).value;
                throw new RestrictionEditorException(ob.toString() + " (type=" + ob.getClass().getName() + ") should be instance of Comparable");
            }
        }

        //  op1 - атрибут    и  op2 - атрибут => программируем как op1 > op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(this.criteriaBuilder.greaterThanOrEqualTo(from.get(op1.toString()), from.get(op2.toString())));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> lt() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 < op2
        if (isAttribute(op1) && isOneValue(op2)) {
            if (((OneValue) op2).value instanceof Comparable) {
                this.expression.add(this.criteriaBuilder.lessThan(from.get(op1.toString()), (Comparable) ((OneValue) op2).value));
                return this;
            } else {
                Object ob = ((OneValue) op2).value;
                throw new RestrictionEditorException(ob.toString() + " (type=" + ob.getClass().getName() + ") should be instance of Comparable");
            }
        }

        //  op1 - константа и  op2 - атрибут => программируем как op2 > op1
        if (isOneValue(op1) && isAttribute(op2)) {
            if (((OneValue) op1).value instanceof Comparable) {
                this.expression.add(this.criteriaBuilder.greaterThan(from.get(op2.toString()), (Comparable) ((OneValue) op1).value));
                return this;
            } else {
                Object ob = ((OneValue) op1).value;
                throw new RestrictionEditorException(ob.toString() + " (type=" + ob.getClass().getName() + ") should be instance of Comparable");
            }
        }

        //  op1 - атрибут    и  op2 - атрибут => программируем как op1 < op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(this.criteriaBuilder.lessThan(from.get(op1.toString()), from.get(op2.toString())));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> le() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 <= op2
        if (isAttribute(op1) && isOneValue(op2)) {
            if (((OneValue) op2).value instanceof Comparable) {
                this.expression.add(this.criteriaBuilder.lessThanOrEqualTo(from.get(op1.toString()), (Comparable) ((OneValue) op2).value));
                return this;
            } else {
                Object ob = ((OneValue) op2).value;
                throw new RestrictionEditorException(ob.toString() + " (type=" + ob.getClass().getName() + ") should be instance of Comparable");
            }
        }

        //  op1 - константа и  op2 - атрибут => программируем как op2 >= op1
        if (isOneValue(op1) && isAttribute(op2)) {
            if (((OneValue) op1).value instanceof Comparable) {
                this.expression.add(this.criteriaBuilder.greaterThanOrEqualTo(from.get(op2.toString()), (Comparable) ((OneValue) op1).value));
                return this;
            } else {
                Object ob = ((OneValue) op1).value;
                throw new RestrictionEditorException(ob.toString() + " (type=" + ob.getClass().getName() + ") should be instance of Comparable");
            }
        }

        //  op1 - атрибут    и  op2 - атрибут => программируем как op1 <= op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(this.criteriaBuilder.lessThanOrEqualTo(from.get(op1.toString()), from.get(op2.toString())));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> neq() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        //  op1 - атрибут    и  op2 - константа => программируем как op1 != op2
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(this.criteriaBuilder.notEqual(from.get(op1.toString()), ((OneValue) op2).value));
            return this;
        }
        // op1 - константа  и  op2 - атрибут => программируем как op2 != op1
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(this.criteriaBuilder.notEqual(from.get(op2.toString()), ((OneValue) op1).value));
            return this;
        }
        // op1 - атрибут и op2 - атрибут=> программируем как op1 != op2
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(this.criteriaBuilder.notEqual(from.get(op1.toString()), from.get(op2.toString())));
            return this;
        }
        // случай op1 - константа и op2 - константа  является ошибкой
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> like() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        //  op1 - атрибут    и  op2 - константа
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(
                    this.criteriaBuilder.like(
                    from.get(op1.toString()),
                    ((OneValue) op2).value.toString()));
            return this;
        }
        //  op1 - атрибут и  op2 - атрибут
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(
                    this.criteriaBuilder.like(
                    from.get(op1.toString()),
                    from.get(op2.toString())));
            return this;
        }
        //  op1 - константа  и op2 - атрибут
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(
                    this.criteriaBuilder.like(
                    this.criteriaBuilder.literal(((OneValue) op1).value.toString()),
                    from.get(op2.toString())));
            return this;
        }
        // op1 - константа и op2 - константа  - это ошибка
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> contains() throws RestrictionEditorException {
        Object op1, op2;
        op2 = this.pop();
        op1 = this.pop();

        // op1 - атрибут и op2 - константа 
        if (isAttribute(op1) && isOneValue(op2)) {
            this.expression.add(this.criteriaBuilder.gt(
                    this.criteriaBuilder.locate(from.get(op1.toString()),
                    ((OneValue) op2).value.toString()),
                    0));
            return this;
        }
        //  op1 - константа и  op2 - атрибут 
        if (isOneValue(op1) && isAttribute(op2)) {
            this.expression.add(
                    this.criteriaBuilder.gt(
                    this.criteriaBuilder.locate(
                    this.criteriaBuilder.literal(((OneValue) op1).value.toString()),
                    from.get(op2.toString())),
                    0));
            return this;
        }
        // op1 - атрибут и op2 - атрибут
        if (isAttribute(op1) && isAttribute(op2)) {
            this.expression.add(
                    this.criteriaBuilder.gt(
                    this.criteriaBuilder.locate(from.get(op1.toString()),
                    from.get(op2.toString())),
                    0));
            return this;
        }
        // op1 - константа и op2 - константа  - это ошибка
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> isNull() throws RestrictionEditorException {
        Object op1;
        op1 = this.pop();
        if (isAttribute(op1)) {
            this.expression.add(this.criteriaBuilder.isNull(from.get(op1.toString())));
        } else {
            throw new RestrictionEditorException();
        }
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> isNotNull() throws RestrictionEditorException {
        Object op1;
        op1 = this.pop();
        if (isAttribute(op1)) {
            this.expression.add(this.criteriaBuilder.isNotNull(from.get(op1.toString())));
        } else {
            throw new RestrictionEditorException();
        }
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> in() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        // op1 - атрибут и op2 - cписок констант =>  программируем как op1 in op2
        if (isAttribute(op1) && isListOfValues(op2)) {
            this.expression.add(from.get(op1.toString()).in(((RestrictionEditorJPACriteria.ListOfValues) op2).values));
            return this;
        }
        // op1 - атрибут и op2 - атрибут
        // op1 - константа и op2 - константа  - это ошибка
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> not() {
        Object op1;
        op1 = this.pop();
        this.expression.add(this.criteriaBuilder.not((Expression<Boolean>) op1));
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> and() {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();
        this.expression.add(this.criteriaBuilder.and((Expression<Boolean>) op1, (Expression<Boolean>) op2));
        return this;
    }

    @Override
    public RestrictionEditorInterface<CriteriaQuery> or() {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();
        this.expression.add(this.criteriaBuilder.or((Expression<Boolean>) op1, (Expression<Boolean>) op2));
        return this;
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
    public int size() {
        return this.expression.size();
    }
}
