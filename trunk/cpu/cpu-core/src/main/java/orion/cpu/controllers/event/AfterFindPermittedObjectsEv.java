package orion.cpu.controllers.event;

import java.util.List;
import orion.cpu.controllers.event.base.*;

/**
 * Событие после поиска разрешенных объектов
 * @param <T> тип роли
 * @param <V> тип объектов
 * @author sl
 */
public class AfterFindPermittedObjectsEv<T, V> extends AbstractAfterViewEv<List<V>> {

    private final String permissionType;
    private final T abstractRole;

    public AfterFindPermittedObjectsEv(List<V> returnValue, Class<V> objectType, T abstractRole, String permissionType) {
        super(returnValue);
        this.abstractRole = abstractRole;
        this.permissionType = permissionType;
    }

    public T getAbstractRole() {
        return abstractRole;
    }

    public String getPermissionType() {
        return permissionType;
    }
}
