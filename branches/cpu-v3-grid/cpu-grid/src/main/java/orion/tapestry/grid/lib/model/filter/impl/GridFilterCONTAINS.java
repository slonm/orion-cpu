package orion.tapestry.grid.lib.model.filter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Условие "строка содержит подстроку"
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterCONTAINS extends GridFilterText {

    private static Logger logger = LoggerFactory.getLogger(GridFilterCONTAINS.class);

    public GridFilterCONTAINS(GridPropertyModelInterface model) {
        super(model);
        this.setUid(this.fieldName + "CONTAINS");
        this.setLabel(" contains ");
    }

    /**
     * Условие "строка содержит подстроку"
     */
    @Override
    public <T> boolean modifyRestriction(
            RestrictionEditorInterface<T> restriction,
            Object value,
            boolean isActive,
            int nChildren) throws RestrictionEditorException {
        // logger.info("1:" + this.fieldName + " contains " + value);
        // элемент должен быть активным
        if (!isActive) {
            return false;
        }
        // logger.info("2:" + this.fieldName + " contains " + value);
        // значение должно существовать
        if (value == null) {
            return false;
        }
        // logger.info("3:" + this.fieldName + " contains " + value);
        // используем валидатор для проверки данных
        Object checkedValue;
        if (dataType != null) {
            checkedValue = dataType.fromString(value.toString());
            if (checkedValue == null) {
                return false;
            }
        } else {
            checkedValue = value;
        }
        // logger.info(this.fieldName + " contains " + checkedValue);
        restriction.constField(this.fieldName);
        restriction.constValue(checkedValue);
        restriction.contains();
        return true;
    }
}
