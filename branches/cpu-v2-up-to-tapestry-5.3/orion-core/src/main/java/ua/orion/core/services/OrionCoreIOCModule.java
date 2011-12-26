package ua.orion.core.services;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.jpa.EntityManagerSource;
import org.apache.tapestry5.jpa.JpaTransactionAdvisor;
import org.apache.tapestry5.services.UpdateListenerHub;
import org.slf4j.Logger;
//import org.tynamo.jpa.JPAEntityManagerSource;
//import org.tynamo.jpa.JPATransactionAdvisor;
import ua.orion.core.entities.SerializableSingleton;
import ua.orion.core.entities.StringSingleton;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.core.utils.IOCUtils;
import ua.orion.core.validation.UniqueConstraintValidator;
import ua.orion.core.persistence.MetaEntity;
import ua.orion.cpu.core.eduprocplanning.services.EduProcPlanningService;
import ua.orion.cpu.core.eduprocplanning.services.EduProcPlanningServiceImpl;

/**
 *
 * @author sl
 */
@Marker(OrionCore.class)
public class OrionCoreIOCModule {

    @Inject
    private UpdateListenerHub updateListenerHub;

    public static void bind(ServiceBinder binder) {
        binder.bind(StringValueProvider.class, StringValueProviderImpl.class);
        binder.bind(ModelLibraryService.class, ModelLibraryServiceImpl.class);
        binder.bind(PersistentSingletonSource.class, PersistentSingletonSourceImpl.class);
        binder.bind(ApplicationMessagesSource.class, ApplicationMessagesSourceImpl.class);
        binder.bind(InheritedAnnotationProviderSource.class, InheritedAnnotationProviderSourceImpl.class);
        binder.bind(EntityService.class, EntityServiceImpl.class);
        binder.bind(ModelLabelSource.class, ModelLabelSourceImpl.class);
        binder.bind(EduProcPlanningService.class, EduProcPlanningServiceImpl.class);
    }

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo("OrionCore", "ua.orion.core"));
    }

    public static void contributeJPAEntityPackageManager(Configuration<String> conf,
            ModelLibraryService modelLibraryService, Logger LOG) {
        for (ModelLibraryInfo libInfo : modelLibraryService.getModelLibraryInfos()) {
            String libName = libInfo.getLibraryPackage() + ".entities";
            conf.add(libName);
            LOG.debug("Added entity library " + libName);
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
            final EntityManagerSource emSource, final PropertyAccess pAccess) {
        conf.add("InitUniqueConstraintValidator", new Runnable() {

            @Override
            public void run() {
                UniqueConstraintValidator.setENTITY_MANAGER_FACTORY(emSource.getEntityManagerFactory("default"));
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

    public static void contributeMasterObjectProvider(OrderedConfiguration<ObjectProvider> configuration) {
        configuration.addInstance("PersistentSingleton", PersistentSingletonProvider.class);
    }

    @Match("*Service")
    public static void adviseTransactions(JpaTransactionAdvisor advisor, MethodAdviceReceiver receiver) {
        advisor.addTransactionCommitAdvice(receiver);
    }

    /**
     * from String to Class
     * from String to MetaEntity
     */
    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration,
            @Local final EntityService entityService) {
        IOCUtils.addTuple(configuration, String.class, Class.class,
                new Coercion<String, Class>() {

                    @Override
                    public Class coerce(String className) {
                        try {
                            return Class.forName(className);
                        } catch (ClassNotFoundException ex) {
                            return null;
                        }
                    }
                });
        IOCUtils.addTuple(configuration, String.class, MetaEntity.class,
                new Coercion<String, MetaEntity>() {

                    public MetaEntity coerce(String className) {
                        return entityService.getMetaEntity(className);
                    }
                });
        IOCUtils.addTuple(configuration, Calendar.class, Date.class,
                new Coercion<Calendar, Date>() {

                    @Override
                    public Date coerce(Calendar cal) {
                        return cal.getTime();
                    }
                });
    }
}
