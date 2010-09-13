package orion.tapestry.services;

import java.net.URLStreamHandlerFactory;
import org.apache.tapestry5.*;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.*;
import org.apache.tapestry5.services.*;
import orion.tapestry.services.impl.*;
import orion.tapestry.beaneditor.JPAAnnotationsConstraintGenerator;
import orion.tapestry.utils.ByteArrayTranslator;

/**
 * Модуль конфигурации IOC
 * @author sl
 */
public class TapestryExtentionsModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(FieldLabelSource.class, FieldLabelSourceImpl.class);
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
     * Регистация обработчика аннотаций JPA для валидации в Beaneditor
     * @param configuration
     */
    public static void contributeValidationConstraintGenerator(
            OrderedConfiguration<ValidationConstraintGenerator> configuration) {
        configuration.add("JPAAnnotation", new JPAAnnotationsConstraintGenerator());
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

    public static void contributeTranslatorSource(Configuration<Translator> configuration) {

        configuration.add(new ByteArrayTranslator());
    }

    /**
     * from EventContext to Object[]
     * from String to Class
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
        add(configuration, String.class, Class.class,
                new Coercion<String, Class>() {

                    public Class coerce(String className) {
                        try {
                            return Class.forName(className);
                        } catch (ClassNotFoundException ex) {
                            return null;
                        }
                    }
                });
    }

    private static <S, T> void add(Configuration<CoercionTuple> configuration, Class<S> sourceType, Class<T> targetType,
            Coercion<S, T> coercion) {
        CoercionTuple<S, T> tuple = new CoercionTuple<S, T>(sourceType, targetType, coercion);
        configuration.add(tuple);
    }
}
