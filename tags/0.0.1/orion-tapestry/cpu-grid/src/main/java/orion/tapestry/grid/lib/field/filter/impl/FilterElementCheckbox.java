

package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementType;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;

/**
 * элемент фильтра, отображаемый как чекбокс
 * @author Gennadiy Dobrovolsky
 */
public abstract class FilterElementCheckbox extends FilterElementAbstract{

    protected String fieldName;
    FilterElementCheckbox(String newFieldName){
        this.fieldName=newFieldName;
        this.setType(FieldFilterElementType.CHECKBOX);
    }


}
