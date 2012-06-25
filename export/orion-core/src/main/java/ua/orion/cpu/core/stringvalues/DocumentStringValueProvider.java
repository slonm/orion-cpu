/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.stringvalues;

import org.apache.tapestry5.ioc.annotations.Inject;
import ua.orion.core.services.StringValueProvider;
import ua.orion.cpu.core.entities.Document;

/**
 *
 * @author sl
 */
public class DocumentStringValueProvider implements StringValueProvider<Document> {

    @Inject
    private StringValueProvider stringValueProvider;

    @Override
    public String getStringValue(Document entity) {
        if (entity == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getSerial()).append(entity.getNumber());
        sb.append(" (").append(stringValueProvider.getStringValue(entity.getIssue())).append(")");
        return sb.toString();
    }
}
