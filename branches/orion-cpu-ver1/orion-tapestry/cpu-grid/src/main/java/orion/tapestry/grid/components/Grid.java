package orion.tapestry.grid.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.FieldFilterElementValidator;
import orion.tapestry.grid.lib.model.GridModelInterface;
import orion.tapestry.grid.lib.paging.Pager;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.field.view.GridFieldViewComparator;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.sort.GridFieldSortType;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.rows.GridRow;
import orion.tapestry.grid.lib.savedsettings.IGridSettingStore;

/**
 * Основной класс компоненты
 *
 *
 * <h3>Чтобы  по-своему оформить заголовок колонки, надо</h3>
 * <p>
 * 1) В шаблоне страницы внутри компоненты добавить параметр
 * с именем &lt;UID поля&gt;Header, , а в стартовом теге компоненты добавить атрибут
 * <b>currentField="tmpFieldObject"</b>
 * например:
 * <pre>
 * &lt;t:Grid gridModel="PageGridModel" <span style="color:blue;background-color:yellow;">currentField="tmpFieldObject"</span>&gt;
 *    <span style="color:blue;background-color:yellow;">&lt;t:parameter name="id3Header"&gt;
 *       ${currentField.UID} что-то особенное
 *    &lt;/t:parameter&gt;</span>
 * &lt;/t:Grid&gt;
 * </pre>
 * здесь <b>id3</b> - уникальный идентификатор поля, который был придуман в GridModel;
 * <b>id3Header</b> - имя параметра, по которому компонента поймёт, какой именно заголовок надо заменять
 * Между &lt;t:parameter name="id3Header"&gt; и &lt;/t:parameter&gt;> надо вписать шаблон заголовка колонки
 * </p>
 * <p>
 * 2) В классе страницы надо добавить атрибут tmpFieldObject
 * <pre>
 *    {@literal @SuppressWarnings("unused")}
 *    {@literal @Property}
 *    private GridField tmpFieldObject;
 * </pre>
 * значение этого атрибута нельзя изменять, если вы точно не представляете, что именно делаете.
 * <p>
 * <b>tmpFieldObject</b> можно заменить любым другим именем переменной
 * </p>
 * 
 * 
 * 
 * 
 *
 * <h3>Чтобы  по-своему оформить ячейку, надо</h3>
 * <p>
 * 1) В шаблоне страницы внутри компоненты добавить параметр
 * с именем &lt;UID поля&gt;Cell, а в стартовом теге компоненты добавить атрибуты
 * <b>currentField="tmpFieldObject"  currentRow="tmpRowObject"</b>
 * например:
 * <pre>
 * &lt;t:Grid gridModel="PageGridModel"<span style="color:blue;background-color:yellow;"> currentField="tmpFieldObject"  currentRow="tmpRowObject"</span>&gt;
 *   <span style="color:blue;background-color:yellow;"> &lt;t:parameter name="id3Cell"&gt;
 *       ${currentField.getStringValue(currentRow)}
 *    &lt;/t:parameter&gt;</span>
 * &lt;/t:Grid&gt;
 * </pre>
 * здесь <b>id3</b> - уникальный идентификатор поля, который был придуман в GridModel;
 * <b>id3Cell</b> - имя параметра, по которому компонента поймёт, какую именно ячейку надо заменять
 * Между &lt;t:parameter name="id3Cell"&gt; и &lt;/t:parameter&gt; надо вписать шаблон ячейки
 * </p>
 * <p>
 * 2) В классе страницы надо добавить атрибуты tmpFieldObject и tmpRowObject
 * <pre>
 *    {@literal @SuppressWarnings("unused")}
 *    {@literal @Property}
 *    private GridField tmpFieldObject;
 *
 *    {@literal @SuppressWarnings("unused")}
 *    {@literal @Property}
 *    private GridRow tmpRowObject;
 * </pre>
 * значение этих атрибутов нельзя изменять, если вы точно не представляете, что именно делаете.
 * <p>
 * <b>tmpFieldObject</b> и <b>tmpRowObject</b> можно заменить любым другим именем переменной
 * </p>
 *
 *
 * <h3>Чтобы вставить что-нибудь перед таблицей</h3>
 * Hадо добавить в шаблон параметр:
 * <pre>
 * &lt;t:parameter name="beforeTable"&gt;
 * &lt;t:pagelink class="grid-page-link grid-page-link-edit" target="_blank" page="CrudEdit" context="entityClassName"&gt;${message:crud-create}&lt;/t:pagelink&gt;
 * &lt;/t:parameter&gt;
 * </pre>
 *
 * <h3>Чтобы добавить колонку</h3>
 * Надо
 * <p>
 * 1) при создании модели добавить в неё дополнительное поле типа GridFieldCalculable
 * <pre>
 *    GridFieldCalculable extraField;
 *    extraField = new GridFieldCalculable("rowActions");
 *    gridmodel.addField(extraField);
 * </pre>
 * </p>
 * <p>
 * 2) в шаблон добавить параметр
 * <pre>
 * &lt;t:parameter name="rowActionsCell">
 * &lt;t:actionlink class="grid-page-link" t:id="CRUDDelete" context="currentRowContext"&gt;${message:crud-delete}&lt;/t:actionlink&gt;
 * &lt;t:pagelink class="grid-page-link grid-page-link-edit" t:id="CRUDEdit" target="_blank" page="CrudEdit" context="currentRowContext"&gt;${message:crud-edit}&lt;/t:pagelink&gt;
 * &lt;/t:parameter&gt;
 * </pre>
 * <b>rowActions</b> - уникальный (в пределах таблицы) идентификатор колонки.
 * @author Gennadiy Dobrovolsky
 */
