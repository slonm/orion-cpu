package orion.cpu.controllers.impl;

import br.com.arsmachina.controller.Controller;
import orion.cpu.controllers.event.base.ControllerAfterEvent;
import orion.cpu.controllers.event.base.ControllerEvent;
import orion.cpu.controllers.listenersupport.ControllerEventsListenerHub;
import orion.cpu.controllers.listenersupport.AbortControllerEventException;
import orion.cpu.controllers.listenersupport.ControllerEventsListener;
import br.com.arsmachina.dao.SortCriterion;
import java.io.Serializable;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.service.DAOSource;
import java.util.*;
import org.apache.tapestry5.ioc.Orderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.cpu.controllers.event.*;
import orion.cpu.controllers.listenersupport.EventOrderer;
import orion.cpu.services.DefaultControllerListeners;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Класс реализует {@link Controller} делая прямые вызовы методов {@link DAO}.
 * Реализует ControllerEventsListenerHub и вызывает обработчиков всех событий.
 *
 * @author sl
 * @param <T> the entity class related to this controller.
 * @param <K> the type of the field that represents the entity class' primary key.
 */
//TODO протестировать возможность изменения параметров в из слушателей
public class BaseController<T, K extends Serializable>
        implements ControllerEventsListenerHub, Controller<T, K> {

    private Logger LOG;
    private EventOrderer orderer = new EventOrderer();
    private final DAO<T, K> dao;
    private final Class<T> entityClass;

    /**
     * Single constructor of this class.
     * Конструктор получает класс сущности и источник DAO вместо
     * просто объекта DAO что-бы обеспечить контроль не нулевых
     * значений параметров. Так как это проблематично сделать в потомках
     * @param entityClass тип сущности. It cannot be <code>null</code>.
     * @param daoSource источник DAO. It cannot be <code>null</code>.
     */
    public BaseController(Class<T> entityClass, DAOSource daoSource) {
        this(entityClass, daoSource, null);
    }

    /**
     * Single constructor of this class.
     * Конструктор получает класс сущности и источник DAO вместо
     * просто объекта DAO что-бы обеспечить контроль не нулевых
     * значений параметров. Так как это проблематично сделать в потомках
     * @param entityClass тип сущности. It cannot be <code>null</code>.
     * @param daoSource источник DAO. It cannot be <code>null</code>.
     * @param defaultControllerListeners слушатели по умолчанию.
     */
    public BaseController(Class<T> entityClass, DAOSource daoSource, DefaultControllerListeners defaultControllerListeners) {
        Defense.notNull(daoSource, "daoSource");
        this.entityClass = Defense.notNull(entityClass, "entityClass");
        LOG = LoggerFactory.getLogger(this.getClass());
        this.dao = (DAO<T, K>) daoSource.get(entityClass);
        if (defaultControllerListeners != null) {
            for (Orderable<ControllerEventsListener> listener : defaultControllerListeners.getListeners()) {
                orderer.add(listener);
            }
        }
    }

    protected DAO<T, K> getDao() {
        return dao;
    }

    /**
     * Обрабатывает очередь слушателей перед-событий
     * Так как параметры методов хранятся внутри события, то
     * изменяя их можно изменить действующие значения параметров
     * метода, который вызвал событие
     * @param event событие. It cannot be <code>null</code>.
     */
    protected void processBeforeEvent(ControllerEvent event) {
        for (ControllerEventsListener listener : orderer.getOrdered()) {
            if (listener != null) {
                LOG.debug("Process event {} by listener {}",
                        event.getClass().getSimpleName(),
                        listener.getClass().getName());
                listener.onControllerEvent(this, entityClass, event);
            }
        }
    }

    /**
     * Обрабатывает очередь слушателей после-событий
     * Так как возвращаемое значение методов хранится внутри события, то
     * изменяя его можно изменить конечное возвращаемое значение
     * метода, который вызвал событие
     * @param <X> тип возвращаемого значения
     * @param event событие. It cannot be <code>null</code>.
     * @return модифицированное возвращаемое значение метода
     */
    protected <X> X processAfterEvent(ControllerAfterEvent<X> event) {
        X ret = null;
        boolean firstPass = true;
        for (ControllerEventsListener listener : orderer.getOrdered()) {
            if (listener != null) {
                LOG.debug("Process event {} by listener {}",
                        event.getClass().getSimpleName(),
                        listener.getClass().getName());
                if (!firstPass) {
                    event.setReturnValue(ret);
                } else {
                    firstPass = false;
                }
                listener.onControllerEvent(this, entityClass, event);
                ret = event.getReturnValue();
            }
        }
        return ret;
    }

    /**
     * @see orion.cpu.controllers.listenersupport.GenericControllerImpl#addListener(String, ControllerEventsListener, String)
     */
    @Override
    public void addListener(String id, ControllerEventsListener listener, String... constraints) {
        Defense.notNull(id, "id");
        Defense.notNull(listener, "listener");
        orderer.add(id, listener, constraints);
    }

    /**
     * @see orion.cpu.controllers.listenersupport.GenericControllerImpl#overrideListener(String, ControllerEventsListener, String)
     */
    @Override
    public void overrideListener(String id, ControllerEventsListener listener, String... constraints) {
        Defense.notNull(id, "id");
        orderer.override(id, listener, constraints);
    }

    /**
     * @see br.com.arsmachina.controller.impl.GenericControllerImpl#delete(java.io.Serializable)
     */
    @Override
    public void delete(K id) {
        try {
            BeforeDeleteByIdEv ev = new BeforeDeleteByIdEv(id);
            processBeforeEvent(new BeforeDeleteByIdEv(id));
            dao.delete(id);
            processAfterEvent(new AfterDeleteByIdEv(id));
        } catch (AbortControllerEventException ex) {
        }
    }

    /**
     * @see br.com.arsmachina.controller.impl.GenericControllerImpl#delete(java.lang.Object)
     */
    @Override
    public void delete(T object) {
        try {
            processBeforeEvent(new BeforeDeleteByObjectEv<T>(object));
            dao.delete(object);
            processAfterEvent(new AfterDeleteByObjectEv<T>(object));
        } catch (AbortControllerEventException ex) {
        }
    }

    /**
     * @see br.com.arsmachina.controller.impl.GenericControllerImpl#evict(java.lang.Object)
     */
    @Override
    public void evict(T object) {
        try {
            processBeforeEvent(new BeforeEvictEv<T>(object));
            dao.evict(object);
        } catch (AbortControllerEventException ex) {
        }
    }

    /**
     * @see br.com.arsmachina.controller.impl.GenericControllerImpl#save(java.lang.Object)
     */
    @Override
    public void save(T object) {
        try {
            processBeforeEvent(new BeforeSaveEv<T>(object));
            dao.save(object);
            processAfterEvent(new AfterSaveEv<T>(object));
        } catch (AbortControllerEventException ex) {
        }
    }

    /**
     * @see br.com.arsmachina.controller.impl.GenericControllerImpl#saveOrUpdate(java.lang.Object)
     */
    @Override
    public T saveOrUpdate(T object) {
        if (isPersistent(object)) {
            return update(object);
        } else {
            save(object);
            return object;
        }
    }

    /**
     * @see br.com.arsmachina.controller.impl.GenericControllerImpl#update(java.lang.Object)
     */
    @Override
    public T update(T object) {
        try {
            processBeforeEvent(new BeforeUpdateEv<T>(object));
            T t = dao.update(object);
            t = processAfterEvent(new AfterUpdateEv<T>(t, object));
            return t;
        } catch (AbortControllerEventException ex) {
            return object;
        }
    }

    //TODO Какое Количество должен возвращать countAll, с правами чтения или все?
    @Override
    public int countAll() {
        try {
            return processAfterEvent(new AfterCountAllEv(dao.countAll()));
        } catch (AbortControllerEventException ex) {
            return 0;
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return (processAfterEvent(new AfterFindAllEv<List<T>>(dao.findAll())));
        } catch (AbortControllerEventException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<T> findAll(int firstResult, int maxResults, SortCriterion... sortCriteria) {
        try {
            List<T> l = dao.findAll(firstResult, maxResults, sortCriteria);
            return processAfterEvent(new AfterPartialFindAllEv<List<T>>(l, firstResult, maxResults, sortCriteria));
        } catch (AbortControllerEventException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<T> findByExample(T example) {
        try {
            List<T> l = dao.findByExample(example);
            return processAfterEvent(new AfterFindByExampleEv<T>(l, example));
        } catch (AbortControllerEventException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public T findById(K id) {
        try {
            T l = dao.findById(id);
            return processAfterEvent(new AfterFindByIdsEv<T>(l, (K[]) Arrays.asList(id).toArray()));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }

    @Override
    public List<T> findByIds(K... ids) {
        try {
            List<T> l = dao.findByIds(ids);
            return processAfterEvent(new AfterFindByIdsEv<List<T>>(l, ids));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }

    @Override
    public boolean isPersistent(T object) {
        return dao.isPersistent(object);
    }

    @Override
    public T reattach(T object) {
        return dao.reattach(object);
    }

    protected void refresh(T object) {
        dao.refresh(object);
    }
}
