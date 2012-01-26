package orion.tapestry.grid.components;

import org.apache.tapestry5.corelib.components.Form;
import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Delegate;
import org.apache.tapestry5.corelib.components.GridRows;
import org.apache.tapestry5.corelib.data.GridPagerPosition;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.GridModel;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.json.JSONException;
import orion.tapestry.grid.lib.datasource.DataSource;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.services.GridBeanModelSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.grid.lib.model.filter.GridFilterModel;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.model.sort.GridSortModelImpl;
import orion.tapestry.grid.lib.model.view.GridPropertyViewModel;
import orion.tapestry.grid.lib.paging.Page;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorHumanReadable;
import orion.tapestry.grid.lib.savedsettings.IGridSettingStore;

/**
 * Основной класс компоненты
 * @author Gennadiy Dobrovolsky
 */
@Import(stylesheet = {"grid.css"}, library = {"grid.js"})
@SupportsInformalParameters
public class Grid implements GridModel {

    private Logger logger = LoggerFactory.getLogger(Grid.class);
    /**
     * The source of data for the Grid to display.
     * This will usually be a List or array but can also be an explicit
     * {@link DataSource}. For Lists and object arrays,
     * a DataSource is created automatically as a wrapper
     * around the underlying List.
     */
    @Parameter(name = "source", principal = true, required = true, autoconnect = true)
    private DataSource source;
    /**
     * A wrapper around the provided DataSource that caches access to the availableRows property. This is the source
     * provided to sub-components.
     */
    @Property
    private DataSource cachedSource;
    /**
     * The model used to identify the properties to be presented and the order of presentation. The model may be
     * omitted, in which case a default model is generated from the first object in the data source (this implies that
     * the objects provided by the source are uniform). The model may be explicitly specified to override the default
     * behavior, say to reorder or rename columns or add additional columns. The add, include,
     * exclude and reorder
     * parameters are <em>only</em> applied to a default model, not an explicitly provided one.
     */
    @Parameter
    private GridBeanModel model;
    /**
     * The model parameter after modification due to the add, include, exclude and reorder parameters.
     */
    //@Persist
    private GridBeanModel gridBeanModel;
    /**
     * Сервис для создания модели, описывающей таблицу
     */
    @Inject
    private GridBeanModelSource gridBeanModelSource;
    /**
     * Ссылка на фрагмент шаблона, у которого установлен t:id="gridfilter"
     * используется для сообщений об ошибках
     */
    @Component(id = "gridfilter")
    private GridFilter gridFilter;
    /**
     * model to show filter settings form
     */
    @Property
    private GridFilterModel filterModel;
    /**
     * Ссылка на фрагмент шаблона, у которого установлен t:id="gridfilter"
     * используется для сообщений об ошибках
     */
    @Component(id = "gridview")
    private GridView gridView;
    /**
     * model to show view settings form
     */
    @Property
    private GridPropertyViewModel viewModel;
    /**
     * model to show sorting settings form
     */
    private GridSortModel sortModel;
    /**
     * Ссылка на фрагмент шаблона, у которого установлен t:id="gridfilter"
     * используется для сообщений об ошибках
     */
    @Component(id = "gridsort")
    private GridSort gridSort;
    /**
     * The number of rows of data displayed on each page. If there are more rows than will fit, the Grid will divide up
     * the rows into "pages" and (normally) provide a pager to allow the user to navigate within the overall result
     * set.
     */
    @Parameter("25")
    @Property
    private int rowsPerPage;
    @Property
    @Persist
    private Integer nRowsPerPage;
    /**
     * Ссылка на фрагмент шаблона, у которого установлен t:id="gridfilterform"
     * используется для сообщений об ошибках
     */
    @Component(id = "grid_properties_form")
    private Form _form;
    /**
     * A Block to render instead of the table (and pager, etc.) when the source is empty. The default is simply the text
     * "There is no data to display". This parameter is used to customize that message, possibly including components to
     * allow the user to create new objects.
     */
    @Parameter(value = "block:empty", defaultPrefix = BindingConstants.LITERAL)
    private Block empty;
    /**
     * Сообщения интерфейса
     */
    @Inject
    private Messages messages;
    /**
     * Defines where the pager (used to navigate within the "pages" of results) should be displayed: "top", "bottom",
     * "both" or "none".
     */
    @Parameter(value = "top", defaultPrefix = BindingConstants.LITERAL)
    private GridPagerPosition pagerPosition;
    /**
     * Блок со списком страниц
     */
    @Component(parameters = {"source=cachedSource", "rowsPerPage=nRowsPerPage", "currentPage=currentPage"})
    private GridPager pager;
    /**
     * Блок, который делегирует свои обязанности
     */
    @Component(parameters = "to=pagerTop")
    private Delegate pagerTop;
    /**
     * Блок, который делегирует свои обязанности
     */
    @Component(parameters = "to=pagerBottom")
    private Delegate pagerBottom;
    /**
     * Номер текущей страницы
     */
    @Persist
    @Property
    private Integer currentPage;
    /**
     * Defines where block and label overrides are obtained from. By default, the Grid component provides block
     * overrides (from its block parameters).
     */
    @Parameter(value = "this", allowNull = false)
    @Property(write = false)
    private PropertyOverrides overrides;
    /**
     * Optional output parmeter used to identify the index of the column being rendered.
     */
    @Parameter
    private int columnIndex;
    @Component(parameters = {"index=inherit:columnIndex", "overrides=overrides", "columnList=columnList"})
    private GridColumns columns;
    /**
     * Used to store the current object being rendered (for the current row). This is used when parameter blocks are
     * provided to override the default cell renderer for a particular column ... the components within the block can
     * use the property bound to the row parameter to know what they should render.
     */
    @Parameter(principal = true)
    @Property
    private Object row;
    @Component(parameters = {"columnIndex=inherit:columnIndex", "rowsPerPage=nRowsPerPage", "currentPage=currentPage", "row=row",
        "overrides=overrides"}, publishParameters = "rowIndex,rowClass,volatile,encoder,lean")
    private GridRows rows;
    /**
     * обьект для доступа к сохранённым наборам настроек
     */
    @Parameter
    @Property
    private IGridSettingStore gridSettingStore;
    /**
     * A comma-seperated list of property names to be added to the {@link org.apache.tapestry5.beaneditor.BeanModel}.
     * Cells for added columns will be blank unless a cell override is provided. This parameter is only used
     * when a default model is created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String add;
    /**
     * A comma-separated list of property names to be retained from the
     * {@link org.apache.tapestry5.beaneditor.BeanModel}.
     * Only these properties will be retained, and the properties will also be reordered. The names are
     * case-insensitive. This parameter is only used
     * when a default model is created automatically.
     */
    @SuppressWarnings("unused")
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String include;
    /**
     * A comma-separated list of property names to be removed from the {@link org.apache.tapestry5.beaneditor.BeanModel}
     * .
     * The names are case-insensitive. This parameter is only used
     * when a default model is created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String exclude;
    /**
     * A comma-separated list of property names indicating the order in which the properties should be presented. The
     * names are case insensitive. Any properties not indicated in the list will be appended to the end of the display
     * order. This parameter is only used
     * when a default model is created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String reorder;
    /**
     * Restriction editor to create visible search condition
     */
    private RestrictionEditorHumanReadable restrictionEditorHumanReadable;

