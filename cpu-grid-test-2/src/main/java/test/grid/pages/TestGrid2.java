package test.grid.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import orion.tapestry.grid.lib.datasource.DataSource;
import orion.tapestry.grid.lib.datasource.impl.ListDataSource;
import test.grid.lib.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.grid.lib.savedsettings.IGridSettingStore;
import orion.tapestry.grid.lib.savedsettings.SampleGridSettingStore;

/**
 *
 * @author dobro
 */
public class TestGrid2 {

    private Logger logger = LoggerFactory.getLogger(TestGrid2.class);
    @Persist
    private List<Contact> data;
    @Persist
    @Property
    private IGridSettingStore settingStore;

    @Property
    private Contact currentRow;

    /**
     * Подготовка к созданию страницы
     */
    void setupRender() {
        if (data == null) {
            data = createList();
        }

        if (settingStore == null) {
            settingStore = new SampleGridSettingStore();
        }

        // logger.info("setupRender()");
        //        modelNew = this.gridBeanModelSource.createDisplayModel(Contact.class, messages);
        //        filterModel = modelNew.getGridFilterModel();
        //        gridFilter.setGridFilterJSON("{\"typeName\": \"AND\", \"uid\": \"AND1\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"AND\", \"validator\": \"\", \"label\": \"AND\", \"children\": {\"idNEQ4\": {\"typeName\": \"idNEQ\", \"uid\": \"idNEQ4\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"TEXT\", \"validator\": \"validatorInt\", \"label\": \"id !=\"}, \"birthDateISNULL5\": {\"typeName\": \"birthDateISNULL\", \"uid\": \"birthDateISNULL5\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"CHECKBOX\", \"validator\": \"\", \"label\": \"birthDate =null\"}, \"OR6\": {\"typeName\": \"OR\", \"uid\": \"OR6\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"OR\", \"validator\": \"\", \"label\": \"OR\", \"children\": {\"birthDateISNULL7\": {\"typeName\": \"birthDateISNULL\", \"uid\": \"birthDateISNULL7\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"CHECKBOX\", \"validator\": \"\", \"label\": \"birthDate =null\"}, \"idIN8\": {\"typeName\": \"idIN\", \"uid\": \"idIN8\", \"isactive\": \"1\", \"value\": \"\", \"guitype\": \"TEXT\", \"validator\": \"validator_require_int\", \"label\": \"id  in \"}}}}}");
    }

    public DataSource getDataSource() {
        ListDataSource source = new ListDataSource(data);
        // logger.info("Creating ListGridDataSource ..."+source);
        return source;
    }

    private List<Contact> createList() {
        List<Contact> list = new ArrayList<Contact>();
        for (int i = 0; i < 60; i++) {
            Contact cnt = new Contact();
            cnt.setId(i);
            cnt.setFirstName("FirstName" + i);
            cnt.setLastName("LastName" + i);
            cnt.setEmail("email" + i + "@server.ua");
            cnt.setSalary((float) (i * Math.random()));
            cnt.setBirthDate(new Date());
            list.add(cnt);
        }
        return list;
    }
}