@IncludeStylesheet({"grid.css", "gridfilter.css", "dateselector.css"})
@IncludeJavaScriptLibrary({"grid.js", "dateselector.js", "gridfilter.js",
    "${tapestry.scriptaculous}/dragdrop.js"})
@SupportsInformalParameters
public class Grid {

    /**
     * Источник метаданных таблицы
     * Обязательный параметр компоненты,
     * входные данные
     */
    @Parameter(principal = true, required = true)
    @Property
    private GridModelInterface gridModel;
    /**
     * Ссылка на фрагмент шаблона, у которого установлен t:id="gridfilterform"
     * используется для сообщений об ошибках
     */
    @Component(id = "gridpropertiesform")
    private Form _form;
    /**
     * Строка, которая хранит всё условие фильтрации
     */
    @Property  // <= чтобы не писать get... и set...
    @Persist   // <= чтобы данные из формы остались
    private String filterJSON;
    /**
     * Строительные элементы фильтра для источника данных
     */
    private List<FilterElementAbstract> filterElementList;
    /**
     * Строка, которая хранит описание видимости полей
     */
    @Property  // <= чтобы не писать get... и set...
    @Persist   // <= чтобы данные из формы остались
    private String viewJSON;
    /**
     * Строка, которая хранит ширину колонок
     */
    @Property  // <= чтобы не писать get... и set...
    @Persist   // <= чтобы данные из формы остались
    private String widthJSON;
    /**
     *  данные о видимости и последовательности колонок (полей) в таблице
     */
    private List<GridFieldView> fieldViewList;
    /**
     * Строка, которая хранит описание видимости полей
     */
    @Property  // <= чтобы не писать get... и set...
    @Persist   // <= чтобы данные из формы остались
    private String sortJSON;
    /**
     * Строка, которая хранит число - количество строк на странице
     */
    @Property  // <= чтобы не писать get... и set...
    @Persist   // <= чтобы данные из формы остались
    private Integer rowsPerPage;
    /**
     * Строка, которая хранит число - количество строк на странице
     */
    @Property  // <= чтобы не писать get... и set...
    @Persist   // <= чтобы данные из формы остались
    private Integer pageNumber;
    /**
     *  данные о сортировке строк списка
     */
    private List<GridFieldSort> fieldSortList;
    /**
     *  данные о страницах списка
     */
    @Property  // <= чтобы не писать get... и set...
    @Persist   // <= чтобы данные из формы остались
    private Pager pager;
    /**
     * Временная переменная для цикла по колонкам
     * Цикл объявлен в шаблоне
     */
    @Parameter(principal = true)
    @SuppressWarnings("unused") // <= меньше мусора при компиляции
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private GridFieldAbstract currentField;
    /**
     * Временная переменная для цикла по строкам
     * Цикл объявлен в шаблоне
     */
    @Parameter(principal = true)
    @SuppressWarnings("unused") // <= меньше мусора при компиляции
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private GridRow currentRow;
    /**
     * Извлечённые из источника данных записи
     */
    @SuppressWarnings("unused") // <= меньше мусора при компиляции
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private List<GridRow> rows;
    /**
     * Правильно упорядоченный список видимых полей
     */
    private List<GridFieldAbstract> visibleFieldList;
    /**
     * Where to look for informal parameter Blocks used to override column headers.  The default is to look for such
     * overrides in the GridColumns component itself, but this is usually overridden.
     */
    @Parameter("this")
    private PropertyOverrides overrides;
    /**
     * Блок, который содержит заголовки колонок
     * Этот блок нужен для того, чтобы можно было его перекрыть 
     * своим форматом
     */
    @Inject
    private Block standardColumnHeader;
    /**
     * Блок, который содержит стандартное форматирование ячейки
     * Этот блок нужен для того, чтобы можно было его перекрыть своим форматом
     */
    @Inject
    private Block standardCell;
    /**
     * Блок, который размещается непосредственно перед таблицей с данными
     * По умолчанию он пустой, но его можно перекрыть своим
     */
    @Inject
    private Block beforeTable;
    /**
     * Блок, который размещается непосредственно после таблицы с данными
     * По умолчанию он пустой, но его можно перекрыть своим
     */
    @Inject
    private Block afterTable;
    /**
     * Сообщения интерфейса
     */
    @Inject
    private Messages messages;

