package orion.cpu.services;

import br.com.arsmachina.authentication.entity.User;
import orion.cpu.services.factory.*;
import orion.cpu.security.services.SecModule;
import br.com.arsmachina.module.Module;
import br.com.arsmachina.module.factory.ControllerFactory;
import br.com.arsmachina.module.factory.DAOFactory;
import java.util.List;
import orion.cpu.services.impl.*;
import org.apache.tapestry5.hibernate.*;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.cpu.controllers.listeners.CommitTransactionListener;
import orion.cpu.controllers.listenersupport.ControllerEventsListener;
import orion.cpu.security.services.ExtendedAuthorizer;
import orion.cpu.utils.HibernateSessionWrapper;

/*
 * Модуль конфигурирования IOC
 */
@SubModule({SecModule.class})
public class CoreIOCModule {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(CoreIOCModule.class);
    public static final String FILL_TEST_DATA = "tapestry.hibernate.fill-test-data";

    /**
     * Регистрация сервисов и привязка к ним реализаций
     * @param binder
     */
    public static void bind(ServiceBinder binder) {
        binder.bind(DefaultControllerListeners.class, DefaultControllerListenersImpl.class);
        binder.bind(InitializeDatabaseSupport.class);
    }

    /**
     * Регистрация и конфигурирование опций.
     * @param configuration
     */
    public static void contributeFactoryDefaults(
            MappedConfiguration<String, String> configuration) {
        //Следующие опции используются для настройки имен пакетов в ArsMachina Module и TapestryCrudModule
        configuration.add("orion.root-package", "orion.cpu");
        configuration.add("orion.entities-package", "entities");
        configuration.add("orion.dao-package", "dao");
        configuration.add("orion.dao-implementation-package", "dao.impl");
        configuration.add("orion.controllers-package", "controllers");
        configuration.add("orion.controllers-implementation-package", "controllers.impl");
        configuration.add("orion.authorizer-package", "authorizers");
        configuration.add("orion.views-package", "views");
        configuration.add("orion.web-package", "views.tapestry");
        configuration.add("orion.activationcontextencoder-package", "encoders.activationcontext");
        configuration.add("orion.encoder-package", "encoders.encoder");
        configuration.add("orion.labelencoder-package", "encoders.label");
        configuration.add("orion.primarykeyencoder-package", "encoders.key");
        configuration.add("orion.beanmodelcustomizer-package", "beanmodel");
        configuration.add("orion.treeservice-package", "tree");

        //ENTITY_VALUE_ENCODERS вместо HibernateModule предоставит TapestryCrudIoCModule
//        configuration.override(HibernateSymbols.PROVIDE_ENTITY_VALUE_ENCODERS, "false");
        //Не заполняем базу тестовыми данными
        configuration.add(FILL_TEST_DATA, "false");
    }

    /**
     * Добавление модуля типа {@link OrionModuleImpl}
     * @param configuration
     * @param classNameLocator
     * @param symbolSource
     */
    public static void contributeModuleService(Configuration<Module> configuration,
            ClassNameLocator classNameLocator,
            SymbolSource symbolSource) {
        configuration.add(new OrionModuleImpl(null, classNameLocator, symbolSource));
    }

    /**
     * Contributes {@link HibernateReferenceDAOFactory} and
     * {@link SecuredDAOFactory} to the {@link DAOFactory} service.
     *
     * @param configuration an {@link OrderedConfiguration} of {@link DAOFactory}'s.
     */
    public static void contributeDAOFactory(
            OrderedConfiguration<DAOFactory> configuration) {
        configuration.addInstance("named", HibernateNamedEntityDAOFactory.class, "before:hibernate");
        configuration.addInstance("ref", HibernateReferenceDAOFactory.class, "before:named");
        configuration.overrideInstance("hibernate", SecuredDAOFactory.class, "after:*");
    }

    /**
     * Заменяет фабрику по умолчанию на {@link BaseControllerFactory}. Для справочников используется
     * {@link ReferenceControllerFactory}
     *
     * @param configuration an {@link OrderedConfiguration} of {@link ControllerFactory}'s.
     */
    public static void contributeControllerFactory(
            OrderedConfiguration<ControllerFactory> configuration) {

        configuration.addInstance("named", NamedEntityControllerFactory.class, "before:default");
        configuration.addInstance("ref", ReferenceControllerFactory.class, "before:named");
        configuration.overrideInstance("default", BaseControllerFactory.class);
    }

