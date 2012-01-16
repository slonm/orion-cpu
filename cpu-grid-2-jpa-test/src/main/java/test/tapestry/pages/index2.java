package test.tapestry.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.grid.lib.datasource.impl.JPADataSource;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorJPACriteria;
import orion.tapestry.grid.lib.savedsettings.IGridSettingStore;
import orion.tapestry.grid.services.GridBeanModelSource;
import test.tapestry.entities.Person;

/**
 *
 * @author dobro
 */
public class index2 {

    private Logger log = LoggerFactory.getLogger(index2.class);
    @Inject
    private GridBeanModelSource gridBeanModelSource;
    /**
     * JPA EntityManager - подключение к базе данных
     */
    @Inject
    private EntityManager entityManager;
    /**
     * Сообщения интерфейса
     */
    @Inject
    private Messages messages;
    private GridBeanModel<Person> gridBeanModel;
    /**
     * Источник данных таблицы
     */
    @Property
    private JPADataSource dataSource;
    /**
     * Сохранённые настройки таблицы
     */
    @Persist
    @Property
    private IGridSettingStore settingStore;
    /**
     * Временная переменная для цикла по колонкам
     * Цикл объявлен в шаблоне компоненты Grid
     */
    //    @SuppressWarnings("unused") // <= меньше мусора при компиляции
    //    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    //    private GridFieldAbstract currentField;
    /**
     * Временная переменная для цикла по строкам
     * Цикл объявлен в шаблоне компоненты Grid
     */
    @SuppressWarnings("unused") // <= меньше мусора при компиляции
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private Object currentRow;

    @SuppressWarnings("unchecked")
    public Object onActivate(EventContext context) {

        //        if (this.gridSettingStore == null) {
        //            this.gridSettingStore = new SampleGridSettingStore();
        //        }
        //        System.out.println(this.gridSettingStore.getSavedSettingListJSON());
        return null;
    }

    //    @Persist
    //    private IGridSettingStore gridSettingStore;
    //
    //    /**
    //     * Обьект, отвечающий за сохранение и извлечение
    //     * настроек табличного просмотра.
    //     */
    //    public IGridSettingStore getGridSettingStore() {
    //        return this.gridSettingStore;
    //    }
    /**
     * Подготовка к созданию страницы
     */
    void setupRender() {
        Person pe;

        this.entityManager.getTransaction().begin();
        this.entityManager.createQuery("DELETE FROM Person").executeUpdate();
        for (long id = 0; id < 53; id++) {
            pe = new Person();
            pe.setId(id);
            pe.setFirstName("vasya-" + id);
            pe.setLastName("pupkin-" + id);
            pe.setBirthDate(new Date());
            pe.setEmail("vasya" + id + "@email.com");
            pe.setSalary(1f * id);
            entityManager.persist(pe);
            entityManager.flush();
        }
        this.entityManager.getTransaction().commit();

        gridBeanModel = this.gridBeanModelSource.createDisplayModel(Person.class, messages);
        RestrictionEditorJPACriteria editor = new RestrictionEditorJPACriteria(Person.class, entityManager);
        List<SortConstraint> sortConstraints = new ArrayList<SortConstraint>();
        dataSource = new JPADataSource(gridBeanModel,entityManager, editor);
    }

    public int getRowCount() {
        return this.dataSource.getAvailableRows();
    }

    public String getFirstRow() {
        
        Person row = (Person) this.dataSource.getRowValue(-1);
        if(row==null) return "null";
        return String.format("%s %s %s", row.getId(), row.getEmail(), row.getLastName());
    }
}
