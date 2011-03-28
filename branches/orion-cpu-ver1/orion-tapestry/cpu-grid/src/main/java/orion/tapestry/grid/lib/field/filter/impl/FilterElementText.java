

package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementType;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;

/**
 * элемент фильтра, отображаемый как текстовое поле
 * @author Gennadiy Dobrovolsky
 */
public abstract class FilterElementText extends FilterElementAbstract{

    protected String fieldName;
    FilterElementText(String newFieldName){
        this.fieldName=newFieldName;
        this.setType(FieldFilterElementType.TEXT);
    }


}
