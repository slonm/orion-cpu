package orion.cpu.views.tapestry.services;

import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.authentication.springsecurity.ioc.TapestrySpringSecurityGenericAuthenticationModule;
import br.com.arsmachina.module.service.PrimaryKeyTypeService;
import br.com.arsmachina.tapestrycrud.factory.PrimaryKeyEncoderFactory;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleFactory;
import java.io.IOException;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.*;
import org.slf4j.Logger;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.security.services.ExtendedAuthorizer;
import orion.cpu.views.tapestry.pages.ErrorReport;
import orion.cpu.views.tapestry.pages.MenuNavigator;
import orion.tapestry.menu.services.CpuMenuModule;

/**
 * Модуль конфигурирования IOC
 */
@SubModule(TapestrySpringSecurityGenericAuthenticationModule.class)
public class CpuTapestryIOCModule {

    public static final String LIB_NAME = "cpu";

    /**
     * Настройка путей к страницам системы безопасности.
     * @param configuration
     */
    public static void contributeFactoryDefaults(
            MappedConfiguration<String, String> configuration) {
        // tapestry-spring-security configurations
        final String errorPageUrl = "/" + ErrorReport.class.getSimpleName();
        configuration.override("spring-security.loginform.url", "/login");
        configuration.override("spring-security.failure.url", errorPageUrl + "/" + ErrorReport.LOGIN_FAILED);
        configuration.override("spring-security.accessDenied.url", errorPageUrl + "/" + ErrorReport.ACCESS_DENIED);
        configuration.override(CpuMenuModule.MENU_NAVIGATOR, LIB_NAME + "/" + MenuNavigator.class.getSimpleName());
    }

    /**
     * Регистрация библиотеки компонентов
     * @param configuration конфигурация
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping(LIB_NAME, "orion.cpu.views.tapestry"));
    }

    /**
     * Регистрация {@link BasePrimaryKeyEncoderFactory} как основного для
     * BaseEntity
     * @param configuration
     */
    public static void contributePrimaryKeyEncoderFactory(
            OrderedConfiguration<PrimaryKeyEncoderFactory> configuration) {

        configuration.addInstance("base", BasePrimaryKeyEncoderFactory.class);

    }

    /**
     * Добавление модуля типа {@link OrionTapestryCrudModuleFactory}.
     * @param contributions
     */
    public static void contributeTapestryCrudModuleFactory(
            OrderedConfiguration<TapestryCrudModuleFactory> contributions) {
        contributions.addInstance("orion", OrionTapestryCrudModuleFactory.class);
    }

    /**
     * Регистрация простой реализации {@link PrimaryKeyTypeService} для
     * BaseEntity.
     * @param configuration
     */
    public static void contributePrimaryKeyTypeService(
            OrderedConfiguration<PrimaryKeyTypeService> configuration) {

        configuration.add("base", new PrimaryKeyTypeService() {

            @Override
            public Class getPrimaryKeyType(Class entityClass) {
                return Integer.class;
            }

            @Override
            public String getPrimaryKeyPropertyName(Class entityClass) {
                return "id";
            }
        });
    }

    /**
     * Регистрация блоков для автоформирования моделей данных
     * @param configuration
     * @author sl
     */
    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
        //Display
        configuration.add(new BeanBlockContribution("boolean", "ori/PropertyBlocks", "displayBooleanText", false));
    }

    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     *
     * <p>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     *
     * <p>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     * @param log
     * @return
     */
    public RequestFilter buildTimingFilter(final Logger log) {
        return new RequestFilter() {

            @Override
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException {
                long startTime = System.currentTimeMillis();

                try {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     * @param configuration
     * @param filter
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
            @Local RequestFilter filter) {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Timing", filter);
    }

    /**
     * Начальная инициализация базы данных.
     * Установка текущего пользователя для обхода авторизации
     * Удаление текущего пользователя после инициализации.
     * @param configuration
     * @param authorizer 
     */
    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration,
            final ExtendedAuthorizer authorizer) {
        configuration.add("SetSSOUserSYSTEM", new Runnable() {

            @Override
            public void run() {
                authorizer.storeUserAndRole(User.SYSTEM_USER, null);
            }
        }, "before:*");

        configuration.add("UnSetSSOUser", new Runnable() {

            @Override
            public void run() {
                authorizer.storeUserAndRole(null, null);
            }
        }, "after:*");
    }

    /**
     * Регистрация ресурса из библиотеки cpu-core происходит тут, что-бы не вводить
     * зависимость от сервиса в cpu-core
     * @param configuration
     */
    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration) {
        configuration.add("Core", "classpath:CpuCore.properties");
        configuration.add("CpuTapestry", "classpath:CpuTapestry.properties");
    }

    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration) {
        add(configuration, Link.class, BaseEntity.class,
                new Coercion<Link, BaseEntity>() {

                    @Override
                    public BaseEntity coerce(Link input) {
                        return new PropertyOverridesImpl(input);
                    }
                });
    }

    private static <S, T> void add(Configuration<CoercionTuple> configuration, Class<S> sourceType, Class<T> targetType,
            Coercion<S, T> coercion) {
        CoercionTuple<S, T> tuple = new CoercionTuple<S, T>(sourceType, targetType, coercion);
        configuration.add(tuple);
    }
}
