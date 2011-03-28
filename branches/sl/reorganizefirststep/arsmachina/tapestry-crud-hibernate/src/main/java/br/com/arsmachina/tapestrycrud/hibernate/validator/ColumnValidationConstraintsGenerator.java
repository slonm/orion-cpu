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
package br.com.arsmachina.tapestrycrud.hibernate.validator;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.services.ValidationConstraintGenerator;

/**
 * Adds <code>required</code> and <code>maxlength</code> validation to Tapestry based on the
 * {@link Column} annotation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
final public class ColumnValidationConstraintsGenerator implements ValidationConstraintGenerator {

	@SuppressWarnings("unchecked")
	public List<String> buildConstraints(Class propertyType, AnnotationProvider annotationProvider) {

		List<String> validations = null;

		final Column column = annotationProvider.getAnnotation(Column.class);
		int length = 255; // default length value

		if (column != null) {

			if (column.nullable() == false && annotationProvider.getAnnotation(GeneratedValue.class) == null) {
				validations = new ArrayList<String>();
				validations.add("required");
			}

			length = column.length();

		}

		if (propertyType.equals(String.class) && length > 0) {

			if (validations == null) {
				validations = new ArrayList<String>();
			}

			validations.add(String.format("maxlength=%d", length));

		}

		return validations;

	}
	
}
