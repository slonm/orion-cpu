package ua.orion.web.services;

import java.io.IOException;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

/**
 *
 * @author slobodyanuk
 */
public class LastPageHolderImpl implements LastPageHolder {

    private final ComponentEventLinkEncoder encoder;
    private final PageRenderLinkSource source;
    private final ApplicationStateManager manager;

    public LastPageHolderImpl(ComponentEventLinkEncoder encoder, PageRenderLinkSource source, ApplicationStateManager manager) {
        this.encoder = encoder;
        this.source = source;
        this.manager = manager;
    }

    @Override
    public Link getLastPage() {
        if (!manager.exists(Link.class)) {
            manager.set(Link.class, source.createPageRenderLink(""));
        }
        return manager.get(Link.class);
    }

    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        if (encoder.decodeComponentEventRequest(request) == null) {
            PageRenderRequestParameters params = encoder.decodePageRenderRequest(request);
            if (params != null) {
                Link link = encoder.createPageRenderLink(params);
                for (String s : request.getParameterNames()) {
                    link.addParameter(s, request.getParameter(s));
                }
                manager.set(Link.class, link);
            }
        }
        return handler.service(request, response);
    }
}
