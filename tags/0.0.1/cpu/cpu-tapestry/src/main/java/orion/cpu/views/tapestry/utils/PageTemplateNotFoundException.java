package orion.cpu.views.tapestry.utils;

import java.io.IOException;

/**
 * Исключение возникает при попытке доступа к TML шаблону через URL по протоколу
 * tml, если не удалось получить шаблон
 *
 * @author sl
 */
public class PageTemplateNotFoundException extends IOException{

    /**
     * Constructs a <code>ControllersObjectNotFoundException</code> with the
     * specified detail message. The string <code>s</code> can be
     * retrieved later by the
     * <code>{@link java.lang.Throwable#getMessage}</code>
     * method of class <code>java.lang.Throwable</code>.
     *
     * @param   s   the detail message.
     */
    public PageTemplateNotFoundException(String s) {
	super(s);
    }
}
