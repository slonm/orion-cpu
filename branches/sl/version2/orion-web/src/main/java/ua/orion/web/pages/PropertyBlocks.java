package ua.orion.web.pages;

import java.util.*;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.util.AbstractSelectModel;
import ua.orion.core.services.EntityService;
import ua.orion.web.services.TapestryComponentDataSource;

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
    private TapestryComponentDataSource tapestryComponentDataSource;
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
        "model=prop:booleanSelectModel",
        "clientId=prop:editContext.propertyId",
        "encoder=prop:booleanValueEncoder",
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
        return es.getUserPresentable(propertyValue);
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

    public SelectModel getBooleanSelectModel() {
        return new AbstractSelectModel() {

            @Override
            public List<OptionGroupModel> getOptionGroups() {
                return null;
            }

            @Override
            public List<OptionModel> getOptions() {
                List<OptionModel> optionModelList = new ArrayList<OptionModel>();
                optionModelList.add(new AbstractOptionModel() {

                    @Override
                    public String getLabel() {
                        return messages.get("true");
                    }

                    @Override
                    public Object getValue() {
                        return true;
                    }
                });
                optionModelList.add(new AbstractOptionModel() {

                    @Override
                    public String getLabel() {
                        return messages.get("false");
                    }

                    @Override
                    public Object getValue() {
                        return false;
                    }
                });
                return optionModelList;
            }
        };
    }

    public ValueEncoder<Boolean> getBooleanValueEncoder() {
        return new ValueEncoder<Boolean>() {

            @Override
            public String toClient(Boolean serverValue) {
                return String.valueOf(serverValue);
            }

            @Override
            public Boolean toValue(String clientValue) {
                return Boolean.valueOf(clientValue);
            }
        };
    }
}