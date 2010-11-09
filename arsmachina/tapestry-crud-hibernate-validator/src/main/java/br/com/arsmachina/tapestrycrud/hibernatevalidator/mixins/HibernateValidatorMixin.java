// Copyright 2008-2009 Thiago H. de Paula Figueiredo
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

package br.com.arsmachina.tapestrycrud.hibernatevalidator.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.EditPage;

/**
 * Mixin that provides validation for {@link EditPage} using Hibernate Validator.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@MixinAfter
public class HibernateValidatorMixin extends DummyHibernateValidatorMixin {

	@SuppressWarnings("unchecked")
	@InjectContainer
	private EditPage page;
	
	@Inject
	private ComponentResources componentResources;

	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
	private boolean singleErrorMessagePerField = true;

	@Component(id = Constants.FORM_ID)
	private Form form;

	@Inject
	private Request request;

	/**
	 * Validates the object using Hibernate Validator and records the errors in the form.
	 */
	@OnEvent(component = Constants.FORM_ID, value = EventConstants.VALIDATE_FORM)
	public Object validateUsingTapestryCrud() {

		Object returnValue = null;

		final boolean ok = validate(page.getObject(), form, componentResources, singleErrorMessagePerField);

		if (request.isXHR() && ok == false) {
			returnValue = page.getFormZone();
		}

		return returnValue;

	}

}
