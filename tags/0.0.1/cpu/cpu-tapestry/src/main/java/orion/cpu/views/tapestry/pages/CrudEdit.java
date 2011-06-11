/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.cpu.views.tapestry.pages;

import br.com.arsmachina.authorization.Authorizer;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.Request;


/**
 *
 * @author dobro
 */
public class CrudEdit extends orion.cpu.views.tapestry.pages.crud.CrudEdit {

    // configuration: root package
    @Inject
    @Symbol("orion.root-package")
    private String rootPackage;
    // configuration: package where entities are located
    @Inject
    @Symbol("orion.entities-package")
    private String entitiesPackage;
    // permission checker
    @Inject
    private Authorizer authorizer;
    // interface messages
    @Inject
    private Messages messages;
    // navigation menu
    //@Property
    //private Object menudata;
    // HTTP request
    @Inject
    private Request request;

    /**
     * Создаёт класс по имени.
     * Наличие метода позволяет организовать создание класса по сокращённому имени.
     * @param entityClassName полное или частичное имя класса
     * @return класс сущности
     */
    @Override
    public Class entityClassForName(String entityClassName) throws ClassNotFoundException {
        if (entityClassName.startsWith(String.format("%s.%s", this.rootPackage, this.entitiesPackage))) {
            return Class.forName(entityClassName);
        } else {
            return Class.forName(String.format("%s.%s.%s", this.rootPackage, this.entitiesPackage, entityClassName));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object onActivate(EventContext context) {
        Object returnMe = super.onActivate(context);
        if (this.getEntityClass() == null) {
            LOG.error("Activation Error: entity class not found");
            return "";
        }

        // check permissions
        this.authorizer.checkSearch(this.getEntityClass());

        // set page title
        this.setTitle(messages.get("reflect." + this.getEntityClass().getName()));

        //menudata = request.getParameter("menupath");
        //if (menudata == null || menudata.toString().length() == 0) {
        //    menudata = "Start";
        //}


        return returnMe;
    }

    @Override
    public boolean isBlocked() {
        return false;
    }
}
