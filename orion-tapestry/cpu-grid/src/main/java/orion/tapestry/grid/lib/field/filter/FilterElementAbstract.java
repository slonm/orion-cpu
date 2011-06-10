package orion.tapestry.grid.lib.field.filter;

import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Атомарный элемент фильтра.
 * Приспособленец.
 * Принимает и обрабатывает данные из одного элемента в форме поиска.
 * @author Gennadiy Dobrovolsky
 */
public abstract class FilterElementAbstract {

    /**
     * Текстовая метка фильтра
     * например "год публикации &gt;"
     */
    private String label;


    /**
     * уникальный идентификатор элемента
     */
    private String uid;

    /**
     * тип элемента формы
     */
    private FieldFilterElementType type;


    /**
     * Валидатор
     */
    protected FieldFilterElementValidator validator;

    /**
     * Метод изменяет условие отбора, которое будет передано в источник данных
     * @param <T> класс условия отбора
     * @param restriction объект "Редактор условия отбора"
     * @param value Параметр фильтра, чаше всего это введённая пользователем строка
     * @param isActive активный фильтр или нет
     * @param nChildren  Количество активных под-узлов, используется для элементов типа И, ИЛИ, НЕ
     * @return true, если этот элемент изменил условие фильтрации, и false, если условие не изменялось
     * @throws RestrictionEditorException если невозможно изменить условие отбора
     */
    public abstract <T> boolean modifyRestriction(
            RestrictionEditorInterface<T> restriction,
            Object value,
            boolean isActive,
            int nChildren) throws RestrictionEditorException;

    /**
     * @return возвращает текстовую метку элемента формы
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @param newLabel устанавливает текстовую метку элемента формы
     */
    public void setLabel(String newLabel) {
        this.label = newLabel;
    }

    /**
     * @return возвращает уникальный идентификатор элемента
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * @param newUid новый уникальный идентификатор элемента формы
     */
    public void setUid(String newUid) {
        this.uid = newUid;
    }

    /**
     * @return тип элемента формы
     */
    public FieldFilterElementType getType(){
        return this.type;
    }

    /**
     * @param newType новый тип элемента формы
     */
    public void setType(FieldFilterElementType newType){
        this.type = newType;
    }

    /**
     * @return Объект-валидатор пользовательского ввода
     */
    public FieldFilterElementValidator getValidator(){
        return this.validator;
    }

    /**
     * Устанавливает новый объект в качестве валидатора
     * @param _validator объект-валидатор пользовательского ввода
     */
    public void setValidator(FieldFilterElementValidator _validator){
        this.validator=_validator;
    }
}
