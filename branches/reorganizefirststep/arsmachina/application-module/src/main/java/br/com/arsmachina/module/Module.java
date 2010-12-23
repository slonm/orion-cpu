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

package br.com.arsmachina.module;

import java.util.Set;

import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.authorization.SingleTypeAuthorizer;
import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.DAO;

/**
 * Interface that defines information about a module, whatever its conventions are.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface Module {

	/**
	 * Returns the module id. This can be null if this is the single module in the application. This
	 * method cannot return the empty string.
	 * 
	 * @return a {@link String}.
	 */
	String getId();

	/**
	 * Returns the root package of this module.
	 * 
	 * @return a {@link String}.
	 */
	String getRootPackage();

	/**
	 * Returns the entity package of this module.
	 * 
	 * @return a {@link String}.
	 */
	String getEntityPackage();

	/**
	 * Returns a {@link Set} containing all the entity classes in this module.
	 * 
	 * @return a {@link Set} of {@link Class} instances.
	 */
	Set<Class<?>> getEntityClasses();

	/**
	 * Does this module contain the given entity class?.
	 * 
	 * @param entityClass a {@link Class} instance.
	 * @return a <code>boolean</code>.
	 */
	boolean contains(Class<?> entityClass);

	/**
	 * Returns the controller class implementation corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link Controller} or null (if no corresponding one is found).
	 */
	<T> Class<? extends Controller<T, ?>> getControllerImplementationClass(Class<T> entityClass);

	/**
	 * Returns the controller definition (interface) corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link Controller} or null (if no corresponding one is found).
	 */
	<T> Class<? extends Controller<T, ?>> getControllerDefinitionClass(Class<T> entityClass);

	/**
	 * Returns the DAO class implementation corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link DAO} or null (if no corresponding one is found).
	 */
	<T> Class<? extends DAO<T, ?>> getDAOImplementationClass(Class<T> entityClass);

	/**
	 * Returns the DAO definition (interface) corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link DAO} or null (if no corresponding one is found).
	 */
	<T> Class<? extends DAO<T, ?>> getDAODefinitionClass(Class<T> entityClass);

	/**
	 * Returns the fully-qualified name of the DAO class implementation for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	String getDAOImplementationClassName(Class<?> entityClass);

	/**
	 * Returns the fully-qualified name of the DAO definition (interface) for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	String getDAODefinitionClassName(Class<?> entityClass);

	/**
	 * Returns the fully-qualified name of the {@link SingleTypeAuthorizationService}
	 * implementation for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	String getSingleTypeAuthorizationServiceClassName(Class<?> entityClass);

	/**
	 * Returns the fully-qualified name of the controller class implementation for a given entity
	 * class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	String getControllerImplementationClassName(Class<?> entityClass);

	/**
	 * Returns the fully-qualified name of the controller definition (interface) for a given entity
	 * class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	String getControllerDefinitionClassName(Class<?> entityClass);
	
	/**
	 * Returns the authorizer class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return an {@link Authorizer} or null (if no corresponding one is found).
	 */
	<T> Class<? extends SingleTypeAuthorizer<T>> getAuthorizerClass(
			Class<T> entityClass);
	
	/**
	 * Returns the fully-qualified name of the authorizer for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	String getAuthorizerClassName(Class<?> entityClass);
	
}
