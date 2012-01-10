package ua.orion.web.base;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import ua.orion.web.CurrentBeanContext;

/**
 * Superclass of the all links, what manipulate with Object.
 * @author sl
 */
@SupportsInformalParameters
public abstract class AbstractObjectLink {

    @Inject
    private Messages messages;
    @Inject
    private ComponentResources resources;
    @Parameter(value = "true")
    private boolean enabled;
    @Environmental
    private CurrentBeanContext currentBeanContext;

    /**
     * Method that returns the link to be rendered.
     *
     * @return a {@link Link}. If null, then link do not render.
     */
    abstract protected Link createLink();

    abstract protected String getOperationKey();

    void setupRender() {
        SecurityUtils.getSubject().checkPermission(currentBeanContext.getBeanType().getSimpleName() + ":" + getOperationKey());
    }

    boolean beginRender(MarkupWriter writer) {
        Link link = createLink();
        if (link != null) {
            if (enabled) {
                writer.element("a", "href", link.toURI(),
                        "class", "crud-" + getOperationKey() + "-pic",
                        "title", messages.get("action." + getOperationKey()));
                resources.renderInformalParameters(writer);
            } else {
                writer.element("div",
                        "class", "crud-" + getOperationKey() + "-disabled");
            }
        }
        return false;
    }

    void afterRender(MarkupWriter writer) {
        writer.end(); // a
    }
}
