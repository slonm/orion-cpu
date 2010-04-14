package orion.cpu.views.birt;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import javax.servlet.*;

public class BirtTapestryFilter extends TapestryFilter {

    @Override
    protected void init(Registry registry) throws ServletException {
        BirtConnection.locator = registry;
        BirtConnection.isServlet = true;
    }
}
