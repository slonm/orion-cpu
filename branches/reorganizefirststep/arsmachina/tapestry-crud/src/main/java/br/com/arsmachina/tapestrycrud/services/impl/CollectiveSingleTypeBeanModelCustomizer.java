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

import java.util.Map;
import org.apache.tapestry5.beaneditor.BeanModel;

import org.apache.tapestry5.ioc.util.StrategyRegistry;

import br.com.arsmachina.tapestrycrud.beanmodel.BeanModelCustomizer;

/**
 * {@link BeanModelCustomizerSource} implementation.
 * 
 * @author sl
 */
public class CollectiveSingleTypeBeanModelCustomizer<T> implements BeanModelCustomizer<T> {

    @SuppressWarnings("unchecked")
    final private StrategyRegistry<BeanModelCustomizer> registry;

    /**
     * Single constructor.
     *
     * @param registrations
     */
    @SuppressWarnings("unchecked")
    public CollectiveSingleTypeBeanModelCustomizer(Map<Class, BeanModelCustomizer> registrations) {

        if (registrations == null) {
            throw new IllegalArgumentException("Parameter registrations cannot be null");
        }

        registry = StrategyRegistry.newInstance(BeanModelCustomizer.class, registrations, true);

    }

    public BeanModel<T> customizeModel(BeanModel<T> model) {
        BeanModelCustomizer<T> customizer = registry.get(model.getBeanType());
        if (customizer != null) {
            return customizer.customizeModel(model);
        } else {
            return model;
        }
    }

    public BeanModel<T> customizeEditModel(BeanModel<T> model) {
        BeanModelCustomizer<T> customizer = registry.get(model.getBeanType());
        if (customizer != null) {
            return customizer.customizeEditModel(model);
        } else {
            return model;
        }
    }

    public BeanModel<T> customizeDisplayModel(BeanModel<T> model) {
        BeanModelCustomizer<T> customizer = registry.get(model.getBeanType());
        if (customizer != null) {
            return customizer.customizeDisplayModel(model);
        } else {
            return model;
        }
    }
}
