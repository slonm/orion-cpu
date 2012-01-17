package orion.tapestry.grid.lib.model.property;

import java.util.List;
import org.apache.tapestry5.beaneditor.PropertyModel;
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
 * @author dobro
 */
public interface GridPropertyModelInterface extends PropertyModel {

    // унаследованные методы
    // PropertyConduit getConduit() возвращает объект, который используется для записи и изменения свойства
    // String getDataType() возвращает логическое имя типа данных, которое используется интерфейсом для показа и редактирования свойства
    // String getId() возвращает идентификатор, который используется, чтобы получить доступ к другим ресурсам
    //               (он базируется на имени свойства, но без излишней пунктуации).
    // String getLabel() возвращает название свойства, которое будет показано пользователю
    // String getPropertyName()  возвращает название свойства (фактически это может быть неким выражением).
    // Class getPropertyType()   возвращает тип свойства.
    // boolean isSortable()      возвращает true, если свойство может использоваться для сортировки
    /**
     * изменяет логический тип данных для свойства (не фактический)
     */
    @Override
    GridPropertyModelInterface dataType(String dataType);

    /**
     *  изменяет название свойства, которое будет показано пользователю
     */
    @Override
    public GridPropertyModelInterface label(String label);

    /**
     * возвращает ссылку на модель, которой принадлежит данное свойство.
     */
    @Override
    public GridBeanModel model();

    // =================== сортировка ==========================================
    /**
     *  изменяет свойство сортировки.
     */
    @Override
    public GridPropertyModelInterface sortable(boolean sortable);

    /**
     * Возвращает обьект, описывающий сортировку
     */
    public GridSortConstraint getSortConstraint();

    /**
     * Устанавливает новое условие сортировки строк
     * @param _fieldSort новое условие сортировки строк по данному полю
     */
    public void setSortConstraint(GridSortConstraint _sortConstraint);

    // =================== видимость ===========================================
    /**
     * @return информация о видимости поля (поле=колонка в таблице)
     */
    public GridPropertyView getGridPropertyView();

    /**
     * Устанавливает новую информация о видимости поля (поле=колонка в таблице)
     * @param _gridPropertyView новая информация о видимости поля
     */
    public void setGridPropertyView(GridPropertyView _gridPropertyView);

    // =================== фильтры =============================================
    /**
     * Возвращает список фильтров, которые можно применять к данному свойству
     */
    public List<GridFilterAbstract> getGridFilterList();

    /**
     * Возвращает список фильтров, которые можно применять к данному свойству
     */
    public void setGridFilterList(List<GridFilterAbstract> newGridFilterList);

    /**
     * Добавляет элементарный фильтр
     */
    public void addGridFilter(GridFilterAbstract newGridFilter);
}
