package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - типы трудового договора
 *
 * @author molodec
 */
@Entity
@ReferenceBook
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
