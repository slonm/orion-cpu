package orion.cpu.controllers.event;

import java.util.List;
import orion.cpu.controllers.event.base.*;

/**
 * Событие после извлечения всех записей кроме образца не являющихся псевдонимами
 * и не устаревшие
 * @param <T> тип возвращаемого значения метода
 * @author sl
 */
public class AfterFindByAliasToIsNullAndIsNotObsoleteAndExceptExampleEv<T> extends AbstractAfterFindByEv<List<T>> {

    private final T example;

    public AfterFindByAliasToIsNullAndIsNotObsoleteAndExceptExampleEv(List<T> returnValue, T example) {
        super(returnValue);
        this.example=example;
    }

    public T getExample() {
        return example;
    }
    
}
