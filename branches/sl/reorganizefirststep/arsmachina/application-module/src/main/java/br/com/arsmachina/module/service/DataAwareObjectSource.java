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
package br.com.arsmachina.module.service;

/**
 *
 * @author sl
 */
public interface DataAwareObjectSource {

    /**
     * Example:
     * interface EntityDAO extends DAO<Entity, Serializable>{...}
     * ...
     * DAO<T,K> _DAO=get(DAO.class, Entity.class)
     * 
     * do not use:
     *   EntityDAO _DAO=get(EntityDAO.class, Entity.class)
     * use
     *   @Inject EntityDAO _DAO; 
     * instead
     * 
     * but...
     * 
     * interface NamedDAO<T, K> extends DAO<T, K>{...}
     * ...
     * NamedDAO<T,K> _DAO=get(NamedDAO.class, Entity.class)
     * 
     * @return object or null
     */
    <T> T get(Class<T> objectType, Class<?> entity);

    void clearCache();
}
