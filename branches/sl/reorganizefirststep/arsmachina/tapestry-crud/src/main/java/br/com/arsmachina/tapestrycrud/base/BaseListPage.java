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

import java.io.Serializable;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageDetached;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.CrudListPage;
import br.com.arsmachina.tapestrycrud.grid.ControllerGridDataSource;

/**
 * Base class for pages that list entity objects. The <code>object</code> property is meant to be
 * used as the <code>row</code> parameter of the {@link Grid} component.
 * 
 * One example of its use can be found in the Ars Machina Project Example Application (<a
 * href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/java/br/com/arsmachina/example/web/pages/project/ListProject.java?view=markup"
 * >page class</a>. <a
 * href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/ListProject.tml?view=markup"
 * >template</a>).
 * 
 * 
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public abstract class BaseListPage<T> extends BasePage<T> implements
        CrudListPage<T> {

    @Inject
    private ComponentResources componentResources;
    @Inject
    private Request request;
    @Inject
    private BeanModelSource beanModelSource;
    @InjectComponent
    private Grid grid;

    public Grid getGrid() {
        return grid;
    }

    public void onActivate() {
        if (getEntityClass()!=null) {
            getAuthorizer().checkSearch(getEntityClass());
        }else{
            throw new RuntimeException("EntityClass not set, but page render is call");
        }
    }

    /**
     * Method used as the <code>source</code> parameter of the {@link Grid} component. This
     * implementation returns <code>new {@link ControllerGridDataSource}(getController())</code>.
     *
     * @return an {@link Object}.
     */
    @SuppressWarnings("unchecked")
    @Cached
    public Object getObjects() {
        return new ControllerGridDataSource(getEntityClass(), getController());
    }

    /**
     * Creates a {@link BeanModel} and adds an <code>action</code> property to it.
     *
     * @return a {@link BeanModel}.
     */
    public BeanModel<T> getBeanModel() {

        final BeanModel<T> beanModel = beanModelSource.createDisplayModel(getEntityClass(),
                getMessages());
        beanModel.add(Constants.ACTION_PROPERTY_NAME, null);

        return beanModel;

    }

    /**
     * Defines what {@link #doRemove()} will return.
     *
     * @return an {@link Object} or <code>null</code>.
     */
    @Override
    protected Object returnFromDoRemove() {

        Object returnValue = null;

        if (request.isXHR()) {

            if (returnZoneOnXHR()) {
                returnValue = getFormZone();
            } else {
                returnValue = componentResources.getBlock(getFormBlockId());
            }

        }

        return returnValue;

    }

    /**
     * Sets the remove success message in this page.
     */
    protected void setRemoveSuccessMessage() {
        setMessage(getRemoveSuccessMessage());
    }

    /**
     * Sets the remove not done because object not found in this page.
     */
    protected void setRemoveErrorNotFoundMessage() {
        setMessage(getRemoveErrorNotFoundMessage());
    }

    /**
     * Tells if a given object can be removed in this context. It must be overriden if you have some
     * rules about when an object can be removed. This implementation just returns <code>true</code>.
     *
     * @param object a {@link #T}.
     * @return a <code>boolean</code>.
     */
    protected boolean canRemove(T object) {
        return true;
    }

    /**
     * Clears the message after it is shown, preventing the message from appearing twice in AJAX
     * actions.
     */
    @PageDetached
    void clearMessage() {

        if (request.isXHR()) {
            setMessage(null);
        }

    }

    @OnEvent("resetGridSorting")
    Object resetGridSorting() {

        getGrid().getSortModel().clear();
        final Object result = request.isXHR() ? componentResources.getEmbeddedComponent("zone") : null;
        return result;

    }

}