package orion.tapestry.grid.lib.hibernate;

import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;

import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Составитель выражений по правилам org.hibernate.Criteria.
 * Итоговое выражение - просто строка
 * @author Gennadiy Dobrovolsky
 */
// TODO RestrictionEditorHibernateCriteria надо потестировать
public class RestrictionEditorHibernateCriteria implements RestrictionEditorInterface<Criteria> {

    /**
     * Выражение в универсальном формате, записанное в обратной бесскобочной форме
     */
    private ArrayList expression;
    /**
     * класс сущности ORM
     */
    private Class forClass;
    /**
     * сессия Hibernate, подключение к базе данных
     */
    private Session hibernateSession;
    /**
     * Объект, содержащий всю информацию, необходимую для выборки данных
     */
    private Criteria criteria;

    /**
     * Конструктор
     * @param _forClass класс сущности ORM
     * @param _hibernateSession сессия Hibernate, подключение к базе данных
     */
    public RestrictionEditorHibernateCriteria(Class _forClass, Session _hibernateSession) {
        this.forClass = _forClass;
        this.hibernateSession = _hibernateSession;
        this.createEmpty();
    }

    /**
     * Создание нового выражения
     */
    @Override
    public void createEmpty() {
        this.expression = new ArrayList();
        this.criteria = this.hibernateSession.createCriteria(this.forClass);
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
    public RestrictionEditorInterface<Criteria> constField(String value) {
        if(value.contains(".")){
            String fld=value.replaceAll("\\.\\w+$", "");
            this.criteria.createAlias(fld, fld);
        }
        this.expression.add(new RestrictionEditorHibernateCriteria.Attribute(value));
        return this;
    }

    /**
     * Добавляем в выражение константу
     */
    @Override
    public RestrictionEditorInterface<Criteria> constValue(Object value) {
        this.expression.add(new RestrictionEditorHibernateCriteria.OneValue(value));
        return this;
    }

    @Override
    public RestrictionEditorInterface<Criteria> constValueList(Object... value) {
        this.expression.add(new RestrictionEditorHibernateCriteria.ListOfValues(value));
        return this;
    }

    // Добавление элементарных частей в выражение - конец
    // =========================================================================
    // =========================================================================
    // определяем операторы - начало
    @Override
    public RestrictionEditorInterface<Criteria> and() {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();
        this.expression.add(Restrictions.and((Criterion) op1, (Criterion) op2));
        return this;
    }

    @Override
    public RestrictionEditorInterface<Criteria> or() {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();
        this.expression.add(Restrictions.or((Criterion) op1, (Criterion) op2));
        return this;
    }

    @Override
    public RestrictionEditorInterface<Criteria> not() {
        Object op1;
        op1 = this.pop();
        this.expression.add(Restrictions.not((Criterion) op1));
        return this;
    }

    @Override
    public RestrictionEditorInterface<Criteria> gt() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> ge() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> lt() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> le() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> eq() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> neq() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> isNull() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> isNotNull() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> like() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> ilike() throws RestrictionEditorException {
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
    public RestrictionEditorInterface<Criteria> in() throws RestrictionEditorException {
        Object op1, op2;
        op1 = this.pop();
        op2 = this.pop();

        // op1 - атрибут и op2 - cписок констант =>  программируем как op1 in op2
        if (isAttribute(op1) && isListOfValues(op2)) {
            this.expression.add(Restrictions.in(op1.toString(), ((RestrictionEditorHibernateCriteria.ListOfValues) op2).values));
            return this;
        }

        // op1 - атрибут и op2 - атрибут
        // op1 - константа и op2 - константа  - это ошибка
        throw new RestrictionEditorException();
    }

    @Override
    public RestrictionEditorInterface<Criteria> contains() throws RestrictionEditorException {
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
        return p instanceof RestrictionEditorHibernateCriteria.Attribute;
    }

    private boolean isOneValue(Object p) {
        return p instanceof RestrictionEditorHibernateCriteria.OneValue;
    }

    private boolean isListOfValues(Object p) {
        return p instanceof RestrictionEditorHibernateCriteria.ListOfValues;
    }

    // Вспомогательные методы - конец
    // =========================================================================
    @Override
    public Criteria getValue() {
        if (this.expression.size() > 0) {
            this.criteria.add((Criterion) this.expression.get(0));
        }
        return this.criteria;
    }

    @Override
    public int size() {
        return this.expression.size();
    }
//    public static void main(String[] a) {
//        RestrictionEditorInterface<String> rs = new RestrictionEditorHibernateCriteria();
//        rs.constString("fdsf ' 12");
//        rs.constField("fld1");
//        rs.eq();
//        rs.constInteger(10);
//        rs.constField("fld2");
//        rs.ge();
//        rs.and();
//        System.out.println(rs.getValue());
//    }
}