    /**
     * обьект для доступа к сохранённым наборам настроек
     */
    @Parameter
    @Property
    private IGridSettingStore gridSettingStore;


    /**
     * Проверяет, был ли перекрыт блок "Заголовок колонки" для текущего поля
     * Если параметр с именем <b>currentField.getUid() + "Header"</b> существует,
     * то возвращается ссылка на него, иначе возвращается ссылка на блок по умолчанию
     * @return часть компоненты (объект типа Block), которую надо использовать для рисования заголовка колонки
     */
    public Block getBlockColumnHeader() {
        Block override = overrides.getOverrideBlock(currentField.getUid() + "Header");
        if (override != null) {
            return override;
        }

        return standardColumnHeader;
    }

    /**
     * Проверяет, был ли перекрыт блок "Ячейка" для текущего поля
     * Если параметр с именем <b>currentField.getUid() + "Cell"</b> существует,
     * то возвращается ссылка на него, иначе возвращается ссылка на блок по умолчанию
     * @return часть компоненты (объект типа Block), которую надо использовать для рисования ячейки таблицы
     */
    public Block getBlockCell() {
        Block override = overrides.getOverrideBlock(currentField.getUid() + "Cell");
        if (override != null) {
            return override;
        }

        return standardCell;
    }

    /**
     * Проверяет, был ли перекрыт блок "Перед таблицей"
     * Если параметр с именем <b>beforeTable</b> существует,
     * то возвращается ссылка на него, иначе возвращается ссылка на блок по умолчанию
     * @return часть компоненты (объект типа Block), которую надо использовать для рисования ячейки таблицы
     */
    public Block getBlockBeforeTable() {
        Block override = overrides.getOverrideBlock("beforeTable");
        if (override != null) {
            return override;
        }
        return beforeTable;
    }

