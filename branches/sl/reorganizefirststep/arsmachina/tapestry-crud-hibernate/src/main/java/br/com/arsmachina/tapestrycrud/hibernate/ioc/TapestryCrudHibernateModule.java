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

package br.com.arsmachina.tapestrycrud.hibernate.ioc;



import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.services.ValidationConstraintGenerator;
import br.com.arsmachina.tapestrycrud.hibernate.validator.ColumnValidationConstraintsGenerator;
import br.com.arsmachina.tapestrycrud.hibernate.validator.OptionalAttributeValidationConstraintsGenerator;

/**
 * Tapestry CRUD-Hibernate module class.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestryCrudHibernateModule {

	/**
	 * Contributes JPA-related validation constraint generators.
	 * 
	 * @param configuration an {@link OrderedConfiguration}.
	 */
	public static void contributeValidationConstraintGenerator(
			OrderedConfiguration<ValidationConstraintGenerator> configuration) {

		configuration.add("column", new ColumnValidationConstraintsGenerator());
		configuration.add("optional", new OptionalAttributeValidationConstraintsGenerator());

	}

}
