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

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.tapestrycrud.CrudPage;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.runtime.Component;

/**
 * Superclass of the all links, what manipulate with Object.
 * @author sl
 */
@SupportsInformalParameters
public abstract class AbstractObjectLink {

    @Inject
    private Messages messages;
    @Inject
    private ComponentResources resources;
    private CrudPage<?> page;
    /**
     * If false (default value), the body of the tag will be ignored
     * and the internationalized name of the page is used.
     * If true, then the body of the label element (in the template) is not
     * ignored.
     */
    @Parameter(value = "false")
    private boolean ignoreBody;
    @Inject
    private Authorizer authorizer;
    private Element element;

    /**
     * Method that returns the link to be rendered.
     *
     * @return a {@link Link}. If null, then link do not render.
     */
    abstract protected Link createLink(Authorizer authorizer, CrudPage<?> page);

    abstract protected String getOperationKey();

    private String getInternalOperationKey() {
        String s = getOperationKey();
        if (s == null || s.isEmpty()) {
            throw new RuntimeException("Override of getOperationKey() must"
                    + " have not empty value!");
        }
        return s;
    }

    boolean beginRender(MarkupWriter writer) {
        Component container = resources.getPage();
        if (container instanceof CrudPage<?> == false) {
            throw new RuntimeException("The crud/"
                    + this.getClass().getSimpleName()
                    + " must be used inside a page that implements CrudPage");
        }
        page = (CrudPage<?>) container;
        Link link = createLink(authorizer, page);
        if (link != null) {
            element = writer.element("a", "href", link.toURI(),
                    "class", "t-crud-" + getInternalOperationKey() + "-object");
            resources.renderInformalParameters(writer);
        }
        return !ignoreBody;
    }

    void afterRender(MarkupWriter writer) {
        if (element != null) {
            if (element.getChildMarkup() == null
                    || element.getChildMarkup().isEmpty()
                    || ignoreBody) {
                String label;
                String concreteKey = getInternalOperationKey()
                        + "." + page.getEntityClass();
                String key = getInternalOperationKey();
                if (messages.contains(concreteKey)) {
                    label = messages.get(concreteKey);
                } else {
                    label = messages.get(key);
                }
                writer.write(label);
            }
            writer.end(); // a
        }
    }
}
