package orion.cpu.views.birt;

import java.io.IOException;
import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import javax.servlet.*;

public class BirtTapestryFilter implements Filter {

    private final TapestryFilter tapestryFilter = new TapestryFilter() {

        @Override
        protected void init(Registry registry) throws ServletException {
            BirtConnection.locator = registry;
            BirtConnection.isServlet = true;
        }
    };

    public final void init(FilterConfig filterConfig) throws ServletException {
        ServletConfigWrapper.servletContext = filterConfig.getServletContext();
        tapestryFilter.init(filterConfig);
    }

    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        tapestryFilter.doFilter(request, response, chain);
    }

    public final void destroy() {
        tapestryFilter.destroy();
    }
}
