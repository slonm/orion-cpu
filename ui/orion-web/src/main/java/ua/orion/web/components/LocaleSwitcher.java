package ua.orion.web.components;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.LocalizationSetter;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;

/**
 *
 * @author slobodyanuk
 */
public class LocaleSwitcher {

    @Inject
    private LocalizationSetter localizationSetter;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;
    @Inject
    private PageRenderLinkSource pageRenderLinkSource;
    @Inject
    private Request request;
    @Property
    private String locale;
    @Inject
    @Symbol("tapestry.supported-locales")
    private String supportedLocales;

    public Link getLink() {
        String requestLocale = getRequestLocale();
        PageRenderRequestParameters prrp = componentEventLinkEncoder.decodePageRenderRequest(request);
        Link link;
        if (prrp != null) {
            link = componentEventLinkEncoder.createPageRenderLink(prrp);
            for (String s : request.getParameterNames()) {
                link.addParameter(s, request.getParameter(s));
            }
        } else {
            link = pageRenderLinkSource.createPageRenderLink("");
        }
        if (requestLocale == null) {
            link = link.copyWithBasePath("/" + locale + link.getBasePath());
        } else {
            String base = link.getBasePath();
            base = base.substring(locale.length() + 1);
            if (!base.isEmpty() && base.charAt(0) == '/') {
                base = base.substring(1);
            }
            String newBase = "/" + locale;
            if (!base.isEmpty()) {
                newBase += "/" + base;
            }
            link = link.copyWithBasePath(newBase);
        }
        return link;
    }

    public String getRequestLocale() {
        String path = request.getPath();
        String[] split = path.substring(1).split("/");
        if (split.length == 1 && split[0].equals("")) {
            return null;
        }
        String possibleLocaleName = split[0];
        // Might be just the page activation context, or it might be locale then page
        // activation context
        if (localizationSetter.isSupportedLocaleName(possibleLocaleName)) {
            return possibleLocaleName;
        } else {
            return null;
        }
    }

    public boolean getIsCurrentLocale() {
        return locale.equals(getRequestLocale());
    }
    
    public String[] getSupportedLocales() {
        return supportedLocales.split(",");
    }

}
