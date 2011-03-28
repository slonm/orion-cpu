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

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.service.ControllerSource;
import br.com.arsmachina.module.service.EntitySource;
import br.com.arsmachina.module.service.PrimaryKeyTypeService;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.services.EncoderFactory;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.TypeCoercer;

/**
 * {@link PrimaryKeyEncoderFactory} implementation using Hibernate.
 *
 * @see HibernateValueEncoder
 * @author sl
 */
@SuppressWarnings("deprecation")
public class DefaultEncoderFactory<T> implements EncoderFactory<T> {

    final private EntitySource entitySource;
    final private ControllerSource controllerSource;
    final PrimaryKeyTypeService primaryKeyTypeService;
    private final TypeCoercer typeCoercer;
    private final PropertyAccess propertyAccess;

    /**
     * Single constructor of this class.
     *
     * @param entitySource a {@link EntitySource}. It cannot be null.
     * @param controllerSource a {@link ControllerSource}. It cannot be null.
     * @param primaryKeyTypeService a {@link PrimaryKeyTypeService}. It cannot
     *            be null.
     */
    public DefaultEncoderFactory(EntitySource entitySource,
            ControllerSource controllerSource,
            PrimaryKeyTypeService primaryKeyTypeService,
            PropertyAccess propertyAccess, TypeCoercer typeCoercer) {

        if (entitySource == null) {
            throw new IllegalArgumentException(
                    "Parameter entitySource cannot be null");
        }

        if (controllerSource == null) {
            throw new IllegalArgumentException(
                    "Parameter controllerSource cannot be null");
        }

        if (primaryKeyTypeService == null) {
            throw new IllegalArgumentException(
                    "Parameter primaryKeyTypeService cannot be null");
        }

        if (typeCoercer == null) {
            throw new IllegalArgumentException("Parameter typeCoercer cannot be null");
        }

        if (propertyAccess == null) {
            throw new IllegalArgumentException("Parameter propertyAccess cannot be null");
        }

        this.entitySource = entitySource;
        this.controllerSource = controllerSource;
        this.primaryKeyTypeService = primaryKeyTypeService;
        this.propertyAccess = propertyAccess;
        this.typeCoercer = typeCoercer;
    }

    public Encoder<T> create(Class<T> entityClass) {
        Encoder<T> encoder = null;
        if (entitySource.getEntityClasses().contains(entityClass)) {

            Controller<T, ?> controller = controllerSource.get(entityClass);

            if (controller != null) {
                encoder = new DefaultEncoder(entityClass, controller,
                        primaryKeyTypeService, propertyAccess, typeCoercer);
            }

        }
        return encoder;
    }
}
