package test.grid.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.json.JSONException;
import orion.tapestry.grid.services.GridBeanModelSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;
import orion.tapestry.grid.components.GridFilter;
import orion.tapestry.grid.components.GridSort;
import orion.tapestry.grid.components.GridView;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.GridFilterModel;
import orion.tapestry.grid.lib.model.view.GridPropertyViewModel;
import test.grid.lib.Contact;

/**
 *
 * @author dobro
 */
public class TestForm {

    @Inject
    private GridBeanModelSource gridBeanModelSource;

    @Inject
    private Messages messages;
    private GridBeanModel<Contact> modelNew;
    private GridPropertyViewModel viewModel;
    private GridFilterModel filterModel;
    private GridSortModel sortModel;
    /**
     * Ссылка на фрагмент шаблона, у которого установлен t:id="gridfilter"
     * используется для сообщений об ошибках
     */
    @Component(id = "gridview")
    private GridView gridView;
    /**
     * Ссылка на фрагмент шаблона, у которого установлен t:id="gridfilter"
     * используется для сообщений об ошибках
     */
    @Component(id = "gridfilter")
    private GridFilter gridFilter;
    /**
     * Ссылка на фрагмент шаблона, у которого установлен t:id="gridfilter"
     * используется для сообщений об ошибках
     */
    @Component(id = "gridsort")
    private GridSort gridSort;
    /**
     * Подготовка к созданию страницы
     */
    void setupRender() {
        modelNew = this.gridBeanModelSource.createDisplayModel(Contact.class, messages);
        viewModel = modelNew.getGridPropertyViewModel();
        filterModel = modelNew.getGridFilterModel();
        sortModel = modelNew.getGridSortModelImpl();
    }

    public GridPropertyViewModel getViewModel() {
        return viewModel;
    }

    public GridFilterModel getFilterModel() {
        return filterModel;
    }

    public GridSortModel getSortModel() {
        return sortModel;
    }

    public String getProperties() throws JSONException {
        // return modelNew.getPropertyNames().toString();
        return viewModel.exportJSONString();
    }
}
