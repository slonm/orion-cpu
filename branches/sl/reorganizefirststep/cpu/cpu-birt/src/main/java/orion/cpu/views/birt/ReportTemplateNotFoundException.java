package orion.cpu.views.birt;

import java.io.IOException;

/**
 * Исключение возникает при попытке доступа к шаблону через URL по протоколу
 * rptdesign, если не удалось получить шаблон
 *
 * @author sl
 */
public class ReportTemplateNotFoundException extends IOException{

    /**
     * Constructs a <code>ReportTemplateNotFoundException</code> with the
     * specified detail message. The string <code>s</code> can be
     * retrieved later by the
     * <code>{@link java.lang.Throwable#getMessage}</code>
     * method of class <code>java.lang.Throwable</code>.
     *
     * @param   s   the detail message.
     */
    public ReportTemplateNotFoundException(String s) {
	super(s);
    }
}
