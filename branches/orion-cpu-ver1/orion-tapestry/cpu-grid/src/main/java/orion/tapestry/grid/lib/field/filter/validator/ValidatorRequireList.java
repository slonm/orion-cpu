package orion.tapestry.grid.lib.field.filter.validator;

import java.util.ArrayList;
import orion.tapestry.grid.lib.field.filter.FieldFilterElementValidator;

/**
 * Валидатор, который проверяет, 
 * чтобы строка была списком значений заданого типа T.
 * @param <T> тип значений, которые разделяются запятыми в списке.
 * @author Gennadiy Dobrovolsky
 */
public class ValidatorRequireList<T> implements FieldFilterElementValidator<String> {

    private FieldFilterElementValidator<T> itemValidator;
    public ValidatorRequireList(FieldFilterElementValidator<T> _itemValidator){
        this.itemValidator=_itemValidator;
    }

    @Override
    public boolean isValid(String value) {
        String val=fromString(value);
        return val!=null;
    }

    @Override
    public String fromString(String value) {
        String[] list= value.split(",");
        ArrayList<String> checkedList=new ArrayList<String>();
        for(String item:list){
            if(this.itemValidator.isValid(item.trim())){
                checkedList.add(this.itemValidator.fromString(item.trim()).toString());
            }
        }
        if(checkedList.size()>0){
            StringBuilder sb=new StringBuilder();
            int n=0;
            for(String checkedItem:checkedList){
                if(n>0){
                   sb.append(',');
                }
                sb.append(checkedItem);
            }
            return sb.toString();
        }else{
            return null;
        }
    }

    @Override
    public String getJSValidator() {
        return "validator_require_int";
    }
}
