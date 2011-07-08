/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.tapestry.grid.services;

import java.beans.IntrospectionException;
import java.util.List;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import orion.tapestry.grid.lib.field.GridFieldAbstract;

/**
 * Класс конструирует список полей по классу-сущности
 * @author Gennadiy Dobrovolsky
 */
public interface GridFieldFactory {
    /**
     * Основной метод для генерации списка полей для модели данных
     *
     * @param forClass класс сущности, который описывает атрибуты
     * @return список полей, которые описывают колонки в таблице
     * @throws IntrospectionException возникает только если не удалось получить информацию из класса forClass
     */
    public List<GridFieldAbstract> getFields(Class forClass) throws IntrospectionException;

}
