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
package br.com.arsmachina.module.ioc;

import java.util.List;
import java.util.Set;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.services.ChainBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.authorization.SingleTypeAuthorizer;
import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.Module;
import br.com.arsmachina.module.factory.*;
import br.com.arsmachina.module.factory.impl.*;
import br.com.arsmachina.module.service.*;
import br.com.arsmachina.module.service.internal.*;
import org.apache.tapestry5.ioc.ServiceBinder;

/**
 * Tapestry-IoC module class for Application Module.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ApplicationModuleModule {

    /**
     * Symbol used to define the DAO implementation subpackage under <code>dao</code>. The default
     * value is <code>impl</code>.
     */
    final public static String DAO_IMPLEMENTATION_SUBPACKAGE_SYMBOL = "application-module.dao-implementation-package";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationModuleModule.class);

    /**
     * Binds some services.
     *
     * @param binder a {@link ServiceBinder}.
     */
    public static void bind(ServiceBinder binder) {

        binder.bind(ModuleService.class, ModuleServiceImpl.class);
        binder.bind(ControllerSource.class, ControllerSourceImpl.class);
        binder.bind(DAOSource.class, DAOSourceImpl.class);
        binder.bind(EntitySource.class, EntitySourceImpl.class);
        binder.bind(DataAwareObjectSource.class, DataAwareObjectSourceImpl.class);
    }

    public static void contributeDataAwareObjectSource(
            MappedConfiguration<Class, DataAwareObjectSourceAdapter> configuration,
            final DAOSource _DAOSource, final ControllerSource controllerSource) {
        configuration.add(DAO.class, new DataAwareObjectSourceAdapter<DAO>() {

            public DAO get(Class entity) {
                return _DAOSource.get(entity);
            }
        });
        configuration.add(Controller.class, new DataAwareObjectSourceAdapter<Controller>() {

            public Controller get(Class entity) {
                return controllerSource.get(entity);
            }
        });
    }

    /**
     * Defines the default value for {@link #DAO_IMPLEMENTATION_SUBPACKAGE_SYMBOL}.
     *
     * @param configuration a {@link MappedConfiguration}.
     */
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {

        configuration.add(DAO_IMPLEMENTATION_SUBPACKAGE_SYMBOL, "impl");
    }

    /**
     * Contributes the {@link Module}s entity classes to the {@link EntitySource} service.
     *
     * @param configuration a {@link Configuration} of {@link Class} instances.
     * @param moduleService a {@link ModuleService}.
     */
    public static void contributeEntitySource(Configuration<Class<?>> configuration,
            ModuleService moduleService) {

        for (Class<?> entityClass : moduleService.getEntityClasses()) {
            configuration.add(entityClass);
        }

    }

    /**
     * Associates entity classes with their {@link Controller}s.
     *
     * @param contributions a {@link MappedConfiguration}.
     */
    @SuppressWarnings("unchecked")
    public static void contributeControllerSource(
            MappedConfiguration<Class, ControllerSourceContributionsValue> contributions, EntitySource entitySource,
            ModuleService moduleService) {

        final Set<Class<?>> entityClasses = entitySource.getEntityClasses();

        for (Class<?> entityClass : entityClasses) {

            final Class<?> controllerDefinitionClass = moduleService.getControllerDefinitionClass(entityClass);
            final Class<?> controllerImplementationClass = moduleService.getControllerImplementationClass(entityClass);

            // If the entity class has no controller definition (interface), we don't register
            // a controller for it.
            if (controllerDefinitionClass != null) {

                contributions.add(entityClass, new ControllerSourceContributionsValue(controllerImplementationClass, controllerDefinitionClass));

                if (LOGGER.isInfoEnabled()) {

                    final String entityName = entityClass.getSimpleName();
                    final String controllerClassName = controllerDefinitionClass.getName();
                    final String message = String.format(
                            "Associating entity %s with controller %s", entityName,
                            controllerClassName);

                    LOGGER.info(message);

                }

            }

        }

    }

    /**
     * Associates entity classes with their {@link DAO}s.
     *
     * @param contributions a {@link MappedConfiguration}.
     */
    @SuppressWarnings("unchecked")
    public static void contributeDAOSource(MappedConfiguration<Class, DAOSourceContributionsValue> contributions,
            EntitySource entitySource, ModuleService moduleService, ObjectLocator objectLocator) {

        final Set<Class<?>> entityClasses = entitySource.getEntityClasses();

        for (Class<?> entityClass : entityClasses) {

            final Class<?> daoDefinitionClass = moduleService.getDAODefinitionClass(entityClass);
            final Class<?> daoImplementationClass = moduleService.getDAOImplementationClass(entityClass);

            // If the entity class has no dao definition (interface), we don't register
            // a dao for it.
            if (daoDefinitionClass != null && daoImplementationClass != null) {

                contributions.add(entityClass, new DAOSourceContributionsValue(daoImplementationClass, daoDefinitionClass));

                if (LOGGER.isInfoEnabled()) {

                    final String entityName = entityClass.getSimpleName();
                    final String daoClassName = daoDefinitionClass.getName();
                    final String message = String.format("Associating entity %s with DAO %s",
                            entityName, daoClassName);

                    LOGGER.info(message);

                }

            }

        }

    }

    /**
     * Builds the {@link DAOFactory} service.
     *
     * @param contributions a {@link List} of {@link DAOFactory}.
     * @param chainBuilder a {@link ChainBuilder}.
     * @return a {@link DAOFactory}.
     */
    public static DAOFactory buildDAOFactory(final List<DAOFactory> contributions,
            ChainBuilder chainBuilder) {

        return chainBuilder.build(DAOFactory.class, contributions);

    }

    /**
     * Builds the {@link ControllerFactory} service.
     *
     * @param contributions a {@link List} of {@link ControllerFactory}.
     * @param chainBuilder a {@link ChainBuilder}.
     * @return a {@link ControllerFactory}.
     */
    public static ControllerFactory buildControllerFactory(
            final List<ControllerFactory> contributions, ChainBuilder chainBuilder) {

        return chainBuilder.build(ControllerFactory.class, contributions);

    }

    /**
     * Contributes {@link ControllerFactoryImpl} to the {@link ControllerFactory} service.
     *
     * @param configuration an {@link OrderedConfiguration} of {@link ControllerFactory}'s.
     * @param daoSource a {@link DAOSource}.
     */
    public static void contributeControllerFactory(
            OrderedConfiguration<ControllerFactory> configuration, DAOSource daoSource) {

        configuration.add("default", new ControllerFactoryImpl(daoSource), "after:*");

    }

    /**
     * Builds the {@link DefaultLabelEncoderFactory} service.
     *
     * @param contributions a {@link List} of {@link PrimaryKeyTypeService}.
     * @param chainBuilder a {@link ChainBuilder}.
     * @return a {@link PrimaryKeyTypeService}.
     */
    public PrimaryKeyTypeService buildPrimaryKeyTypeService(
            final List<PrimaryKeyTypeService> contributions, ChainBuilder chainBuilder) {

        return chainBuilder.build(PrimaryKeyTypeService.class, contributions);

    }

    /**
     * Contributes the {@link JPAPrimaryKeyTypeService} to the {@link PrimaryKeyTypeService}
     * service.
     *
     * @param configuration an {@link OrderedConfiguration} of {@link PrimaryKeyTypeService}'s.
     * @param sessionFactory a {@link SessionFactory}.
     */
    public static void contributePrimaryKeyTypeService(
            OrderedConfiguration<PrimaryKeyTypeService> configuration) {

        configuration.add("default", new JPAPrimaryKeyTypeService(), "after:*");

    }

    /**
     * Automatically contributes (class, {@link SingleTypeAuthorizer} pairs to the
     * {@link Authorizer} service.
     *
     * @param configuration um {@link MappedConfiguration}.
     * @param tapestryCrudModuleService a {@link TapestryCrudModuleService}.
     * @param objectLocator an {@link ObjectLocator}.
     */
    @SuppressWarnings("unchecked")
    public static void contributeAuthorizer(
            MappedConfiguration<Class, SingleTypeAuthorizer> configuration,
            ModuleService moduleService, ObjectLocator objectLocator) {

        final Set<Module> modules = moduleService.getModules();

        for (Module module : modules) {

            final Set<Class<?>> entityClasses = module.getEntityClasses();

            for (Class entityClass : entityClasses) {

                Class<SingleTypeAuthorizer> authorizerClass =
                        module.getAuthorizerClass(entityClass);

                if (authorizerClass != null) {

                    SingleTypeAuthorizer customizer = objectLocator.autobuild(authorizerClass);
                    configuration.add(entityClass, customizer);

                    if (LOGGER.isInfoEnabled()) {

                        final String entityName = entityClass.getSimpleName();
                        final String authorizerClassName =
                                authorizerClass.getName();
                        final String message =
                                String.format("Associating entity %s with authorizer %s",
                                entityName, authorizerClassName);

                        LOGGER.info(message);

                    }

                }

            }

        }

    }
}
