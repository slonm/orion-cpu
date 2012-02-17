package ua.orion.cpu.web.students.transformers;

import ua.orion.cpu.web.eduprocplanning.transformers.*;
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
public class StudentTapestryDataTransformer extends AbstractTapestryDataTransformer {

    private final EntityService es;

    public StudentTapestryDataTransformer(EntityService es) {
        this.es = es;
    }

//    @Override
//    public <T> BeanModel<T> transformBeanModelForList(BeanModel<T> model) {
//        Set<String> existProps = new HashSet(model.getPropertyNames());
//        List<String> requiredProps = Arrays.asList("person");
//        existProps.removeAll(requiredProps);
//        model.exclude(existProps.toArray(new String[]{}));
//        model.reorder(requiredProps.toArray(new String[]{}));
//        return model;
//    }
    @Override
    public <T> BeanModel<T> transformBeanModelForList(BeanModel<T> model, Messages messages) {
        return transformView(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model, Messages messages) {
        return transformEdit(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model, Messages messages) {
        return transformEdit(model);
    }

    private <T> BeanModel<T> transformView(BeanModel<T> model) {
        model.exclude("Name", "FullNameRu", "InitialsNameRu", "FullNameEn", "SciDegreeSciAreaFull");
        return model;
    }

    private <T> BeanModel<T> transformEdit(BeanModel<T> model) {
        model.exclude("Name");
        return model;
    }
}
