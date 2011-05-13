package ua.orion.web.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.services.DocumentLinker;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.*;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.javascript.StylesheetLink;
import org.slf4j.Logger;
import org.tynamo.shiro.extension.realm.text.ExtendedPropertiesRealm;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.core.services.ApplicationMessagesSource;
import ua.orion.core.services.EntityService;
import ua.orion.core.services.ModelLibraryService;
import ua.orion.core.utils.IOCUtils;
import ua.orion.web.BeanModelWrapper;
import static ua.orion.core.utils.IOCUtils.*;
import ua.orion.web.CompositeMessages;
import ua.orion.web.pages.Crud;
import ua.orion.web.pages.Index;
import ua.orion.web.pages.MenuNavigator;

/**
 *
 * @author slobodyanuk
 */
public class OrionWebIOCModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(TapestryComponentDataSource.class, TapestryComponentDataSourceImpl.class);
        binder.bind(MenuLinkBuilder.class);
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, String> configuration) {
        configuration.override(SymbolConstants.START_PAGE_NAME, "ori/index");
        //Это страница может и не понадобится, если шаблоны tml будут браться их базы
        configuration.add("cpumenu.navigatorpage", "ori/MenuNavigator");
    }

    public static void contributeMetaLinkCoercion(Configuration<Coercion> configuration,
            final EntityService entityService) {
        configuration.add(new Coercion<IMenuLink, Class<?>>() {

            @Override
            public Class<?> coerce(IMenuLink input) {
                try {
                    if (!"ori/crud".equalsIgnoreCase(input.getPage())
                            || input.getContext().length == 0) {
                        return null;
                    }
                    return entityService.getMetaEntity((String) input.getContext()[0]).getEntityClass();
                } catch (Exception ex) {
                }
                return null;
            }
        });
    }

    public static Coercion<IMenuLink, Class<?>> buildMetaLinkCoercion(Collection<Coercion> configuration,
            ChainBuilder chainBuilder) {
        return chainBuilder.build(Coercion.class, new ArrayList<Coercion>(configuration));
    }

    /**
     * Скрещиваем меню с системой безопасности
     * @param receiver приемник событий CpuMenu
     */
