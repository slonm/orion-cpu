/*
 *  Copyright 2010 sl.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package br.com.arsmachina.tapestrycrud;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;

/**
 *
 * @author sl
 */
public interface CrudListPage<T> extends CrudPage<T> {

    Grid getGrid();

    Object getObjects();

    /**
     * Set current object (for grid).
     *
     * @param object a {@link T}.
     */
    void setObject(T object);

    /**
     * Creates a {@link BeanModel} and removes the primary key property from it.
     *
     * @return a {@link BeanModel}.
     */
    BeanModel<T> getBeanModel();
}
