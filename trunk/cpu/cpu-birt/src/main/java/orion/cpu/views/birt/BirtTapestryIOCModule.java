package orion.cpu.views.birt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
        "orion.birt.BIRT_VIEWER_DOCUMENT_FOLDER",
"orion.birt.BIRT_VIEWER_PRINT_SERVERSIDE",
"orion.birt.BIRT_VIEWER_CUBE_MEMORY_SIZE",
"orion.birt.BIRT_VIEWER_SCRIPTLIB_DIR",
"orion.birt.BIRT_VIEWER_IMAGE_DIR",
"orion.birt.BIRT_VIEWER_CONFIG_FILE",
"orion.birt.BIRT_VIEWER_MAX_ROWS",
"orion.birt.BIRT_VIEWER_MAX_CUBE_ROWLEVELS",
"orion.birt.BIRT_VIEWER_LOCALE",
"orion.birt.BIRT_VIEWER_MAX_CUBE_COLUMNLEVELS",
"orion.birt.BIRT_VIEWER_WORKING_FOLDER",
"orion.birt.BIRT_VIEWER_LOG_DIR",
"orion.birt.BIRT_RESOURCE_PATH",
"orion.birt.HTML_ENABLE_AGENTSTYLE_ENGINE",
"orion.birt.BIRT_VIEWER_LOG_LEVEL"};
    
    private static String[] BIRT_ENGINE_PATH = {"/document", "/download",
        "/parameter", "/extract", "/preview", "/output"};
    private static String[] BIRT_VIEW_PATH = {"/frameset", "/run"};

    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration,
            final SymbolSource symbols) {
        configuration.addInstance("CpuBirtInitializeDatabase", CpuBirtInitializeDatabase.class, "after:CpuTapestryInitializeDatabase");
//        configuration.add("birtServletInitParameters", new Runnable() {
//
//            @Override
//            public void run() {
//                for(String name:BIRT_PARAMETER_NAMES){
//                    BirtTapestryFilter.servletContext.setAttribute(name, symbols.valueForSymbol(name));
//                }
//            }
//        });
    }

