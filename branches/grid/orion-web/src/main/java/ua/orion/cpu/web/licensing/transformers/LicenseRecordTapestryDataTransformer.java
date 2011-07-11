/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.web.licensing.transformers;

import java.util.ListIterator;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.TrainingDirectionOrSpeciality;
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
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model) {
        return transform(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model, Messages messages) {
        return transform(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model) {
        return transform(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model, Messages messages) {
        return transform(model);
    }

    private <T> BeanModel<T> transform(BeanModel<T> model) {
        model.exclude("KnowledgeAreaOrTrainingDirectionCode",
                "KnowledgeAreaOrTrainingDirectionName");
        return model;
    }

    @Override
    public <T> SelectModel transformSelectModel(SelectModel model, Class<T> entityClass, String property) {
        if ("TrainingDirectionOrSpeciality".equalsIgnoreCase(property)) {
            ListIterator<OptionModel> it = (ListIterator<OptionModel>) model.getOptions().listIterator();
            while(it.hasNext()){
                OptionModel om=it.next();
                final TrainingDirectionOrSpeciality tdos=(TrainingDirectionOrSpeciality) om.getValue();
                it.set(new AbstractOptionModel() {

                @Override
                public String getLabel() {
                    return es.getStringValue(tdos.getKnowledgeAreaOrTrainingDirection())+" - "+es.getStringValue(tdos);
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
}