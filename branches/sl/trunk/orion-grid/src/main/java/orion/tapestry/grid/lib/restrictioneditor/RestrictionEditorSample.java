package orion.tapestry.grid.lib.restrictioneditor;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Пример составителя выражений для условия выборки строк.
 * Итоговое выражение - просто строка
 * @author Gennadiy Dobrovolsky
 */
public class RestrictionEditorSample implements RestrictionEditorInterface<String> {

    private static final Logger log=LoggerFactory.getLogger(RestrictionEditorSample.class); 
    private ArrayList<Item> expression;

    public RestrictionEditorSample() {
        this.expression = new ArrayList();
    }

    // внутренние классы для членов выражения
    private static interface Item {

        /**
         * Строка, отформатированная по правилам базы данных
         */
        String getDbExpression();
    }

    /**
     * Список констант
     */
    private static class ItemValueList implements RestrictionEditorSample.Item {

        public Object[] value;

        public ItemValueList(Object[] val) {
            value = val;
        }

        @Override
        public String getDbExpression() {
            StringBuilder sb = new StringBuilder();
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
    private static class ItemField implements RestrictionEditorSample.Item {

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
     * Строка
     */
    private static class ItemValue implements RestrictionEditorSample.Item {

        public Object value;

        public ItemValue(Object val) {
            value = val;
        }

        @Override
        public String getDbExpression() {
            return "'" + dbStr(this.value.toString()) + "'";
        }
    }

    /**
     * Готовое выражение
     */
    private static class ItemExpression implements RestrictionEditorSample.Item {

        public String value;

        public ItemExpression(String val) {
            value = val;
        }

        @Override
        public String getDbExpression() {
            return this.value;
        }
    }

    @Override
    public RestrictionEditorInterface<String> constField(String value) {
        this.expression.add(new RestrictionEditorSample.ItemField(value));
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> constValue(Object value) {
        this.expression.add(new RestrictionEditorSample.ItemValue(value));
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> constValueList(Object... value) {
        this.expression.add(new RestrictionEditorSample.ItemValueList(value));
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
        this.expression.add(new RestrictionEditorSample.ItemExpression(" isNull (" + this.expression.get(size - 2).getDbExpression() + ")"));
        this.expression.remove(size - 1);
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> isNotNull() {
        int size;
        size = this.expression.size();
        this.expression.add(new RestrictionEditorSample.ItemExpression(" isNotNull (" + this.expression.get(size - 2).getDbExpression() + ")"));
        this.expression.remove(size - 1);
        return this;
    }

    @Override
    public RestrictionEditorInterface<String> in() {
        int size;
        size = this.expression.size();
        RestrictionEditorSample.Item op1;
        RestrictionEditorSample.Item op2;
        op1 = this.expression.get(size - 2);
        op2 = this.expression.get(size - 1);
        this.expression.add(new RestrictionEditorSample.ItemExpression("(" + op1.getDbExpression() + ") IN (" + op2.getDbExpression() + ")"));
        this.expression.remove(size - 1);
        this.expression.remove(size - 2);

        return this;
    }

    @Override
    public RestrictionEditorInterface<String> contains() {
        int size;
        size = this.expression.size();
        RestrictionEditorSample.Item op1;
        RestrictionEditorSample.Item op2;
        op1 = this.expression.get(size - 2);
        op2 = this.expression.get(size - 1);
        this.expression.add(new RestrictionEditorSample.ItemExpression("(" + op1.getDbExpression() + ") contains (" + op2.getDbExpression() + ")"));
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
        this.expression.add(new RestrictionEditorSample.ItemExpression(" not (" + this.expression.get(size - 2).getDbExpression() + ")"));
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
        RestrictionEditorSample.Item op1;
        RestrictionEditorSample.Item op2;
        op1 = this.expression.get(size - 2);
        op2 = this.expression.get(size - 1);
        this.expression.add(new RestrictionEditorSample.ItemExpression("(" + op1.getDbExpression() + ")" + op + "(" + op2.getDbExpression() + ")"));
        this.expression.remove(size - 1);
        this.expression.remove(size - 2);
    }

    private static String dbStr(String s) {
        return s.replace("'", "\\'");
    }

    public static void main(String[] a) {
        RestrictionEditorInterface<String> rs = new RestrictionEditorSample();
        rs.constValue("fdsf ' 12");
        rs.constField("fld1");
        try {
            rs.eq();
        } catch (RestrictionEditorException ex) {
            log.warn(ex.getMessage());
        }
        rs.constValue(10);
        rs.constField("fld2");
        try {
            rs.ge();
        } catch (RestrictionEditorException ex) {
            log.warn(ex.getMessage());
        }
        try {
            rs.and();
        } catch (RestrictionEditorException ex) {
            log.warn(ex.getMessage());
        }
        try {
            System.out.println(rs.getValue());
        } catch (RestrictionEditorException ex) {
            log.warn(ex.getMessage());
        }
    }
}
