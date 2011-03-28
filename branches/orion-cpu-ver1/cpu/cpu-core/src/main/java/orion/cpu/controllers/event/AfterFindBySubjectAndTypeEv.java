package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.*;

/**
 * Событие после поиска по субъекту прав и типу прав
 * @param <V> тип возвращаемого значения метода
 * @author sl
 */
public class AfterFindBySubjectAndTypeEv<V> extends AbstractAfterFindByEv<V> {

    private final Class<?> subject;
    private final String type;

    public AfterFindBySubjectAndTypeEv(V returnValue, Class<?> subject, String type) {
        super(returnValue);
        this.subject=subject;
        this.type=type;
    }

    public Class<?> getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }
    
}