    /**
     * подготовка к загрузке страницы
     */
    Object setupRender() {
        // при первой загрузке страницы создаём модель
        if (gridBeanModel == null) {
            if (model == null) {
                // если модель не была задана в параметрах, опрашиваем источник данных
                gridBeanModel = source.getRowType() != null ? gridBeanModelSource.createDisplayModel(source.getRowType(), messages) : null;
            } else {
                // если модель была задана в параметрах
                gridBeanModel = model;
            }
            // если модель была создана/прочитана
            if (gridBeanModel != null) {

                // добавляем колонки
                if (add != null) {
                    for (String name : split(add)) {
                        gridBeanModel.add(name);
                    }
                }
                // исключаем колонки
                if (exclude != null) {
                    gridBeanModel.exclude(split(exclude));
                }
                // включаем колонки
                if (include != null) {
                    gridBeanModel.include(split(include));
                }
                // упорядочиваем колонки
                if (reorder != null) {
                    gridBeanModel.reorder(split(reorder));
                }
            }
            // stop if the grid bean model cannot be defined
            if (gridBeanModel == null) {
                return empty;
            }
        }

        // create model to show "view settings" form
        viewModel = gridBeanModel.getGridPropertyViewModel();

        // create model to show "filter settings" form
        filterModel = gridBeanModel.getGridFilterModel();

        // create model to show "sorting settings" form
        sortModel = gridBeanModel.getGridSortModelImpl();

        this.restrictionEditorHumanReadable = new RestrictionEditorHumanReadable();

        // TAP5-34: We pass the source into the CachedDataSource now; previously
        // we were accessing source directly, but during submit the value wasn't
        // cached, and therefore access was very inefficient, and sorting was
        // very inconsistent during the processing of the form submission.

        // set default value
        if (nRowsPerPage == null) {
            nRowsPerPage = rowsPerPage;
        }

        if (currentPage == null) {
            currentPage = 1;
        }

        // декорируем источник данных кешированием
        cachedSource = new CachedDataSource(source);

        // выборка данных
        Page visiblePage = new Page(currentPage, nRowsPerPage);

        cachedSource.prepare(visiblePage.getFirstRow(), visiblePage.getLastRow(), GridSortModelImpl.importJSONString(gridBeanModel, this.gridSort.getGridSortJSON()).getSortConstraints(), this.gridFilter.getGridFilterJSON());

        int availableRows = cachedSource.getAvailableRows();

        visiblePage.setMaxRowNumber(availableRows);

        if (availableRows == 0) {
            return null;
        }

        // If there's no rows, display the empty block placeholder.
        return cachedSource.getAvailableRows() == 0 ? empty : null;

    }

