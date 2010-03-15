package orion.cpu.controllers.event;

import java.util.List;
import orion.cpu.controllers.event.base.*;

/**
 * Событие после поиска разрешенных
 * @author sl
 */
public class AfterFindPermittedTypesEv<T> extends AbstractAfterViewEv<List<Class<?>>> {

    private final String permissionType;
    private final T abstractRole;

    public AfterFindPermittedTypesEv(List<Class<?>> returnValue, T abstractRole, String permissionType) {
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
