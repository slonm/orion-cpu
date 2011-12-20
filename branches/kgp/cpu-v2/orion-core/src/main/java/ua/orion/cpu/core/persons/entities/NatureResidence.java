package ua.orion.cpu.core.persons.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Справочный класс - тип населенного пункта. Пример: областной центр, столица,
 * поселок городского типа и другие.
 *
 * @author molodec
 */
@Entity
@Table(schema = "ref")
public class NatureResidence extends AbstractEnumerationEntity<NatureResidence> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Краткое название. Пример: Поселок городского типа - "ПГТ"
     */
    private String shortName;

    /**
     * Создание типа населенного пункта на основе полного и краткого названия.
     *
     * @param name - полное название(обязательный параметр)
     * @param shortName - краткое название
     */
    public NatureResidence(String name, String shortName) {
        setName(name);
        this.shortName = shortName;
    }

    /**
     * Создание типа населенного пункта по умолчанию. Для сохранения нужно обя-
     * зательно указать полное название при помощи метода setName(String name)
     */
    public NatureResidence() {
    }

    /**
     * Получение краткого названия типа населенного пункта
     *
     * @return краткое название(Строка)
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Установка краткого названия
     *
     * @param shortName - краткое название
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
