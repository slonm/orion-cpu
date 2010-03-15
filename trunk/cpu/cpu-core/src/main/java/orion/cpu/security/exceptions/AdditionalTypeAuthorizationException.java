package orion.cpu.security.exceptions;

import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authorization.TypeAuthorizationException;

/**
 * Исключение, вызываемое при проверке авторизации
 * и отсутствии дополнительных прав доступа к типу обьекта
 * @author sl
 */
public class AdditionalTypeAuthorizationException extends TypeAuthorizationException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor that receives a message and a type.
     *
     * @param message a {@link String}.
     * @param type a {@link Class} instance. It cannot be null.
     */
    public AdditionalTypeAuthorizationException(String message, Class<?> type) {
        super(message, type);
    }

    /**
     * Constructor that receives a type.
     *
     * @param permission право. It cannot be null.
     */
    public AdditionalTypeAuthorizationException(Permission permission) {
        super(String.format("Unauthorized operation %s on %s attempt", permission.getPermissionType(), permission.getSubject().getName()),
                permission.getSubject());
    }
}