//    @Match("OrionMenuService")
//    public static void adviseCpuMenu(MethodAdviceReceiver receiver, @Autobuild CpuMenuMethodAdvice advice) {
//        receiver.adviseAllMethods(advice);
//    }
    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory
     */
    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb, final EntityService entityService) {
        String path;
        path = "Start";
        configuration.add(path, mlb.buildPageMenuLink("", path));
//        path = "Start>Admin>TML";
//        configuration.add(path, mlb.buildListPageMenuLink(PageTemplate.class, path));
        //add All Reference Book
        for (Class<?> e : entityService.getManagedEntities()) {
            javax.persistence.Table a = e.getAnnotation(javax.persistence.Table.class);
            if (a != null && "ref".equals(a.schema())) {
                path = "Start>Admin>Reference>" + e.getSimpleName();
                configuration.add(path, mlb.buildCrudPageMenuLink(e, path));
            }
        }
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("ori", "ua.orion.web"));
    }

    public void contributeApplicationMessagesSource(Configuration<String> conf,
            ComponentClassResolver componentClassResolver) {
        for (String lib : componentClassResolver.getFolderToPackageMapping().keySet()) {
              conf.add(lib+"Web");		
          }
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
     * from EventContext to Object[]
     * from IMenuLink to Class
     */
    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration,
            @InjectService("MetaLinkCoercion") Coercion coercion) {
        addTuple(configuration, IMenuLink.class, Class.class, coercion);
        addTuple(configuration, EventContext.class, Object[].class,
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

    @Match("ComponentMessagesSource")
    public static void adviseComponentMessagesSourceWithApplicationMessagesSource(MethodAdviceReceiver receiver,
            final ApplicationMessagesSource messagesSource) {
        receiver.adviseAllMethods(new MethodAdvice() {

            @Override
            public void advise(Invocation invocation) {
                invocation.proceed();
                if (invocation.getMethodName().equals("getMessages")) {
                    invocation.overrideResult(new CompositeMessages((Locale) invocation.getParameter(1),
                            messagesSource.getMessages((Locale) invocation.getParameter(1)),
                            (Messages) invocation.getResult()));
                }
            }
        });
    }

    public static void contributeDataTypeAnalyzer(
            OrderedConfiguration<DataTypeAnalyzer> configuration,
            final EntityService entityService) {

        configuration.add("entity", new DataTypeAnalyzer() {

            @Override
            public String identifyDataType(PropertyAdapter adapter) {
                return entityService.getManagedEntities().contains(adapter.getType()) ? "entity" : null;
            }
        }, "before:Annotation");

    }

    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
        configuration.add(new EditBlockContribution("rang", "PropertyEditBlocks", "number"));
        configuration.add(new EditBlockContribution("entity", "ori/PropertyBlocks", "editEntity"));
        configuration.add(new DisplayBlockContribution("entity", "ori/PropertyBlocks", "displayEntity"));
        configuration.add(new DisplayBlockContribution("boolean", "ori/PropertyBlocks", "displayBooleanCheck"));
//        configuration.add(new DisplayBlockContribution("boolean", "ori/PropertyBlocks", "displayBooleanText"));
        configuration.add(new DisplayBlockContribution("booleanSelect", "ori/PropertyBlocks", "displayBooleanText"));
        configuration.add(new EditBlockContribution("booleanSelect", "ori/PropertyBlocks", "editBooleanSelect"));
    }

    /*
     * Позволяет BeanEditor создавать новый бин, если он не существовал, стандартными средствами
     */
    @Match("BeanModelSource")
    public static void adviseBeanModelSourceWithBeanModelWrapper(MethodAdviceReceiver receiver) {

        MethodAdvice advice = new MethodAdvice() {

            @Override
            public void advise(Invocation invocation) {
                invocation.proceed();
                invocation.overrideResult(new BeanModelWrapper((BeanModel) invocation.getResult()));
            }
        };
        receiver.adviseMethod(getMethod(BeanModelSource.class, "createEditModel", Class.class, Messages.class), advice);
    }

    /**
     * Патч позволяет делать редирект на "" если index в библиотеке
     */
    @Match("ComponentClassResolver")
    public static void adviseComponentClassResolverWithIndexInLib(MethodAdviceReceiver receiver) {

        MethodAdvice advice = new MethodAdvice() {

            @Override
            public void advise(Invocation invocation) {
                if ("".equals(invocation.getParameter(0).toString())) {
                    invocation.override(0, "ori/index");
                }
                invocation.proceed();
            }
        };
        receiver.adviseMethod(IOCUtils.getMethod(ComponentClassResolver.class, "canonicalizePageName", String.class), advice);
    }

    /**
     * Добавляет CSS соответствующие библиотекам сущностей
     */
    public void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration,
            final AssetSource assetSource, final Environment environment,
            final ModelLibraryService modelLibraryService) {
        for (ModelLibraryInfo modelLibraryInfo : modelLibraryService.getModelLibraryInfos()) {
            try {
                final Asset stylesheet = assetSource.getClasspathAsset("ua/orion/web/" + modelLibraryInfo.getLibraryName() + ".css");
                MarkupRendererFilter injectDefaultStylesheet = new MarkupRendererFilter() {

                    public void renderMarkup(MarkupWriter writer, MarkupRenderer renderer) {
                        DocumentLinker linker = environment.peekRequired(DocumentLinker.class);
                        linker.addStylesheetLink(new StylesheetLink(stylesheet.toClientURL()));
                        renderer.renderMarkup(writer);
                    }
                };
                configuration.add("Inject" + modelLibraryInfo.getLibraryName() + "Styleheet",
                        injectDefaultStylesheet, "after:InjectDefaultStyleheet");
            } catch (RuntimeException e) {
            }
        }
    }
}
