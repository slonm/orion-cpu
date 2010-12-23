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

import br.com.arsmachina.tapestrycrud.CrudListPage;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.service.DataAwareObjectSource;
import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.CrudPage;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.internal.services.ComponentEventDispatcher;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that implements some common infrastructure for listing and editing pages. This class is not
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the entity class related to this encoder.
 */
@SuppressWarnings("unchecked")
public abstract class BasePage<T> implements CrudPage<T> {

    private final static Logger LOG = LoggerFactory.getLogger(BasePage.class);
    /**
     * You can change the persistence strategy from flash to another using
     * <code>@Meta("tapestry.persistence-strategy=" + PersistenceConstants.FLASH)</code> in your class.
     */
    @Persist
    private T object;
    @Inject
    private Authorizer authorizer;
    @Inject
    private TapestryCrudModuleService tapestryCrudModuleService;
    private Class<T> entityClass;
    @Persist(PersistenceConstants.FLASH)
    private String message;
    @Inject
    private ComponentResources componentResources;
    @Inject
    private Messages messages;
    private boolean removedObjectNotFound;
    @Inject
    private DataAwareObjectSource objectSource;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;
    @Inject
    private Request request;
    @Persist
    private Class<T> predefinedEntityClass = null;

    public boolean isComponentEventRequst() {
        return componentEventLinkEncoder.decodePageRenderRequest(request)==null;
    }

    @SuppressWarnings("unchecked")
    public BasePage() {
        try {
            final Type genericSuperclass = getClass().getGenericSuperclass();
            final ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);
            predefinedEntityClass=(Class<T>) parameterizedType.getActualTypeArguments()[0];
        } catch (Exception x) {
            LOG.debug("Default initialization failed: "+x.getMessage());
        }

    }

    public Class<T> getPredefinedEntityClass() {
        return predefinedEntityClass;
    }

    /**
     * initialize entityClass field
     */
    final public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @see br.com.arsmachina.tapestrycrud.CrudPage#getMessage()
     */
    public String getMessage() {
        return message;
    }

    /**
     * @see br.com.arsmachina.tapestrycrud.CrudPage#setMessage(java.lang.String)
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the value of the <code>controller</code> property.
     *
     * @return a {@link Controller<T,?>}.
     */
    public final Controller<T, ?> getController() {
        return objectSource.get(Controller.class, getEntityClass());
    }

    public final Encoder<T> getEncoder() {
        return objectSource.get(Encoder.class, getEntityClass());
    }

    public final Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * Returns the value of the <code>messages</code> property.
     *
     * @return a {@link Messages}.
     */
    public final Messages getMessages() {
        return messages;
    }

    /**
     * Returns the {@link Zone} that surrounds the form.
     *
     * @return a {@link Zone}.
     */
    public Zone getFormZone() {
        return (Zone) componentResources.getEmbeddedComponent(getFormZoneId());
    }

    /**
     * ID of the {@link Block} that will be returned when a form is submitted via AJAX. This
     * implementation returns {@link #DEFAULT_FORM_BLOCK_ID} (<code>block</code>).
     *
     * @return a {@link String}.
     */
    String getFormBlockId() {
        return Constants.DEFAULT_FORM_BLOCK_ID;
    }

    /**
     * ID of the {@link Zone} that will be returned when a form is submitted via AJAX. This
     * implementation returns {@link #DEFAULT_FORM_ZONE_ID} (<code>zone</code>).
     *
     * @return a {@link String}.
     */
    String getFormZoneId() {
        return Constants.DEFAULT_FORM_ZONE_ID;
    }

    /**
     * Used by {@link #returnFromRemove()} to know whether it must return a {@link Zone} or a
     * {@link Block}. This implementation returns <code>true</code>.
     *
     * @return a <code>boolean</code>.
     */
    protected boolean returnZoneOnXHR() {
        return true;
    }

    /**
     * Returns the value of the <code>authorizer</code> property.
     *
     * @return a {@link Authorizer}.
     */
    final public Authorizer getAuthorizer() {
        return authorizer;
    }

    /**
     * This method listens to the {@link Constants#REMOVE_OBJECT_ACTION} event and removes the
     * corresponding object.
     *
     * @param context an {@link EventContext}.
     */
    @OnEvent(Constants.REMOVE_OBJECT_EVENT)
    protected Object remove(EventContext context) {

        getAuthorizer().checkRemove(getEntityClass());

        final T toBeRemoved = getEncoder().toObject(context);

        if (toBeRemoved != null) {
            getAuthorizer().checkRemove(toBeRemoved);
        }

        return remove(toBeRemoved);

    }

    /**
     * Removes or not a given object.
     *
     * @param object a {@link K}.
     */
    protected Object remove(T object) {

        if (object != null) {
            getController().delete(object);
        } else {
            removedObjectNotFound = true;
        }

        return returnFromDoRemove();

    }

    /**
     * Defines what {@link #doRemove()} will return.
     *
     * @return an {@link Object} or <code>null</code>.
     */
    protected Object returnFromDoRemove() {
        Object page = getListPage();
        if (page != null && page instanceof CrudPage) {
            CrudPage<T> crudPage = (CrudPage<T>) page;
            if (removedObjectNotFound) {
                crudPage.setMessage(getRemoveErrorNotFoundMessage());
            } else {
                crudPage.setMessage(getRemoveSuccessMessage());
            }
        }
        return page;

    }

    /**
     * Returns the remove success message.
     *
     * @return a {@link String}.
     */
    protected String getRemoveSuccessMessage() {
        return getMessages().get(Constants.MESSAGE_SUCCESS_REMOVE);
    }

    /**
     * Returns the remove success message.
     *
     * @return a {@link String}.
     */
    protected String getRemoveErrorNotFoundMessage() {
        return getMessages().get(Constants.MESSAGE_ERROR_REMOVE_NOT_FOUND);
    }

    /**
     * Returns the value of the <code>tapestryCrudModuleService</code> property.
     *
     * @return a {@link TapestryCrudModuleService}.
     */
    final protected TapestryCrudModuleService getTapestryCrudModuleService() {
        return tapestryCrudModuleService;
    }

    public Object getListPage() {
        return objectSource.get(CrudListPage.class, getEntityClass());
    }

    /**
     * Returns the value of the <code>object</code> property.
     *
     * @return a {@link T}.
     */
    public T getObject() {
        return object;
    }

    /**
     * Changes the value of the <code>object</code> property.
     *
     * @param object a {@link T}.
     */
    public void setObject(T object) {
        this.object = object;
    }
}
