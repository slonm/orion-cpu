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

package br.com.arsmachina.module.service.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.arsmachina.module.service.PrimaryKeyTypeService;

/**
 * A {@link PrimaryKeyTypeService} implemented using JPA alone.
 * 
 * @todo use a map to cache answers.
 * @author Thiago H. de Paula Figueiredo
 */
final public class JPAPrimaryKeyTypeService implements PrimaryKeyTypeService {

	@SuppressWarnings("unchecked")
	public String getPrimaryKeyPropertyName(Class entityClass) {

		String propertyName = null;

		if (entityClass.isAnnotationPresent(Entity.class)) {

			Field field = getIdField(entityClass);

			if (field != null) {
				propertyName = field.getName();
			}
			else {

				Method method = getIdProperty(entityClass);

				if (method != null) {

					String methodName = method.getName();
					propertyName = Character.toLowerCase(methodName.charAt(3))
							+ methodName.substring(4, methodName.length());

				}

			}

		}

		return propertyName;

	}

	// @todo handle classes which has @Id in a getter and the property name is different
	// from the field name.
	@SuppressWarnings("unchecked")
	public Class getPrimaryKeyType(Class entityClass) {

		Class primaryKeyType = null;

		if (entityClass.isAnnotationPresent(Entity.class)) {

			Field field = getIdField(entityClass);

			if (field != null) {
				primaryKeyType = field.getClass();
			}
			else {

				Method method = getIdProperty(entityClass);

				if (method != null) {
					primaryKeyType = method.getReturnType();
				}

			}
			

		}

		return primaryKeyType;

	}
	
	@SuppressWarnings("unchecked")
	private Method getIdProperty(Class entityClass) {

		Method idProperty = null;
		final Method[] methods = entityClass.getMethods();

		for (Method method : methods) {

			if (method.isAnnotationPresent(Id.class) && method.getName().startsWith("get")
					&& method.getParameterTypes().length == 0) {

				idProperty = method;
				break;

			}

			if (idProperty == null) {
				
				Class<?> superClass = entityClass.getSuperclass();
				
				if (superClass.equals(Object.class) == false) {
					idProperty = getIdProperty(superClass);
				}
				
			}
			
		}

		return idProperty;

	}

	@SuppressWarnings("unchecked")
	private Field getIdField(Class entityClass) {

		Field field = null;

		final Field[] fields = entityClass.getFields();

		for (Field f : fields) {

			if (f.isAnnotationPresent(Id.class)) {
				field = f;
				break;
			}
			
			if (field == null) {
				
				Class<?> superClass = entityClass.getSuperclass();
				
				if (superClass.equals(Object.class) == false) {
					field = getIdField(superClass);
				}
				
			}

		}

		return field;

	}

}
