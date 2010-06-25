package orion.tapestry.services.impl;

import java.util.Locale;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.model.ComponentModel;
import orion.tapestry.utils.DataTMLResource;

/**
 * Помощник для PageTemplateLocator, который обеспечивает загрузку шаблонов страниц
 * из базы данных
 * @author sl
 */
public class PageTemplateLocatorAdvice implements MethodAdvice {

    @Override
    public void advise(Invocation invocation) {
        if (invocation.getMethodName().equals("findPageTemplateResource")) {
            ComponentModel model = (ComponentModel) invocation.getParameter(0);
            Locale locale = (Locale) invocation.getParameter(1);
            String className = model.getComponentClassName();
            // A bit of a hack, but should work.
            if (!className.contains(".pages.")) {
                invocation.proceed();
                return;
            }
            Resource url = new DataTMLResource(className + ".tml").forLocale(locale);
            if (url != null) {
                invocation.overrideResult(url);
            } else {
                invocation.proceed();
            }
        }
    }

}
