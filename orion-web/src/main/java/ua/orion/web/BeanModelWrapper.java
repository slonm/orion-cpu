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

package ua.orion.web;

import java.util.List;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.beaneditor.RelativePosition;

/**
 * BeanModelWrapper override method #newInstance() to standart call
 * newInstance of BeanType
 * @author sl
 */
public class BeanModelWrapper<T> implements BeanModel<T>{
    
    private final BeanModel<T> model;

    public BeanModelWrapper(BeanModel<T> model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return model.toString();
    }

    @Override
    public int hashCode() {
        return model.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return model.equals(obj);
    }

    public BeanModel<T> reorder(String... propertyNames) {
        return model.reorder(propertyNames);
    }

    public T newInstance() {
        try {
            return getBeanType().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public BeanModel<T> include(String... propertyNames) {
        return model.include(propertyNames);
    }

    public List<String> getPropertyNames() {
        return model.getPropertyNames();
    }

    public PropertyModel getById(String propertyId) {
        return model.getById(propertyId);
    }

    public Class<T> getBeanType() {
        return model.getBeanType();
    }

    public PropertyModel get(String propertyName) {
        return model.get(propertyName);
    }

    public BeanModel<T> exclude(String... propertyNames) {
        return model.exclude(propertyNames);
    }

    public PropertyModel add(String propertyName, PropertyConduit conduit) {
        return model.add(propertyName, conduit);
    }

    public PropertyModel add(RelativePosition position, String existingPropertyName, String propertyName, PropertyConduit conduit) {
        return model.add(position, existingPropertyName, propertyName, conduit);
    }

    public PropertyModel add(RelativePosition position, String existingPropertyName, String propertyName) {
        return model.add(position, existingPropertyName, propertyName);
    }

    public PropertyModel add(String propertyName) {
        return model.add(propertyName);
    }

    @Override
    public PropertyModel addExpression(String propertyName, String expression) {
        return model.addExpression(propertyName, expression);
    }

    @Override
    public PropertyModel addEmpty(String propertyName) {
        return model.addEmpty(propertyName);
    }
   
}
