package orion.tapestry.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.InternalSymbols;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;
import orion.tapestry.services.impl.*;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ValidationConstraintGenerator;
import orion.tapestry.beaneditor.HibernateAnnotationsConstraintGenerator;

/**
 * Модуль конфигурации IOC
 * @author sl
 */
public class TapestryExtentionsModule {

    public static void bind(ServiceBinder binder) {
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
            @Inject @Symbol(InternalSymbols.APP_NAME) String app_name){
        //Последний каталог это глобальный каталог приложения.
        configuration.add(SymbolConstants.APPLICATION_CATALOG, String.format("context:WEB-INF/%s.properties", app_name), "after:*");
    }
}
