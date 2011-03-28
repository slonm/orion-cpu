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

package br.com.arsmachina.module.service.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.Module;
import br.com.arsmachina.module.service.ModuleService;
import java.util.Collection;

/**
 * Default {@link ModuleService} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ModuleServiceImpl implements ModuleService {

	final private Set<br.com.arsmachina.module.Module> modules;

	final private Set<Class<?>> entityClasses;
	
	/**
	 * Single constructor of this class.
	 * 
	 * @param entityClasses a {@link Set} of {@link Class} instances. It cannot be null.
	 */
	public ModuleServiceImpl(Collection<br.com.arsmachina.module.Module> modules) {

		if (modules == null) {
			throw new IllegalArgumentException("Parameter modules cannot be null");
		}

		this.modules = Collections.unmodifiableSet(new HashSet(modules));

		Set<Class<?>> set = new HashSet<Class<?>>();

		for (Module module : modules) {

			final Set<Class<?>> entityClasses = module.getEntityClasses();

			for (Class<?> entityClass : entityClasses) {
				set.add(entityClass);
			}

		}

		entityClasses = Collections.unmodifiableSet(set);

	}

	public Set<Module> getModules() {
		return modules;
	}

	public Set<Class<?>> getEntityClasses() {
		return entityClasses;
	}

	public <T> Class<? extends Controller<T, ?>> getControllerImplementationClass(Class<T> entityClass) {

		Class<? extends Controller<T, ?>> controller = null;
		
		for (Module module : modules) {
			
			controller = module.getControllerImplementationClass(entityClass);
			
			if (controller != null) {
				break;
			}
			
		}
		
		return controller;
		
	}

	public <T> Class<? extends Controller<T, ?>> getControllerDefinitionClass(Class<T> entityClass) {

		Class<? extends Controller<T, ?>> controller = null;
		
		for (Module module : modules) {
			
			controller = module.getControllerDefinitionClass(entityClass);
			
			if (controller != null) {
				break;
			}
			
		}
		
		return controller;
		
	}

	public <T> Class<? extends DAO<T, ?>> getDAOImplementationClass(Class<T> entityClass) {

		Class<? extends DAO<T, ?>> dao = null;
		
		for (Module module : modules) {
			
			dao = module.getDAOImplementationClass(entityClass);
			
			if (dao != null) {
				break;
			}
			
		}
		
		return dao;
		
	}

	public <T> Class<? extends DAO<T, ?>> getDAODefinitionClass(Class<T> entityClass) {

		Class<? extends DAO<T, ?>> dao = null;
		
		for (Module module : modules) {
			
			dao = module.getDAODefinitionClass(entityClass);
			
			if (dao != null) {
				break;
			}
			
		}
		
		return dao;
		
	}

	public String getControllerDefinitionClassName(Class<?> entityClass) {
		
		String className = null;
		
		for (Module module : getModules()) {
			
			if (module.contains(entityClass)) {
				className = module.getControllerDefinitionClassName(entityClass);
			}
			
		}
		
		return className;
		
	}

	public String getControllerImplementationClassName(Class<?> entityClass) {
		
		String className = null;
		
		for (Module module : getModules()) {
			
			if (module.contains(entityClass)) {
				className = module.getControllerImplementationClassName(entityClass);
			}
			
		}
		
		return className;
		
	}

	public String getDAODefinitionClassName(Class<?> entityClass) {
		
		String className = null;
		
		for (Module module : getModules()) {
			
			if (module.contains(entityClass)) {
				
				className = module.getDAODefinitionClassName(entityClass);
				break;
				
			}
			
		}
		
		return className;
		
	}

	public String getDAOImplementationClassName(Class<?> entityClass) {
		
		String className = null;
		
		for (Module module : getModules()) {
			
			if (module.contains(entityClass)) {
				
				className = module.getDAOImplementationClassName(entityClass);
				break;
				
			}
			
		}
		
		return className;
		
	}

}
