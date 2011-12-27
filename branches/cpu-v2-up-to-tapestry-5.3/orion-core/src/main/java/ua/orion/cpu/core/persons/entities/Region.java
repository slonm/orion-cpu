package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - регион Хранит список регионов Регион — определённая
 * территория, обладающая целостностью и взаимосвязью её составных элементов.
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class Region extends AbstractEnumerationEntity<Region> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Страна в которой находится данный регион
     */
    @ManyToOne
    private Country country;
    /**
     * Дополнительная информация о регионе
     */
    @DataType("longtext")
    private String addinitionalData;

    /**
     * Конструктор по умолчанию для создание региона
     */
    public Region() {
    }

    /**
     * Создание региона на основе имени
     *
     * @param name - имя
     */
    public Region(String name) {
        setName(name);
    }

    /**
     * Создание реги она на основе:
     *
     * @param name - имя
     * @param country - страна
     * @param addinitionalData - дополнительная информация
     */
    public Region(String name, Country country, String addinitionalData) {
        setName(name);
        this.country = country;
        this.addinitionalData = addinitionalData;
    }

    /**
     * Получение страны в которой находится регион
     *
     * @return страна
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Установка страны в которой находится регион
     *
     * @param country - страна
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Дополнительная информация о регионе Данное поле хранит все то, что
     * относится к региону, но не относится ни к одному другому полю данной
     * сущности
     *
     * @return
     */
    public String getAddinitionalData() {
        return addinitionalData;
    }

    /**
     * Установка дополнительной информации о регионе
     *
     * @param addinitionalData
     */
    public void setAddinitionalData(String addinitionalData) {
        this.addinitionalData = addinitionalData;
    }
}
