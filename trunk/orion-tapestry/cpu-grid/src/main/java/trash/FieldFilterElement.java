package trash;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementType;

/**
 * Один элемент формы фильтрации
 * @author Gennadiy Dobrovolsky
 */
public class FieldFilterElement {

    // внутренняя нумерация элементов
    private static int id;

    // идентификатор элемента
    private String uid;

    // значение
    private String value;

    // подпись
    private String label;

    // тип элемента формы
    private FieldFilterElementType type;

    public FieldFilterElement() {
        id++;
        setUid("element" + id);
    }

    public FieldFilterElement(String _uid) {
        id++;
        setUid(_uid);
    }


    public String getUid() {
        return this.uid;
    }

    public void setUid(String _uid) {
        this.uid = _uid;
    }


    public String getValue() {
        return this.value;
    }

    public void setValue(String _value) {
        this.value = _value;
    }


    public String getLabel() {
        return this.label;
    }

    public void setLabel(String _label) {
        this.label = _label;
    }

    public FieldFilterElementType getType() {
        return this.type;
    }

    public void setType(FieldFilterElementType _type) {
        this.type = _type;
    }

    /**
     * Mapping to boolean values
     */
    public void setIsTrue(boolean _isTrue) {
        this.value = _isTrue?"y":"n";
    }
    public boolean getIsTrue() {
        return this.value.equals("y");
    }
}
