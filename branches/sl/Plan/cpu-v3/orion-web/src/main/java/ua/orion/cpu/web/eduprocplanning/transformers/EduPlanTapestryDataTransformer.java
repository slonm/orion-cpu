package ua.orion.cpu.web.eduprocplanning.transformers;

import java.util.*;
import java.util.Arrays;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
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
                "knowledgeAreaName", "trainingDirection", "speciality",
                "licenseRecordGroupName",
                "educationalQualificationLevel", "trainingTerm",
                "qualification", "introducingDate", "confirmationDate", "confirmationPerson",
                "eduPlanState");
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

    private <T> BeanModel<T> transformEdit(BeanModel<T> model) {
        Set<String> existProps = new HashSet(model.getPropertyNames());
        List<String> requiredProps = Arrays.asList(
                "licenseRecord", "trainingTerm",
                "qualification", "introducingDate");
        existProps.removeAll(requiredProps);
        model.exclude(existProps.toArray(new String[]{}));
        model.reorder(requiredProps.toArray(new String[]{}));
        return model;
    }

    @Override
    public <T> SelectModel transformSelectModel(SelectModel model, Class<T> entityClass, String property) {
        if ("LicenseRecord".equalsIgnoreCase(property)) {
            ListIterator<OptionModel> it = (ListIterator<OptionModel>) model.getOptions().listIterator();
            while (it.hasNext()) {
                OptionModel om = it.next();
                final LicenseRecord licenseRecord = (LicenseRecord) om.getValue();
                it.set(new AbstractOptionModel() {

                    @Override
                    public String getLabel() {
                        return es.getStringValue(licenseRecord.getLicense()) + " - " + es.getStringValue(licenseRecord);
                    }

                    @Override
                    public Object getValue() {
                        return licenseRecord;
                    }
                });
            }
        }
        return model;
    }

    @Override
    public String transformCrudPage(String page, Class<?> entityClass) {
        return "EduProcPlanning/EduPlans";
    }
}
