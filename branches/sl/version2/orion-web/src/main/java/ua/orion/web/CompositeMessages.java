package ua.orion.web;

import java.util.Locale;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.util.AbstractMessages;

/**
 *
 * @author slobodyanuk
 */
public class CompositeMessages extends AbstractMessages {

    private Messages[] allMessages;

    public CompositeMessages(Locale locale, Messages... allMessages) {
        super(locale);
        this.allMessages = allMessages;
    }

    @Override
    protected String valueForKey(String string) {
        for (Messages mes : allMessages) {
            if (mes.contains(string)) {
                return mes.get(string);
            }
        }
        return null;
    }
}
