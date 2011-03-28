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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract superclass for module classes.
 * Модуль отличается от оригинального тем, что
 * сделана отложенная инициализация fillEntityClasses() (Mihail Slobodyanuk)
 * @author Thiago H. de Paula Figueiredo
 */
public abstract class AbstractModule {

	private final String id;

	private final String rootPackage;

	private final ClassNameLocator classNameLocator;

	private final Logger logger;

	private final Set<Class<?>> entityClasses = new HashSet<Class<?>>();

        private boolean entityClassesFilled=false;

	/**
	 * Constructor without a module id.
	 *
	 * @param id a {@link String} containing the id. It can be null but not empty.
	 * @param rootPackage a {@link String} containing the module parent package. It cannot be null.
	 * @param classNameLocator a {@link ClassNameLocator}. It cannot be null.
	 */
	protected AbstractModule(String rootPackage, ClassNameLocator classNameLocator) {
		this(null, rootPackage, classNameLocator);
	}

	/**
	 * Constructor with a module id.
	 *
	 * @param id a {@link String} containing the id. It can be null but not empty.
	 * @param rootPackage a {@link String} containing the module parent package. It cannot be null.
	 * @param classNameLocator a {@link ClassNameLocator}. It cannot be null.
	 */
	protected AbstractModule(String id, String rootPackage, ClassNameLocator classNameLocator) {

		if (id != null && id.trim().length() == 0) {
			throw new IllegalArgumentException("Parameter name cannot be empty");
		}

		if (rootPackage == null) {
			throw new IllegalArgumentException("Parameter rootPackage cannot be null");
		}

		if (classNameLocator == null) {
			throw new IllegalArgumentException("Parameter classNameLocator cannot be null");
		}

		this.id = id;
		this.rootPackage = rootPackage;
		this.classNameLocator = classNameLocator;
		this.logger = LoggerFactory.getLogger(getClass());
                logger.debug("Created module {}", id);
	}

	final public String getId() {
		return id;
	}

	public final String getRootPackage() {
		return rootPackage;
	}

	/**
	 * Returns <code>getClass(name, false)</code>.
	 *
	 * @param name a {@link String}. It cannot be null.
	 * @return a {@link Class}.
	 */
	@SuppressWarnings("unchecked")
	protected Class getClass(String name) {
		return getClass(name, true);
	}

	/**
	 * Returns the {@link Class} instance given a class name.
	 *
	 * @param name a {@link String}. It cannot be null.
	 * @param lenient a <code>boolean</code>. If <code>false</code>, it will throw an exception if
	 * the class is not found. Otherwise, this method will return null.
	 * @return a {@link Class} or null.
	 */
	@SuppressWarnings("unchecked")
	protected Class getClass(String name, boolean lenient) {

		Class<?> clasz = null;

		try {
			clasz = Thread.currentThread().getContextClassLoader().loadClass(name);
		}
		catch (ClassNotFoundException e) {

			if (lenient == false) {
				throw new RuntimeException(e);
			}
			else {

				if (logger.isDebugEnabled()) {
					logger.debug("Class not found: " + name);
				}

			}

		}

		return clasz;

	}

	/**
	 * Returns the <code>classNameLocator</code> property.
	 *
	 * @return a {@link ClassNameLocator}.
	 */
	final protected ClassNameLocator getClassNameLocator() {
		return classNameLocator;
	}

	@Override
	public String toString() {
		return String.format("%s module (%s)", getId(), getRootPackage());
	}

	public Set<Class<?>> getEntityClasses() {
                if(!entityClassesFilled){
                    fillEntityClasses();
                    entityClassesFilled=true;
                }
		return entityClasses;
	}

	/**
	 * Returns the package that contains the entity classes.
	 *
	 * @return a {@link String}.
	 */
	public String getEntityPackage() {
		return getRootPackage() + ".entity";
	}

	private final void fillEntityClasses() {

		String entityPackage = getEntityPackage();
		final Collection<String> classNames = getClassNameLocator().locateClassNames(entityPackage);

		for (String className : classNames) {

			final Class<?> entityClass = getClass(className);

			if (accept(entityClass)) {
				entityClasses.add(entityClass);
			}

		}

	}

	/**
	 * Tells if a given class must be considered an entity. This method returns <code>true</code> if
	 * it is annotated with {@link Entity}.
	 *
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a <code>boolean</code>.
	 */
	protected boolean accept(Class<?> clasz) {
		return clasz.isAnnotationPresent(Entity.class);
	}

	public boolean contains(Class<?> entityClass) {

		boolean contains = getEntityClasses().contains(entityClass);

		if (contains == false && entityClass.getName().contains("$$_javassist_")) {
			contains = getEntityClasses().contains(entityClass.getSuperclass());
		}

		return contains;

	}

}
