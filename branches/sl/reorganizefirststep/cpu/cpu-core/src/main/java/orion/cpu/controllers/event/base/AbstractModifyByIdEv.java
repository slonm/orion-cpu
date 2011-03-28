package orion.cpu.controllers.event.base;

import java.io.Serializable;

/**
 * Базовый класс событий при модификации объекта по Id
 * @author sl
 */
public abstract class AbstractModifyByIdEv implements ModifyEv {

    private Serializable id;

    public AbstractModifyByIdEv(Serializable id) {
        this.id = id;
    }

    protected void setId(Serializable id) {
        this.id = id;
    }

    public Serializable getId() {
        return id;
    }
}