    /**
     * Проверяет, был ли перекрыт блок "После таблицы"
     * Если параметр с именем <b>afterTable</b> существует,
     * то возвращается ссылка на него, иначе возвращается ссылка на блок по умолчанию
     * @return часть компоненты (объект типа Block), которую надо использовать для рисования ячейки таблицы
     */
    public Block getBlockAfterTable() {
        Block override = overrides.getOverrideBlock("afterTable");
        if (override != null) {
            return override;
        }
        return afterTable;
    }

    /**
     * Подготовка к созданию страницы
     */
    void setupRender() {

        // ------------- set localized field labels - begin --------------------
        for (GridFieldAbstract fld : gridModel.getFields()) {
            fld.setLabel(messages.get(fld.getLabel()));
        }
        // ------------- set localized field labels - end ----------------------


        // ------------- create filter list - begin ----------------------------
        // load list of possible filter elements
        this.filterElementList = gridModel.getFilterElementList();
        gridModel.setFilter(this.filterJSON);
        // ------------- create filter list - end ------------------------------

        // ------------- данные о сортировке строк - begin ---------------------
        if (this.sortJSON == null) {
            this.fieldSortList = this.gridModel.getFieldSortList();
            this.sortJSON = jsonFromFieldSortList(this.fieldSortList);
        } else {
            this.fieldSortList = this.fieldSortListFromJson(this.sortJSON);
            if (this.fieldSortList != null) {
                this.gridModel.setFieldSortList(this.fieldSortList);
            } else {
                this.fieldSortList = this.gridModel.getFieldSortList();
                this.sortJSON = jsonFromFieldSortList(this.fieldSortList);
            }
        }
        // ------------- данные о сортировке строк - end -----------------------

        // ------------- данные о видимости колонок - begin --------------------
        // данные определяется в модели таблицы
        if (this.viewJSON == null) {
            this.fieldViewList = gridModel.getFieldViewList();
            //for(GridFieldView fv:this.fieldViewList){
            //    System.out.println(fv.getUid());
            //}
            this.viewJSON = this.jsonFromFieldViewList(fieldViewList);
        } else {
            this.fieldViewList = this.fieldViewListFromJson(this.viewJSON);
            if (this.fieldViewList != null) {
                gridModel.setFieldViewList(this.fieldViewList);
            } else {
                this.fieldViewList = gridModel.getFieldViewList();
                this.viewJSON = this.jsonFromFieldViewList(fieldViewList);
            }
        }
        // ------------- данные о видимости колонок - end ----------------------


        // ------------- данные о разбивке на страницы - begin -----------------
        // pager обязательно должен создаваться после установки
        // всех остальных параметров таблицы
        if (this.pager == null) {
            // Если это первая загрузка страницы, то создаём pager
            this.pager = this.gridModel.getPager();
            this.rowsPerPage = 10;
            this.pageNumber = 1;
        } else {
            this.gridModel.setPager(this.pager);
        }
        this.pager.setRowsPerPage(this.rowsPerPage);
        this.pager.setVisiblePage(this.pageNumber);

        // ------------- список строк - начало ---------------------------------
        try {
            // здесь извлекаются строки и обновляется количество найденных строк
            this.rows = gridModel.getRows();
        } catch (RestrictionEditorException ex) {
            Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
        }
        // ------------- список строк - конец ----------------------------------
    }

    /**
     * Возвращает правильно упорядоченный список видимых полей
     * При первом вызове прочитывает данные формы,
     * создаёт и запоминает список
     * @return Список полей
     */
    public List<GridFieldAbstract> getVisibleFields() {
        if (this.visibleFieldList == null) {
            this.visibleFieldList = new ArrayList<GridFieldAbstract>();
            Collections.sort(this.fieldViewList, new GridFieldViewComparator());
            for (GridFieldView fw : this.fieldViewList) {
                for (GridFieldAbstract f : gridModel.getFields()) {
                    if (fw.getUid().equals(f.getUid()) && f.getFieldView().getIsVisible()) {
                        this.visibleFieldList.add(f);
                    }
                }
            }
        }
        return this.visibleFieldList;
    }

