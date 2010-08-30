package orion.tapestry.services.impl;

import java.util.List;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.MethodAdvice;
import orion.tapestry.services.FieldLabelSource;

/**
 * Консультант {@link BeanModelSource} для поддержки сервиса
 * {@link FieldLabelSource}
 * @author sl
 */
public class BeanModelSourceMethodAdvice implements MethodAdvice {

    private final FieldLabelSource fieldLabelSource;

    public BeanModelSourceMethodAdvice(FieldLabelSource fieldLabelSource) {
        this.fieldLabelSource = fieldLabelSource;
    }

    private void setBeanModelPropertyLabels(BeanModel<?> model, Messages messages) {
        List<String> names = model.getPropertyNames();
        for (String name : names) {
            String label = fieldLabelSource.get(model.getBeanType(), name, messages);
            if (label != null) {
                //Если имя свойства где-то явно определено как <propName-label> то не заменяем его!
                String defaultLabel = TapestryInternalUtils.defaultLabel(model.get(name).getId(), messages, name);
                if (label.equals(defaultLabel)) {
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
