package com.mycompany.mavenproject1.pages;

import com.mycompany.mavenproject1.services.DataSource;
import com.mycompany.mavenproject1.services.Entity;
import java.util.List;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

@Import(library = "../components/WindowUtils.js")
public class About {

    @Component
    private Zone listZone;
    @Inject
    private Block editBlock;
    @Inject
    private Block viewBlock;
    @Inject
    private DataSource dataSource;
    @Inject
    private BeanModelSource modelSource;
    @Inject
    private Messages messages;
    @Persist
    @Property
    private Entity object;
    @Persist
    private String mode;
    
    private static final String EDIT="edit";
    private static final String ADD="add";
    private static final String VIEW="view";
    private static final String DEL="del";
            
    public List<Entity> getObjects() {
        return dataSource.findAll();
    }

    public Object onSuccessFromForm() {
        if(ADD.equals(mode)) {
            dataSource.insert(object);
        } else {
            dataSource.update(object);
        }
        return listZone.getBody();
    }

    public Object onEdit(Integer id) {
        mode=EDIT;
        object = dataSource.findById(id);
        return editBlock;
    }

    public Object onAdd() {
        mode=ADD;
        object = dataSource.newEntity();
        return editBlock;
    }
    
    public Object onView(Integer id) {
        mode=VIEW;
        object = dataSource.findById(id);
        return viewBlock;
    }
    
    public Object onDelete(Integer id) {
        mode=DEL;
        dataSource.delete(id);
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
    
    public BeanModel<?> getListModel(){
        BeanModel<?> bm=modelSource.createDisplayModel(Entity.class, messages);
        bm.add("action", null);
        bm.exclude("veryLongStringField");
        return bm;
    }
    
    public BeanModel<?> getEditModel(){
        BeanModel<?> bm=modelSource.createEditModel(Entity.class, messages);
        bm.exclude("id");
        return bm;
    }
    
    public BeanModel<?> getViewModel(){
        BeanModel<?> bm=modelSource.createDisplayModel(Entity.class, messages);
        return bm;
    }
}
