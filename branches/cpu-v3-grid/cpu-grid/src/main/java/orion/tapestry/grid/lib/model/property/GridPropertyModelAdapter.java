package orion.tapestry.grid.lib.model.property;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.GridFilterAbstract;
import orion.tapestry.grid.lib.model.sort.GridSortConstraint;
import orion.tapestry.grid.lib.model.view.GridPropertyView;

/**
 *
 * Part of a {@link org.apache.tapestry5.beaneditor.BeanModel}
 * that defines the attributes of a single property of a bean.
 * <p/>
 * <p/>
 * A PropertyModel is also an {@link AnnotationProvider},
 * as long as the {@link org.apache.tapestry5.PropertyConduit}
 * is non-null.  When there is no property conduit,
 * then {@link org.apache.tapestry5.ioc.AnnotationProvider#getAnnotation(Class)}
 * will return null.
 *
 * Полноценная модель, которой не хватает только фильтров.
 * @author dobro
 */
public class GridPropertyModelAdapter implements GridPropertyModelInterface {

    // ================== основные свойства ====================================
    /**
     * хранит название свойства (фактически это может быть неким выражением).
     */
    protected String propertyName;
    /**
     * хранит тип свойства.
     */
    protected Class propertyType;
    /**
     * хранит ссылку на модель, которой принадлежит данное свойство.
     */
    protected GridBeanModel gridBeanModel;
    /**
     * объект, который используется для записи и изменения свойства
     */
    protected PropertyConduit propertyConduit;
    // ================== производные свойства =================================
    /**
     * логическое имя типа данных,
     * которое используется интерфейсом для показа и редактирования свойства
     */
    protected String dataType;
    /**
     * хранит название свойства, которое будет показано пользователю
     */
    protected String label;
    /**
     * описание способа сортировки по этому полю
     * (степень важности, сортировать по возрастанию, по убыванию или не сортировать)
     * Если NULL, то сортировать нельзя
     */
    protected GridSortConstraint sortConstraint;
    /**
     * Идентификатор поля
     */
    protected String id;
    /**
     * Как показывать соответствующую колонку в таблице
     * (видима или нет, на каком месте)
     * Если NULL, то поле невидимое
     */
    protected GridPropertyView gridPropertyView;
    /**
     * Элементарные фильтры, которые можно применять к этому свойству
     */
    protected List<GridFilterAbstract> gridFilterList;
    /**
     * Источник локализованных сообщений
     */
    private Messages messages;
    /**
     * Можно ли упорядочить по этому полю
     */
    protected boolean iSortable = true;

    /**
     * @param _gridBeanModel модель, которой принадлежит данное свойство
     * @param _propertyName название свойства
     * @param _propertyConduit объект для чтения/записи значений
     * @param _messages источник сообщений на человеческом языке
     */
    public GridPropertyModelAdapter(
            GridBeanModel _gridBeanModel,
            String _propertyName,
            PropertyConduit _propertyConduit,
            Messages _messages) {
        this.gridBeanModel = _gridBeanModel;
        this.propertyName = _propertyName;
        this.propertyConduit = _propertyConduit;
        this.id = this.propertyName.replaceAll("\\W", _propertyName);

        this.messages = _messages;
        this.label = String.format("%s.%s", this.gridBeanModel.getBeanType().getSimpleName(), propertyName);
        //String key = String.format("property.%s.%s", this.gridBeanModel.getBeanType().getSimpleName(), capitalize(propertyName));

        this.sortConstraint = new GridSortConstraint(this, ColumnSort.UNSORTED, 0);
        this.gridPropertyView = new GridPropertyView(this, 0);
        this.gridFilterList = new ArrayList<GridFilterAbstract>();
    }

    // =============== управление видимостью ===================================
    /**
     * @return информация о видимости поля (поле=колонка в таблице)
     */
    @Override
    public GridPropertyView getGridPropertyView() {
        return this.gridPropertyView;
    }

    /**
     * Устанавливает новую информация о видимости поля (поле=колонка в таблице)
     * @param _gridPropertyView новая информация о видимости поля
     */
    @Override
    public void setGridPropertyView(GridPropertyView _gridPropertyView) {
        this.gridPropertyView = _gridPropertyView;
    }

    // =============== свойства сортировки =====================================
    /**
     * возвращает true, если свойство может использоваться для сортировки
     */
    @Override
    public boolean isSortable() {
        return iSortable;
    }

    /**
     * изменяет свойство сортировки.
     */
    @Override
    public GridPropertyModelInterface sortable(boolean sortable) {
        this.iSortable = sortable;
        return this;
    }

    /**
     * @return условие сортировки строк по данному полю
     */
    @Override
    public GridSortConstraint getSortConstraint() {
        return this.sortConstraint;
    }

    /**
     * Устанавливает новое условие сортировки строк
     * @param _fieldSort новое условие сортировки строк по данному полю
     */
    @Override
    public void setSortConstraint(GridSortConstraint _sortConstraint) {
        this.sortConstraint = _sortConstraint;
    }

    // =============== логическое имя типа данных ==============================
    /**
     * возвращает логическое имя типа данных,
     * которое используется интерфейсом для показа
     * и редактирования свойства
     */
    @Override
    public String getDataType() {
        return this.dataType;
    }

    /**
     * изменяет логический тип данных для свойства (не фактический)
     */
    @Override
    public GridPropertyModelAdapter dataType(String _dataType) {
        dataType = _dataType;
        return this;
    }

    // ===================== подпись к свойству ================================
    /**
     * возвращает название свойства, которое будет показано пользователю
     */
    @Override
    public String getLabel() {
        return messages.contains(this.label) ? messages.get(this.label) : this.label;
    }


    /**
     *  изменяет название свойства, которое будет показано пользователю
     */
    @Override
    public GridPropertyModelInterface label(String _label) {
        this.label = _label;
        return this;
    }

    // ==================== основные характеристики свойства ===================
    /**
     * возвращает название свойства (фактически это может быть неким выражением),
     * но чаще всего это просто имя атрибута
     */
    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * возвращает тип свойства.
     */
    @Override
    public Class getPropertyType() {
        return this.propertyType;
    }

    /**
     * возвращает ссылку на модель, которой принадлежит данное свойство.
     */
    @Override
    public GridBeanModel model() {
        return this.gridBeanModel;
    }

    /**
     * возвращает объект, который используется для записи и изменения свойства
     */
    @Override
    public PropertyConduit getConduit() {
        return this.propertyConduit;
    }

    /**
     * возвращает идентификатор, который используется, чтобы получить доступ к другим ресурсам
     * (он базируется на имени свойства, но без излишней пунктуации).
     */
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return propertyConduit == null ? null : propertyConduit.getAnnotation(annotationClass);
    }

    @Override
    public List<GridFilterAbstract> getGridFilterList() {
        return this.gridFilterList;
    }

    @Override
    public void setGridFilterList(List<GridFilterAbstract> newGridFilterList) {
        this.gridFilterList = newGridFilterList;
    }

    @Override
    public void addGridFilter(GridFilterAbstract newGridFilter) {
        this.gridFilterList.add(newGridFilter);
    }

    @Override
    public void setMessage(Messages msg) {
        this.messages = msg;
    }
}
