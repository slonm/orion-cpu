package orion.cpu.controllers.event;

import java.util.List;
import orion.cpu.controllers.event.base.*;

/**
 * Событие после поиска по образцу
 * @param <T> тип объекта
 * @author sl
 */
public class AfterFindByExampleEv<T> extends AbstractAfterFindByEv<List<T>> {

    private final T example;

    public AfterFindByExampleEv(List<T> returnValue, T example) {
        super(returnValue);
        this.example=example;
    }

    public T getExample() {
        return example;
    }
    
}
