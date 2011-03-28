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
package br.com.arsmachina.tapestrycrud.selectmodel.impl;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ioc.util.StrategyRegistry;

import br.com.arsmachina.tapestrycrud.selectmodel.CrudSelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;

/**
 * Default {@link SelectModelFactory} implementation. It delegates all its methods for
 * {@link SelectModelFactory} instances.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @see SelectModel
 * @see SingleTypeSelectModelFactory
 * @todo handle enums.
 */
public class CrudSelectModelFactoryImpl implements CrudSelectModelFactory {

    @SuppressWarnings("unchecked")
    final private StrategyRegistry<SingleTypeSelectModelFactory> registry;
    private final PropertyAccess propertyAccess;

    /**
     * Single constructor.
     *
     * @param registrations a {@link Map}&lt;{@link Class}, {@link Link}{@link SingleTypeSelectModelFactory}&gt;.
     * It cannot be null.
     * @param encoderSource an {@link EncoderSource}. It cannot be null.
     */
    @SuppressWarnings("unchecked")
    public CrudSelectModelFactoryImpl(Map<Class, SingleTypeSelectModelFactory> registrations,
            PropertyAccess propertyAccess) {
        registry = StrategyRegistry.newInstance(SingleTypeSelectModelFactory.class, registrations);
        this.propertyAccess = propertyAccess;
    }

    /**
     * @see br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory#create(java.lang.Class)
     */
    public SelectModel create(Class<?> clasz) {
        return create(clasz, null);
    }

    /**
     * @see br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory#create(java.lang.Class,
     * java.util.List)
     */
    public <T> SelectModel create(Class<T> clasz, List<T> objects) {
        return get(clasz).create(objects);
    }

    @SuppressWarnings("unchecked")
    private <T> SingleTypeSelectModelFactory<T> get(Class<T> clasz) {
        return registry.get(clasz);
    }

    public SelectModel create(String propertyName, Class<?> beanType) {
        PropertyAdapter propertyAdapter = propertyAccess.getAdapter(beanType).
                getPropertyAdapter(propertyName);
        return registry.get(beanType).create(propertyAdapter.getType(),
                propertyName, registry.get(propertyAdapter.getType()));
    }
}
