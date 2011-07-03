/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.tapestry.grid.lib.rows;

/**
 * Контейнер, содержащего одну строку таблицы
 * @author Gennadiy Dobrovolsky
 */
public interface GridRow {
    /**
     * @param uid uid поля, уникальный идентификатор колонки в таблице
     * @return Возвращает строковое представление значения поля с заданным именем <b>uid</b>
     */
    public Object getValue(String uid);

    /**
     * Метод добавляет новое значение в контейнер или заменяет его, если значение с таким именем уже существует.
     * @param uid uid поля, уникальный идентификатор колонки в таблице
     * @param value новое значение, которое надо добавить в контейнер
     */
    public void setValue(String uid,Object value);

    /**
     * Метод добавляет новое значение в контейнер или заменяет его, если значение с таким именем уже существует.
     * @param uid uid поля, уникальный идентификатор колонки в таблице
     * @param value новое значение, которое надо добавить в контейнер
     * @return Возвращает ссылку на текущий объект для составления цепочки
     */
    public GridRow _setValue(String uid,Object value);
}
