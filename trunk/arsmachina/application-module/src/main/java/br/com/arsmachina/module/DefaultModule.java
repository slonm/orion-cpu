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

import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.authorization.SingleTypeAuthorizer;
import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.DAO;

/**
 * Default {@link Module} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class DefaultModule extends AbstractModule implements Module {

	final static Logger LOGGER = LoggerFactory.getLogger(DefaultModule.class);

	final private String daoImplementationSubpackage;

	/**
	 * Constructor with a module id.
	 * 
	 * @param rootPackage a {@link String} containing the module parent package. It cannot be null.
	 * @param classNameLocator a {@link ClassNameLocator}. It cannot be null.
	 * @param daoImplementationSubpackage a {@link String}. It cannot be null.
	 */
	public DefaultModule(String rootPackage, ClassNameLocator classNameLocator,
			String daoImplementationSubpackage) {

		this(null, rootPackage, classNameLocator, daoImplementationSubpackage);

	}

	/**
	 * Constructor with a module id.
	 * 
	 * @param id a {@link String} containing the module id. It cannot be null.
	 * @param rootPackage a {@link String} containing the module parent package. It cannot be null.
	 * @param classNameLocator a {@link ClassNameLocator}. It cannot be null.
	 * @param daoImplementationSubpackage a {@link String}. It cannot be null.
	 */
	public DefaultModule(String id, String rootPackage, ClassNameLocator classNameLocator,
			String daoImplementationSubpackage) {

		super(id, rootPackage, classNameLocator);

		if (daoImplementationSubpackage == null) {
			throw new IllegalArgumentException(
					"Parameter daoImplementationSubpackage cannot be null");
		}

		this.daoImplementationSubpackage = daoImplementationSubpackage;

	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends Controller<T, ?>> getControllerImplementationClass(
			Class<T> entityClass) {
		return getClass(getControllerImplementationClassName(entityClass));
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends Controller<T, ?>> getControllerDefinitionClass(Class<T> entityClass) {
		return getClass(getControllerDefinitionClassName(entityClass));
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends DAO<T, ?>> getDAOImplementationClass(Class<T> entityClass) {
		return getClass(getDAOImplementationClassName(entityClass));
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends SingleTypeAuthorizer<T>> getAuthorizerClass(Class<T> entityClass) {

		return getClass(getAuthorizerClassName(entityClass));

	}

	/**
	 * Returns the fully-qualified name of the authorizer for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null.
	 */
	public String getAuthorizerClassName(Class<?> entityClass) {

		return String.format("%s.%sAuthorizer", getAuthorizationPackage(),
				entityClass.getSimpleName());

	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends DAO<T, ?>> getDAODefinitionClass(Class<T> entityClass) {
		return getClass(getDAODefinitionClassName(entityClass));
	}

	/**
	 * Returns the fully-qualified name of the controller class implementation for a given entity
	 * class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	public String getControllerImplementationClassName(Class<?> entityClass) {

		return String.format("%s.controller.impl.%sControllerImpl", getRootPackage(),
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the controller definition (interface) for a given entity
	 * class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	public String getControllerDefinitionClassName(Class<?> entityClass) {

		return String.format("%s.controller.%sController", getRootPackage(),
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the DAO class implementation for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	public String getDAOImplementationClassName(Class<?> entityClass) {

		return String.format("%s.dao.%s.%sDAOImpl", getRootPackage(), daoImplementationSubpackage,
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the DAO definition (interface) for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	public String getDAODefinitionClassName(Class<?> entityClass) {

		return String.format("%s.dao.%sDAO", getRootPackage(), entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the {@link SingleTypeAuthorizationService} class for a
	 * given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	public String getSingleTypeAuthorizationServiceClassName(Class<?> entityClass) {

		return String.format("%s.authorization.%sAuthorizationService", getRootPackage(),
				entityClass.getSimpleName());

	}

	/**
	 * Returns the name of the package where the authorizaiont-related classes are located This
	 * implementation returns <code>[rootPackage].authorization</code>.
	 * 
	 * @return a {@link String}.
	 */
	protected String getAuthorizationPackage() {
		return getRootPackage() + ".authorization";
	}

}