    /**
     * Регистрация размещения классов сущностей
     * @param configuration
     * @param rootPackage абсолютное имя корневого пакета
     * @param entitiesPackage относительное имя пакета сущностей
     */
    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration,
            @Inject @Symbol("orion.root-package") final String rootPackage,
            @Inject @Symbol("orion.entities-package") final String entitiesPackage) {
        configuration.add(rootPackage + "." + entitiesPackage);
    }

    /**
     * Конфигуратор Hibernate. Включает ImprovedNamingStrategy для имен объектов
     */
    public static class OrionHibernateConfigurer implements HibernateConfigurer {

        @Override
        public void configure(org.hibernate.cfg.Configuration configuration) {
            configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
        }
    }

    /**
     * Регистрация OrionHibernateConfigurer для включения ImprovedNamingStrategy
     * именования объектов базы данных
     * @param config
     */
    public static void contributeHibernateSessionSource(OrderedConfiguration<HibernateConfigurer> config) {
        config.addInstance("SchemaCreator", DatabaseSchemaObjectCreator.class, "before:*");
        config.addInstance("Orion", OrionHibernateConfigurer.class, "before:PackageName");
    }

    /**
     * Добавление слушателя событий контролера для управления транзакциями
     * {@link CommitTransactionListener}
     * @param configuration
     * @param locator
     */
    public void contributeDefaultControllerListeners(
            Configuration<Orderable<ControllerEventsListener>> configuration,
            ObjectLocator locator) {
        configuration.add(new Orderable("CommitTransaction",
                locator.autobuild(CommitTransactionListener.class)));
    }

    /**
     * Создание сервиса SessionFactory.
     * @param session Hibernate session
     * @param shadowBuilder
     * @return
     */
    public SessionFactory build(final org.hibernate.Session session, PropertyShadowBuilder shadowBuilder) {
        return shadowBuilder.build(session, "sessionFactory", SessionFactory.class);
    }

    /**
     * Модификация сервиса SessionFactory.
     * Метод getCurrentSession() возвращает session.
     * @param receiver
     * @param session Hibernate session
     */
    @Match("SessionFactory")
    public static void adviseSessionFactory(MethodAdviceReceiver receiver,
            final org.hibernate.Session session) {
        receiver.adviseAllMethods(new MethodAdvice() {

            @Override
            public void advise(Invocation invocation) {
                invocation.proceed();
                if (invocation.getMethodName().equals("getCurrentSession")) {
                    invocation.overrideResult(new HibernateSessionWrapper(session));
                }
            }
        });
    }

    /**
     * Добавляет к источнику констант источник константных справочников {@link ReferenceConstantsSource}
     * и источник констант по умолчанию {@link DefaultConstantsSource}
     * @param configuration
     */
    public void contributeStoredConstantsSource(OrderedConfiguration<StoredConstantsSource> configuration) {
        configuration.addInstance("reference", ReferenceConstantsSource.class, "before:default");
        configuration.addInstance("default", DefaultConstantsSource.class, "after:*");
    }

    /**
     * Начальная инициализация базы данных
     * Установка текущего пользователя для обхода авторизации
     * Удаление текущего пользователя после инициализации.
     * @param configuration
     * @param objectLocatorInstance
     */
    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration) {
    }

    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration,
            final ExtendedAuthorizer authorizer) {
        configuration.add("SetSSOUserSYSTEM", new Runnable() {

            @Override
            public void run() {
                authorizer.storeUserAndRole(User.SYSTEM_USER, null);
            }
        }, "before:*");

        configuration.addInstance("InitializeDatabase", InitializeDatabase.class);

        configuration.add("UnSetSSOUser", new Runnable() {

            @Override
            public void run() {
                authorizer.storeUserAndRole(null, null);
            }
        }, "after:*");
    }

    /**
     * Создание сервиса источника констант
     * @param contributions
     * @param chainBuilder
     * @return
     */
    public StoredConstantsSource build(
            final List<StoredConstantsSource> contributions,
            ChainBuilder chainBuilder) {
        return chainBuilder.build(StoredConstantsSource.class, contributions);
    }
    /**
     * Так будет выглядеть конфигурирование привязки DAO к сущностям при нестандартном именовании и размещении
     * Associates entity classes with their {@link DAO}s.
     *
     * @param contributions a {@link MappedConfiguration}.
     */
//    @SuppressWarnings("unchecked")
//    public static void contributeDAOSource(MappedConfiguration<Class, DAO> contributions,
//            ObjectLocator objectLocator) {
//        contributions.add(someEntityClass, (DAO) objectLocator.autobuild(someDaoImplementationClass));
//    }
    /**
     * Так будет выглядеть конфигурирование привязки Controller к представлениям при нестандартном именовании и размещении
     * Associates entity classes with their {@link Controller}s.
     *
     * @param contributions a {@link MappedConfiguration}.
     */
//    @SuppressWarnings("unchecked")
//    public static void contributeControllerSource(
//            MappedConfiguration<Class, Controller> contributions,
//            ObjectLocator objectLocator) {
//        contributions.add(someEntityClass, (Controller) objectLocator.autobuild(someDaoImplementationClass));
//    }
}
