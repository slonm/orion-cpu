package orion.tapestry.grid.lib.model;

/**
 * Описание поля в таблице
 * @param <T> тип значения, хранимого в поле
 * @author Gennadiy Dobrovolsky
 */
public interface FieldInterface<T> {

    /**
     * Валидация данных
     * @param checkme данные, которые надо проверить
     * @return ответ на вопрос, валидны ли данные
     */
    public boolean isValid(T checkme);

    /**
     * Получить значение поля
     * @return объект, который считается данными
     */
    public T getValue();

//    /**
//     * Установить значение поля
//     * @param val новое значение поля
//     * @return ссылка на текущий объект для построения цепочки
//     */
//    public FieldInterface<T> setValue(T val);

    /**
     * @return  строковый идентификатор поля
     */
    public String getId();

    /**
     * @param val  новый идентификатор поля
     * @return ссылка на текущий объект для построения цепочки
     */
    public FieldInterface<T> setId(String val);

    /**
     * @return тип хранимых данных
     */
    Class<T> getDataType();


    /**
     * Получить подпись
     * @return текст для подписи
     */
    public String getLabel();

    /**
     * Установить подпись
     * @param val новое значение подписи
     * @return ссылка на текущий объект для построения цепочки
     */
    public FieldInterface<T> setLabel(String val);

    /**
     * Установить правило сортировки
     * @param newSortingModelEntry новое правило сортировки
     * @return ссылка на текущий объект для построения цепочки
     */
    public FieldInterface<T> setSorting(SortingModelEntry newSortingModelEntry);

    /**
     * Извлечь правило сортировки
     * @return правило сортировки или null, если правило не установлено
     */
    public SortingModelEntry getSorting();

    /**
     * @return ответ на вопрос, можно ли сортировать по данному полю
     */
    public boolean isSortable();

    /**
     * @param val новое значение установки свойства сортируемости поля
     * @return ссылка на текущий объект для построения цепочки
     */
    public FieldInterface<T> setSortable(boolean val);
}
/*
–	PropertyModel	dataType(String dataType) – изменяет тип данных
–	PropertyConduit getConduit() – возвращает объект для чтения и изменения свойства
–	String	getPropertyName() – получить имя атрибута
–	Class	getPropertyType() – получить тип атрибута
–	BeanModel model() – ссылка на класс, в котором находиться данный атрибут
–	PropertyModel sortable(boolean sortable) – изменить свойство «поддаётся сортировке»
-   public boolean getPresentation();  - Визуальное представление значения
 */
