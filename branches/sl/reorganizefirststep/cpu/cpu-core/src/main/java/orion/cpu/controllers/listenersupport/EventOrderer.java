package orion.cpu.controllers.listenersupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.tapestry5.ioc.Orderable;
import org.apache.tapestry5.ioc.internal.util.Orderer;
import org.apache.tapestry5.ioc.util.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Сортировщик очереди слушателей событий контроллера. Построен на базе
 * {@link org.apache.tapestry5.ioc.internal.util.Orderer}
 * @author sl
 */
//TODO Реализовать без internal классов Tapestry
public class EventOrderer {

    private static final Logger LOG = LoggerFactory.getLogger(EventOrderer.class);
    private final List<Orderable<ControllerEventsListener>> orderables = new ArrayList<Orderable<ControllerEventsListener>>();
    private final Map<String, Orderable<ControllerEventsListener>> idToOrderable = new CaseInsensitiveMap<Orderable<ControllerEventsListener>>();
    private List<ControllerEventsListener> listeners = new ArrayList<ControllerEventsListener>();

    public void add(Orderable<ControllerEventsListener> orderable) {
        String id = orderable.getId();
        if (idToOrderable.containsKey(id)) {
            LOG.warn("Duplicate key {}", id);
            return;
        }
        orderables.add(orderable);
        idToOrderable.put(id, orderable);
        listeners.clear();
    }

    public void add(String id, ControllerEventsListener target, String... constraints) {
        orderables.add(new Orderable<ControllerEventsListener>(id, target, constraints));
    }

    public List<ControllerEventsListener> getOrdered() {
        if (listeners.size()>0) {
            return listeners;
        }
        Orderer<ControllerEventsListener> orderer = new Orderer<ControllerEventsListener>(LOG);
        for (Orderable<ControllerEventsListener> orderable : orderables) {
            orderer.add(orderable);
        }
        listeners = orderer.getOrdered();
        return Collections.unmodifiableList(listeners);
    }

    public void override(Orderable<ControllerEventsListener> orderable) {
        String id = orderable.getId();
        Orderable<ControllerEventsListener> existing = idToOrderable.get(id);
        if (existing == null) {
            throw new IllegalArgumentException(
                    String.format("Override for object '%s' is invalid as it does not match an existing object.", id));
        }

        orderables.remove(existing);
        orderables.add(orderable);

        idToOrderable.put(id, orderable);
        listeners.clear();
    }

    public void override(String id, ControllerEventsListener target, String... constraints) {
        override(new Orderable<ControllerEventsListener>(id, target, constraints));
    }
}
