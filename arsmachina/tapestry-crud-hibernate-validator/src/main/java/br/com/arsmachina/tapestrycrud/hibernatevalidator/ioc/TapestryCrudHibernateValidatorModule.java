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

package br.com.arsmachina.tapestrycrud.hibernatevalidator.ioc;


import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.ValidationConstraintGenerator;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.validator.EmailValidationConstraintsGenerator;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.validator.LengthValidationConstraintsGenerator;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.validator.MaxValidationConstraintsGenerator;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.validator.MinValidationConstraintsGenerator;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.validator.NotEmptyValidationConstraintsGenerator;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.validator.NotNullValidationConstraintsGenerator;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Tapestry-IoC module for tapestry-crud-hibernate-validator.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestryCrudHibernateValidatorModule {

	/**
	 * Contributes the tapestry-crud components under the <code>crud</code> prefix.
	 * 
	 * @param configuration a {@link Configuration}.
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {

		configuration.add(new LibraryMapping("crudhv",
				"br.com.arsmachina.tapestrycrud.hibernatevalidator"));

	}
	
        public static Validator buildValidator(){
            return Validation.buildDefaultValidatorFactory().getValidator();
        }
        
	/**
	 * Contributes Hibernate Validator-related validation constraint generators.
	 * 
	 * @param configuration an {@link OrderedConfiguration}.
	 */
	public static void contributeValidationConstraintGenerator(
			OrderedConfiguration<ValidationConstraintGenerator> configuration) {

		configuration.add("notnull", new NotNullValidationConstraintsGenerator());
		configuration.add("notempty", new NotEmptyValidationConstraintsGenerator());
		configuration.add("length", new LengthValidationConstraintsGenerator());
		configuration.add("min", new MinValidationConstraintsGenerator());
		configuration.add("max", new MaxValidationConstraintsGenerator());
		configuration.add("email", new EmailValidationConstraintsGenerator());

	}
	
}
