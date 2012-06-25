package ua.orion.core.services;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.*;
import org.apache.tapestry5.jpa.EntityManagerSource;
import org.apache.tapestry5.jpa.JpaTransactionAdvisor;
import org.apache.tapestry5.services.UpdateListenerHub;
import org.slf4j.Logger;
import ua.orion.core.entities.SerializableSingleton;
import ua.orion.core.entities.StringSingleton;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.core.OrionSymbols;
import ua.orion.core.foreigndata.XMLExport;
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
        binder.bind(ModelLibraryService.class, ModelLibraryServiceImpl.class);
        binder.bind(PersistentSingletonSource.class, PersistentSingletonSourceImpl.class);
        binder.bind(InheritedAnnotationProviderSource.class, InheritedAnnotationProviderSourceImpl.class);
        binder.bind(EntityService.class, EntityServiceImpl.class);
        binder.bind(ModelLabelSource.class, ModelLabelSourceImpl.class);
        binder.bind(EduProcPlanningService.class, EduProcPlanningServiceImpl.class);
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, String> configuration) {
        //MEDIUM DateFormat
        configuration.add(OrionSymbols.DATE_FORMAT, "2");
    }

    public static StringValueProvider build(List<StringValueProvider> conf,
            ChainBuilder builder) {
        return builder.build(StringValueProvider.class, conf);
    }

    /**
     * Добавляет провайдеры для сущностей, enum, java.util.Calendar,
     * java.util.Date
     *
     * @param conf
     * @param thLocale
     */
    public static void contributeStringValueProvider(OrderedConfiguration<StringValueProvider> conf,
            final ThreadLocale thLocale, @Symbol(OrionSymbols.DATE_FORMAT) final int dateFormat,
            final Messages mes) {
        conf.addInstance("entity", StringValueProviderImpl.class);

        conf.add("enum", new StringValueProvider() {

            @Override
            public String getStringValue(Object o) {
                if (o == null || (!(o instanceof Enum))) {
                    return null;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(mes.get(o.getClass().getSimpleName() + "." + o.toString()));
                return sb.toString();
            }
        });
        conf.add("date", new StringValueProvider() {

            @Override
            public String getStringValue(Object date) {
                if (date == null) {
                    return null;
                }
                DateFormat _dateFormat = DateFormat.getDateInstance(dateFormat, thLocale.getLocale());

                if (Calendar.class.isInstance(date)) {
                    return _dateFormat.format(((Calendar) date).getTime());
                }
                if (Date.class.isInstance(date)) {
                    return _dateFormat.format(date);
                }
                return null;
            }
        });
        conf.add("default", new StringValueProvider() {

            @Override
            public String getStringValue(Object o) {
                return o == null ? "" : String.valueOf(o);
            }
        }, "after:*");
    }

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo("OrionCore", "ua.orion.core"));
    }

    public static void contributeJpaEntityPackageManager(Configuration<String> conf,
            ModelLibraryService modelLibraryService, Logger LOG) {
        for (ModelLibraryInfo libInfo : modelLibraryService.getModelLibraryInfos()) {
            String libName = libInfo.getLibraryPackage() + ".entities";
            conf.add(libName);
            LOG.debug("Added entity library " + libName);
        }
    }

    public void contributeRegistryStartup(OrderedConfiguration<Runnable> conf,
            final EntityManagerSource emSource, final PropertyAccess pAccess) {
        conf.add("InitUniqueConstraintValidator", new Runnable() {

            @Override
            public void run() {
                UniqueConstraintValidator.setENTITY_MANAGER_FACTORY(emSource.getEntityManagerFactory(IOCUtils.getDefaultPersistenceUnitName(emSource)));
                UniqueConstraintValidator.setPROPERTY_ACCESS(pAccess);
            }
        });

        conf.addInstance("SeedEntity", SeedEntity.class, "after:InitObjectLocatorSource");
        conf.addInstance("XMLExport", XMLExport.class, "after:SeedEntity");
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
     * from String to Class from String to MetaEntity
     */
    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration,
            @Local final EntityService entityService) {
        configuration.add(CoercionTuple.create(String.class, Class.class,
                new Coercion<String, Class>() {

                    @Override
                    public Class coerce(String className) {
                        try {
                            return Class.forName(className);
                        } catch (ClassNotFoundException ex) {
                            return null;
                        }
                    }
                }));
        configuration.add(CoercionTuple.create(String.class, MetaEntity.class,
                new Coercion<String, MetaEntity>() {

                    @Override
                    public MetaEntity coerce(String className) {
                        return entityService.getMetaEntity(className);
                    }
                }));
        configuration.add(CoercionTuple.create(Calendar.class, Date.class,
                new Coercion<Calendar, Date>() {

                    @Override
                    public Date coerce(Calendar cal) {
                        return cal.getTime();
                    }
                }));
    }
}
