// Copyright 2008-2009 Thiago H. de Paula Figueiredo
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

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ValueEncoder;
import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.service.PrimaryKeyTypeService;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link Encoder} implementation for classes
 * Based on
 * @param <T> the entity class related to this encoder.
 * 
 * @author sl
 */
public class DefaultEncoder<T> implements Encoder<T> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEncoder.class);
    final private Controller controller;
    private final PropertyAdapter propertyAdapter;
    final private String primaryKeyPropertyName;
    private final TypeCoercer typeCoercer;
    private final Class<T> entityClass;

    /**
     * Constructor meant to be used when subclassing this class.
     * @param controller a {@link Controller}. It cannot be null.
     * @param primaryKeyTypeService a {@link PrimaryKeyTypeService}. It cannot be null.
     */
    protected DefaultEncoder(Controller<T, ?> controller,
            PrimaryKeyTypeService primaryKeyTypeService,
            PropertyAccess propertyAccess, TypeCoercer typeCoercer) {
        this(null, controller, primaryKeyTypeService,
                propertyAccess, typeCoercer);
    }

    /**
     * Constructor meant to be used when instantiating this class directly.
     * @param entityClass a {@link Class} instance representing the entity class.
     * @param controller a {@link Controller}. It cannot be null.
     * @param primaryKeyTypeService a {@link PrimaryKeyTypeService}. It cannot be null.
     */
    @SuppressWarnings("unchecked")
    public DefaultEncoder(Class<T> entityClass, Controller<T, ?> controller,
            PrimaryKeyTypeService primaryKeyTypeService,
            PropertyAccess propertyAccess, TypeCoercer typeCoercer) {

        if (controller == null) {
            throw new IllegalArgumentException("Parameter controller cannot be null");
        }

        if (primaryKeyTypeService == null) {
            throw new IllegalArgumentException("Parameter primaryKeyTypeService cannot be null");
        }

        if (typeCoercer == null) {
            throw new IllegalArgumentException("Parameter typeCoercer cannot be null");
        }

        if (propertyAccess == null) {
            throw new IllegalArgumentException("Parameter propertyAccess cannot be null");
        }

        this.controller = controller;
        this.typeCoercer = typeCoercer;
        this.entityClass = entityClass != null ? entityClass : getEntityClassFromHierarchy();
        primaryKeyPropertyName = primaryKeyTypeService.getPrimaryKeyPropertyName(this.entityClass);
        propertyAdapter = propertyAccess.getAdapter(this.entityClass).getPropertyAdapter(primaryKeyPropertyName);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEntityClassFromHierarchy() {
        final Type genericSuperclass = getClass().getGenericSuperclass();
        final ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);

        Class<T> type = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        return type;
    }

    public String toClient(T value) {
        if (value == null) {
            return null;
        }

        Object id = propertyAdapter.get(value);

        if (id == null) {
            throw new IllegalStateException(String.format(
                    "Entity %s has an %s property of null; this probably means that it has not been persisted yet.",
                    value, primaryKeyPropertyName));
        }

        return typeCoercer.coerce(id, String.class);
    }

    @SuppressWarnings("unchecked")
    public T toValue(String clientValue) {
        if (clientValue == null || clientValue.isEmpty()) {
            return null;
        }

        Serializable id = (Serializable) typeCoercer.coerce(clientValue, propertyAdapter.getType());

        T result = (T) controller.findById(id);

        if (result == null) {
            logger.error(String.format("Unable to convert client value '%s' into an entity instance of '%s'.",
                    clientValue, entityClass.getName()));
        }

        return result;
    }

    public ValueEncoder<T> create(Class<T> type) {
        return this;
    }

    public Object toActivationContext(T object) {
        return toClient(object);
    }

    public T toObject(EventContext context) {
        if (context.getCount() > 0) {
            return toValue(context.get(String.class, 0));
        }
        return null;
    }

    /**
     * Returns <code>object.toString()</code>. Override this method to use
     * another value for the object label.
     * 
     * @see br.com.arsmachina.tapestrycrud.encoder.LabelEncoder#toLabel(java.lang.Object)
     */
    public String toLabel(T object) {
        return String.valueOf(object);
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }
}
