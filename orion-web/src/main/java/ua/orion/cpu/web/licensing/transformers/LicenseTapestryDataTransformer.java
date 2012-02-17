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
    public <T> BeanModel<T> transformBeanModelForList(BeanModel<T> model, Messages messages) {
        model.exclude("body");
        return model;
    }

    @Override
    public String transformCrudPage(String page, Class<?> entityClass) {
        return "licensing/licenses";
    }

}
