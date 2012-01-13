package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - типы пенсий. Примеры: Пенсіонер за віком, ветеран праці,
 * інвалід з дитинства и другие.
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class Retiree extends AbstractEnumerationEntity<Retiree> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Является ли человек инвалидом
     */
    private Boolean isInvalid;

    /**
     * Создание типа пенсии на основе названия.
     *
     * @param name - название
     */
    public Retiree(String name) {
        setName(name);
    }

    /**
     * Создание типа пенсии на основании:
     *
     * @param name - название
     * @param isInvalid - является ли человек инвалидом
     */
    public Retiree(String name, Boolean isInvalid) {
        setName(name);
        this.isInvalid = isInvalid;
    }

    /**
     * Создание типа пенсии по умолчанию.
     */
    public Retiree() {
    }

    /**
     * Получение информации о том, является ли человек инвалидом или нет
     *
     * @return
     */
    public Boolean getIsInvalid() {
        return isInvalid;
    }

    /**
     * Установка информации о инвалидности
     *
     * @param isInvalid - является ли инвалидом
     */
    public void setIsInvalid(Boolean isInvalid) {
        this.isInvalid = isInvalid;
    }
}
