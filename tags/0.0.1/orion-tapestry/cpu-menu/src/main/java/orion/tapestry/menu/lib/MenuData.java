package orion.tapestry.menu.lib;

import java.util.PriorityQueue;

/**
 * @author Gennadiy Dobrovolsky
 * Контейнер для одного меню
 */
public class MenuData {

    /**
     * Пункты меню, упорядоченные по весу
     */
    private PriorityQueue<MenuItem> items;

    /**
     * Пункт, привязанный к заголовку меню.
     */
    private MenuItem header;

    /**
     * Конструктор класса
     * @param mi Заголовок меню. Часть, видимая на линейке меню.
     * @param _items Список выпадающих пунктов
     */
    public MenuData(MenuItem mi, PriorityQueue<MenuItem> _items){
        this.header = mi;
        this.items = _items;
    }

    /**
     * Добавление пункта в меню
     * @param mi is menu item to add
     * @return current object to allow chaining
     */
    public MenuData addItem(MenuItem mi) {
        this.items.add(mi);
        return this;
    }

    /**
     * @return Возвращает список пунктов меню
     */
    public PriorityQueue<MenuItem> getItems() {
        return this.items;
    }

    /**
     * Устанавливает новый список пунктов меню
     * @param t список пунктов, которые надо добавить в меню
     */
    public void setItems(PriorityQueue<MenuItem> t) {
        this.items = t;
    }


    /**
     * @return Возвращает заголовок меню
     */
    public String getTitle() {
        //return this.header.getLabel();
        return this.header.getUid();
    }

    /**
     * Устанавливает новый заголовок меню
     * @param t новый заголовок
     */
    public void setTitle(String t) {
        this.header.setLabel(t);
    }


    /**
     * @return Возвращает ссылку в заголовке меню
     */
    public IMenuLink getPageLink() {
        return this.header.getItemLink();
    }

    /**
     * Устанавливает новую ссылку в заголовок меню
     * @param t новая ссылка
     */
    public void setPageLink(IMenuLink t) {
        this.header.setItemLink(t);
    }
}
