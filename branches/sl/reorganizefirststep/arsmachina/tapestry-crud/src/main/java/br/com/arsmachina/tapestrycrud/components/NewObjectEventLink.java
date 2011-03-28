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
package br.com.arsmachina.tapestrycrud.components;

import br.com.arsmachina.authorization.Authorizer;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.CrudPage;
import br.com.arsmachina.tapestrycrud.base.AbstractObjectLink;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;

/**
 * A link to edit a new object. Created because Firefox (and maybe other browsers) sometimes keeps
 * the field values from one page, editing one object, to a new object page. One example can be
 * found in <a href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/EditProject.tml?view=markup"
 * >Ars Machina Project Example</a>.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @author sl
 */
public class NewObjectEventLink extends AbstractObjectLink {

    @Inject
    private ComponentResources componentResources;
    @Inject
    private EncoderSource encoderSource;

    @Override
    protected Link createLink(Authorizer authorizer, CrudPage<?> page) {
        if (!authorizer.canStore(page.getEntityClass())) {
            return null;
        }
        Encoder encoder = encoderSource.get(page.getEntityClass());
        Object activationContext = encoder.toActivationContext(null);
        ComponentResources containerResources = componentResources.getContainerResources();
        return containerResources.createEventLink(Constants.NEW_OBJECT_EVENT, activationContext);
    }

    @Override
    protected String getOperationKey() {
        return "edit";
    }
}
