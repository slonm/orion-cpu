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

import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.tapestrycrud.CrudPage;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.annotations.Inject;
import br.com.arsmachina.tapestrycrud.CrudEditPage;
import br.com.arsmachina.tapestrycrud.base.AbstractObjectLink;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import org.apache.tapestry5.services.PageRenderLinkSource;

/**
 * Component that creates a link to the corresponding edition page (a {@link CrudEditPage} instance,
 * typically). It must be used inside pages that implements {@link CrudPage}.
 * 
 * @author sl
 */
public class NewObjectPageLink extends AbstractObjectLink {

    @Inject
    private TapestryCrudModuleService tapestryCrudModuleService;
    @Inject
    private PageRenderLinkSource pageRenderLinkSource;
    @Inject
    private EncoderSource encoderSource;

    @Override
    protected Link createLink(Authorizer authorizer, CrudPage<?> page) {
        if (!authorizer.canStore(page.getEntityClass())) {
            return null;
        }
        Encoder encoder = encoderSource.get(page.getEntityClass());
        Object activationContext = encoder.toActivationContext(null);
        Class editPageClass = tapestryCrudModuleService.getEditPageClass(page.getEntityClass());
        return pageRenderLinkSource.createPageRenderLinkWithContext(editPageClass, activationContext);
    }

    @Override
    protected String getOperationKey() {
        return "new";
    }
}
