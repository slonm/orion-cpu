// Copyright 2008, 2010 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package ua.orion.web.mixins;

import java.lang.annotation.Annotation;
import javax.inject.Inject;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.BindParameter;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.plastic.FieldConduit;
import org.apache.tapestry5.plastic.InstanceContext;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.ValueEncoderSource;
import ua.orion.core.services.ModelLabelSource;
import ua.orion.web.services.TapestryDataSource;

/**
 * Подмешиватель применяется к компоненту {@link org.apache.tapestry5.corelib.components.Select}
 * для привязки его к конкретному свойству сущности
 */
public class EntitySelect {
    //---Parameters---

    @Parameter(required = true, allowNull = false)
    private Object entity;
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String property;
    //---Container Resources---
    @BindParameter
    private SelectModel model;
    @BindParameter
    private FieldValidator<Object> validate;
    @BindParameter
    private Object value;
    @BindParameter
    private String label;
    @BindParameter
    private String clientId;
    @BindParameter
    private ValueEncoder encoder;
    @InjectContainer
    private Select container;
    //---Services---
    @Inject
    private ComponentResources resources;
    @Inject
    private TapestryDataSource dataSource;
    @Inject
    private ValueEncoderSource valueEncoderSource;
    @Inject
    private ModelLabelSource labelSource;
    @Inject
    private PropertyAccess propertyAccess;
    @Inject
    private FieldValidatorDefaultSource fieldValidatorDefaultSource;

    void beginRender() {
        ComponentResources cntRes = resources.getContainerResources();
        if (!cntRes.isBound("label")) {
            label = labelSource.getPropertyLabel(entity.getClass(), property, cntRes.getMessages());
        }
        ClassPropertyAdapter cpa = propertyAccess.getAdapter(entity);
        final PropertyAdapter pa = cpa.getPropertyAdapter(property);
        //????value = pa.get(entity);
        if (!cntRes.isBound("model")) {
            model = dataSource.getSelectModel(entity, property);
        }
        if (!cntRes.isBound("clientId")) {
            clientId = property;
        }
        if (!cntRes.isBound("encoder")) {
            encoder = valueEncoderSource.getValueEncoder(pa.getType());
        }
        if (!cntRes.isBound("validate")) {
            validate = fieldValidatorDefaultSource.createDefaultValidator(container,
                    property, cntRes.getMessages(), cntRes.getLocale(),
                    pa.getType(), pa);
        }
    }
}
