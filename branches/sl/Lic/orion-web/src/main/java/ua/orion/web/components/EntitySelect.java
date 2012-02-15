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
package ua.orion.web.components;

import java.lang.annotation.Annotation;
import javax.inject.Inject;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.*;
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
 * Обертка для компонента {@link org.apache.tapestry5.corelib.components.Select}
 * для привязки его к конкретному свойству сущности
 */
@SupportsInformalParameters
public class EntitySelect {

    //---Parameters---
    @Parameter(required = true, allowNull = false)
    private Object entity;
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String property;
    //---Container Resources---
    @Parameter
    private SelectModel model;
//    @Parameter(defaultPrefix = BindingConstants.VALIDATE)
//    private FieldValidator<Object> validate;
//    @Parameter(defaultPrefix = BindingConstants.LITERAL)
//    private String label;
//    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
//    private String clientId;
//    @Parameter
//    private ValueEncoder encoder;
    @Component(parameters = {
        "value=prop:value",
        "label=prop:editContext.label",
        "model=inherit:model",
        "clientId=prop:editContext.propertyId",
        "encoder=prop:entityValueEncoder",
        "validate=prop:entitySelectValidator"
    }, publishParameters = "label,clientId,encoder,validate,model,blankOption,blankLabel,zone")
    Select select;
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
    //---Locals----
    private PropertyAdapter pa = propertyAccess.getAdapter(entity).getPropertyAdapter(property);

    String defaultLabel() {
        return labelSource.getPropertyLabel(entity.getClass(), property, resources.getMessages());
    }

    public Object getValue() {
        return pa.get(entity);
    }

    public void setValue(Object o) {
        pa.set(entity, o);
    }

    SelectModel defaultModel() {
        return dataSource.getSelectModel(entity, property);
    }

    String defaultClientId() {
        return property;
    }

    ValueEncoder defaultEncoder() {
        return valueEncoderSource.getValueEncoder(pa.getType());
    }

    FieldValidator defaultValidate() {
        return fieldValidatorDefaultSource.createDefaultValidator(select,
                property, resources.getMessages(), resources.getLocale(),
                pa.getType(), pa);
    }

    Select beginRender() {
        return select;
        /*
         * if (!resources.isBound("label")) { label =
         * labelSource.getPropertyLabel(entity.getClass(), property,
         * resources.getMessages()); } //????value = pa.get(entity); if
         * (!resources.isBound("model")) { model =
         * dataSource.getSelectModel(entity, property); } if
         * (!resources.isBound("clientId")) { clientId = property; } if
         * (!resources.isBound("encoder")) { encoder =
         * valueEncoderSource.getValueEncoder(pa.getType()); } if
         * (!resources.isBound("validate")) { validate =
         * fieldValidatorDefaultSource.createDefaultValidator(container,
         * property, resources.getMessages(), resources.getLocale(),
         * pa.getType(), pa); }
         */
    }
}
