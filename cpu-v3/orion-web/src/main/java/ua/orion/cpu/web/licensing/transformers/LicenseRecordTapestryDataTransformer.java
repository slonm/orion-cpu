package ua.orion.cpu.web.licensing.transformers;

import java.util.*;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.TrainingDirection;
import ua.orion.web.AbstractTapestryDataTransformer;

/**
 *
 * @author sl
 */
public class LicenseRecordTapestryDataTransformer extends AbstractTapestryDataTransformer {

    private final EntityService es;

    public LicenseRecordTapestryDataTransformer(EntityService es) {
        this.es = es;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForList(BeanModel<T> model, Messages messages) {
        Set<String> existProps = new HashSet(model.getPropertyNames());
        List<String> requiredProps = Arrays.asList("code",
                "knowledgeAreaName", "trainingDirection", "speciality",
                "licenseRecordGroup", "licenseQuantityByEducationForm",
                "termination", "orgUnit");
        existProps.removeAll(requiredProps);
        model.exclude(existProps.toArray(new String[]{}));
        model.reorder(requiredProps.toArray(new String[]{}));
        return model;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForView(BeanModel<T> model, Messages messages) {
        Set<String> existProps = new HashSet(model.getPropertyNames());
        List<String> requiredProps = Arrays.asList("license", "code", "educationalQualificationLevel",
                "knowledgeAreaName", "trainingDirection", "speciality",
                "licenseRecordGroup", "licenseQuantityByEducationForm",
                "termination", "orgUnit");
        existProps.removeAll(requiredProps);
        model.exclude(existProps.toArray(new String[]{}));
        model.reorder(requiredProps.toArray(new String[]{}));
        return model;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model, Messages messages) {
        return transformEdit(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model, Messages messages) {
        return transformEdit(model);
    }

    private <T> BeanModel<T> transformEdit(BeanModel<T> model) {
        model.exclude("KnowledgeAreaCode",
                "KnowledgeAreaName",
                "code");
        return model;
    }
//    @Override
//    public <T> SelectModel transformSelectModel(SelectModel model, Class<T> entityClass, String property) {
//        if ("TrainingDirection".equalsIgnoreCase(property)) {
//            ListIterator<OptionModel> it = (ListIterator<OptionModel>) model.getOptions().listIterator();
//            while(it.hasNext()){
//                OptionModel om=it.next();
//                final TrainingDirection tdos=(TrainingDirection) om.getValue();
//                it.set(new AbstractOptionModel() {
//
//                @Override
//                public String getLabel() {
//                    return es.getStringValue(tdos.getKnowledgeArea())+" - "+es.getStringValue(tdos);
//                }
//
//                @Override
//                public Object getValue() {
//                    return tdos;
//                }
//            });
//            }
//        }
//        return model;
//    }
}
