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
        return Arrays.asList("core");
    }

    @Override
    public List<Asset> getJavaScriptLibraries() {
        return Arrays.asList(assets.getClasspathAsset("ua/orion/web/jquery.js"), 
                assets.getClasspathAsset("ua/orion/web/orion.js"));
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
        StringBuilder ret=new StringBuilder();
        String path=assets.getClasspathAsset("ua/orion/web/orion.js").toClientURL();
        int pos=path.lastIndexOf("/");
        path=path.substring(0, pos+1);
        ret.append(String.format("Ori.ORION_ASSET_PATH='%s';",path));
        ret.append(showHints ? "Ori.SHOW_HINTS = true;" : "");
        return ret.toString();
    }
}
