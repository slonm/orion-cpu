package ua.orion.web.components;

import javax.inject.Inject;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.ValueEncoderSource;
import ua.orion.core.services.ModelLabelSource;
import ua.orion.web.services.TapestryDataSource;

/**
 * Обертка для компонента {@link org.apache.tapestry5.corelib.components.Select}
 * для привязки его к конкретному свойству сущности
 */
@SupportsInformalParameters
public class EntitySelect implements Field {

    //---Parameters---
    @Parameter(required = true, principal = true)
    private Object entity;
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false, principal = true)
    private String property;
    @Parameter
    private SelectModel model;
    @Component(parameters = {
        "value=prop:value",
        "label=prop:label",
        "model=prop:model",
        "clientId=prop:clientId",
        "encoder=prop:encoder",
        "validate=prop:validate"
    }, publishParameters = "blankOption,blankLabel,zone")
    private Select select;
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

    private PropertyAdapter getPropertyAdapter() {
        return propertyAccess.getAdapter(entity).getPropertyAdapter(property);
    }

    public Object getValue() {
        return getPropertyAdapter().get(entity);
    }

    public void setValue(Object o) {
        getPropertyAdapter().set(entity, o);
    }

    @Override
    public String getLabel() {
        return labelSource.getPropertyLabel(entity.getClass(), property, resources.getMessages());
    }

    public Object getModel() {
        return resources.isBound("model") ? model : dataSource.getSelectModel(entity, property);
    }

    @Override
    public String getClientId() {
        return property;
    }

    public ValueEncoder getEncoder() {
        return valueEncoderSource.getValueEncoder(getPropertyAdapter().getType());
    }

    public FieldValidator getValidate() {
        return fieldValidatorDefaultSource.createDefaultValidator(select,
                property, resources.getMessages(), resources.getLocale(),
                getPropertyAdapter().getType(), getPropertyAdapter());
    }

    boolean setupRender() {
        return entity != null;
    }

    @Override
    public String getControlName() {
        return select.getControlName();
    }

    @Override
    public boolean isDisabled() {
        return select.isDisabled();
    }

    @Override
    public boolean isRequired() {
        return select.isRequired();
    }
}
