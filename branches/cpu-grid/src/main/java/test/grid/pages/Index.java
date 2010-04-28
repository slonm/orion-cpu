package test.grid.pages;

import java.util.Locale;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;

/**
 * Тест компонеты Grid
 */
public class Index {

    @Inject
    private PersistentLocale persistentLocale;

    {
        persistentLocale.set(new Locale("ru"));
    }
}
