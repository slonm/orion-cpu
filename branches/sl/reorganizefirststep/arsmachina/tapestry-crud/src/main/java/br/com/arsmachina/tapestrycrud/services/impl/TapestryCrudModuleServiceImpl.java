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

package br.com.arsmachina.tapestrycrud.services.impl;

import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.arsmachina.module.service.ModuleService;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.module.TapestryCrudModule;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import java.util.Collection;

/**
 * Default {@link ModuleService} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestryCrudModuleServiceImpl implements TapestryCrudModuleService {

	final private Set<TapestryCrudModule> modules;

	final private Set<Class<?>> entityClasses = new HashSet<Class<?>>();

	/**
	 * Single constructor of this class.
	 * 
	 * @param entityClasses a {@link Set} of {@link TapestryCrudModule}s. It cannot be null.
	 */
	public TapestryCrudModuleServiceImpl(Collection<TapestryCrudModule> modules) {

		if (modules == null) {
			throw new IllegalArgumentException("Parameter modules cannot be null");
		}

		this.modules = Collections.unmodifiableSet(new HashSet(modules));
		fillEntityClasses();

	}

	public Set<TapestryCrudModule> getModules() {
		return modules;
	}


	public <T> Class<? extends Encoder<T>> getEncoderClass(Class<T> entityClass) {

		Class<? extends Encoder<T>> encoder = null;

		for (TapestryCrudModule module : modules) {

			encoder = module.getEncoderClass(entityClass);

			if (encoder != null) {
				break;
			}

		}

		return encoder;

	}

    public <T> Class<? extends SingleTypeSelectModelFactory<T>> getSingleTypeModelFactory(Class<T> entityClass) {

		Class<? extends SingleTypeSelectModelFactory<T>> model = null;

		for (TapestryCrudModule module : modules) {

			model = module.getSingleTypeSelectModelFactory(entityClass);

			if (model != null) {
				break;
			}

		}

		return model;

	}

	public Class<?> getEditPageClass(Class<?> entityClass) {

		Class<?> clasz = null;

		for (TapestryCrudModule module : getModules()) {

			if (module.contains(entityClass)) {
				clasz = module.getEditPageClass(entityClass);
				break;
			}

		}

		return clasz;

	}

	public Class<?> getListPageClass(Class<?> entityClass) {

		Class<?> clasz = null;

		for (TapestryCrudModule module : getModules()) {

			if (module.contains(entityClass)) {
				clasz = module.getListPageClass(entityClass);
				break;
			}

		}

		return clasz;

	}

	public Class<?> getViewPageClass(Class<?> entityClass) {

		Class<?> clasz = null;

		for (TapestryCrudModule module : getModules()) {

			if (module.contains(entityClass)) {
				clasz = module.getViewPageClass(entityClass);
				break;
			}

		}

		return clasz;

	}

	public Set<Class<?>> getEntityClasses() {
		return entityClasses;
	}

	public boolean contains(Class<?> entityClass) {
		return getEntityClasses().contains(entityClass);
	}

	public TapestryCrudModule getModule(Class<?> entityClass) {
		
		TapestryCrudModule owner = null;
		
		for (TapestryCrudModule module : modules) {
			
			if (module.contains(entityClass)) {
				owner = module;
				break;
			}
			
		}
		
		return owner;
		
	}

	private void fillEntityClasses() {

		for (TapestryCrudModule module : modules) {

			final Set<Class<?>> moduleEntityClasses = module.getEntityClasses();
			entityClasses.addAll(moduleEntityClasses);

		}

	}

}
