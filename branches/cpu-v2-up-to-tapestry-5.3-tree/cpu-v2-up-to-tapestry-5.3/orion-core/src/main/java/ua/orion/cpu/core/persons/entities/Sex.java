package ua.orion.cpu.core.persons.entities;

import javax.persistence.Entity;
import ua.orion.core.persistence.*;

/**
 * Справочник - пол человека. Содержит уникальное имя.
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class Sex extends AbstractEnumerationEntity<Sex> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Краткое название. Пример: Мужской - "м."
     */
    private String shortName;

    /**
     * Создание экземпляра класса пол человека на основе полного и короткого
     * имени. Например, ("Мужской", "М.").
     *
     * @param name - уникальное имя(название)
     * @param shortName - сокращенное название.
     */
    public Sex(String name, String shortName) {
        setName(name);
        this.shortName = shortName;
    }

    /**
     * Создание объекта сущности Пол по умолчанию. Для сохранения обязателен
     * параметр name
     */
    public Sex() {
    }

    /**
     * Получить краткое название пола
     *
     * @return - строка с картки названием
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Установить краткое название пола
     *
     * @param shortName - краткое название
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
