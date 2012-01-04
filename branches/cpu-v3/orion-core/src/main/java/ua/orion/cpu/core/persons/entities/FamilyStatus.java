package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - семейный статус Примеры: женат, замужем, не замужем и
 * другие. Хранит только название статуса и его идентификатор.
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class FamilyStatus extends AbstractEnumerationEntity<FamilyStatus> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Создание объекта сущности "Семейный статус" на основе названия.
     * Единственный способ создать объект данного класса, так как в классе фи-
     * гурирует одно поле - name. И оно обязательно.
     *
     * @param name
     */
    public FamilyStatus(String name) {
        setName(name);
    }

    /**
     * Конструктор по умолчанию. Для сохранения нужно будет прежде установить
     * название семейного статуса с помощью метода setName(String name)
     */
    public FamilyStatus() {
    }
}
