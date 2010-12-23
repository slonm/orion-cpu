package orion.cpu.services.impl;

import java.util.Collection;
import java.util.Collections;
import org.apache.tapestry5.ioc.Orderable;
import orion.cpu.controllers.listenersupport.ControllerEventsListener;
import orion.cpu.services.DefaultControllerListeners;

/**
 * Простая реализация сервиса DefaultControllerListeners.
 * @author sl
 */
public class DefaultControllerListenersImpl implements DefaultControllerListeners {

    private final Collection<Orderable<ControllerEventsListener>> configuration;

    public DefaultControllerListenersImpl(Collection<Orderable> configuration) {
        this.configuration = Collections.unmodifiableCollection((Collection<Orderable<ControllerEventsListener>>) (Collection<?>) configuration);
    }

    /**
     *
     * @return коллекцию слушателей по умолчанию, которая получена из
     * конфигурации IOC
     */
    public Collection<Orderable<ControllerEventsListener>> getListeners() {
        return configuration;
    }
}
