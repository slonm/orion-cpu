package test.grid.pages;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import orion.tapestry.grid.services.GridBeanModelSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.GridFilterModel;
import orion.tapestry.grid.lib.model.sort.GridSortModelImpl;
import orion.tapestry.grid.lib.model.view.GridPropertyViewModel;
import test.grid.lib.Contact;

/**
 *
 * @author dobro
 */
public class TestBeanModel {

    @Inject
    private GridBeanModelSource gridBeanModelSource;
    @Inject
    private BeanModelSource beanModelSource;
    @Inject
    private Messages messages;

    private GridBeanModel<Contact> modelNew;
    private BeanModel<Contact> modelOld;
    private GridFilterModel filterModel;
    private GridPropertyViewModel viewModel;
    private GridSortModelImpl sortModel;
    /**
     * Подготовка к созданию страницы
     */
    void setupRender() {
        modelNew = this.gridBeanModelSource.createDisplayModel(Contact.class, messages);
        modelNew.add("somefield",null);
        modelOld=beanModelSource.createDisplayModel(Contact.class, messages);

        filterModel=modelNew.getGridFilterModel();
        viewModel=modelNew.getGridPropertyViewModel();
        sortModel=modelNew.getGridSortModelImpl();
    }

    public String getFilterModelInfo(){
        return filterModel.toString();
    }
    public String getViewModelInfo(){
        return viewModel.toString();
    }
    public String getSortModelInfo(){
        return sortModel.toString();
    }
    // [lastName, birthDate, salary, id, email, firstName]
    public String getProperties(){
        return modelNew.getPropertyNames().toString();
    }

    // [lastName, birthDate, salary, id, email, firstName]
    public String getPropertiesOld(){
        return modelOld.getPropertyNames().toString();
    }

    public String getNewLastNameId(){
        return modelNew.get("lastName").getId();
    }
    public String getOldLastNameId(){
        return modelOld.get("lastName").getId();
    }


}
