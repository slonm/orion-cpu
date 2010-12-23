// Copyright 2008 Thiago H. de Paula Figueiredo
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package br.com.arsmachina.tapestrycrud.ioc;

import java.lang.reflect.Method;
import java.util.*;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.arsmachina.authentication.service.UserService;
import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.DefaultModule;
import br.com.arsmachina.module.Module;
import br.com.arsmachina.module.ioc.ApplicationModuleModule;
import br.com.arsmachina.module.service.ControllerSource;
import br.com.arsmachina.module.service.DataAwareObjectSource;
import br.com.arsmachina.module.service.DataAwareObjectSourceAdapter;
import br.com.arsmachina.module.service.EntitySource;
import br.com.arsmachina.module.service.ModuleService;
import br.com.arsmachina.module.service.PrimaryKeyTypeService;
import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.CrudEditPage;
import br.com.arsmachina.tapestrycrud.CrudListPage;
import br.com.arsmachina.tapestrycrud.CrudViewPage;
import br.com.arsmachina.tapestrycrud.beanmodel.AbstractBeanModelCustomizer;
import br.com.arsmachina.tapestrycrud.beanmodel.BeanModelWrapper;
import br.com.arsmachina.tapestrycrud.beanmodel.BeanModelCustomizer;
import br.com.arsmachina.tapestrycrud.beanmodel.EntityDataTypeAnalyzer;
import br.com.arsmachina.tapestrycrud.encoder.*;
import br.com.arsmachina.tapestrycrud.module.TapestryCrudModule;
import br.com.arsmachina.tapestrycrud.selectmodel.DefaultSingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.CrudSelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.impl.CrudSelectModelFactoryImpl;
import br.com.arsmachina.tapestrycrud.services.*;
import br.com.arsmachina.tapestrycrud.services.impl.*;
import br.com.arsmachina.tapestrycrud.tree.SingleTypeTreeService;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.services.*;

