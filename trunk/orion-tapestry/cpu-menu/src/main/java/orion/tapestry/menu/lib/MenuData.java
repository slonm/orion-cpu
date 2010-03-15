package orion.tapestry.menu.lib;

import java.util.PriorityQueue;
import org.apache.tapestry5.Link;

/**
 * @author Gennadiy Dobrovolsky
 * Контейнер для одного меню
 */
public class MenuData {

    /**
     * Заголовок меню по умолчанию
     */
    static final private String detaultMenuTitle = "new menu";
    /**
     * Заголовок меню
     */
    private String title;
    /**
     * Пункты меню, упорядоченные по весу
     */
    private PriorityQueue<MenuItem> items;
    /**
     * Ссылка, привязанная к заголовку меню.
     */
    private Link pageLink;

    /**
     * Конструктор класса
     */
    public MenuData(MenuItem mi, PriorityQueue<MenuItem> _items) {
        this.title = mi.getLabel();
        this.pageLink = mi.getItemLink();
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
        return this.title;
    }

    /**
     * Устанавливает новый заголовок меню
     * @param t новвй заголовок
     */
    public void setTitle(String t) {
        this.title = t;
    }

    /**
     * @return Возвращает ссылку в заголовке меню
     */
    public Link getPageLink() {
        return this.pageLink;
    }

    /**
     * Устанавливает новую ссылку в заголовок меню
     * @param t новая ссылка
     */
    public void setPageLink(Link t) {
        this.pageLink = t;
    }
}
