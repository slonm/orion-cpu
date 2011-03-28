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

package br.com.arsmachina.module.service;

import java.util.Set;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.Module;

/**
 * Service that provides module-related methods.
 * 
 * @see Module
 * @author Thiago H. de Paula Figueiredo
 */
public interface ModuleService {

	/**
	 * Returns the set of all entity classes.
	 * 
	 * @return a {@link Set} of {@link Module} instances.
	 */
	Set<Module> getModules();

	/**
	 * Returns the set of all entity classes.
	 * 
	 * @return a {@link Set} of {@link Class} instances.
	 */
	Set<Class<?>> getEntityClasses();

	/**
	 * Returns the controller definition (interface) corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link Class} or null (if no corresponding one is found).
	 */
	<T> Class<? extends Controller<T, ?>> getControllerDefinitionClass(Class<T> entityClass);

	/**
	 * Returns the controller implementation class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link Class} or null (if no corresponding one is found).
	 */
	<T> Class<? extends Controller<T, ?>> getControllerImplementationClass(Class<T> entityClass);

	/**
	 * Returns the DAO definition (interface) corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link Class} or null (if no corresponding one is found).
	 */
	<T> Class<? extends DAO<T, ?>> getDAODefinitionClass(Class<T> entityClass);

	/**
	 * Returns the DAO implementation class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link Class} or null (if no corresponding one is found).
	 */
	<T> Class<? extends DAO<T, ?>> getDAOImplementationClass(Class<T> entityClass);
	
}
