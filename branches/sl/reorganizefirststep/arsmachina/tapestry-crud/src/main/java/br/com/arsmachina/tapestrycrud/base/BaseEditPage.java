// Copyright 2008 Thiago H. de Paula Figueiredo
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package br.com.arsmachina.tapestrycrud.base;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.AfterRenderTemplate;
import org.apache.tapestry5.annotations.Meta;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;

import br.com.arsmachina.controller.ReadableController;
import br.com.arsmachina.module.service.PrimaryKeyTypeService;
import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.CrudEditPage;
import br.com.arsmachina.tapestrycrud.CrudPage;

/**
 * Base class for pages that edit entity objects. One example of its use can be found in the Ars
 * Machina Project Example Application (<a
 * href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/java/br/com/arsmachina/example/web/pages/project/EditProject.java?view=markup"
 * >page class</a>. <a
 * href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/EditProject.tml?view=markup"
 * >template</a>).
 * @param <T> the entity class related to this encoder.
 *
 * @author Thiago H. de Paula Figueiredo
 * @author sl
 */
@Meta("tapestry.persistence-strategy=" + PersistenceConstants.FLASH)
public abstract class BaseEditPage<T> extends BasePage<T> implements
        CrudEditPage<T> {

    @Inject
    private Request request;
    @Inject
    private ComponentResources componentResources;
    @org.apache.tapestry5.annotations.Component(id = Constants.FORM_ID)
    private Form form;
    @Inject
    private BeanModelSource beanModelSource;

    /**
     * Ensures the edited object is not null. If not,
     * <code>getController.reattach(object)</code> is invoked.
     *
     * @see ReadableController#reattach(Object)
     */
    @OnEvent(component = Constants.FORM_ID, value = EventConstants.PREPARE)
    final protected void prepare() {
        if (isObjectPersistent()) {
            getController().reattach(getObject());
        }
    }

    /**
     * Validates the object. This method invokes {@link #validateObject(Form)} and then takes care
     * of handling AJAX form submissions.
     */
    @OnEvent(component = Constants.FORM_ID, value = EventConstants.VALIDATE_FORM)
    final protected Object validate() {

        // clear the confirmation message, if set.
        setMessage(null);

        final T obj = getObject();
        final Form frm = getForm();

        validateObject(obj, frm);

        Object returnValue = null;

        final boolean hasErrors = getForm().getHasErrors();

        if (hasErrors && request.isXHR()) {
            returnValue = getFormZone();
        }

        return returnValue;

    }

    /**
     * Validates an <code>object</code> and stores the validation erros in a <code>form</code>.
     * This implementation does nothing.
     *
     * @param object an {@link #T}.
     * @param a {@link Form}.
     */
    protected void validateObject(T object, Form form) {
    }

    /**
     * Adds an error to a given field in the form.
     *
     * @param fieldId a {@link String}. It cannot be null.
     * @param message a {@link String}. It cannot be null.
     */
    final protected void addError(String fieldId, String message) {

        assert fieldId != null;
        assert message != null;

        Field field = (Field) componentResources.getEmbeddedComponent(fieldId);
        getForm().recordError(field, message);

    }

    /**
     * Saves or updates the edited object. This method does the following steps:
     * <ol>
     * <li>Invokes {@link #prepareObjectForSaveOrUpdate()}</li>.
     * <li>Invokes {@link #getController()}<code>.saveOrUpdate(entity);</code>.
     * <li>Invokes and returns {@link #returnFromRemove()} </li>
     * </ol>
     */
    @OnEvent(component = Constants.FORM_ID, value = EventConstants.SUCCESS)
    final public Object saveOrUpdate() {

        prepareObjectForSaveOrUpdate();
        T entity = getObject();
        entity = saveOrUpdate(entity);
        setObject(entity);

        return returnFromRemove();

    }

    /**
     * Saves or updates an entity object. This implementation returns
     * <code>getController().saveOrUpdate(entity)</code>.
     *
     * @param entity a <code>T</code>. It cannot be null.
     * @return a <code>T</code>.
     */
    protected T saveOrUpdate(T entity) {
        return getController().saveOrUpdate(entity);
    }

    /**
     * Defines what {@link #saveOrUpdate()} will return and sets the success message.
     *
     * @return an {@link Object} or <code>null</code>.
     */
    protected Object returnFromRemove() {

        Object returnValue = null;

        if (request.isXHR()) {

            if (returnZoneOnXHR()) {
                returnValue = getFormZone();
            } else {
                returnValue = componentResources.getBlock(getFormBlockId());
            }

            setSaveOrUpdateSuccessMessage();

        } else {

            Object page = getListPage();

            if (page != null) {

                if (page instanceof CrudPage) {
                    ((CrudPage) page).setMessage(Constants.MESSAGE_SAVEORUPDATE_SUCCESS);
                }
                returnValue = page;

            } else {
                setSaveOrUpdateSuccessMessage();
            }

        }

        return returnValue;

    }

    /**
     * Sets the save or update success message in this page.
     */
    protected void setSaveOrUpdateSuccessMessage() {
        setMessage(getMessages().get(Constants.MESSAGE_SAVEORUPDATE_SUCCESS));
    }

    /**
     * Does any processing that must be done in the object before it is saved or updated.
     */
    protected void prepareObjectForSaveOrUpdate() {
    }

    /**
     * Returns the value of the <code>form</code> property.
     *
     * @return a {@link Form}.
     */
    public Form getForm() {
        return form;
    }

    /**
     * Returns the current activation context of this page.
     */
    public Object onPassivate() {
        return getEncoder().toActivationContext(getObject());
    }

    /**
     * Clears the form errors and message after page rendering.
     */
    @AfterRenderTemplate
    final void clearErrors() {
        getForm().getDefaultTracker().clear();
        setMessage(null);
    }

    /**
     * Sets the object property from a given activation context value.
     *
     * @param value an {@link EventContext}.
     */
    public Object onActivate(EventContext context) {
        if (getPredefinedEntityClass() != null) {
            setEntityClass(getPredefinedEntityClass());
        }
        setObject(getEncoder().toObject(context));
        if (getObject() != null) {
            checkUpdateTypeAccess();
            checkUpdateObjectAccess();
        } else {
            checkStoreTypeAccess();
        }
        return null;
    }

    /**
     * Checks if the current user has permission to update instances of the page entity class
     * and throws an exception if not.
     * This method calls <code>getAuthorizer().checkUpdate(getEntityClass())</code>.
     */
    protected void checkUpdateTypeAccess() {
        getAuthorizer().checkUpdate(getEntityClass());
    }

    /**
     * Checks if the current user has permission to store instances of the page entity class
     * and throws an exception if not.
     * This method calls <code>getAuthorizer().checkStore(getEntityClass())</code>.
     */
    protected void checkStoreTypeAccess() {
        getAuthorizer().checkStore(getEntityClass());
    }

    /**
     * Checks if the current user has permission to update a given object.
     * and throws an exception if not.
     * This method calls <code>getAuthorizer().checkUpdate(activationContextObject)</code>.
     *
     * @param activationContextObject a <code>T</code> instance. It cannot be null.
     */
    protected void checkUpdateObjectAccess() {
        getAuthorizer().checkUpdate(getObject());
    }

    /**
     * Tells if the object is persistent.
     *
     * @return a <code>boolean</code>.
     */
    private boolean isObjectPersistent() {
        return getObject() != null && getController().isPersistent(getObject());
    }

    /**
     * Returns <code>null</code> if we are inserting a new object and
     * {@link #getFormZoneId()} (<code>zone</code>) otherwise.
     *
     * @return a {@link String}.
     */
    public String getZone() {
        return isObjectPersistent() ? getFormZoneId() : null;
    }

    /**
     * Sets the object to <code>null</code>.
     */
    @OnEvent(Constants.NEW_OBJECT_EVENT)
    public final void clearObject() {
        setObject(null);
    }
}
