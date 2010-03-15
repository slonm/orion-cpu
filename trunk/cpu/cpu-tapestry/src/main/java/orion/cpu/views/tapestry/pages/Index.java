package orion.cpu.views.tapestry.pages;

//import org.apache.tapestry5.ComponentResources;
import java.util.Locale;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;

/**
 * Стартовая страница информационной системы КПУ.
 * Навигация по другим страницам описана в ./components/Layout.java
 */
public class Index {

    @Inject
    private PersistentLocale persistentLocale;

    {
        persistentLocale.set(new Locale("uk"));
    }
}
