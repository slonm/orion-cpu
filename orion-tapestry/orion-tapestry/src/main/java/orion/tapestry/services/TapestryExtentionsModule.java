package orion.tapestry.services;

import java.net.URLStreamHandlerFactory;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.internal.InternalSymbols;
import org.apache.tapestry5.internal.services.ComponentTemplateSource;
import org.apache.tapestry5.internal.services.PageTemplateLocator;
import org.apache.tapestry5.internal.services.TemplateParser;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;
import orion.tapestry.services.impl.*;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.UpdateListenerHub;
import org.apache.tapestry5.services.ValidationConstraintGenerator;
import orion.tapestry.beaneditor.HibernateAnnotationsConstraintGenerator;
import orion.tapestry.utils.ByteArrayTranslator;

/**
 * Модуль конфигурации IOC
 * @author sl
 */
public class TapestryExtentionsModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(URLStreamHandlerFactory.class, URLStreamHandlerFactoryImpl.class);
        binder.bind(FieldLabelSource.class, FieldLabelSourceImpl.class);
        binder.bind(GlobalMessageAppender.class);
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, String> configuration) {
        //Запретим использовать глобальные ресурсы стандартными средствами
        //вместо этого модифицируем работу сервиса ComponentMessagesSourceImpl
        configuration.override(SymbolConstants.APPLICATION_CATALOG, "*");
    }

    /**
     * Регистрация блоков для автоформирования beanmodel
     * @param configuration конфигурация
     */
    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
        configuration.add(new BeanBlockContribution("booleanCheckBox", "ori/PropertyBlocks", "displayBooleanCheckBox", false));
        configuration.add(new BeanBlockContribution("booleanText", "ori/PropertyBlocks", "displayBooleanText", false));
    }

    /**
     * Регистрация библиотеки компонентов
     * @param configuration конфигурация
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("ori", "orion.tapestry"));
    }

    /**
     * Регистация обработчика аннотаций Hibernate для валидации в Beaneditor
     * @param configuration
     */
    public static void contributeValidationConstraintGenerator(
            OrderedConfiguration<ValidationConstraintGenerator> configuration) {
        configuration.add("HibernateField", new HibernateAnnotationsConstraintGenerator());
    }

    /**
     * Поддержка аннотации FieldLabel
     * @param receiver приемник событий BeanModelSource
     * @param fieldLabelSource сервис {@link FieldLabelSource}
     */
    @Match("BeanModelSource")
    public static void adviseBeanModelSource(MethodAdviceReceiver receiver, FieldLabelSource fieldLabelSource) {
        MethodAdvice advice = new BeanModelSourceMethodAdvice(fieldLabelSource);
        receiver.adviseAllMethods(advice);
    }

    @Match("ComponentMessagesSource")
    public static void adviseComponentMessagesSource(MethodAdviceReceiver receiver,
            GlobalMessageAppender globalMessageAppender) {
        receiver.adviseAllMethods(globalMessageAppender);
    }

    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration,
            @Inject @Symbol(InternalSymbols.APP_NAME) String app_name) {
        //Последний каталог это глобальный каталог приложения.
        configuration.add(SymbolConstants.APPLICATION_CATALOG, String.format("context:WEB-INF/%s.properties", app_name), "after:*");
    }

    public static void contributeTranslatorSource(Configuration<Translator> configuration) {

        configuration.add(new ByteArrayTranslator());
    }

    public ComponentTemplateSource buildOrionComponentTemplateSourceImpl(TemplateParser parser,
            PageTemplateLocator locator,
            ClasspathURLConverter classpathURLConverter,
            UpdateListenerHub updateListenerHub) {
        OrionComponentTemplateSourceImpl service = new OrionComponentTemplateSourceImpl(parser, locator, classpathURLConverter);

        updateListenerHub.addUpdateListener(service);

        return service;
    }

    public void contributeServiceOverride(
            MappedConfiguration<Class, Object> configuration,
            @Local ComponentTemplateSource componentTemplateSource) {
        configuration.add(ComponentTemplateSource.class, componentTemplateSource);
    }

    /**
     * Coertion from EventContext to Object[]
     */
    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration) {
        add(configuration, EventContext.class, Object[].class,
                new Coercion<EventContext, Object[]>() {

                    public Object[] coerce(EventContext context) {
                        int count = context.getCount();
                        Object[] result = new Object[count];
                        for (int i = 0; i < count; i++) {
                            result[i] = context.get(Object.class, i);
                        }
                        return result;
                    }
                });
    }

    private static <S, T> void add(Configuration<CoercionTuple> configuration, Class<S> sourceType, Class<T> targetType,
            Coercion<S, T> coercion) {
        CoercionTuple<S, T> tuple = new CoercionTuple<S, T>(sourceType, targetType, coercion);
        configuration.add(tuple);
    }
}
