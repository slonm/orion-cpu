package ua.orion.cpu.web.licensing.pages;

import javax.naming.event.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.PropertyOutputContext;
import ua.orion.web.BooleanSelectModel;

/**
 * Блоки для beaneditor
 * @author sl
 */
@SuppressWarnings("unused")
public class PropertyBlocks {

    @Inject
    private Messages messages;
    @Environmental
    @Property(write = false)
    private PropertyOutputContext outputContext;
    @Environmental
    @Property(write = false)
    private PropertyEditContext editContext;
    @Component(parameters = {
        "value=editContext.propertyValue",
        "label=prop:editContext.label",
        "model=prop:TrainingDirectionOrSpecialitySelectModel",
        "clientId=prop:editContext.propertyId",
        "validate=prop:TrainingDirectionOrSpecialityValidator"
    })
    private Select trainingDirectionOrSpeciality;
    @Component(parameters = {
        "value=editContext.propertyValue",
        "label=prop:editContext.label",
        "model=prop:KnowledgeAreaOrTrainingDirectionSelectModel",
        "clientId=prop:editContext.propertyId",
        "validate=prop:KnowledgeAreaOrTrainingDirectionValidator"
    })
    private Select knowledgeAreaOrTrainingDirection;

    public FieldValidator<?> getKnowledgeAreaOrTrainingDirectionValidator() {
        return editContext.getValidator(knowledgeAreaOrTrainingDirection);
    }

    public FieldValidator<?> getTrainingDirectionOrSpecialityValidator() {
        return editContext.getValidator(trainingDirectionOrSpeciality);
    }

    public SelectModel getKnowledgeAreaOrTrainingDirectionSelectModel() {
        return new BooleanSelectModel(messages.get("KnowledgeAreaOrTrainingDirection-true"), messages.get("KnowledgeAreaOrTrainingDirection-false"));
    }

    public SelectModel getTrainingDirectionOrSpecialitySelectModel() {
        return new BooleanSelectModel(messages.get("TrainingDirectionOrSpeciality-true"), messages.get("TrainingDirectionOrSpeciality-false"));
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
