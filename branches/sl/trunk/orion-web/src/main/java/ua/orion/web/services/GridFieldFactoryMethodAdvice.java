/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.services;

import java.util.Iterator;
import java.util.List;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import ua.orion.core.services.ApplicationMessagesSource;
import ua.orion.core.services.ModelLabelSource;

/**
 *
 * @author slobodyanuk
 */
class GridFieldFactoryMethodAdvice implements MethodAdvice {

    private final ModelLabelSource mls;
    private final ApplicationMessagesSource ams;

    public GridFieldFactoryMethodAdvice(ModelLabelSource mls, ApplicationMessagesSource ams) {
        this.mls = mls;
        this.ams = ams;
    }

    @Override
    public void advise(Invocation invocation) {
        invocation.proceed();
        if ("getFields".equals(invocation.getMethodName())) {
            List<GridFieldAbstract> list = (List<GridFieldAbstract>) invocation.getResult();
            Class forClass = (Class) invocation.getParameter(0);
            Iterator<GridFieldAbstract> it = list.listIterator();
            while (it.hasNext()) {
                GridFieldAbstract field = it.next();
                field.setLabel(mls.getPropertyLabel(forClass, field.getAttributeName(), ams.getMessages()));
            }
        }
    }
}
