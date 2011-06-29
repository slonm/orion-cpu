

package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementGUIType;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;

/**
 * элемент фильтра, отображаемый как текстовое поле
 * @author Gennadiy Dobrovolsky
 */
public abstract class FilterElementText extends FilterElementAbstract{

    public String fieldName;
    public FilterElementText(String newFieldName){
        this.fieldName=newFieldName;
        this.setType(FieldFilterElementGUIType.TEXT);
    }

    public FilterElementText(){
    }


}
