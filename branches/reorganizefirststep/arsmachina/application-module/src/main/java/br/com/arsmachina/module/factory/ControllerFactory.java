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

package br.com.arsmachina.module.factory;

import br.com.arsmachina.controller.Controller;

/**
 * Interface that defines a factory of default {@link Controller}s. A default controller is one
 * with only the methods defined by the {@link Controller} instance.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface ControllerFactory {

	/**
	 * Builds a default {@link Controller} instance for the given entity class. It must return null
	 * if the given class is not supported.
	 * 
	 * @param <T> an entity class.
	 * @param entityClass a {@link Class} entity. It cannot be null.
	 * @return a {@link Controller} or null.
	 */
	<T> Controller<T, ?> build(Class<T> entityClass);

}
