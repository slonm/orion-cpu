package orion.tapestry.grid.lib.model.filter.impl;

import java.util.ArrayList;
import java.util.List;
import orion.tapestry.grid.lib.model.filter.GridFilterDataType;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Условие "принадлежит множеству"
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterIN extends GridFilterText {

    public GridFilterIN(GridPropertyModelInterface model) {
        super(model);
        this.setUid(this.fieldName + "IN");
        this.setLabel(" in ");
    }

    /**
     * Условие "принадлежит множеству"
     */
    @Override
    public <T> boolean modifyRestriction(
            RestrictionEditorInterface<T> restriction,
            Object value,
            boolean isActive,
            int nChildren) throws RestrictionEditorException {
        // элемент должен быть активным
        if (!isActive) {
            return false;
        }

        // значение должно существовать
        if (value == null) {
            return false;
        }

        // используем валидатор для проверки данных
        // Object checkedValue;
        Object[] valueList = value.toString().split(",");
        if (this.dataType != null) {
            Object[] checkedValue = new String[valueList.length];
            for(int i=0;i<valueList.length;i++){
                Object vl=this.dataType.fromString(valueList[i].toString());
                if (vl == null) {
                    return false;
                }
                checkedValue[i]=vl.toString();
            }
            valueList = checkedValue;
        }
        
        restriction.constValueList(valueList);
        restriction.constField(this.fieldName);
        restriction.in();
        return true;
    }

    /**
     * Устанавливает новый объект в качестве валидатора
     * @param _validator объект-валидатор пользовательского ввода
     */
    @Override
    public void setDataType(GridFilterDataType _datatype) {
        // особенный валидатор для списка
        this.dataType = new GridFilterDataTypeList(_datatype);
    }
}
