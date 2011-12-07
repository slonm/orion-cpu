package test.grid.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import orion.tapestry.grid.services.GridBeanModelSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;
import orion.tapestry.grid.components.GridSort;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import test.grid.lib.Contact;

/**
 *
 * @author dobro
 */
public class TestSort {

    @Inject
    private GridBeanModelSource gridBeanModelSource;
    @Inject
    private BeanModelSource beanModelSource;
    @Inject
    private Messages messages;
    private GridBeanModel<Contact> modelNew;
    private GridSortModel sortModel;
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
        sortModel = modelNew.getGridSortModelImpl();
        //gridFilter.setGridFilterJSON("{\"typeName\": \"AND\", \"uid\": \"AND1\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"AND\", \"validator\": \"\", \"label\": \"AND\", \"children\": {\"idNEQ4\": {\"typeName\": \"idNEQ\", \"uid\": \"idNEQ4\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"TEXT\", \"validator\": \"validatorInt\", \"label\": \"id !=\"}, \"birthDateISNULL5\": {\"typeName\": \"birthDateISNULL\", \"uid\": \"birthDateISNULL5\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"CHECKBOX\", \"validator\": \"\", \"label\": \"birthDate =null\"}, \"OR6\": {\"typeName\": \"OR\", \"uid\": \"OR6\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"OR\", \"validator\": \"\", \"label\": \"OR\", \"children\": {\"birthDateISNULL7\": {\"typeName\": \"birthDateISNULL\", \"uid\": \"birthDateISNULL7\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"CHECKBOX\", \"validator\": \"\", \"label\": \"birthDate =null\"}, \"idIN8\": {\"typeName\": \"idIN\", \"uid\": \"idIN8\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"TEXT\", \"validator\": \"validator_require_int\", \"label\": \"id  in \"}}}}}");
    }

    public GridSortModel getSortModel() {
        return sortModel;
    }

    // [lastName, birthDate, salary, id, email, firstName]
    public String getProperties() {
        // return modelNew.getPropertyNames().toString();
        return gridSort.getGridSortJSON();
    }
}
