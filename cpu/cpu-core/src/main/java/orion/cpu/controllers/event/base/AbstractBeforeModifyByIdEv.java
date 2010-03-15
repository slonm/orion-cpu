package orion.cpu.controllers.event.base;

import java.io.Serializable;

/**
 * Базовый класс событий перед модификацией объекта по Id
 * @author sl
 */
public abstract class AbstractBeforeModifyByIdEv extends AbstractModifyByIdEv {

    public AbstractBeforeModifyByIdEv(Serializable id) {
        super(id);
    }

    @Override
    public void setId(Serializable id) {
        super.setId(id);
    }
}
