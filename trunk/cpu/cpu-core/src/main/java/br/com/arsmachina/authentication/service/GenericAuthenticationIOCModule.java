package br.com.arsmachina.authentication.service;

import br.com.arsmachina.authentication.controller.*;
import br.com.arsmachina.authentication.controller.impl.*;
import br.com.arsmachina.module.DefaultModule;
import br.com.arsmachina.module.Module;
import br.com.arsmachina.module.ioc.ApplicationModuleModule;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ClassNameLocator;

/**
 * Конфигурация IOC для подсистемы безопасности
 * @author sl
 */
public class GenericAuthenticationIOCModule {

    /**
     * Регистрация сервисов и привязка к ним реализаций
     * @param binder
     */
    public static void bind(ServiceBinder binder) {
        binder.bind(UserController.class, UserControllerImpl.class);
        binder.bind(PermissionController.class, PermissionControllerImpl.class);
        binder.bind(PermissionGroupController.class, PermissionGroupControllerImpl.class);
        binder.bind(RoleController.class, RoleControllerImpl.class);
    }

    /**
     * Добавляет модуль для пакетов системы безопасности ArsMachina.
     * Так как в отличии от ArsMachina используется Tapestry IOC вместо Spring
     * @param configuration
     * @param classNameLocator
     * @param daoImplementationSubpackage
     */
    public static void contributeModuleService(Configuration<Module> configuration,
            ClassNameLocator classNameLocator,
            @Inject
            @Symbol(ApplicationModuleModule.DAO_IMPLEMENTATION_SUBPACKAGE_SYMBOL) String daoImplementationSubpackage) {

        configuration.add(new DefaultModule(null,
                "br.com.arsmachina.authentication", classNameLocator, daoImplementationSubpackage));
    }

    /**
     * Регистрирует пакет с сущностями системы безопасности.
     * @param configuration
     */
    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration) {
        configuration.add("br.com.arsmachina.authentication.entity");
    }
}
