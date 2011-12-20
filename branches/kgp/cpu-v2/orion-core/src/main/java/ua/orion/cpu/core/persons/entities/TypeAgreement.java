package ua.orion.cpu.core.persons.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.core.persistence.AbstractEnumerationEntity;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 * Справочный класс - типы трудового договора
 *
 * @author molodec
 */
@Entity
@Table(schema = "ref")
public class TypeAgreement extends AbstractReferenceEntity<TypeAgreement> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Создание типа трудового договора на основе названия и краткого названия
     */
    public TypeAgreement(String name, String shortName) {
        setName(name);
        setShortName(name);
    }

    /**
     * Конструктор по умолчанию для создания типа трудового договора
     */
    public TypeAgreement() {
    }
}
