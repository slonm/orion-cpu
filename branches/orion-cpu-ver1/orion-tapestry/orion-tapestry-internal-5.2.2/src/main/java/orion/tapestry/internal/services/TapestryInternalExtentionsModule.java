package orion.tapestry.internal.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.*;
import org.apache.tapestry5.internal.services.*;
import org.apache.tapestry5.internal.services.templates.PageTemplateLocator;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.services.UpdateListenerHub;
import orion.tapestry.internal.services.impl.*;
import orion.tapestry.internal.utils.TapestryInternalUtilsWrapperImpl;
import orion.tapestry.services.TapestryInternalUtilsWrapper;

/**
 *
 * @author sl
 */
public class TapestryInternalExtentionsModule {

    private static final String TMLinDatabase = "orion.tapestry.useTMLinDatabase";

    public static void bind(ServiceBinder binder) {
        binder.bind(GlobalMessagesImpl.class, GlobalMessagesImpl.class).withId("GlobalMessageAppender");
        binder.bind(TapestryInternalUtilsWrapper.class, TapestryInternalUtilsWrapperImpl.class);
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, String> configuration) {
        //Запретим использовать глобальные ресурсы стандартными средствами
        //вместо этого модифицируем работу сервиса ComponentMessagesSourceImpl
        configuration.override(SymbolConstants.APPLICATION_CATALOG, "*");
        configuration.add(TMLinDatabase, "false");
    }

    @Match("ComponentMessagesSource")
    public static void adviseComponentMessagesSource(MethodAdviceReceiver receiver,
            GlobalMessagesImpl globalMessageAppender) {
        receiver.adviseAllMethods(globalMessageAppender);
    }

    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration,
            @Inject @Symbol(InternalSymbols.APP_NAME) String app_name) {
        //Последний каталог это глобальный каталог приложения.
        configuration.add(SymbolConstants.APPLICATION_CATALOG, String.format("context:WEB-INF/%s.properties", app_name), "after:*");
    }

    public ComponentTemplateSource buildOrionComponentTemplateSourceImpl(TemplateParser parser,
            @Autobuild OrionComponentTemplateSourceImpl service,
            UpdateListenerHub updateListenerHub) {
        updateListenerHub.addUpdateListener(service);

        return service;
    }

    public void contributeServiceOverride(
            MappedConfiguration<Class, Object> configuration,
            @Local ComponentTemplateSource componentTemplateSource) {
        configuration.add(ComponentTemplateSource.class, componentTemplateSource);
    }

    @Match("PageTemplateLocator")
    public static void advisePageTemplateLocator(MethodAdviceReceiver receiver, ObjectLocator locator,
            @Inject @Value("${"+TMLinDatabase+"}") Boolean isUse) {
        if (isUse) {
            MethodAdvice advice = locator.autobuild(PageTemplateLocatorAdvice.class);
            receiver.adviseAllMethods(advice);
        }
    }
}
