package orion.cpu.security.exceptions;

import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authorization.ObjectAuthorizationException;

/**
 * Исключение, вызываемое при проверке авторизации
 * и отсутствии дополнительных прав доступа к обьекту
 * @author sl
 */
public class AdditionalObjectAuthorizationException extends ObjectAuthorizationException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor that receives a message and a type.
     *
     * @param message a {@link String}.
     * @param object an {@link Object} instance. It cannot be null.
     */
    public AdditionalObjectAuthorizationException(String message, Object object) {
        super(message, object);
    }

    /**
     * Constructor that receives an object.
     *
     * @param permission право
     * @param object an {@link Object}. It cannot be null.
     */
    public AdditionalObjectAuthorizationException(Permission permission, Object object) {

        super(String.format("Unauthorized operation %s on %s (%s)", permission.getPermissionType(), object,
                permission.getSubject()), object);

    }
}
