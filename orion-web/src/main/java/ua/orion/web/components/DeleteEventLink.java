package ua.orion.web.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.ioc.annotations.Inject;
import ua.orion.web.CurrentBeanContext;
import ua.orion.web.base.AbstractObjectLink;

/**
 *
 * @author slobodyanuk
 */
public class DeleteEventLink extends AbstractObjectLink {

    @Environmental
    private CurrentBeanContext currentBeanContext;
    @Inject
    private ComponentResources componentResources;

    @Override
    protected Link createLink() {
        ComponentResources containerResources = componentResources.getContainerResources();
        return containerResources.createEventLink(getOperationKey(),
                new Object[]{currentBeanContext.getBeanType().getSimpleName(),
                    currentBeanContext.getCurrentBeanId()});
    }

    @Override
    protected String getOperationKey() {
        return "del";
    }
}
