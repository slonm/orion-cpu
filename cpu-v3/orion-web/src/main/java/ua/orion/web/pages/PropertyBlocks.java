package ua.orion.web.pages;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.BeanEditor;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.*;
import ua.orion.core.services.EntityService;
import ua.orion.web.services.TapestryDataSource;

/**
 * <p>
 * A page that holds the editing and viewing blocks provided by Orion Tapestry for
 * {@link BeanEditor}, {@link BeanEditForm}, and {@link Grid}.
 * </p>
 * @author sl
 */
@SuppressWarnings("unused")
public class PropertyBlocks {

    @Inject
    private Messages messages;
    @Inject
    private EntityService es;
    @Inject
    private ValueEncoderSource valueEncoderSource;
    @Inject
    private TapestryDataSource tapestryComponentDataSource;
    @Environmental
    @Property(write = false)
    private PropertyEditContext editContext;
    @Environmental
    @Property(write = false)
    private PropertyOutputContext outputContext;
    @Environmental
    @Property(write = false)
    private BeanEditContext beanEditContext;
    @Component(parameters = {
        "value=editContext.propertyValue",
        "label=prop:editContext.label",
//        "model={true:${message:true}, false:${message:false}}",
        "model={'true':trueLabel, 'false':falseLabel}",
        "clientId=prop:editContext.propertyId",
        "validate=prop:booleanSelectValidator"
    })
    private Select booleanSelect;
    @Component(parameters = {
        "value=editContext.propertyValue",
        "label=prop:editContext.label",
        "model=prop:entitySelectModel",
        "clientId=prop:editContext.propertyId",
        "encoder=prop:entityValueEncoder",
        "validate=prop:entitySelectValidator"
    })
    private Select entitySelect;

    public FieldValidator<Boolean> getBooleanSelectValidator() {
        return editContext.getValidator(booleanSelect);
    }

    /**
     * Запрещает явное открытие этой псевдо-страницы.
     * @param ec Page activation context
     * @return the empty string
     * @author sl
     */
    public String onActivate(EventContext ec) {
        return "";
    }

    /**
     * Returns the entity converted in a user-presentable string.
     *
     * @return a {@link String}.
     */
    public String getEntity() {
        Object propertyValue = outputContext.getPropertyValue();
        if (propertyValue == null) {
            return "";
        }
        return es.getStringValue(propertyValue);
    }

    public SelectModel getEntitySelectModel() {
        return tapestryComponentDataSource.getSelectModel(beanEditContext.getBeanClass(), editContext.getPropertyId());
    }

    public <T> ValueEncoder<T> getEntityValueEncoder() {
        return valueEncoderSource.getValueEncoder(editContext.getPropertyType());
    }

    public FieldValidator<?> getEntitySelectValidator() {
        return editContext.getValidator(entitySelect);
    }

    public String getTrueLabel(){
        return messages.get("true");
    }

    public String getFalseLabel(){
        return messages.get("false");
    }
}