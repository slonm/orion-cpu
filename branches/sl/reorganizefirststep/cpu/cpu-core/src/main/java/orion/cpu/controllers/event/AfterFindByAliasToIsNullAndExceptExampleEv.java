package orion.cpu.controllers.event;

import java.util.List;
import orion.cpu.controllers.event.base.*;

/**
 * Событие после извлечения всех записей кроме образца не являющихся псевдонимами
 * @param <T> тип возвращаемого значения метода
 * @author sl
 */
public class AfterFindByAliasToIsNullAndExceptExampleEv<T> extends AbstractAfterFindByEv<List<T>> {

    private final T example;

    public AfterFindByAliasToIsNullAndExceptExampleEv(List<T> returnValue, T example) {
        super(returnValue);
        this.example=example;
    }

    public T getExample() {
        return example;
    }
    
}
