package orion.tapestry.grid.lib.model.filter.impl;

import java.util.ArrayList;
import orion.tapestry.grid.lib.model.filter.GridFilterDataType;

/**
 * Валидатор, который проверяет, 
 * чтобы строка была списком значений заданого типа T.
 * @param <T> тип значений, которые разделяются запятыми в списке.
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterDataTypeList<T> implements GridFilterDataType<String> {

    private GridFilterDataType<T> itemValidator;

    public GridFilterDataTypeList(GridFilterDataType<T> _itemValidator) {
        this.itemValidator = _itemValidator;
    }

    @Override
    public boolean isValid(String value) {
        String val = fromString(value);
        return val != null;
    }

    @Override
    public String fromString(String value) {
        if (this.itemValidator == null) {
            return value;
        }
        String[] list = value.split(",");
        ArrayList<String> checkedList = new ArrayList<String>();

        for (String item : list) {
            if (this.itemValidator.isValid(item.trim())) {
                checkedList.add(this.itemValidator.fromString(item.trim()).toString());
            }
        }


        if (checkedList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            int n = 0;
            for (String checkedItem : checkedList) {
                if (n > 0) {
                    sb.append(',');
                }
                sb.append(checkedItem);
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    @Override
    public String getJSValidator() {
        return null;
    }
}
