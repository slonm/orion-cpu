/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.stringvalues;

import java.text.DateFormat;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import ua.orion.core.services.StringValueProvider;
import ua.orion.cpu.core.entities.Document;

/**
 *
 * @author sl
 */
public class DocumentStringValueProvider implements StringValueProvider<Document> {

    private final ThreadLocale thLocale;

    public DocumentStringValueProvider(ThreadLocale thLocale) {
        this.thLocale = thLocale;
    }

    @Override
    public String getStringValue(Document entity) {
        if (entity == null) {
            return null;
        }
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, thLocale.getLocale());
        String s = entity.getSerial() + entity.getNumber() + " (" + dateFormat.format(entity.getIssue().getTime()) + ")";
        return s;
    }
}
