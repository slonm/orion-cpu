package ua.orion.cpu.web.licensing.transformers;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import ua.orion.web.AbstractTapestryDataTransformer;

/**
 *
 * @author sl
 */
public class LicenseTapestryDataTransformer extends AbstractTapestryDataTransformer {

    @Override
    public <T> BeanModel<T> transformBeanModelForView(BeanModel<T> model, Messages messages) {
        model.exclude("licenseState");
        return model;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model, Messages messages) {
        model.exclude("licenseState");
        return model;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model, Messages messages) {
        model.exclude("licenseState");
        return model;
    }

    @Override
    public String transformCrudPage(String page, Class<?> entityClass) {
        return "licensing/licenses";
    }

}
