package trash;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Фильтр, композиция элементов
 * @author Gennadiy Dobrovolsky
 */
public abstract class FieldFilterAbstract {

    /**
     * Идентификатор поля
     */
    protected String uid;
    /**
     * Атрибут или выражение, к которому применяется фильтр.
     * Например, название поля в БД
     * ли выражение для вычисления значения в колонке
     */
    protected String attributeName;
    /**
     * Подпись для поля
     */
    protected String label;
    /**
     * Данные для фильтра, элементы формы
     */
    protected List<FieldFilterElement> elements;

    /**
     * Активный фильтр или нет
     */
    private boolean isActive = true;

    /**
     * Устанавливает все необходимые параметры
     * @param _attributeName значение атрибута объекта
     * @param _uid новое значение UID
     * @param _label текстовая метка элемента
     */
    public FieldFilterAbstract(String _attributeName, String _uid, String _label) {
        this.uid = _uid;
        this.attributeName = _attributeName;
        this.label = _label;
        this.elements = new ArrayList<FieldFilterElement>();
        this.createElements(null);
    }

    /**
     * Метод вызывается конструктором класса
     * Создаёт список элементов формы фильтрации
     * @param messages каталог сообщений на выбранном языке
     */
    public abstract void createElements(Messages messages);

    /**
     * Метод изменяет условие отбора, которое будет передано в источник данных
     * @param <T> класс условия отбора
     * @param restriction объект "Редактор условия отбора"
     * @throws RestrictionEditorException 
     */
    public abstract <T> void modifyRestriction(RestrictionEditorInterface<T> restriction) throws RestrictionEditorException;


    /**
     * Метод устанавливает новые сообщения для интерфейсных сообщений фильтра
     * @param messages список сообщений для отображения на странице
     */
    public abstract void setMessages(Messages messages);


    /**
     * Фильтр опознаёт свои данные по uid элементов
     * и выбирает их из общего списка данных формы
     * @param _elements Данные для фильтра
     */
    public void setElementData(List<FieldFilterElement> _elements) {
        int cnt = elements.size(), i;
        for (FieldFilterElement new_element : _elements) {
            for (i = 0; i < cnt; i++) {
                if (elements.get(i).getUid().equalsIgnoreCase(new_element.getUid())) {
                    elements.set(i, new_element);
                }
            }
        }
    }

    // =========================================================================
    // Ниже находятся примитивные методы для чтения и записи атрибутов
    // =========================================================================
    /**
     * @return Возвращает идентификатор фильтра UID
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * @return Возвращает атрибут или выражение, к которому применяется фильтр
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * @return Возвращает текстовую метку фильтра
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @param _label новое значение текстовой метки
     * @return Устанавливает текстовую метку фильтра
     */
    public FieldFilterAbstract setLabel(String _label) {
        this.label = _label;
        return this;
    }

    /**
     * Возвращает существующие элементы формы
     * @return список элементов, которые надо нарисовать в форме
     */
    public List<FieldFilterElement> getElements() {
        if (this.elements == null) {
            this.createElements(null);
        }
        return this.elements;
    }

    /**
     * Устанавливаем видимость поля
     * @param _isActive новое значение видимости
     */
    public void setIsActive(boolean _isActive) {
        this.isActive = _isActive;
    }

    /**
     * Устанавливаем видимость поля
     * @param _isActive новое значение видимости
     * @return ссылка на текущий объект для составления цепочек
     */
    public FieldFilterAbstract _setIsActive(boolean _isActive) {
        this.isActive = _isActive;
        return this;
    }

}
