package orion.cpu.services.impl;

import br.com.arsmachina.module.service.ControllerSource;
import java.io.Serializable;
import orion.cpu.controllers.sys.StoredConstantController;
import orion.cpu.entities.sys.StoredConstant;
import orion.cpu.services.StoredConstantsSource;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Сохраняет все в таблице StoredConstant
 * @author sl
 */
public class DefaultConstantsSource implements StoredConstantsSource {

    private final StoredConstantController сonstantController;

    public DefaultConstantsSource(ControllerSource controllerSource) {
        this.сonstantController = (StoredConstantController) (Object) controllerSource.get(StoredConstant.class);
    }

    @Override
    public <T> T get(Class<T> clasz, String key) {
        Defense.notNull(clasz, "clasz");
        Defense.notNull(key, "key");
        if (Serializable.class.isAssignableFrom(clasz)) {
            StoredConstant c = сonstantController.findByNameFirst(key);
            if (c != null) {
                return (T) c.getConstValue();
            }
        }
        return null;
    }

    @Override
    public boolean contains(Class<?> clasz, String key) {
        return get(clasz, key) != null;
    }

    @Override
    public <T> boolean put(Class<T> clasz, String key,
            T value) {
        Defense.notNull(clasz, "clasz");
        if (Serializable.class.isAssignableFrom(clasz)) {
            Defense.notNull(key, "key");
            Defense.notNull(value, "value");
            Serializable serialValue = (Serializable) value;
            StoredConstant oldConst = сonstantController.findByNameFirst(key);
            if (oldConst == null) {
                StoredConstant c = new StoredConstant();
                c.setConstValue((Serializable) value);
                c.setName(key);
                сonstantController.save(c);
            } else {
                oldConst.setConstValue(serialValue);
                сonstantController.update(oldConst);
            }
        }
        return false;
    }

    @Override
    public void remove(Class<?> clasz, String key) {
        Defense.notNull(clasz, "clasz");
        Defense.notNull(key, "key");
        StoredConstant sсonst = сonstantController.findByNameFirst(key);
        if (sсonst != null) {
            сonstantController.delete(sсonst);
        }
    }
}
