/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;
import ua.orion.web.OrionWebSymbols;

/**
 *
 * @author slobodyanuk
 */
public class OrionCoreJavaScriptStack implements JavaScriptStack {

    @Inject
    private AssetSource assets;

    @Override
    public List<String> getStacks() {
        return Collections.emptyList();
    }

    @Override
    public List<Asset> getJavaScriptLibraries() {
        return Arrays.asList(assets.getClasspathAsset("ua/orion/web/orion.js"));
    }

    @Override
    public List<StylesheetLink> getStylesheets() {
        return Collections.emptyList();
    }
    @Inject
    @Symbol(OrionWebSymbols.SHOW_HINTS)
    private boolean showHints;

    @Override
    public String getInitialization() {
        return showHints ? "Ori.SHOW_HINTS = true;" : null;
    }
}
