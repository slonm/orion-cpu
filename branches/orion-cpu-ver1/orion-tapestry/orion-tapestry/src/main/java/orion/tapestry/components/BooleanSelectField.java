package orion.tapestry.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import orion.tapestry.utils.BooleanSelectModel;
import orion.tapestry.utils.BooleanValueEncoder;

/**
 * Select для булева типа. В списке выбора есть только две записи
 * (может присутствовать третья, пустая запись).
 * Значения подписей списка задается в параметрах trueLabel и falseLabel
 * @author sl
 */
@SupportsInformalParameters
@SuppressWarnings("unused")
public class BooleanSelectField implements ClientElement, Field {

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;
    /**
     * The value(boolean) to read or update.
     */
    @Property
    @Parameter(required = true, principal = true)
    private Boolean value;
    /**
     * true value Boolean should diplayed.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String trueLabel;
    /**
     * false value Boolean should diplayed.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String falseLabel;
    @Component(inheritInformalParameters = true, parameters = {"blankLabel=inherit:blankLabel",
        "blankOption=inherit:blankOption", "clientId=prop:clientId",
        "validate=inherit:validate", "value=value", "model=beanModel", "encoder=beanEncoder",
        "label=inherit:label"})
    private Select select;
    @Inject
    private Messages messages;
    @Persist
    private BooleanSelectModel model;
    @Persist
    private BooleanValueEncoder encoder;

    @Log
    void beginRender(MarkupWriter writer) {
        if (trueLabel == null) {
            trueLabel = messages.contains(Boolean.toString(true)) ? messages.get(Boolean.toString(true)) : Boolean.toString(true);
        }
        if (falseLabel == null) {
            falseLabel = messages.contains(Boolean.toString(false)) ? messages.get(Boolean.toString(false)) : Boolean.toString(false);
        }
        encoder = new BooleanValueEncoder();
        model = new BooleanSelectModel(trueLabel, falseLabel);
    }

    public BooleanSelectModel getBeanModel() {
        return model;
    }

    public BooleanValueEncoder getBeanEncoder() {
        return encoder;
    }

    @Override
    public String getClientId() {
        return select.getClientId();
    }

    @Override
    public String getControlName() {
        return select.getControlName();
    }

    @Override
    public String getLabel() {
        return select.getLabel();
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
