package ua.orion.core.services;

import java.io.Serializable;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.UpdateListenerHub;
import org.tynamo.jpa.JPAEntityManagerSource;
import ua.orion.core.entities.SerializableSingleton;
import ua.orion.core.entities.StringSingleton;
import ua.orion.core.utils.ModelLibraryInfo;
import ua.orion.core.validation.UniqueConstraintValidator;

/**
 *
 * @author sl
 */
public class OrionCoreIOCModule {

    @Inject
    private UpdateListenerHub updateListenerHub;

    public static void bind(ServiceBinder binder) {
        binder.bind(ModelLibraryService.class, ModelLibraryServiceImpl.class);
        binder.bind(PersistentSingletonSource.class, PersistentSingletonSourceImpl.class);
        binder.bind(ApplicationMessagesSource.class, ApplicationMessagesSourceImpl.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
    }

    public void contributeJPAEntityPackageManager(Configuration<String> conf,
            ModelLibraryService modelLibraryService) {
        for (ModelLibraryInfo libInfo : modelLibraryService.getModelLibraryInfos()) {
            conf.add(libInfo.getLibraryPackage() + ".entities");
        }
    }

    //TODO Test if Bundle not found
    public void contributeApplicationMessagesSource(Configuration<String> conf,
            ModelLibraryService modelLibraryService) {
        for (ModelLibraryInfo libInfo : modelLibraryService.getModelLibraryInfos()) {
            conf.add(libInfo.getLibraryName());
        }
    }

    public void contributeRegistryStartup(OrderedConfiguration<Runnable> conf,
            final JPAEntityManagerSource emSource, final PropertyAccess pAccess) {
        conf.add("InitUniqueConstraintValidator", new Runnable()    {

            @Override
            public void run() {
                UniqueConstraintValidator.setENTITY_MANAGER_FACTORY(emSource.getEntityManagerFactory());
                UniqueConstraintValidator.setPROPERTY_ACCESS(pAccess);
            }
        });

        conf.addInstance("SeedEntity", SeedEntity.class, "after:InitObjectLocatorSource");
    }

    /**
     */
    public void contributePersistentSingletonSource(MappedConfiguration<Class, Class> configuration) {
        configuration.add(String.class, StringSingleton.class);
        configuration.add(Serializable.class, SerializableSingleton.class);
    }

    public static void contributeObjectProviders(OrderedConfiguration<ObjectProvider> configuration) {
        configuration.addInstance("PersistentSingleton", PersistentSingletonProvider.class);
    }

    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration) {
        add(configuration, String.class, Class.class,
                new Coercion<String, Class>()   {

                    @Override
                    public Class coerce(String className) {
                        try {
                            return Class.forName(className);
                        } catch (ClassNotFoundException ex) {
                            return null;
                        }
                    }
                });
    }

    private static <S, T> void add(Configuration<CoercionTuple> configuration, Class<S> sourceType, Class<T> targetType,
            Coercion<S, T> coercion) {
        CoercionTuple<S, T> tuple = new CoercionTuple<S, T>(sourceType, targetType, coercion);
        configuration.add(tuple);
    }
}
