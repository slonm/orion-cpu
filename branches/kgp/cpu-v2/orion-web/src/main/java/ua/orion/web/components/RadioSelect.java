package ua.orion.web.components;

import ua.orion.web.data.FieldSetMode;
import java.util.Map;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.data.BlankOption;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;

/**
 * Select an item from a list of values, using an [X]HTML  radio typed &lt;input&gt; elements wrapped in &lt;fieldset&gt; on the client side. An validation
 * decorations will go around the entire &lt;select&gt; element.
 * <p/>
 * Component it is fork of Select component. It use same SelectModel but ignore render OprionGroup entrys (only it's options)
 * <p/>
 * A core part of this component is the {@link ValueEncoder} (the encoder parameter) that is used to convert between
 * server-side values and client-side strings. In many cases, a {@link ValueEncoder} can be generated automatically from
 * the type of the value parameter. The {@link ValueEncoderSource} service provides an encoder in these situations; it
 * can be overriden by binding the encoder parameter, or extended by contributing a {@link ValueEncoderFactory} into the
 * service's configuration.
 */
@Events({EventConstants.VALIDATE, EventConstants.VALUE_CHANGED + " when 'zone' parameter is bound"})
@Import(library = "RadioSelect.js")
public class RadioSelect extends AbstractField {

    public static final String CHANGE_EVENT = "change";

    private class Renderer implements SelectModelVisitor {

        private final MarkupWriter writer;

        public Renderer(final MarkupWriter writer) {
            this.writer = writer;
        }

        public void beginOptionGroup(OptionGroupModel groupModel) {
//            writer.element("optgroup", "label", groupModel.getLabel());
//
//            writeDisabled(groupModel.isDisabled());
//            writeAttributes(groupModel.getAttributes());
        }

        public void endOptionGroup(OptionGroupModel groupModel) {
//            writer.end(); // select
        }

        @SuppressWarnings("unchecked")
        public void option(OptionModel optionModel) {
            Object optionValue = optionModel.getValue();

            option(getControlName(), javascriptSupport.allocateClientId(getClientId()),
                    encoder.toClient(optionValue), optionModel.getLabel());

            writeDisabled(optionModel.isDisabled());
            writeAttributes(optionModel.getAttributes());

            writer.end();//input
        }

        private void option(String name, String id, String value, String label) {
            writer.element("input", "type", "radio", "name", name, "value", value, "id", id);

            if (isSelected(value)) {
                writer.attributes("checked", "checked");
            }

            writer.write(label);
            if (vertical) {
                writer.element("br");
            }
            writer.end();//br

            if (zone != null) {
                Link link = resources.createEventLink(CHANGE_EVENT);

                JSONObject spec = new JSONObject("selectId", id, "zoneId", zone, "url", link.toURI());

                javascriptSupport.addInitializerCall("linkRadioSelectToZone", spec);
            }
        }

        private void writeDisabled(boolean disabled) {
            if (isDisabled() || disabled) {
                writer.attributes("disabled", "disabled");
            }
        }

