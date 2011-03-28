package orion.tapestry.utils;

import org.apache.tapestry5.ValueEncoder;

/**
 * ValueEncoder для булева типа данных.
 * @author sl
 */
public class BooleanValueEncoder implements ValueEncoder<Boolean> {

    @Override
    public String toClient(Boolean serverValue) {
        return String.valueOf(serverValue);
    }

    @Override
    public Boolean toValue(String clientValue) {
        return Boolean.valueOf(clientValue);
    }
}
