// Copyright 2009 Thiago H. de Paula Figueiredo
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

import br.com.arsmachina.tapestrycrud.CrudViewPage;
import org.apache.tapestry5.EventContext;

/**
 * Base class for pages that edit entity objects.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the entity class related to this encoder.
 */
public class BaseViewPage<T> extends BasePage<T> implements CrudViewPage<T> {


    /**
     * Sets the object property from a given activation context value.
     *
     * @param value an {@link EventContext}.
     */
    public Object onActivate(EventContext context) {

        boolean validRequest = false;

        checkReadTypeAccess();

        if (context.getCount() > 0) {

            setObject(getEncoder().toObject(context));

            if (getObject() != null) {
                checkReadObjectAccess(getObject());
                validRequest = true;
            }

        } // event requests
        else {
            // is this an event request?
            if (isComponentEventRequst()) {
                validRequest = true;
            }

        }

        return validRequest ? null : getListPage();
    }

    /**
     * Checks if the current user has permission to read instances of the page entity class and
     * throws an exception if not. This method calls
     * <code>getAuthorizer().checkRead(getEntityClass())</code>.
     */
    protected void checkReadTypeAccess() {
        getAuthorizer().checkRead(getEntityClass());
    }

    /**
     * Checks if the current user has permission to ra given object and throws an exception if not.
     * This method calls <code>getAuthorizer().checkRead(object)</code>.
     */
    protected void checkReadObjectAccess(T object) {
        getAuthorizer().checkRead(object);
    }
}