        private void writeAttributes(Map<String, String> attributes) {
            if (attributes == null) {
                return;
            }

            for (Map.Entry<String, String> e : attributes.entrySet()) {
                writer.attributes(e.getKey(), e.getValue());
            }
        }
    }
    /**
     * Allows a specific implementation of {@link ValueEncoder} to be supplied. This is used to create client-side
     * string values for the different options.
     * 
     * @see ValueEncoderSource
     */
    @Parameter
    private ValueEncoder encoder;
    /**
     * Vertical block of items (add br tag at the and of each item)
     */
    @Parameter(allowNull = false, value = "true", defaultPrefix = BindingConstants.LITERAL)
    private boolean vertical;
    @Inject
    private ComponentDefaultProvider defaultProvider;
    // Maybe this should default to property "<componentId>Model"?
    /**
     * The model used to identify the option groups and options to be presented to the user. This can be generated
     * automatically for Enum types.
     */
    @Parameter(required = true, allowNull = false)
    private SelectModel model;
    /**
     * Controls whether an additional blank option is provided. The blank option precedes all other options and is never
     * selected. The value for the blank option is always the empty string, the label may be the blank string; the
     * label is from the blankLabel parameter (and is often also the empty string).
     */
    @Parameter(value = "auto", defaultPrefix = BindingConstants.LITERAL)
    private BlankOption blankOption;
    /**
     * Field Set mode (NONE, RECT, RECT_LABEL)
     */
    @Parameter(value = "rect", defaultPrefix = BindingConstants.LITERAL)
    private FieldSetMode fieldSet;
    /**
     * The label to use for the blank option, if rendered. If not specified, the container's message catalog is
     * searched for a key, <code><em>id</em>-blanklabel</code>.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String blankLabel;
    @Inject
    private Request request;
    @Inject
    private ComponentResources resources;
    @Environmental
    private ValidationTracker tracker;
    /**
     * Performs input validation on the value supplied by the user in the form submission.
     */
    @Parameter(defaultPrefix = BindingConstants.VALIDATE)
    private FieldValidator<Object> validate;
    /**
     * The value to read or update.
     */
    @Parameter(required = true, principal = true, autoconnect = true)
    private Object value;
    /**
     * Binding the zone parameter will cause any change of Select's value to be handled as an Ajax request that updates
     * the
     * indicated zone. The component will trigger the event {@link EventConstants#VALUE_CHANGED} to inform its
     * container that Select's value has changed.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String zone;
    @Inject
    private FieldValidationSupport fieldValidationSupport;
    @Environmental
    private FormSupport formSupport;
    @Inject
    private JavaScriptSupport javascriptSupport;
    private String selectedClientValue;

    private boolean isSelected(String clientValue) {
        return clientValue == null || clientValue.isEmpty() ? selectedClientValue == null
                : clientValue.equals(selectedClientValue);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected void processSubmission(String elementName) {
        String submittedValue = request.getParameter(elementName);

        tracker.recordInput(this, submittedValue);

        Object selectedValue = toValue(submittedValue);

        putPropertyNameIntoBeanValidationContext("value");

        try {
            fieldValidationSupport.validate(selectedValue, resources, validate);

            value = selectedValue;
        } catch (ValidationException ex) {
            tracker.recordError(this, ex.getMessage());
        }

        removePropertyNameFromBeanValidationContext();
    }

    void afterRender(MarkupWriter writer) {
        writer.end();
    }

    void beginRender(MarkupWriter writer) {
        writer.element("fieldset");
        if (FieldSetMode.NONE == fieldSet) {
            writer.attributes("style", "display: inline; border: 0px;");
        } else {
            writer.attributes("style", "display: inline;");
            if (FieldSetMode.RECT_LABEL == fieldSet) {
                writer.element("legend");
                writer.write(getLabel());
                writer.end();
            }
        }
    }

    Object onChange(@RequestParameter(value = "t:radioselectvalue", allowBlank = true)
            final String selectValue) {
        final Object newValue = toValue(selectValue);

        final Object[] res = new Object[1];
        ComponentEventCallback<Object> callback = new ComponentEventCallback<Object>() {

            public boolean handleResult(Object result) {
                res[0] = result;
                return true;
            }
        };

        this.resources.triggerEvent(EventConstants.VALUE_CHANGED, new Object[]{newValue}, callback);

        this.value = newValue;

        return res[0];
    }

    protected Object toValue(String submittedValue) {
        return (submittedValue == null || "".equals(submittedValue)) ? null : this.encoder.toValue(submittedValue);
    }

    @SuppressWarnings("unchecked")
    ValueEncoder defaultEncoder() {
        return defaultProvider.defaultValueEncoder("value", resources);
    }

    @SuppressWarnings("unchecked")
    SelectModel defaultModel() {
        Class valueType = resources.getBoundType("value");

        if (valueType == null) {
            return null;
        }

        if (Enum.class.isAssignableFrom(valueType)) {
            return new EnumSelectModel(valueType, resources.getContainerMessages());
        }

        return null;
    }

    /**
     * Computes a default value for the "validate" parameter using {@link FieldValidatorDefaultSource}.
     */
    Binding defaultValidate() {
        return defaultProvider.defaultValidatorBinding("value", resources);
    }

    Object defaultBlankLabel() {
        Messages containerMessages = resources.getContainerMessages();

        String key = resources.getId() + "-blanklabel";

        if (containerMessages.contains(key)) {
            return containerMessages.get(key);
        }

        return null;
    }

    /**
     * Renders the options, including the blank option.
     */
    @BeforeRenderTemplate
    void options(MarkupWriter writer) {
        selectedClientValue = tracker.getInput(this);

        // Use the value passed up in the form submission, if available.
        // Failing that, see if there is a current value (via the value parameter), and
        // convert that to a client value for later comparison.

        if (selectedClientValue == null) {
            selectedClientValue = value == null ? null : encoder.toClient(value);
        }

        Renderer renderer = new Renderer(writer);

        if (getShowBlankOption()) {
            renderer.option(getControlName(), getClientId(), "", blankLabel);
            writer.end();//input
        }

        model.visit(renderer);
    }

    @Override
    public boolean isRequired() {
        return validate.isRequired();
    }

    public boolean getShowBlankOption() {
        switch (blankOption) {
            case ALWAYS:
                return true;

            case NEVER:
                return false;

            default:
                return !isRequired();
        }
    }

    // For testing.
    void setModel(SelectModel model) {
        this.model = model;
        blankOption = BlankOption.NEVER;
    }

    void setValue(Object value) {
        this.value = value;
    }

    void setValueEncoder(ValueEncoder encoder) {
        this.encoder = encoder;
    }

    void setValidationTracker(ValidationTracker tracker) {
        this.tracker = tracker;
    }

    void setBlankOption(BlankOption option, String label) {
        blankOption = option;
        blankLabel = label;
    }
}