/**
 * Tapestry-IoC module for Tapestry CRUD.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestryCrudIoCModule {

    /**
     * Tapestry CRUD library prefix.
     */
    final public static String TAPESTRY_CRUD_COMPONENT_PREFIX =
            Constants.TAPESTRY_CRUD_LIBRARY_PREFIX;
    /**
     * Tapestry CRUD version.
     */
    final public static String TAPESTRY_CRUD_VERSION = "1.0";
    /**
     * Path under with the Tapestry CRUDs assets will be accessed.
     */
    final public static String TAPESTRY_CRUD_ASSET_PREFIX =
            "tapestry-crud/" + TAPESTRY_CRUD_VERSION;
    final private static Logger LOGGER =
            LoggerFactory.getLogger(TapestryCrudIoCModule.class);

    /**
     * Binds some Tapestry CRUD services.
     *
     * @param binder a {@link ServiceBinder}.
     */
    public static void bind(ServiceBinder binder) {

        binder.bind(UserService.class, UserServiceImpl.class);
        binder.bind(EntityDataTypeAnalyzer.class);
        binder.bind(FormValidationSupport.class,
                FormValidationSupportImpl.class);
        binder.bind(AuthorizationErrorMessageService.class, AuthorizationErrorMessageServiceImpl.class);
        binder.bind(BeanModelCustomizer.class, CollectiveSingleTypeBeanModelCustomizer.class).withId("");
        binder.bind(TreeServiceSource.class, TreeServiceSourceImpl.class);
        binder.bind(TapestryCrudModuleService.class, TapestryCrudModuleServiceImpl.class);
        binder.bind(CrudSelectModelFactory.class, CrudSelectModelFactoryImpl.class);
        binder.bind(BeanModelCustomizer.class, BeanModelCustomizerChain.class);
    }

    public static EncoderSource buildEncoderSource(@Autobuild EncoderSourceImpl service,
            @ComponentClasses InvalidationEventHub hub) {
        hub.addInvalidationListener(service);
        return service;
    }

    /**
     * Advise the {@link BeanModelSource} to apply the customizations
     * implemented by {@link BeanModelCustomizer}s.
     *
     * @param beanModelSource the decorated {@link BeanModelSource} object.
     * @param aspectDecorator an {@link AspectDecorator}.
     * @return a decorated {@link BeanModelSource}.
     */
    @Match("BeanModelSource")
    public static void adviseBeanModelSourceForCustomizer(MethodAdviceReceiver receiver,
            final BeanModelCustomizer customizer) {

        receiver.adviseMethod(getMethod(BeanModelSource.class,
                "createDisplayModel", Class.class, Messages.class), new MethodAdvice() {

            @SuppressWarnings("unchecked")
            public void advise(Invocation invocation) {
                invocation.proceed();
                BeanModel model = (BeanModel) invocation.getResult();
                model = customizer.customizeModel(model);
                model = customizer.customizeDisplayModel(model);
                invocation.overrideResult(model);
            }
        });
        receiver.adviseMethod(getMethod(BeanModelSource.class,
                "createEditModel", Class.class, Messages.class), new MethodAdvice() {

            @SuppressWarnings("unchecked")
            public void advise(Invocation invocation) {
                invocation.proceed();
                BeanModel model = (BeanModel) invocation.getResult();
                model = customizer.customizeModel(model);
                model = customizer.customizeEditModel(model);
                invocation.overrideResult(model);
            }
        });
    }

    private static Method getMethod(Class<?> type, String methodName, Class<?> ... methodArguments) {
        try {
            return type.getMethod(methodName, methodArguments);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Automatically contributes (class, {@link BeanModelCustomizer} pairs to the
     * {@link CollectiveSingleTypeBeanModelCustomizer} service.
     *
     * @param configuration um {@link MappedConfiguration}.
     * @param tapestryCrudModuleService a {@link TapestryCrudModuleService}.
     * @param objectLocator an {@link ObjectLocator}.
     */
    @SuppressWarnings("unchecked")
    public static void contributeCollectiveSingleTypeBeanModelCustomizer(
            MappedConfiguration<Class, BeanModelCustomizer> configuration,
            TapestryCrudModuleService tapestryCrudModuleService, ObjectLocator objectLocator) {

        final Set<TapestryCrudModule> modules = tapestryCrudModuleService.getModules();

        for (TapestryCrudModule module : modules) {

            final Set<Class<?>> entityClasses = module.getEntityClasses();

            for (Class entityClass : entityClasses) {

                Class<BeanModelCustomizer> customizerClass =
                        module.getBeanModelCustomizerClass(entityClass);

                if (customizerClass != null) {

                    BeanModelCustomizer customizer = objectLocator.autobuild(customizerClass);
                    configuration.add(entityClass, customizer);

                    if (LOGGER.isInfoEnabled()) {

                        final String entityName = entityClass.getSimpleName();
                        final String customizerClassName =
                                customizerClass.getName();
                        final String message =
                                String.format("Associating entity %s with bean model customizer %s",
                                entityName, customizerClassName);

                        LOGGER.info(message);

                    }

                }

            }

        }

    }

    /**
     * Automatically contributes (class, {@link SingleTypeTreeService} pairs to the
     * {@link TreeServiceSource} service.
     *
     * @param configuration um {@link MappedConfiguration}.
     * @param tapestryCrudModuleService a {@link TapestryCrudModuleService}.
     * @param objectLocator an {@link ObjectLocator}.
     */
    @SuppressWarnings("unchecked")
    public static void contributeTreeServiceSource(
            MappedConfiguration<Class, SingleTypeTreeService> configuration,
            TapestryCrudModuleService tapestryCrudModuleService, ObjectLocator objectLocator) {

        final Set<TapestryCrudModule> modules = tapestryCrudModuleService.getModules();

        for (TapestryCrudModule module : modules) {

            final Set<Class<?>> entityClasses = module.getEntityClasses();

            for (Class entityClass : entityClasses) {

                Class<SingleTypeTreeService> factoryClass =
                        module.getTreeServiceClass(entityClass);

                if (factoryClass != null) {

                    SingleTypeTreeService factory = objectLocator.autobuild(factoryClass);
                    configuration.add(entityClass, factory);

                    if (LOGGER.isInfoEnabled()) {

                        final String entityName = entityClass.getSimpleName();
                        final String factoryClassName =
                                factoryClass.getName();
                        final String message =
                                String.format("Associating entity %s with tree service %s",
                                entityName, factoryClassName);

                        LOGGER.info(message);

                    }

                }

            }

        }

    }

    /**
     * Builds the {@link ModuleService} service.
     *
     * @param contributions a {@link Map<Class, Controller>}.
     * @return an {@link ControllerSource}.
     */
    public static void contributeTapestryCrudModuleService(
            Configuration<TapestryCrudModule> contributions,
            ModuleService moduleService,
            ClassNameLocator classNameLocator,
            @Inject @Symbol(ApplicationModuleModule.DAO_IMPLEMENTATION_SUBPACKAGE_SYMBOL) String daoImplementationSubpackage,
            @Inject @Symbol(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM) final String tapestryRootPackage,
            TapestryCrudModuleFactory tapestryCrudModuleBuilder) {

        final Set<Module> modules = moduleService.getModules();

        for (Module module : modules) {
            contributions.add(tapestryCrudModuleBuilder.build(module));
        }

    }

    /**
     * Contributes the Tapestry CRUD components under the <code>crud</code>
     * prefix and all the {@link TapestryCrudModule}s with their id property as
     * the prefix.
     *
     * @param configuration a {@link Configuration}.
     */
    public static void contributeComponentClassResolver(
            Configuration<LibraryMapping> configuration,
            TapestryCrudModuleService tapestryCrudModuleService) {

        configuration.add(new LibraryMapping(
                Constants.TAPESTRY_CRUD_LIBRARY_PREFIX,
                "br.com.arsmachina.tapestrycrud"));

        final Set<TapestryCrudModule> modules =
                tapestryCrudModuleService.getModules();

        for (TapestryCrudModule module : modules) {

            final String id = module.getId();

            if (id != null && id.trim().length() > 0) {

                final String tapestryPackage = module.getTapestryPackage();
                configuration.add(new LibraryMapping(id, tapestryPackage));

            }

        }

    }

    /**
     * Contributes the main (default module) to the {@link ModuleService}
     * service.
     *
     * @param configuration a {@link Configuration} of {@link Module}s.
     */
    public static void contributeModuleService(
            Configuration<Module> configuration,
            ClassNameLocator classNameLocator,
            @Inject @Symbol(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM) final String tapestryRootPackage,
            @Inject @Symbol(ApplicationModuleModule.DAO_IMPLEMENTATION_SUBPACKAGE_SYMBOL) String daoImplementationSubpackage) {

        // The convention is that the module root is one package level above the
        // Tapestry root package

        String modulePackage =
                tapestryRootPackage.substring(0,
                tapestryRootPackage.lastIndexOf('.'));

        final DefaultModule module =
                new DefaultModule(modulePackage, classNameLocator,
                daoImplementationSubpackage);
        configuration.add(module);

    }

    /**
     * Associates entity classes with their {@link SelectModel}s.
     *
     * @param contributions a {@link MappedConfiguration}.
     */
    @SuppressWarnings("unchecked")
    public static void contributeCrudSelectModelFactory(
            MappedConfiguration<Class, SingleTypeSelectModelFactory> contributions,
            ControllerSource controllerSource, EntitySource entitySource,
            EncoderSource labelEncoderSource, ObjectLocator objectLocator,
            TapestryCrudModuleService tapestryCrudModuleService) {

        final Set<TapestryCrudModule> modules = tapestryCrudModuleService.getModules();

        for (TapestryCrudModule module : modules) {

            final Set<Class<?>> entityClasses = module.getEntityClasses();

            for (Class entityClass : entityClasses) {

                Class<SingleTypeSelectModelFactory> singleTypeSelectModelFactory =
                        module.getSingleTypeSelectModelFactory(entityClass);

                if (singleTypeSelectModelFactory != null) {

                    SingleTypeSelectModelFactory modelFactory = objectLocator.autobuild(singleTypeSelectModelFactory);
                    contributions.add(entityClass, modelFactory);

                    if (LOGGER.isInfoEnabled()) {

                        final String entityName = entityClass.getSimpleName();
                        final String customizerClassName =
                                singleTypeSelectModelFactory.getName();
                        final String message =
                                String.format("Associating entity %s with select model factory %s",
                                entityName, customizerClassName);

                        LOGGER.info(message);

                    }

                } else {
                    Controller controller = controllerSource.get(entityClass);
                    Encoder labelEncoder = labelEncoderSource.get(entityClass);

                    if (controller != null && labelEncoder != null) {

                        SingleTypeSelectModelFactory stsmf =
                                new DefaultSingleTypeSelectModelFactory(controller,
                                labelEncoder);

                        contributions.add(entityClass, stsmf);

                    }
                }

            }

        }

    }

    /**
     * Adds a listener to the {@link org.apache.tapestry5.internal.services.ComponentInstantiatorSource} that clears the
     * {@link DataAwareObjectSource} caches on
     * a class loader invalidation.
     */
    public void contributeApplicationInitializer(OrderedConfiguration<ApplicationInitializerFilter> configuration,
            @ComponentClasses final InvalidationEventHub invalidationEventHub,
            final DataAwareObjectSource service) {
        ApplicationInitializerFilter clearCaches = new ApplicationInitializerFilter() {

            public void initializeApplication(Context context, ApplicationInitializer initializer) {
                invalidationEventHub.addInvalidationListener(new InvalidationListener() {

                    public void objectWasInvalidated() {
                        service.clearCache();
                    }
                });
                initializer.initializeApplication(context);
            }
        };
        configuration.add("CrudClearCachesOnInvalidation", clearCaches);
    }

    public static void contributeDataAwareObjectSource(
            MappedConfiguration<Class, DataAwareObjectSourceAdapter> configuration,
            final EncoderSource encoderSource, final ValueEncoderSource valueEncoderSource,
            final TapestryCrudModuleService tapestryCrudModuleService,
            final ComponentSource componentSource) {
        configuration.add(Encoder.class, new DataAwareObjectSourceAdapter<Encoder>() {

            public Encoder get(Class entity) {
                return encoderSource.get(entity);
            }
        });

        configuration.add(ValueEncoder.class, new DataAwareObjectSourceAdapter<ValueEncoder>() {

            public ValueEncoder get(Class entity) {
                return valueEncoderSource.getValueEncoder(entity);
            }
        });

        configuration.add(CrudEditPage.class, new DataAwareObjectSourceAdapter() {

            public Object get(Class entity) {
                return componentSource.getPage(
                        tapestryCrudModuleService.getEditPageClass(entity));
            }
        });
        configuration.add(CrudViewPage.class, new DataAwareObjectSourceAdapter() {

            public Object get(Class entity) {
                return componentSource.getPage(
                        tapestryCrudModuleService.getViewPageClass(entity));
            }
        });
        configuration.add(CrudListPage.class, new DataAwareObjectSourceAdapter() {

            public Object get(Class entity) {
                return componentSource.getPage(
                        tapestryCrudModuleService.getListPageClass(entity));
            }
        });
    }

    public ValueEncoderSource buildCrudValueEncoderSource(
            final EncoderSource encoderSource) {
        return new ValueEncoderSource() {

            public <T> ValueEncoder<T> getValueEncoder(Class<T> type) {
                return encoderSource.get(type);
            }
        };
    }

    public void contributeServiceOverride(
            MappedConfiguration<Class, Object> configuration,
            @InjectService("CrudValueEncoderSource") ValueEncoderSource encoder) {
        configuration.add(ValueEncoderSource.class, encoder);
    }

    @SuppressWarnings("unchecked")
    public static void contributeEncoderSource(
            OrderedConfiguration<EncoderFactory> contributions,
            final TapestryCrudModuleService tapestryCrudModuleService,
            final ObjectLocator objectLocator) {
        contributions.addInstance("default", DefaultEncoderFactory.class, "after:*");
        contributions.add("module", new EncoderFactory() {

            public Encoder create(Class entityClass) {

                Class<?> encoderClass = tapestryCrudModuleService.getEncoderClass(entityClass);
                Encoder encoder = null;
                if (encoderClass != null) {
                    encoder = (Encoder) getServiceIfExists(encoderClass, objectLocator);
                    if (encoder == null) {
                        encoder = (Encoder) objectLocator.autobuild(encoderClass);
                    }

                    if (LOGGER.isInfoEnabled()) {
                        final String entityName = entityClass.getSimpleName();
                        final String encoderClassName = encoder.getClass().getName();
                        final String message =
                                String.format("Associating entity %s with encoder %s",
                                entityName, encoderClassName);
                        LOGGER.info(message);
                    }
                }
                return encoder;
            }
        }, "before:*");
    }

    /**
     * Builds the {@link TapestryCrudModuleBuilderFactory} service.
     *
     * @param contributions a {@link List} of {@link TapestryCrudModuleFactory}.
     * @param chainBuilder a {@link ChainBuilder}.
     * @return a {@link DAOFactory}.
     */
    public static TapestryCrudModuleFactory buildTapestryCrudModuleFactory(
            final List<TapestryCrudModuleFactory> contributions,
            ChainBuilder chainBuilder) {

        // default implementation.
        contributions.add(new TapestryCrudModuleFactoryImpl());

        return chainBuilder.build(TapestryCrudModuleFactory.class,
                contributions);

    }

    /**
     * Contributes {@link EntityDataTypeAnalyzer} to the
     * {@link DataTypeAnalyzer} service.
     *
     * @param configuration an {@link OrderedConfiguration}.
     * @param entityDataTypeAnalyzer an {@link EntityDataTypeAnalyzer}.
     */
    public static void contributeDataTypeAnalyzer(
            OrderedConfiguration<DataTypeAnalyzer> configuration,
            EntityDataTypeAnalyzer entityDataTypeAnalyzer) {

        configuration.add(Constants.ENTITY_DATA_TYPE, entityDataTypeAnalyzer,
                "before:Annotation");

    }

    /**
     * Contributes
     *
     * @param configuration a {@link Configuration}.
     */
    public static void contributeBeanBlockSource(
            Configuration<BeanBlockContribution> configuration) {

        configuration.add(new BeanBlockContribution(Constants.ENTITY_DATA_TYPE,
                Constants.BEAN_MODEL_BLOCKS_PAGE, "editEntity", true));

        configuration.add(new BeanBlockContribution(Constants.ENTITY_DATA_TYPE,
                Constants.BEAN_MODEL_BLOCKS_PAGE, "viewEntity", false));

    }

    /**
     * Returns a service if it exists. Otherwise, this method returns null.
     *
     * @param <T> type of service.
     * @param serviceInterface a {@link Class}.
     * @param objectLocator an {@link ObjectLocator}.
     * @return aa <code>T</code> or null.
     */
    private static <T> T getServiceIfExists(final Class<T> serviceInterface,
            ObjectLocator objectLocator) {

        try {
            return objectLocator.getService(serviceInterface);
        } catch (RuntimeException e) {
            return null;
        }

    }

    public static void cotributeBeanModelCustomizer(
            OrderedConfiguration<BeanModelCustomizer> contribution,
            final PrimaryKeyTypeService primaryKeyTypeService,
            @InjectService("CollectiveSingleTypeBeanModelCustomizer")
            final BeanModelCustomizer customizer) {
        contribution.add("collectiveSingleType", customizer);
        contribution.add("simpleNewObject", new AbstractBeanModelCustomizer() {

            @Override
            public BeanModel customizeModel(BeanModel model) {
                return new BeanModelWrapper(model);
            }
        });
        contribution.add("removePrimaryKeyProperty", new AbstractBeanModelCustomizer() {

            @Override
            public BeanModel customizeModel(BeanModel model) {
                return model.exclude(primaryKeyTypeService.getPrimaryKeyPropertyName(model.getBeanType()));
            }
        });
    }
}