    /**
     * Показ понятного человеку условия фильтрации
     * @return строка, которая отображает понятное человеку условия фильтрации
     */
    public String getHumanReadableFilterInfo() {
        return this.gridModel.getHumanReadableFilterInfo();
    }

    // Это временнные информеры
    public String getPagerInfo() {
        return "Page " + this.pager.getVisiblePage().getPageNumber() + " : from " + this.pager.getVisiblePage().getFirstRow() + " : from " + this.pager.getVisiblePage().getLastRow();
    }

    public String getSortConstraints() {
        return "no sorting";
    }

    /**
     * Составляет список элементов для редактора фильтра
     */
    public String getJSFilterElements() {
        // В зависимости от типа элемента, выдаём разный JavaScript
        StringBuilder s = new StringBuilder();
        for (FilterElementAbstract fe : this.filterElementList) {
            //System.out.println(fe.getUid() + "  "+fe.getType());
            switch (fe.getType()) {
                case TEXT: {
                    s.append("filterNodeType['");
                    s.append(fe.getUid());
                    s.append("']=function(){var tmp=new NodeText('");
                    s.append(fe.getLabel());
                    s.append("','");
                    s.append(fe.getUid());
                    s.append("','',");
                    FieldFilterElementValidator validator;
                    validator = fe.getValidator();
                    if (validator == null) {
                        s.append("null");
                    } else {
                        s.append(validator.getJSValidator());
                    }
                    s.append(");for(var i in tmp) this[i]=tmp[i];};\n");

                    s.append("filterNodeTypeLabel['");
                    s.append(fe.getUid());
                    s.append("']='");
                    s.append(fe.getLabel());
                    s.append("';\n");
                    break;
                }
                case CHECKBOX: {
                    s.append("filterNodeType['");
                    s.append(fe.getUid());
                    s.append("']=function(){var tmp=new NodeCheckbox('");
                    s.append(fe.getLabel());
                    s.append("','");
                    s.append(fe.getUid());
                    s.append("','');for(var i in tmp) this[i]=tmp[i];};\n");

                    s.append("filterNodeTypeLabel['");
                    s.append(fe.getUid());
                    s.append("']='");
                    s.append(fe.getLabel());
                    s.append("';\n");
                    break;
                }
                case DATE: {
                    s.append("filterNodeType['");
                    s.append(fe.getUid());
                    s.append("']=function(){var tmp=new NodeDate('");
                    s.append(fe.getLabel());
                    s.append("','");
                    s.append(fe.getUid());
                    s.append("','',");
                    FieldFilterElementValidator validator2;
                    validator2 = fe.getValidator();
                    if (validator2 == null) {
                        s.append("null");
                    } else {
                        s.append(validator2.getJSValidator());
                    }
                    s.append(");for(var i in tmp) this[i]=tmp[i];};\n");

                    s.append("filterNodeTypeLabel['");
                    s.append(fe.getUid());
                    s.append("']='");
                    s.append(fe.getLabel());
                    s.append("';\n");
                    break;
                }
            }
        }
        return s.toString();
    }

    /**
     * TODO add JavaDoc
     */
    public String jsonFromFieldViewList(List<GridFieldView> fvl) {
        StringBuilder s = new StringBuilder("[");
        int n = 0;
        for (GridFieldView gfw : fvl) {
            if (n > 0) {
                s.append(",");
            }
            n++;
            s.append("{\"uid\":\"");
            s.append(gfw.getUid());
            s.append("\",\"isVisible\":\"");
            s.append(gfw.getIsVisible());
            s.append("\",\"ordering\":\"");
            s.append(gfw.getOrdering());
            s.append("\",\"label\":\"");
            s.append(gfw.getLabel().replaceAll("\"", Matcher.quoteReplacement("\\\"")));
            s.append("\"}");
            // System.out.println(s.toString());
        }
        s.append("]");
        return s.toString();
    }

