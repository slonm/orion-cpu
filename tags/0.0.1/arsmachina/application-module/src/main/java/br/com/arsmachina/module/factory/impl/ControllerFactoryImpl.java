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

package br.com.arsmachina.module.factory.impl;

import java.io.Serializable;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.controller.impl.ControllerImpl;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.factory.ControllerFactory;
import br.com.arsmachina.module.service.DAOSource;

/**
 * Default {@link ControllerFactory} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ControllerFactoryImpl implements ControllerFactory {

	final DAOSource daoSource;

	/**
	 * Single constructor of this class.
	 * 
	 * @param daoSource a {@link DAOSource}. It cannot be null.
	 */
	public ControllerFactoryImpl(DAOSource daoSource) {
		
		if (daoSource == null) {
			throw new IllegalArgumentException("Parameter daSource cannot be null");
		}
		
		this.daoSource = daoSource;
		
	}

	@SuppressWarnings("unchecked")
	public <T> Controller<T, ?> build(Class<T> entityClass) {
		
		Controller<T, ?> controller = null;
		final DAO<T, Serializable> dao = daoSource.get(entityClass);

		if (dao != null) {
			controller = build(dao);
		}
		
		return controller;
		
	}

	/**
	 * Builds a {@link Controller} instance given a corresponding {@link DAO}.
	 * 
	 * @param <T> an entity class.
	 * @param dao a {@link DAO}. It cannot be null.
	 * @return a {@link Controller}.
	 */
	@SuppressWarnings("unchecked")
	protected <T> Controller build(final DAO<T, Serializable> dao) {
		return new ConcreteControllerImpl(dao);
	}

	final private static class ConcreteControllerImpl<T, K extends Serializable> extends
			ControllerImpl<T, K> {

		/**
		 * @param dao
		 */
		public ConcreteControllerImpl(DAO<T, K> dao) {
			super(dao);
		}

	}

}
