package orion.tapestry.pages;

import javax.naming.event.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.services.*;
import orion.tapestry.components.BooleanSelectField;

/**
 * <p>
 * A page that holds the editing and viewing blocks provided by Orion Tapestry for
 * {@link BeanEditor}, {@link BeanEditForm}, and {@link Grid}.
 * </p>
 * @author sl
 */
@SuppressWarnings("unused")
public class PropertyBlocks {

    @Environmental
    @Property(write = false)
    private PropertyEditContext editContext;
    @Environmental
    @Property(write = false)
    private PropertyOutputContext outputContext;
    @Component(parameters = {"value=editContext.propertyValue",
        "trueLabel=message:true",
        "falseLabel=message:false",
        "validate=prop:booleanSelectFieldValidator",
        "label=prop:editContext.label",
        "clientId=prop:editContext.propertyId"})
    private BooleanSelectField booleanSelectField;

    public FieldValidator<?> getBooleanSelectFieldValidator() {
        return editContext.getValidator(booleanSelectField);
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
}
