/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.lib;

import org.apache.tapestry5.EventContext;

/**
 *
 * @author Gennadiy Dobrovolsky
 * Этот класс нужен для преобразования EventContext &lt; = &gt; Object[]
 */
public class EventContextEncoder {

    /**
     * Метод для преобразования Object[] = &gt; org.apache.tapestry5.EventContext
     * @param f - массив объектов
     * @return обект типа EventContext для страниц из Apache Tapestry
     */
    public static EventContext toEventContext(Object... f) {
        return new EventContextEncoder.EventContextImpl<Object>(f);
    }

    /**
     * Реализация интерфейса org.apache.tapestry5.EventContext
     */
    static class EventContextImpl<T> implements EventContext {

        private Object[] items;

        EventContextImpl(Object... f) {
            items = f;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Class<T> desiredType, int index) {
            return (T) items[index];
        }

        public String[] toStrings() {
            String[] ret = new String[items.length];
            for(int i=0;i<items.length;i++){
                ret[i]=items[i].toString();
            }
            return ret;
        }
    }

    /**
     * Метод для преобразования org.apache.tapestry5.EventContext = &gt; Object[]
     * @param ec объект типа org.apache.tapestry5.EventContext
     * @return массив объектов
     */
    public static Object[] toObjectArray(EventContext ec) {
        if (ec.getCount() == 0) {
            return null;
        } else {
            int parametersCount = ec.getCount();
            Object[] mP = new Object[parametersCount];
            int i;
            for (i = 0; i < parametersCount; i++) {
                mP[i] = ec.get(Object.class, i);
            }
            return mP;
        }
    }
}
