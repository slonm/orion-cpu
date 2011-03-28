package orion.cpu.services;

import java.util.Collection;
import org.apache.tapestry5.ioc.Orderable;
import org.apache.tapestry5.ioc.annotations.UsesConfiguration;
import orion.cpu.controllers.listenersupport.ControllerEventsListener;

/**
 * Интерфейс сервиса, который используется {@link orion.cpu.controllers.impl.BaseController}
 * для добавления слушателей по умолчанию
 * @author sl
 */
@UsesConfiguration(Orderable.class)
public interface DefaultControllerListeners {

    Collection<Orderable<ControllerEventsListener>> getListeners();
}
