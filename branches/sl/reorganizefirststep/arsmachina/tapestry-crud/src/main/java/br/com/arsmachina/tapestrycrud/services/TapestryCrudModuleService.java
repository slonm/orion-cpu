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

package br.com.arsmachina.tapestrycrud.services;

import java.util.Set;

import br.com.arsmachina.module.Module;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.module.TapestryCrudModule;
import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;

/**
 * Service that provides module-related methods.
 * 
 * @see Module
 * @author Thiago H. de Paula Figueiredo
 */
public interface TapestryCrudModuleService {

	/**
	 * Returns the set of all modules.
	 * 
	 * @return a {@link Set} of {@link TapestryCrudModule}s.
	 */
	Set<TapestryCrudModule> getModules();

	/**
	 * Returns the set of all entity classes.
	 * 
	 * @return a {@link Set} of {@link Class} instances.
	 */
	Set<Class<?>> getEntityClasses();
	
	/**
	 * Return the {@link TapestryCrudModule} that owns a given entity class.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link TapestryCrudModule} or null.
	 */
	TapestryCrudModule getModule(Class<?> entityClass);
	
	/**
	 * Does one of the available Tapestry CRUD modules contain the given entity class?.
	 * 
	 * @param entityClass a {@link Class} instance.
	 * @return a <code>boolean</code>.
	 */
	boolean contains(Class<?> entityClass);

	/**
	 * Returns the encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return an {@link Class} or null (if no corresponding one is found).
	 */
	<T> Class<? extends Encoder<T>> getEncoderClass(
			Class<T> entityClass);

	/**
	 * Returns the {@link Class} of the edition page for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	Class<?> getEditPageClass(Class<?> entityClass);

	/**
	 * Returns the {@link Class} of the listing page for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	Class<?> getListPageClass(Class<?> entityClass);
	
	/**
	 * Returns the {@link Class} of the viewing page for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	Class<?> getViewPageClass(Class<?> entityClass);
	
	/**
	 * Returns the {@link Class} of the SingleTypeSelectModelFactory
         * for a given entity class.
	 */
	<T> Class<? extends SingleTypeSelectModelFactory<T>>
                getSingleTypeModelFactory(Class<T> entityClass);

}
