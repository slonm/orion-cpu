/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.utils;

import java.io.UnsupportedEncodingException;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.services.FormSupport;

/**
 * Транслятор Byte[] в String. Для Blob полей базы данных, хранящих текст
 * @author sl
 */
public class ByteArrayTranslator implements Translator<Byte[]> {

    @Override
    public String getName() {
        return "ByteArray";
    }

    @Override
    public String toClient(Byte[] value) {
        byte[] ba1 = new byte[value.length];
        for (int i = 0; i < value.length; i++) {
            ba1[i] = value[i].byteValue();
        }
        try {
            return new String(ba1, "UTF8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Class<Byte[]> getType() {
        return Byte[].class;
    }

    @Override
    public String getMessageKey() {
        return "a-string-is-a-string";
    }

    @Override
    public Byte[] parseClient(Field field, String clientValue, String message) throws ValidationException {
        byte[] ba1;
        try {
            ba1 = clientValue.getBytes("UTF8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        Byte[] ret = new Byte[ba1.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ba1[i];
        }
        return ret;
    }

    @Override
    public void render(Field field, String message, MarkupWriter writer, FormSupport formSupport) {
    }
}
