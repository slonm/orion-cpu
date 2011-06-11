package orion.cpu.security.services;

import br.com.arsmachina.authentication.service.GenericAuthenticationIOCModule;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.SubModule;
import orion.cpu.controllers.listeners.CommitTransactionListener;
import orion.cpu.controllers.listenersupport.ControllerEventsListener;
import orion.cpu.security.AuthorityControllerListener;
import orion.cpu.security.services.impl.ExtendedAuthorizerImpl;

/**
 * Конфигурация IOC для подсистемы безопасности
 * @author sl
 */
@SubModule({GenericAuthenticationIOCModule.class})
public class SecModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(ExtendedAuthorizer.class, ExtendedAuthorizerImpl.class).withId("Authorizer");
    }

    /**
     * Добавление слушателя событий контролера для проверки прав доступа
     * {@link CommitTransactionListener}
     * @param configuration
     * @param locator
     */
    public void contributeDefaultControllerListeners(
            Configuration<Orderable<ControllerEventsListener>> configuration,
            ObjectLocator locator) {
        configuration.add(new Orderable("AuthorityControllerListener",
                locator.autobuild(AuthorityControllerListener.class), "before:CommitTransaction"));
    }
}
