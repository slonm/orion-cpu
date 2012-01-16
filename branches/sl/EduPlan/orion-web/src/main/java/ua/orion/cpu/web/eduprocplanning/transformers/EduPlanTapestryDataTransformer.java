package ua.orion.cpu.web.eduprocplanning.transformers;

import java.util.*;
import java.util.Arrays;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import ua.orion.core.services.EntityService;
import ua.orion.web.AbstractTapestryDataTransformer;

/**
 *
 * @author sl
 */
public class EduPlanTapestryDataTransformer extends AbstractTapestryDataTransformer {

    private final EntityService es;

    public EduPlanTapestryDataTransformer(EntityService es) {
        this.es = es;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForList(BeanModel<T> model) {
        Set<String> existProps = new HashSet(model.getPropertyNames());
        List<String> requiredProps = Arrays.asList("code",
                "knowledgeArea", "trainingDirection", "speciality",
                "educationalQualificationLevel", "trainingTerm",
                "qualification", "introducingDate","confirmationDate", "confirmationPerson");
        existProps.removeAll(requiredProps);
        model.exclude(existProps.toArray(new String[]{}));
        model.reorder(requiredProps.toArray(new String[]{}));
        return model;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForList(BeanModel<T> model, Messages messages) {
        return transformBeanModelForList(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model) {
        return transformEdit(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model, Messages messages) {
        return transformEdit(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model) {
        return transformEdit(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model, Messages messages) {
        return transformEdit(model);
    }

    private <T> BeanModel<T> transformView(BeanModel<T> model) {
        return model;
    }

    private <T> BeanModel<T> transformEdit(BeanModel<T> model) {
        return model;
    }

/*    @Override
    public <T> SelectModel transformSelectModel(SelectModel model, Class<T> entityClass, String property) {
        if ("TrainingDirectionOrSpeciality".equalsIgnoreCase(property)) {
            ListIterator<OptionModel> it = (ListIterator<OptionModel>) model.getOptions().listIterator();
            while (it.hasNext()) {
                OptionModel om = it.next();
                final TrainingDirectionOrSpeciality tdos = (TrainingDirectionOrSpeciality) om.getValue();
                it.set(new AbstractOptionModel() {

                    @Override
                    public String getLabel() {
                        return es.getStringValue(tdos.getKnowledgeAreaOrTrainingDirection()) + " - " + es.getStringValue(tdos);
                    }

                    @Override
                    public Object getValue() {
                        return tdos;
                    }
                });
            }
        }
        return model;
    }
*/
    @Override
    public String transformCrudPage(String page, Class<?> entityClass) {
        return "EduProcPlanning/EduPlans";
    }
}