//    public static HttpServletRequestFilter buildBirtEngineServletHttpServletRequestFilter(final SymbolSource symbols) {
//        final Servlet servlet = new org.eclipse.birt.report.servlet.BirtEngineServlet();
//        try {
//            servlet.init(new ServletConfig() {
//
//                @Override
//                public String getServletName() {
//                    return servlet.getClass().getName();
//                }
//
//                @Override
//                public ServletContext getServletContext() {
//                    return BirtTapestryFilter.servletContext;
//                }
//
//                @Override
//                public String getInitParameter(String name) {
//                    return BirtTapestryFilter.servletContext.getInitParameter(name);
//                }
//
//                @Override
//                public Enumeration getInitParameterNames() {
//                    return BirtTapestryFilter.servletContext.getInitParameterNames();
//                }
//            });
//        } catch (ServletException ex) {
//            throw new RuntimeException(ex.getMessage(), ex);
//        }
//        return new HttpServletRequestFilter() {
//
//            @Override
//            public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler) throws IOException {
//                String path = request.getServletPath();
//                String pathInfo = request.getPathInfo();
//                if (pathInfo != null) {
//                    path += pathInfo;
//                    for (String p : BIRT_ENGINE_PATH) {
//                        if (path.equals(p) || path.startsWith(p + "?")) {
//                            try {
//                                servlet.service(request, response);
//                                return false;
//                            } catch (ServletException e) {
//                                throw new IOException(e.getMessage(), e);
//                            }
//                        }
//                    }
//                }
//                return handler.service(request, response);
//            }
//        };
//    }
//
//    public static HttpServletRequestFilter buildBirtViewServletHttpServletRequestFilter(final SymbolSource symbols) {
//        final Servlet servlet = new org.eclipse.birt.report.servlet.ViewerServlet();
//        try {
//            servlet.init(new ServletConfig() {
//
//                @Override
//                public String getServletName() {
//                    return servlet.getClass().getName();
//                }
//
//                @Override
//                public ServletContext getServletContext() {
//                    return BirtTapestryFilter.servletContext;
//                }
//
//                @Override
//                public String getInitParameter(String name) {
//                    return BirtTapestryFilter.servletContext.getInitParameter(name);
//                }
//
//                @Override
//                public Enumeration getInitParameterNames() {
//                    return BirtTapestryFilter.servletContext.getInitParameterNames();
//                }
//            });
//        } catch (ServletException ex) {
//            throw new RuntimeException(ex.getMessage(), ex);
//        }
//        return new HttpServletRequestFilter() {
//
//            @Override
//            public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler) throws IOException {
//                String path = request.getServletPath();
//                String pathInfo = request.getPathInfo();
//                if (pathInfo != null) {
//                    path += pathInfo;
//                    for (String p : BIRT_VIEW_PATH) {
//                        if (path.equals(p) || path.startsWith(p + "?")) {
//                            try {
//                                servlet.service(request, response);
//                                return false;
//                            } catch (ServletException e) {
//                                throw new IOException(e.getMessage(), e);
//                            }
//                        }
//                    }
//                }
//                return handler.service(request, response);
//            }
//        };
//    }
//
//    public static HttpServletRequestFilter buildBirtViewerFilterHttpServletRequestFilter() {
//        final Filter filter = new org.eclipse.birt.report.filter.ViewerFilter();
//        final List<String> patterns = Arrays.asList(BIRT_VIEW_PATH);
//        patterns.addAll(Arrays.asList(BIRT_ENGINE_PATH));
//        return new HttpServletRequestFilter() {
//
//            @Override
//            public boolean service(final HttpServletRequest request, final HttpServletResponse response, final HttpServletRequestHandler handler) throws IOException {
//                String path = request.getServletPath();
//                String pathInfo = request.getPathInfo();
//                if (pathInfo != null) {
//                    path += pathInfo;
//                    for (String p : patterns) {
//                        if (path.equals(p) || path.startsWith(p + "/")) {
//                            try {
//                                filter.doFilter(request, response, new FilterChain() {
//
//                                    @Override
//                                    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
//                                    }
//                                });
//                            } catch (ServletException e) {
//                                throw new IOException(e.getMessage(), e);
//                            }
//                        }
//                    }
//                }
//                return handler.service(request, response);
//            }
//        };
//    }
//
//    public static void contributeHttpServletRequestHandler(
//            OrderedConfiguration<HttpServletRequestFilter> configuration,
//            @InjectService("BirtEngineServletHttpServletRequestFilter") HttpServletRequestFilter birtEngineServletHttpServletRequestFilter,
//            @InjectService("BirtViewServletHttpServletRequestFilter") HttpServletRequestFilter viewServletHttpServletRequestFilter,
//            @InjectService("BirtViewerFilterHttpServletRequestFilter") HttpServletRequestFilter birtViewerFilterHttpServletRequestFilter) {
//
//        configuration.add("birtEngineServlet",
//                birtEngineServletHttpServletRequestFilter, "after:birtViewerFilter");
//        configuration.add("birtViewServlet",
//                viewServletHttpServletRequestFilter, "after:birtViewerFilter");
//        configuration.add("birtViewerFilter",
//                birtViewerFilterHttpServletRequestFilter, "after:rolesso");
//    }

//    public static void contributeIgnoredPathsFilter(Configuration<String> configuration) {
//        configuration.add("/frameset*");
//	  configuration.add("/document*");
//	  configuration.add("/download*");
//	  configuration.add("/parameter*");
//	  configuration.add("/extract*"); 
//	  configuration.add("/run*");
//        configuration.add("/preview*");
//        configuration.add("/output*");
//
//    }

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
