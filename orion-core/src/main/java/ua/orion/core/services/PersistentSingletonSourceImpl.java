package ua.orion.core.services;

import java.io.Serializable;
import java.util.*;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import ua.orion.core.persistence.PersistentSingleton;
import ua.orion.core.utils.Defense;

/**
 * Реализация PersistentSingletonSource, для получения записей
 * из справочников по uKey
 * @author sl
 */
public class PersistentSingletonSourceImpl implements PersistentSingletonSource {

    private final EntityService entityService;
    private final TypeCoercer typeCoercer;
    private final List<Class> orderedKeys = new ArrayList();
    private final Map<Class, Class> configuration;
    private final Map<Class, Class<? extends PersistentSingleton>> cache = new HashMap();

    public PersistentSingletonSourceImpl(Map<Class, Class> configuration,
            EntityService entityService, TypeCoercer typeCoercer) {
        this.entityService = entityService;
        this.configuration = configuration;
        this.typeCoercer = typeCoercer;
        orderedKeys.addAll(configuration.keySet());
        //Поместим String и Serializable в конец списка
        Collections.sort(orderedKeys, new Comparator<Class>()   {

            @Override
            public int compare(Class o1, Class o2) {
                if (o1.equals(Serializable.class)) {
                    return 1;
                }
                if (o1.equals(String.class)) {
                    if (o2.equals(Serializable.class)) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
                return 0;
            }
        });
    }

    @Override
    public <T extends Serializable> T get(Class<T> clasz, String key) {
        Defense.notNull(clasz, "clasz");
        Defense.notNull(key, "key");
        try {
            return entityService.findByUKey(clasz, key);
        } catch (IllegalArgumentException ex) {
            Class<PersistentSingleton<T>> entityType = getSimpleTypePersistentSingletonEntityClass(clasz);
            return typeCoercer.coerce(entityService.findByName(entityType, key).getSingleton(), clasz);
        }
    }

    @Override
    public <T extends Serializable> boolean contains(Class<T> clasz, String key) {
        return get(clasz, key) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Serializable> void put(String key, T value) {
        Defense.notNull(key, "key");
        Defense.notNull(value, "value");
        try {
            entityService.setUKey(value, key);
            T oldVal = entityService.findByUKey((Class<T>) value.getClass(), key);
            if (oldVal == null) {
                entityService.persist(value);
            } else {
                //TODO Test it
                entityService.setPrimaryKey(value, entityService.getPrimaryKey(oldVal));
                entityService.getEntityManager().detach(oldVal);
                entityService.merge(value);
            }
        } catch (IllegalArgumentException ex) {
            PersistentSingleton<T> oldVal = entityService.findByName(getSimpleTypePersistentSingletonEntityClass((Class<T>) value.getClass()), key);
            if (oldVal == null) {
                PersistentSingleton<T> newVal = newSimpleTypePersistentSingletonEntity(value, key);
                entityService.persist(newVal);
            } else {
                oldVal.setSingleton(typeCoercer.coerce(value,  oldVal.getSingletonClass()));
                entityService.merge(value);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Serializable> void remove(Class<T> clasz, String key) {
        Defense.notNull(clasz, "clasz");
        Defense.notNull(key, "key");
        try {
            Serializable obj = entityService.findByUKey((Class<Serializable>) clasz, key);
            if (obj != null) {
                entityService.remove(obj);
            }
        } catch (IllegalArgumentException ex) {
            Class<PersistentSingleton<T>> entityType = getSimpleTypePersistentSingletonEntityClass(clasz);
            PersistentSingleton<T> oldVal = entityService.findByName(entityType, key);
            if (oldVal != null) {
                entityService.remove(oldVal);
            }
        }
    }

    private <T extends Serializable> Class<PersistentSingleton<T>> getSimpleTypePersistentSingletonEntityClass(Class<T> clasz) {
        Class<PersistentSingleton<T>> result = (Class<PersistentSingleton<T>>) cache.get(clasz);
        if (result == null) {
            //Сначала ищем PersistentSingleton для конкретного типа 
            result = (Class<PersistentSingleton<T>>) configuration.get(clasz);
            if (result == null) {
                //В этом цикле перебираются все регистрированные простые типы данных, и
                //в последнюю очередь String и Serializable
                for (Class<?> key : orderedKeys) {
                    try {
                        //Check existing coercion
                        typeCoercer.explain(clasz, key);
                        result = (Class<PersistentSingleton<T>>) configuration.get(key);
                        break;
                    } catch (RuntimeException ex) {
                    }
                }
                cache.put(clasz, result);
            }
        }
        return result;
    }

    private <T extends Serializable> PersistentSingleton<T> newSimpleTypePersistentSingletonEntity(T value, String key) {
        PersistentSingleton<T> result;
        try {
            result = getSimpleTypePersistentSingletonEntityClass((Class<T>) value.getClass()).newInstance();
            result.setName(key);
            result.setSingleton(typeCoercer.coerce(value,  result.getSingletonClass()));
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
