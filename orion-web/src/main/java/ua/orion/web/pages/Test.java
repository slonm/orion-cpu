package ua.orion.web.pages;

import javax.inject.Inject;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class Test {

    @Inject
    private Block content;
    @Inject
    private Block econtent;
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    @Property
    private String title;

    Object onShow() {
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            @Override
            public void run(JavaScriptSupport js) {

                js.addScript("jQuery('#win').dialog('open')");
            }
        });
        return content;
    }

    Object onActionFromOtherEShow() {
        title="OtherContent";
        return econtent;
    }
    
    Object onActionFromEShow() {
        title="econtent";
        return econtent;
    }
    
    void onActionFromNoajax() {
    }
    
    @Log
    Object onActionFromAjax() {
        return content;
    }
}
