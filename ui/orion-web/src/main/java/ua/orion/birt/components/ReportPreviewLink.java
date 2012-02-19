package ua.orion.birt.components;

import java.util.Locale;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import ua.orion.birt.services.ReportPreviewLinkFactory;

/**
 * Generates a render request link to Birt preview servlet
 * List of Options from http://www.eclipse.org/birt/phoenix/deploy/viewerUsage.php
 */
public class ReportPreviewLink extends AbstractLink {

    /**
     * Report locale.
     */
    @Parameter(required = true, allowNull = false)
    private Locale locale;
    /**
     * The path to the report design.
     */
    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String report;
    /**
     * The path to the report document.
     */
    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String document;
    @Inject
    private ReportPreviewLinkFactory linkFactory;
    @Inject
    private Locale glocale;

    public Locale defaultLocale() {
        return glocale;
    }

    void beginRender(MarkupWriter writer) {
        if (isDisabled()) {
            return;
        }
        Link link;
        if (report != null) {
            link = linkFactory.buildByReport(report, locale);
        } else if (document != null) {
            link = linkFactory.buildByDocument(report, locale);
        } else {
            return;
        }
        writeLink(writer, link);
    }

    void afterRender(MarkupWriter writer) {
        if (isDisabled()) {
            return;
        }

        writer.end(); // <a>
    }
}