    public Object getPagerTop() {
        return pagerPosition.isMatchTop() ? pager : null;
    }

    public Object getPagerBottom() {
        return pagerPosition.isMatchBottom() ? pager : null;
    }

    /**
     * Для отображения заголовков колонок нужен список моделей
     */
    public List<GridPropertyModelInterface> getColumnList() {
        List<GridPropertyModelInterface> columnList = new ArrayList<GridPropertyModelInterface>();
        for (Object name : this.gridBeanModel.getPropertyNames()) {
            GridPropertyModelInterface vi = this.gridBeanModel.get(name.toString());
            if (vi.getGridPropertyView() != null) {
                columnList.add(vi);
            }
        }
        return columnList;
    }

    @Override
    public BeanModel getDataModel() {
        return gridBeanModel;
    }

    @Override
    public GridDataSource getDataSource() {
        return cachedSource;
    }

    @Override
    public GridSortModel getSortModel() {
        return sortModel;
    }

    public void getSortModel(GridSortModel _sortModel) {
        sortModel = _sortModel;
    }

    static String[] split(String propertyNames) {
        String trimmed = propertyNames.trim();

        if (trimmed.length() == 0) {
            return null;
        }
        return trimmed.split("\\s*,\\s*");
    }

    /**
     * Показ понятного человеку условия фильтрации
     * @return строка, которая отображает понятное человеку условия фильтрации
     */
    public String getHumanReadableFilterInfo() {
        if (this.restrictionEditorHumanReadable == null) {
            return "";
        }
        try {
            if (this.gridFilter != null && this.filterModel != null) {
                this.filterModel.modifyRestriction(this.gridFilter.getGridFilterJSON(), this.restrictionEditorHumanReadable);
                logger.info(this.gridFilter.getGridFilterJSON());
            }
        } catch (RestrictionEditorException ex) {
        } catch (JSONException ex) {
        }
        return this.restrictionEditorHumanReadable.getValue();
    }

    /**
     * A version of DataSource that caches the availableRows property. This addresses TAPESTRY-2245.
     */
    static class CachedDataSource implements DataSource {

        private final DataSource delegate;
        private boolean availableRowsCached;
        private int availableRows;
        private String filterJSON;

        CachedDataSource(DataSource delegate) {
            this.delegate = delegate;
        }

        @Override
        public int getAvailableRows() {
            if (!availableRowsCached) {
                availableRows = delegate.getAvailableRows();
                availableRowsCached = true;
            }
            return availableRows;
        }

        @Override
        public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
            delegate.prepare(startIndex, endIndex, sortConstraints);
        }

        @Override
        public Object getRowValue(int index) {
            return delegate.getRowValue(index);
        }

        @Override
        public Class getRowType() {
            return delegate.getRowType();
        }

        @Override
        public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints, String filterJSON) {
            delegate.prepare(startIndex, endIndex, sortConstraints, filterJSON);
        }
    }

    /**
     * Устанавливает модель записи
     */
    public void setModel(GridBeanModel _model) {
        this.model = _model;
    }

    /**
     * Возвращает модель записи
     */
    public GridBeanModel getModel() {
        return this.model;
    }
}