    /**
     * Получает строку, которая хранит информацию
     * о видимости и порядке столбцов в таблице.
     * set default value if _viewJSON is null
     * @param _viewJSON новая строка с информацией о видимости и порядке столбцов
     */
    public List<GridFieldView> fieldViewListFromJson(String _viewJSON) {
        // set default value if this.viewJSON is null
        if (_viewJSON == null || _viewJSON.isEmpty()) {
            return null;
        }
        ArrayList<GridFieldView> fvl = new ArrayList<GridFieldView>();
        try {
            //System.out.println("Parsing " + _viewJSON);
            JSONArray root = new JSONArray(_viewJSON);
            int cnt = root.length();
            JSONObject node;
            GridFieldView gfv;
            for (int i = 0; i < cnt; i++) {
                node = root.getJSONObject(i);
                gfv = new GridFieldView();
                gfv.setLabel(node.getString("label"));
                gfv.setUid(node.getString("uid"));
                gfv.setOrdering(node.getInt("ordering"));
                gfv.setIsVisible(node.getBoolean("isVisible"));
                //System.out.println(node.getString("label")
                //        + " " + node.getString("uid")
                //        + " " + node.getInt("ordering")
                //        + " " + node.getBoolean("isVisible"));
                fvl.add(gfv);
            }

            return fvl;
        } catch (JSONException ex) {
            //Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * TODO add JavaDoc
     */
    public String jsonFromFieldSortList(List<GridFieldSort> fsl) {
        StringBuilder s = new StringBuilder("[");
        int n = 0;
        for (GridFieldSort gfs : fsl) {
            if (n > 0) {
                s.append(",");
            }
            n++;

            s.append("{\"attributeName\":\"");
            s.append(gfs.getAttributeName());
            s.append("\",\"label\":\"");
            s.append(gfs.getLabel().replaceAll("\"", Matcher.quoteReplacement("\\\"")));
            s.append("\",\"ordering\":\"");
            s.append(gfs.getOrdering());
            s.append("\",\"sortType\":\"");
            s.append(gfs.getSortType());
            s.append("\",\"sortTypeAsc\":\"");
            s.append(gfs.getSortTypeValueAsc());
            s.append("\",\"sortTypeDesc\":\"");
            s.append(gfs.getSortTypeValueDesc());
            s.append("\",\"sortTypeNone\":\"");
            s.append(gfs.getSortTypeValueNone());
            s.append("\",\"uid\":\"");
            s.append(gfs.getUid());
            s.append("\"}");
            // System.out.println(s.toString());
        }
        s.append("]");
        return s.toString();
    }

    /**
     * TODO Add JavaDoc here
     */
    public List<GridFieldSort> fieldSortListFromJson(String _sortJSON) {
        // set default value if this.viewJSON is null
        if (_sortJSON == null || _sortJSON.isEmpty()) {
            return null;
        }
        ArrayList<GridFieldSort> fsl = new ArrayList<GridFieldSort>();
        try {
            //System.out.println("Parsing " + _viewJSON);
            JSONArray root = new JSONArray(_sortJSON);
            int cnt = root.length();
            JSONObject node;
            GridFieldSort gfs;
            for (int i = 0; i < cnt; i++) {
                node = root.getJSONObject(i);
                gfs = new GridFieldSort();
                gfs.setAttributeName(node.getString("attributeName"));
                gfs.setLabel(node.getString("label"));
                gfs.setOrdering(node.getInt("ordering"));
                gfs.setSortType(GridFieldSortType.valueOf(node.getString("sortType")));
                gfs.setUid(node.getString("uid"));
                fsl.add(gfs);
            }

            return fsl;
        } catch (JSONException ex) {
            //Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
