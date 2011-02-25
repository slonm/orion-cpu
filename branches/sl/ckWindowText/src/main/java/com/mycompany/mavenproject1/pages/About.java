package com.mycompany.mavenproject1.pages;

import com.mycompany.mavenproject1.services.DataSource;
import com.mycompany.mavenproject1.services.Entity;
import java.util.List;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.chenillekit.tapestry.core.components.Window;

public class About {

    @Component
    private Window editWindow;
    @Component
    private Zone listZone;
    @Component
    private Zone editZone;
    @Inject
    private DataSource dataSource;
    @Inject
    private Request request;
    @Inject
    private JavaScriptSupport javascriptSupport;
    @Persist
    private Entity object;
    @Persist
    private boolean isNewObject;

    public Entity getObject() {
        return object;
    }

    public void setObject(Entity object) {
        this.object = object;
    }

    public List<Entity> getObjects() {
        return dataSource.findAll();
    }

    public Object onSuccessFromForm() {
        if (isNewObject) {
            dataSource.insert(object);
        } else {
            dataSource.update(object);
        }
        return listZone.getBody();
    }

    public Object onEdit(Integer id) {
        object = dataSource.findById(id);
        isNewObject=false;
        return editZone.getBody();
    }

    public Object onAdd() {
        object = dataSource.newEntity();
        isNewObject=true;
        return editZone.getBody();
    }
    
    void beginRender(){
        javascriptSupport.addScript(InitializationPriority.IMMEDIATE, "Tapestry.Initializer.showWindow = function(){$('editWindow').getStorage().ck_window.showCenter(true);}");
        if(request.isXHR()){
            javascriptSupport.addInitializerCall(InitializationPriority.IMMEDIATE, "showWindow", "");
        }
    }
}
