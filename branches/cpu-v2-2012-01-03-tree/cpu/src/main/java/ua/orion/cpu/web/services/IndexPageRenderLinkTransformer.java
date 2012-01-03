package ua.orion.cpu.web.services;

import java.util.*;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.TapestryConstants;
import org.apache.tapestry5.services.LocalizationSetter;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.linktransform.PageRenderLinkTransformer;

/**
 *
 * @author slobodyanuk
 */
public class IndexPageRenderLinkTransformer implements PageRenderLinkTransformer {

    private LocalizationSetter localizationSetter;

    public IndexPageRenderLinkTransformer(LocalizationSetter localizationSetter) {
        this.localizationSetter = localizationSetter;
    }
    
    @Override
    public PageRenderRequestParameters decodePageRenderRequest(Request request) {
//        String path = request.getPath();
//        String[] split = path.substring(1).split("/");
//        boolean isLoopback = request.getParameter(TapestryConstants.PAGE_LOOPBACK_PARAMETER_NAME) != null;
//        //Страница не указана. Example: http://host/
//        if (split.length == 1 && split[0].equals("")) {
//            return new PageRenderRequestParameters("ori/index", new EmptyEventContext(), isLoopback);
//        }
//        int pacx = 0;
//        String possibleLocaleName = split[0];
//        // Might be just the page activation context, or it might be locale then page
//        // activation context
//        boolean localeSpecified = localizationSetter.isSupportedLocaleName(possibleLocaleName);
//        if (localeSpecified) {
//            pacx++;
//        }
//        //Страница не указана, локализация указана. Example: http://host/en/
//        if (pacx > split.length) {
//            localizationSetter.setLocaleFromLocaleName(possibleLocaleName);
//            return new PageRenderRequestParameters("ori/index", new EmptyEventContext(), isLoopback);
//        }
        return null;
    }

    @Override
    public Link transformPageRenderLink(Link defaultLink, PageRenderRequestParameters parameters) {
        return null;
    }
}
