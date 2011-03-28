package orion.cpu.services.impl;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.service.ControllerSource;
import orion.cpu.baseentities.ReferenceEntity;
import orion.cpu.controllers.ReferenceController;
import orion.cpu.services.StoredConstantsSource;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Реализация StoredConstantsSource, для получения константных записей
 * из справочников
 * @author sl
 */
public class ReferenceConstantsSource implements StoredConstantsSource {

    private final ControllerSource controllerSource;

    public ReferenceConstantsSource(ControllerSource controllerSource) {
        this.controllerSource = Defense.notNull(controllerSource, "controllerSource");
    }

    @Override
    public <T> T get(Class<T> clasz, String key) {
        Defense.notNull(clasz, "clasz");
        Defense.notNull(key, "key");
        if (ReferenceEntity.class.isAssignableFrom(clasz)) {
            try {
                ReferenceController<?> c =
                        (ReferenceController<?>) (Object) controllerSource.get(clasz);
                return (T) c.findByKey(key);
            } catch (Exception ex) {
            }
        }
        return null;
    }

    @Override
    public boolean contains(Class<?> clasz, String key) {
        return get(clasz, key) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean put(Class<T> clasz, String key, T value) {
        Defense.notNull(clasz, "clasz");
        if (ReferenceEntity.class.isAssignableFrom(clasz)) {
            Defense.notNull(key, "key");
            Defense.notNull(value, "value");
            ReferenceEntity<?> newVal = (ReferenceEntity<?>) value;
            newVal.setKey(key);
            ReferenceEntity<?> oldVal = (ReferenceEntity<?>) get(clasz, key);
            ReferenceController controller = (ReferenceController) controllerSource.get(clasz);
            if (oldVal == null) {
                newVal.setId(null);
                controller.save(value);
            } else {
                //TODO Test it
                controller.evict(oldVal);
                newVal.setId(oldVal.getId());
                controller.reattach(newVal);
                controller.update(value);
            }
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void remove(Class<?> clasz, String key) {
        Defense.notNull(clasz, "clasz");
        Defense.notNull(key, "key");
        ReferenceController c =
                (ReferenceController) (Object) controllerSource.get(clasz);
        ReferenceEntity sсonst = (ReferenceEntity) c.findByNameFirst(key);
        if (sсonst != null) {
            c.delete(sсonst);
        }
    }
}
