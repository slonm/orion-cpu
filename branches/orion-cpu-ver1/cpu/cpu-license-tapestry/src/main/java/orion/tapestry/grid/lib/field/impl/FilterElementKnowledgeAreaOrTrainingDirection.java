/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.tapestry.grid.lib.field.impl;

import orion.tapestry.grid.lib.field.filter.impl.FilterElementText;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Элементарный фильтр, который будет использоватся при выборке данных
 * и получать значения из тестового поля
 * @author Gennadiy Dobrovolsky
 */
public class FilterElementKnowledgeAreaOrTrainingDirection extends FilterElementText {

    public FilterElementKnowledgeAreaOrTrainingDirection(String newFieldName, String newLabel) {
        super(newFieldName);
        this.setUid(this.fieldName + "CONTAINS");
        this.setLabel(newLabel);
    }

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
        Object checkedValue;
        if (validator != null) {
            checkedValue = validator.fromString(value.toString());
            if (checkedValue == null) {
                return false;
            }
        }else{
            checkedValue=value;
        }
        restriction.constField(this.fieldName+".name");
        restriction.constValue(checkedValue);
        restriction.contains();
        return true;
    }
}

