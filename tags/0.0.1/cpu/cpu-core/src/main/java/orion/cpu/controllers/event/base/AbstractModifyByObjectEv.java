package orion.cpu.controllers.event.base;

/**
 * Базовый класс событий при модификации объекта
 * @param <T> тип объекта
 * @author sl
 */
public abstract class AbstractModifyByObjectEv<T> implements ModifyEv {

    private T object;

    public AbstractModifyByObjectEv(T object) {
        this.object = object;
    }

    protected void setObject(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
