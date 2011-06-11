package orion.tapestry.services.impl;

import java.util.List;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.services.BeanModelSource;
import orion.tapestry.services.FieldLabelSource;
import orion.tapestry.services.TapestryInternalUtilsWrapper;

/**
 * Консультант {@link BeanModelSource} для поддержки сервиса
 * {@link FieldLabelSource}
 * @author sl
 */
public class BeanModelSourceMethodAdvice implements MethodAdvice {

    private final FieldLabelSource fieldLabelSource;
    private final TapestryInternalUtilsWrapper tapestryInternalUtilsWrapper;

    public BeanModelSourceMethodAdvice(FieldLabelSource fieldLabelSource,
            TapestryInternalUtilsWrapper tapestryInternalUtilsWrapper) {
        this.fieldLabelSource = fieldLabelSource;
        this.tapestryInternalUtilsWrapper = tapestryInternalUtilsWrapper;
    }

    private void setBeanModelPropertyLabels(BeanModel<?> model, Messages messages) {
        List<String> names = model.getPropertyNames();
        for (String name : names) {
            String label = fieldLabelSource.get(model.getBeanType(), name, messages);
            if (label != null) {
                //Если имя свойства где-то явно определено как <propName-label> то не заменяем его!
                String defaultLabel = tapestryInternalUtilsWrapper.defaultLabel(model.get(name).getId(), name);
                if (model.get(name).getLabel().equals(defaultLabel)) {
                    model.get(name).label(label);
                }
            }
        }
    }


    @Override
    public void advise(Invocation invocation) {
        invocation.proceed();
        if (invocation.getMethodName().equals("createEditModel")
                || invocation.getMethodName().equals("createDisplayModel")) {
            setBeanModelPropertyLabels((BeanModel<?>) invocation.getResult(), (Messages) invocation.getParameter(1));
        }
        if (invocation.getMethodName().equals("create")) {
            setBeanModelPropertyLabels((BeanModel<?>) invocation.getResult(), (Messages) invocation.getParameter(2));
        }
    }
}
