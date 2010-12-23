package orion.cpu.controllers.event.base;

/**
 * Базовый класс событий перед модификацией объекта
 * @param <T> тип объекта
 * @author sl
 */
public abstract class AbstractBeforeModifyByObjectEv<T> extends AbstractModifyByObjectEv<T> {

    public AbstractBeforeModifyByObjectEv(T object) {
        super(object);
    }

    @Override
    public void setObject(T object) {
        super.setObject(object);
    }

}
