package orion.cpu.views.tapestry.services;

import br.com.arsmachina.authentication.springsecurity.ioc.TapestrySpringSecurityGenericAuthenticationModule;
import br.com.arsmachina.module.service.PrimaryKeyTypeService;
import br.com.arsmachina.tapestrycrud.factory.PrimaryKeyEncoderFactory;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleFactory;
import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.*;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.*;
import org.apache.tapestry5.services.*;
import org.slf4j.Logger;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.sys.PageTemplate;
import orion.cpu.views.tapestry.pages.Edit;
import orion.cpu.views.tapestry.pages.ErrorReport;
import orion.cpu.views.tapestry.pages.Index;
import orion.cpu.views.tapestry.pages.ListView;
import orion.cpu.views.tapestry.pages.MenuNavigator;
import orion.cpu.views.tapestry.utils.DataTMLURLConnection;
import orion.tapestry.menu.lib.IMenuLink;
import orion.tapestry.services.FieldLabelSource;

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
        //Это страница может и не понадобится, если шаблоны tml будут браться их базы
        configuration.add("cpumenu.navigatorpage", MenuNavigator.class.getCanonicalName());
        configuration.add("security.logout.url", "/index.layout.logout");
        configuration.override("orion.tapestry.useTMLinDatabase", "true");
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
     * Регистрация ресурса из библиотеки cpu-core происходит тут, что-бы не вводить
     * зависимость от сервиса в cpu-core
     * @param configuration
     */
    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration) {
        configuration.add("Core", "classpath:CpuCore.properties");
        configuration.add("CpuTapestry", "classpath:CpuTapestry.properties");
    }

    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration,
            @InjectService("MetaLinkCoercion") Coercion coercion) {
        configuration.add(new CoercionTuple(IMenuLink.class, Class.class, coercion));
    }

    public static void contributeMetaLinkCoercion(Configuration<Coercion> configuration) {
        configuration.addInstance(ListView.MetaLinkCoercion.class);
        configuration.addInstance(Edit.MetaLinkCoercion.class);
    }

    public static Coercion<IMenuLink, BaseEntity> buildMetaLinkCoercion(Collection<Coercion> configuration,
            ChainBuilder chainBuilder) {
        return chainBuilder.build(Coercion.class, new ArrayList<Coercion>(configuration));
    }

    /**
     * Скрещиваем меню с системой безопасности
     * @param receiver приемник событий CpuMenu
     * @param fieldLabelSource сервис {@link FieldLabelSource}
     */
    @Match("CpuMenu")
    public static void adviseCpuMenu(MethodAdviceReceiver receiver, @Autobuild CpuMenuMethodAdvice advice) {
        receiver.adviseAllMethods(advice);
    }

    @Match("RequestExceptionHandler")
    public static void adviseRequestExceptionHandler(MethodAdviceReceiver receiver, ObjectLocator locator) {
        MethodAdvice advice = locator.autobuild(RequestExceptionHandlerAdvice.class);
        receiver.adviseAllMethods(advice);
    }

    @Match("LocalizationSetter")
    public static void adviseLocalizationSetter(MethodAdviceReceiver receiver,
            @Autobuild LocalizationSetterAdvice advice) {
        receiver.adviseAllMethods(advice);
    }

    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory
     */
    public static void contributeCpuMenu(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;
        path = "Start";
        configuration.add(path, mlb.buildPageMenuLink(Index.class, path));
        path = "Start>Admin";
        configuration.add(path, mlb.buildDefaultMenuLink(path));
        path = "Start>Admin>TML";
        configuration.add(path, mlb.buildListPageMenuLink(PageTemplate.class, path));
    }

    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration,
            URLStreamHandlerFactory _URLStreamHandlerFactory) {
        //Вызывается что-бы явно построить фабрику перед регистрацией ее
        //иначе возникает ошибка циклической зависимоси
        _URLStreamHandlerFactory.createURLStreamHandler("ff");
        URL.setURLStreamHandlerFactory(_URLStreamHandlerFactory);
        configuration.addInstance("CpuTapestryInitializeDatabase", CpuTapestryInitializeDatabase.class, "after:InitializeDatabase");
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(DataURLStreamHandler.class);
        binder.bind(MenuLinkBuilder.class);
    }

    public static void contributeURLStreamHandlerFactory(MappedConfiguration<String, URLStreamHandler> configuration,
            DataURLStreamHandler dataURLStreamHandler) {
        configuration.add("data", dataURLStreamHandler);
    }

    public static void contributeDataURLStreamHandler(MappedConfiguration<String, Class<?>> configuration) {
        configuration.add("tml", DataTMLURLConnection.class);
    }
}
