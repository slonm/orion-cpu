package orion.cpu.views.tapestry.pages;

import javax.naming.event.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.PropertyOutputContext;
import orion.tapestry.components.BooleanSelectField;

/**
 * Блоки для beaneditor
 * @author sl
 */
@SuppressWarnings("unused")
public class PropertyBlocks {

    @Environmental
    @Property(write = false)
    private PropertyOutputContext outputContext;
    @Environmental
    @Property(write = false)
    private PropertyEditContext editContext;
    @Component(parameters = {"value=editContext.propertyValue",
        "label=prop:editContext.label",
        "trueLabel=message:TrainingDirectionOrSpeciality-true",
        "falseLabel=message:TrainingDirectionOrSpeciality-false",
        "validate=prop:trainingDirectionOrSpecialityValidator",
        "clientId=prop:editContext.propertyId"})
    private BooleanSelectField trainingDirectionOrSpeciality;
    @Component(parameters = {"value=editContext.propertyValue",
        "label=prop:editContext.label",
        "trueLabel=message:knowledgeAreaOrTrainingDirection-true",
        "falseLabel=message:knowledgeAreaOrTrainingDirection-false",
        "validate=prop:knowledgeAreaOrTrainingDirectionValidator",
        "clientId=prop:editContext.propertyId"})
    private BooleanSelectField knowledgeAreaOrTrainingDirection;

    public FieldValidator<?> getKnowledgeAreaOrTrainingDirectionValidator() {
        return editContext.getValidator(knowledgeAreaOrTrainingDirection);
    }

    public FieldValidator<?> getTrainingDirectionOrSpecialityValidator() {
        return editContext.getValidator(trainingDirectionOrSpeciality);
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
