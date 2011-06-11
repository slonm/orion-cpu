package orion.cpu.views.birt;

import java.util.Locale;
import org.apache.tapestry5.Link;

/**
 *
 * @author sl
 */
public interface ReportPreviewLinkFactory {

    Link buildByReport(String name);

    Link buildByReport(String name, Locale locale);

    Link buildByDocument(String name);

    Link buildByDocument(String name, Locale locale);
}
