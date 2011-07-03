package orion.tapestry.grid.lib.restrictioneditor;

import java.util.ArrayList;

/**
 * Пример составителя выражений для условия выборки строк.
 * Итоговое выражение - просто строка
 * @author Gennadiy Dobrovolsky
 */
public class RestrictionEditorHumanReadable implements RestrictionEditorInterface<String> {

    private ArrayList<Item> expression;

    public RestrictionEditorHumanReadable() {
        this.expression = new ArrayList();
    }



    // внутренние классы для членов выражения
    private interface Item {
        /**
         * Строка, отформатированная по правилам базы данных
         */
        String getDbExpression();
    }

    /**
     * Готовое выражение
     */
    private static class ItemExpression implements RestrictionEditorHumanReadable.Item {

        public String value;

        public ItemExpression(String val) {
            value = val;
        }

        @Override
        public String getDbExpression() {
            return this.value;
        }
    }

    /**
     * Список констант
     */
    private static class ItemValueList implements RestrictionEditorHumanReadable.Item {

        public Object[] value;

        public ItemValueList(Object[] val) {
            value = val;
        }

        @Override
        public String getDbExpression() {
            StringBuffer sb = new StringBuffer();
            String div = "";
            for (Object s : this.value) {
                sb.append(div);
                sb.append(dbStr(s.toString()));
                div = "','";
            }
            return "'" + sb.toString() + "'";
        }
    }



    /**
     * Имя поля в базе данных
     */
    private static class ItemField implements RestrictionEditorHumanReadable.Item {

        public String value;

        public ItemField(String val) {
            value = val;
        }

        @Override
        public String getDbExpression() {
            return this.value;
        }
    }

    /**
     * Константа
     */
    private static class ItemValue implements RestrictionEditorHumanReadable.Item {

        public Object value;

        public ItemValue(Object val) {
            value = val;
        }

        @Override
        public String getDbExpression() {
            return "'" + dbStr(this.value.toString()) + "'";
        }
    }

    @Override
    public RestrictionEditorInterface<String> constValue(Object value) {
        this.expression.add(new RestrictionEditorHumanReadable.ItemValue(value));
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> constValueList(Object... value) {
        this.expression.add(new RestrictionEditorHumanReadable.ItemValueList(value));
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> constField(String value) {
        this.expression.add(new RestrictionEditorHumanReadable.ItemField(value));
        return this;
    }


    @Override
    public RestrictionEditorInterface<String> gt() {
        this.operator2(" > ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> ge() {
        this.operator2(" >= ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> eq() {
        this.operator2(" = ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> neq() {
        this.operator2(" <> ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> le() {
        this.operator2(" <= ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> lt() {
        this.operator2(" < ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> like() {
        this.operator2(" like ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> ilike() {
        this.operator2(" ilike ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> isNull() {
        int size;
        size = this.expression.size();
        this.expression.add(new RestrictionEditorHumanReadable.ItemExpression(" isNull (" + this.expression.get(size - 1).getDbExpression() + ")"));
        this.expression.remove(size - 1);
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> isNotNull() {
        int size;
        size = this.expression.size();
        this.expression.add(new RestrictionEditorHumanReadable.ItemExpression(" isNotNull (" + this.expression.get(size - 1).getDbExpression() + ")"));
        this.expression.remove(size - 1);
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> in() {
        int size;
        size = this.expression.size();
        RestrictionEditorHumanReadable.Item op1;
        RestrictionEditorHumanReadable.Item op2;
        op1 = this.expression.get(size - 1);
        op2 = this.expression.get(size - 2);
        System.out.println(op1.getDbExpression());
        System.out.println(op2.getDbExpression());
        this.expression.add(new RestrictionEditorHumanReadable.ItemExpression(this.brakets(op1.getDbExpression()) + " IN (" + op2.getDbExpression() + ")"));
        this.expression.remove(size - 1);
        this.expression.remove(size - 2);

        return this;
    }

    @Override
    public RestrictionEditorInterface<String> contains() {
        int size;
        size = this.expression.size();
        RestrictionEditorHumanReadable.Item op1;
        RestrictionEditorHumanReadable.Item op2;
        op1 = this.expression.get(size - 2);
        op2 = this.expression.get(size - 1);
        this.expression.add(new RestrictionEditorHumanReadable.ItemExpression(this.brakets( op1.getDbExpression() ) + " contains " + this.brakets(op2.getDbExpression()) ));
        this.expression.remove(size - 1);
        this.expression.remove(size - 2);

        return this;
    }

    @Override
    public RestrictionEditorInterface<String> and() {
        this.operator2(" AND ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> or() {
        this.operator2(" OR ");
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> not() {
        int size;
        size = this.expression.size();
        this.expression.add(new RestrictionEditorHumanReadable.ItemExpression(" not (" + this.expression.get(size - 1).getDbExpression() + ")"));
        this.expression.remove(size - 1);
        return this;
    }
    @Override
    public String getValue() {
        if (this.expression.size() > 0) {
            return this.expression.get(0).getDbExpression();
        }
        return "";
    }

//    @Override
//    public String getHumanReadableValue() {
//        return this.getValue();
//    }

    @Override
    public void createEmpty() {
        this.expression = new ArrayList();
    }

    @Override
    public int size() {
        return this.expression.size();
    }

    private void operator2(String op) {
        int size;
        size = this.expression.size();
        RestrictionEditorHumanReadable.Item op1, op2;
        StringBuilder tmp1, tmp2;
        op1 = this.expression.get(size - 2);
        op2 = this.expression.get(size - 1);
        this.expression.add(new RestrictionEditorHumanReadable.ItemExpression(this.brakets(op1.getDbExpression()) + " " + op + this.brakets(op2.getDbExpression())));
        this.expression.remove(size - 1);
        this.expression.remove(size - 2);
    }

    private String brakets(String tmp){
        StringBuilder tmp1=new StringBuilder(tmp);
        if(tmp1.toString().replaceAll("[a-zA-Z0-9'.]","").length()>0){
            tmp1.append(")");
            tmp1.insert(0, "(");
        }
        return tmp1.toString();
    }

    private static String dbStr(String s) {
        return s.replace("'", "\\'");
    }
}
