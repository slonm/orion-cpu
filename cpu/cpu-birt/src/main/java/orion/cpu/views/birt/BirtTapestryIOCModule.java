package orion.cpu.views.birt;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.apache.tapestry5.services.LibraryMapping;
import orion.cpu.entities.sys.ReportTemplate;
import orion.cpu.views.tapestry.services.MenuLinkBuilder;
import orion.tapestry.menu.lib.IMenuLink;

public class BirtTapestryIOCModule {

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, String> configuration) {
        //Temporary document files directory. Defaults to ${Context Root}/documents
        configuration.add("orion.birt.BIRT_VIEWER_DOCUMENT_FOLDER", "documents");
        //Flag whether to allow server-side printing. Possible values are ON and OFF. Defaults to ON
        configuration.add("orion.birt.BIRT_VIEWER_PRINT_SERVERSIDE", "ON");
        //Memory size in MB for creating a cube.
        configuration.add("orion.birt.BIRT_VIEWER_CUBE_MEMORY_SIZE", "");
        //Directory where to store all the birt report script libraries (JARs).. Defaults to ${Context Root}/scriptlib
        configuration.add("orion.birt.BIRT_VIEWER_SCRIPTLIB_DIR", "scriptlib");
        //Temporary images/charts directory. Defaults to ${Context Root}/report/images
        configuration.add("orion.birt.BIRT_VIEWER_IMAGE_DIR", "report/images");
        //BIRT viewer extended configuration file
        configuration.add("orion.birt.BIRT_VIEWER_CONFIG_FILE", "WEB-INF/viewer.properties");
        //Preview report rows limit. An empty value means no limit.
        configuration.add("orion.birt.BIRT_VIEWER_MAX_ROWS", "");
        //Max cube fetch rows levels limit for report preview (Only used when previewing a report design file using the preview pattern). Defaults to return all levels
        configuration.add("orion.birt.BIRT_VIEWER_MAX_CUBE_ROWLEVELS", "");
        //Default locale setting
        configuration.add("orion.birt.BIRT_VIEWER_LOCALE", "uk");
        //Max cube fetch columns levels limit for report preview (Only used when previewing a report design file using the preview pattern). Defaults to return all levels
        configuration.add("orion.birt.BIRT_VIEWER_MAX_CUBE_COLUMNLEVELS", "");
        //Report resources(design files or document files) directory for preview. Defaults to ${Context Root}
        configuration.add("orion.birt.BIRT_VIEWER_WORKING_FOLDER", "report");
        //Report Engine logs directory. Default to ${Context Root}/logs
        configuration.add("orion.birt.BIRT_VIEWER_LOG_DIR", "logs");
        //Resource location directory (library files, images files or others). Defaults to ${Context Root}
        configuration.add("orion.birt.BIRT_RESOURCE_PATH", "");
        //Flag whether to force browser-optimized HTML output. Defaults to true
        configuration.add("orion.birt.HTML_ENABLE_AGENTSTYLE_ENGINE", "true");
        //Report engine log level. (ALL|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|OFF)
        configuration.add("orion.birt.BIRT_VIEWER_LOG_LEVEL", "OFF");
    }
    private static String[] BIRT_PARAMETER_NAMES = {
        "BIRT_VIEWER_DOCUMENT_FOLDER",
        "BIRT_VIEWER_PRINT_SERVERSIDE",
        "BIRT_VIEWER_CUBE_MEMORY_SIZE",
        "BIRT_VIEWER_SCRIPTLIB_DIR",
        "BIRT_VIEWER_IMAGE_DIR",
        "BIRT_VIEWER_CONFIG_FILE",
        "BIRT_VIEWER_MAX_ROWS",
        "BIRT_VIEWER_MAX_CUBE_ROWLEVELS",
        "BIRT_VIEWER_LOCALE",
        "BIRT_VIEWER_MAX_CUBE_COLUMNLEVELS",
        "BIRT_VIEWER_WORKING_FOLDER",
        "BIRT_VIEWER_LOG_DIR",
        "BIRT_RESOURCE_PATH",
        "HTML_ENABLE_AGENTSTYLE_ENGINE",
        "BIRT_VIEWER_LOG_LEVEL"};
    private static String[] BIRT_ENGINE_PATH = {"/document", "/download",
        "/parameter", "/extract", "/preview", "/output"};
    private static String[] BIRT_VIEW_PATH = {"/frameset", "/run"};

    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration,
            final SymbolSource symbols) {
        configuration.addInstance("CpuBirtInitializeDatabase", CpuBirtInitializeDatabase.class, "after:CpuTapestryInitializeDatabase");
        configuration.add("birtServletInitParameters", new Runnable() {

            @Override
            public void run() {
                for (String name : BIRT_PARAMETER_NAMES) {
                    ServletConfigWrapper.initParametrs.put(name, symbols.valueForSymbol("orion.birt."+name));
                }
            }
        });
    }

    public static HttpServletRequestFilter buildBirtEngineServletHttpServletRequestFilter(final SymbolSource symbols) {
        final Servlet servlet = new org.eclipse.birt.report.servlet.BirtEngineServlet();
        try {
            servlet.init(new ServletConfigWrapper(servlet.getClass().getName()));
        } catch (ServletException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        return new HttpServletRequestFilter() {

            @Override
            public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler) throws IOException {
                String path = request.getServletPath();
                for (String p : BIRT_ENGINE_PATH) {
                    if (path.equalsIgnoreCase(p)) {
                        try {
                            servlet.service(request, response);
                            return true;
                        } catch (ServletException e) {
                            throw new IOException(e.getMessage(), e);
                        }
                    }
                }
                return handler.service(request, response);
            }
        };
    }

    public static HttpServletRequestFilter buildBirtViewServletHttpServletRequestFilter(final SymbolSource symbols) {
        final Servlet servlet = new org.eclipse.birt.report.servlet.ViewerServlet();
        try {
            servlet.init(new ServletConfigWrapper(servlet.getClass().getName()));
        } catch (ServletException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        return new HttpServletRequestFilter() {

            @Override
            public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler) throws IOException {
                String path = request.getServletPath();
                for (String p : BIRT_VIEW_PATH) {
                    if (path.equalsIgnoreCase(p)) {
                        try {
                            servlet.service(request, response);
                            return true;
                        } catch (ServletException e) {
                            throw new IOException(e.getMessage(), e);
                        }
                    }
                }
                return handler.service(request, response);
            }
        };
    }

    public static HttpServletRequestFilter buildBirtViewerFilterHttpServletRequestFilter() {
        final Filter filter = new org.eclipse.birt.report.filter.ViewerFilter();
        final List<String> patterns = new ArrayList<String>(Arrays.asList(BIRT_VIEW_PATH));
        patterns.addAll(Arrays.asList(BIRT_ENGINE_PATH));
        return new HttpServletRequestFilter() {

            @Override
            public boolean service(final HttpServletRequest request, final HttpServletResponse response, final HttpServletRequestHandler handler) throws IOException {
                String path = request.getServletPath();
                Boolean processed = false;
                final Boolean[] ret = new Boolean[1];
                for (String p : patterns) {
                    if (path.equalsIgnoreCase(p)) {
                        processed = true;
                        try {
                            filter.doFilter(request, response, new FilterChain() {

                                @Override
                                public void doFilter(ServletRequest request1, ServletResponse response1) throws IOException, ServletException {
                                    ret[0] = handler.service(request, response);
                                }
                            });
                        } catch (ServletException e) {
                            throw new IOException(e.getMessage(), e);
                        }
                    }
                }
                if (processed) {
                    return ret[0];
                } else {
                    return handler.service(request, response);
                }
            }
        };
    }

    public static void contributeHttpServletRequestHandler(
            OrderedConfiguration<HttpServletRequestFilter> configuration,
            @InjectService("BirtEngineServletHttpServletRequestFilter") HttpServletRequestFilter birtEngineServletHttpServletRequestFilter,
            @InjectService("BirtViewServletHttpServletRequestFilter") HttpServletRequestFilter viewServletHttpServletRequestFilter,
            @InjectService("BirtViewerFilterHttpServletRequestFilter") HttpServletRequestFilter birtViewerFilterHttpServletRequestFilter) {

        configuration.add("birtEngineServlet",
                birtEngineServletHttpServletRequestFilter, "after:birtViewerFilter");
        configuration.add("birtViewServlet",
                viewServletHttpServletRequestFilter, "after:birtViewerFilter");
        configuration.add("birtViewerFilter",
                birtViewerFilterHttpServletRequestFilter, "after:authorizerUserAndRole");
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(ReportPreviewLinkFactory.class, ReportPreviewLinkFactoryImpl.class);
    }

    /**
     * Регистрация библиотеки компонентов
     * @param configuration конфигурация
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("birt", "orion.cpu.views.birt"));
    }

    public static void contributeDataURLStreamHandler(MappedConfiguration<String, Class<?>> configuration) {
        configuration.add("rptdesign", DataRptDesignURLConnection.class);
    }

    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration) {
        configuration.add("BirtTapestry", "classpath:BirtTapestry.properties");
    }

    public static void contributeCpuMenu(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;
        path = "Start>Admin>Report";
        configuration.add(path, mlb.buildListPageMenuLink(ReportTemplate.class, path));
    }
}
