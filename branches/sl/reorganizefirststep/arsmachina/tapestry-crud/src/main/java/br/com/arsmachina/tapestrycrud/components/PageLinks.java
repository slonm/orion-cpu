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
package br.com.arsmachina.tapestrycrud.components;

import br.com.arsmachina.tapestrycrud.CrudEditPage;
import br.com.arsmachina.tapestrycrud.CrudListPage;
import br.com.arsmachina.tapestrycrud.CrudPage;
import br.com.arsmachina.tapestrycrud.CrudViewPage;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.Component;

/**
 * Simple component that just aggregates the view object, remove object, new object and 
 * back to edit components.
 * 
 * @author sl
 */
public class PageLinks {

    /**
     * Show the edit link?
     */
    @Parameter(value = "true", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    @Property
    private Boolean edit;
    /**
     * Show the remove link?
     */
    @Parameter(value = "true", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    @Property
    private Boolean remove;
    /**
     * Show the view link?
     */
    @Parameter(value = "true", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    @Property
    private Boolean view;
    /**
     * Show the create link?
     */
    @Parameter(value = "true", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    @Property
    private Boolean create;
    /**
     * Show the list link?
     */
    @Parameter(value = "true", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    @Property
    private Boolean list;
    @Inject
    private ComponentResources resources;

    void setupRender() {
        Component page = resources.getPage();

        if (page instanceof CrudViewPage<?>) {
            view = false;
        } else if (page instanceof CrudEditPage<?>) {
            edit = false;
            view = false;
        } else if (page instanceof CrudListPage<?>) {
            list = false;
            remove = false;
            view = false;
            edit = false;
        } else {
            throw new RuntimeException("The PageLinks must be used inside a page "
                    + "that implements one of CrudListPage, CrudViewPage or CrudEditPage");
        }

    }
}
