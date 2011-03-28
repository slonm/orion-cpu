/*
 *  Copyright 2010 sl.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package br.com.arsmachina.tapestrycrud.mixins;

import br.com.arsmachina.tapestrycrud.CrudEditPage;
import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.corelib.components.Form;

/**
 *
 * @author sl
 */
public class CrudEditPageMixin<T> extends BaseEditPage<T> {

    @InjectContainer
    private CrudEditPage page;

    @Override
    public void checkStoreTypeAccess() {
        super.checkStoreTypeAccess();
    }

    @Override
    public void checkUpdateObjectAccess(T activationContextObject) {
        super.checkUpdateObjectAccess(activationContextObject);
    }

    @Override
    public void checkUpdateTypeAccess() {
        super.checkUpdateTypeAccess();
    }

    @Override
    public Form getForm() {
        return page.getForm();
    }


}
