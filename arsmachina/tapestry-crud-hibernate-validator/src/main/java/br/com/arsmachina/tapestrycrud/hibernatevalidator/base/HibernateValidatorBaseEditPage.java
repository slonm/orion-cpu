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

package br.com.arsmachina.tapestrycrud.hibernatevalidator.base;

import java.io.Serializable;

import org.apache.tapestry5.annotations.Mixin;

import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.mixins.HibernateValidatorMixin;

/**
 * {@link BaseEditPage} subclass that validates the edited object using Hibernate Validator.
 * 
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public abstract class HibernateValidatorBaseEditPage<T, K extends Serializable> extends
		BaseEditPage<T, K> {

	@Mixin
	@SuppressWarnings("unused")
	private HibernateValidatorMixin hibernateValidatorMixin;

}
