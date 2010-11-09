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
package br.com.arsmachina.tapestrycrud.hibernatevalidator.validator;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.services.ValidationConstraintGenerator;

/**
 * Adds <code>required</code> validation to Tapestry based on the {@link NotNull} annotation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
final public class NotNullValidationConstraintsGenerator implements ValidationConstraintGenerator {

	final private static List<String> REQUIRED;

	static {
		REQUIRED = new ArrayList<String>();
		REQUIRED.add("required");
	}

	@SuppressWarnings("unchecked")
	public List<String> buildConstraints(Class propertyType, AnnotationProvider annotationProvider) {

		boolean notNull = annotationProvider.getAnnotation(NotNull.class) != null;
		return notNull ? REQUIRED : null;

	}

}
