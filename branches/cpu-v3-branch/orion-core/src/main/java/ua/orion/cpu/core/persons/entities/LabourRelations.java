package ua.orion.cpu.core.persons.entities;

import javax.persistence.Entity;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - виды трудовых отношений
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class LabourRelations extends AbstractEnumerationEntity<LabourRelations> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Создание вида трудовых отношений на основе указания названия.
     *
     * @param name
     */
    public LabourRelations(String name) {
        setName(name);
    }

    /**
     * Стандартный конструктор для создания вида трудовых отношений. Для после-
     * дующего сохранения нужно будет сначала указать название при помощи мето-
     * да setName(String name) родительского класса.
     */
    public LabourRelations() {
    }
}
