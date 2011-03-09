package ua.orion.web.pages;

import java.util.List;
import javax.persistence.Entity;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.persistence.MetaEntity;
import ua.orion.web.services.TapestryComponentDataSource;

/**
 * Страница, которая предоставляет CRUD для простых сущностей
 * @author slobodyanuk
 */
@Import(library = "../components/WindowUtils.js")
public class Crud {
    //---Components---
    @Component
    private Zone listZone;
    @Inject
    private Block editBlock;
    @Inject
    private Block viewBlock;
    //---Services---
    @Inject
    private EntityService entityService;
    @Inject
    private Messages messages;
    @Inject
    private Logger LOG;
    @Inject
    @Property(write=false)
    private TapestryComponentDataSource dataSource;
    //---Locals---
    @Persist
    private Class<?> objectClass;
    @Persist
    private Object object;
    @Persist
    private String mode;
    
    private static final String EDIT="edit";
    private static final String ADD="add";
    private static final String VIEW="view";
    private static final String DEL="del";

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Class<?> getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(Class<?> objectClass) {
        this.objectClass = objectClass;
    }
            
    /**
     * При закрытии страницы
     * @return
     * @author sl
     */
//    public Object onPassivate() {
//        if (getObject() != null) {
//            return getActivationContextEncoder(getEntityClass()).
//                    toActivationContext(getObject());
//        } else if (isInitiated()) {
//            return BaseEntity.getFullClassName(getEntityClass());
//        }
//        return null;
//    }
    
    @SuppressWarnings("unchecked")
    public Object onActivate(EventContext context) {
        try {
            assert context.getCount() != 1;
            objectClass=context.get(MetaEntity.class, 0).getEntityClass();
        } catch (Exception ex) {
            LOG.debug("Invalid activation context. Redirect to root page");
            return "";
        }
//        SecurityUtils.getSubject().isPermitted(objectClass.getSimpleName()+":read");
        //title = messages.get("reflect." + beanClass.getName());
        return null;
    }

    public GridDataSource getObjects() {
        return dataSource.getGridDataSource(objectClass);
    }

    public Object onSuccessFromForm() {
        if(ADD.equals(mode)) {
            entityService.persist(object);
        } else {
            entityService.merge(object);
        }
        return listZone.getBody();
    }

    public Object onEdit(Integer id) {
        mode=EDIT;
        object = entityService.find(objectClass, id);
        return editBlock;
    }

    public Object onAdd() {
        mode=ADD;
        object = entityService.newInstance(objectClass);
        return editBlock;
    }
    
    public Object onView(Integer id) {
        mode=VIEW;
        object = entityService.find(objectClass, id);
        return viewBlock;
    }
    
    public Object onDelete(Integer id) {
        mode=DEL;
        entityService.remove(entityService.find(objectClass, id));
        return listZone.getBody();
    }
    
    public String getMode(){
        if(ADD.equals(mode))return "Добавление";
        else if(EDIT.equals(mode))return "Редактирование";
        else if(VIEW.equals(mode))return "Просмотр";
        return "Удаление";
    }
    
    public String getModeAction(){
        if(ADD.equals(mode))return "Добавить";
        else if(EDIT.equals(mode))return "Изменить";
        else if(VIEW.equals(mode))return "Просмотреть";
        return "Удалить";
    }
    
//    public BeanModel<?> getListModel(){
//        BeanModel<?> bm=dataSource.getBeanModelForList(objectClass);
////        bm.add("action", null);
//        return bm;
//    }
//
//    public BeanModel<?> getEditModel(){
//        BeanModel<?> bm=dataSource.getBeanModelForEdit(objectClass);
//        return bm;
//    }
//
//    public BeanModel<?> getViewModel(){
//        BeanModel<?> bm=dataSource.getBeanModelForView(objectClass);
//        return bm;
//    }
}
