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

package br.com.arsmachina.tapestrycrud.module;

import java.util.Set;

import br.com.arsmachina.tapestrycrud.beanmodel.BeanModelCustomizer;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.tree.SingleTypeTreeService;

/**
 * Interface that defines information about a module, whatever its conventions are.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface TapestryCrudModule {
	
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
	 * Returns the encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return an {@link Encoder} or null (if no corresponding one is found).
	 */
	<T> Class<? extends Encoder<T>> getEncoderClass(
			Class<T> entityClass);

	/**
	 * Returns the bean model customizer class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link BeanModelCustomizer} or null (if no corresponding one is found).
	 */
	<T> Class<? extends BeanModelCustomizer<T>> getBeanModelCustomizerClass(
			Class<T> entityClass);
	
	/**
	 * Returns the tree service class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link SingleTypeTreeService} or null (if no corresponding one is found).
	 */
	<T> Class<? extends SingleTypeTreeService<T>> getTreeServiceClass(
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
	 * Returns the module id. This can be null if this is the single module in the application.
	 * This method cannot return the empty string.
	 * 
	 * @return a {@link String}.
	 */
	String getId();
	
	/**
	 * Returns the name of the package where the Tapestry-related packages are located (i.e. under
	 * which the <code>pages</code> component is located).
	 * 
	 * @return a {@link String}.
	 */
	String getTapestryPackage();
	
	/**
	 * Returns the prefix used to name edition pages.
	 * 
	 * @return a {@link String}. It cannot be null.
	 */
	String getEditPagePrefix();

	/**
	 * Returns the prefix used to name listing pages.
	 * 
	 * @return a {@link String}. It cannot be null.
	 */
	public String getListPagePrefix();

	/**
	 * Returns the prefix used to name viewing pages.
	 * 
	 * @return a {@link String}. It cannot be null.
	 */
	public String getViewPagePrefix();

        /**
	 * Returns the {@link Class} of the SingleTypeSelectModelFactory
         * for a given entity class.
	 */
	<T> Class<? extends SingleTypeSelectModelFactory<T>>
                getSingleTypeSelectModelFactory(Class<T> entityClass);

}
