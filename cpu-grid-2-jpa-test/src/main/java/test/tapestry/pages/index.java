package test.tapestry.pages;

import java.util.Date;
import javax.persistence.EntityManager;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.tapestry.entities.Person;

/**
 *
 * @author dobro
 */
public class index {


    private Logger log=LoggerFactory.getLogger(index.class);
    /**
     * Конфигурация компоненты
     */
//    @Inject
//    private GridPropertyModelSource gridFieldFactory;

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

//    /**
//     * Модель данных таблицы
//     */
//    public GridModelInterface getGridModel() {
//        try {
//            return new GridModelJPABean(
//            Person.class,
//            gridFieldFactory,
//            entityManager){
//
//                @Override
//                public String customFilterJSON() {
//                    // не делает ничего, но можно и поправить условие фильтрации
//                    return "{\"type\": \"emailGE\", \"isactive\": \"1\", \"value\": \"QQ\"}";
//                }
//            };
//        } catch (IntrospectionException ex) {
//            log.debug(ex.getMessage());
//        }
//        return null;
//    }
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

        //this.entityManager.getTransaction().begin();
        this.entityManager.createQuery("DELETE FROM Person").executeUpdate();
        for (long id = 0; id < 53; id++) {
            pe = new Person();
            pe.setId(id);
            pe.setFirstName("vasya-"+id);
            pe.setLastName("pupkin-"+id);
            pe.setBirthDate(new Date());
            pe.setEmail("vasya"+id+"@email.com");
            pe.setSalary(1f*id);
            entityManager.persist(pe);
            entityManager.flush();
        }
        //this.entityManager.getTransaction().commit();
    }
}
