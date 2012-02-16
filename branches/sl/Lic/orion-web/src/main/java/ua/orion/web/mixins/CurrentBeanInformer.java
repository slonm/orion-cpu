package ua.orion.web.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.Environment;
import org.slf4j.Logger;
import ua.orion.web.CurrentBeanContext;

/**
 * Миксин для сохранения в {@link Environment} объекта {@link CurrentBeanContext}.
 * Подключается к {@link BeanDisplay}, {@link Grid}, {@link BeanEditor}, {@link BeanEditForm}
 * По мотивам http://svn.codehaus.org/tynamo/trunk/tapestry-model/tapestry-model-core/src/main/java/org/tynamo/mixins/BeanModelAdvisor.java
 */
public class CurrentBeanInformer {

    /**
     * The component to which this mixin is attached.
     */
    @InjectContainer
    private Object container;
    @Inject
    private Environment environment;
    @Inject
    private PropertyAccess propertyAccess;
    @Inject
    private Logger LOG;
    @Inject
    private ComponentResources resources;
    /**
     * The container's property to retrieve the object (or bean)
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String containerProperty;

    @SetupRender
    void init() {
        pushCurrentBeanContext();
    }

    private void pushCurrentBeanContext() {
        LOG.debug("pushCurrentBeanContext");
        if (!resources.isBound("containerProperty")) {
            LOG.debug("containerProperty isEmpty");
            containerProperty = guessPropertyName();
        }

        CurrentBeanContext context = new CurrentBeanContext() {

            @Override
            public Class<?> getBeanType() {
                return getObject().getClass();
            }

            private Object getObject() {
                return propertyAccess.get(container, containerProperty);
            }

            @Override
            public Object getCurrentBean() {
                return getObject();
            }
        };
        environment.push(CurrentBeanContext.class, context);
    }

    private String guessPropertyName() {
        if (container instanceof Grid) {
            return "row";
        }
        if (container instanceof BeanDisplay
                || container instanceof BeanEditor
                || container instanceof BeanEditForm
                || container instanceof PropertyEditor) {
            return "object";
        }
        throw new RuntimeException(
                "The container's object property couldn't be guessed, please provide one. eg: avisor.containerProperty=\"bean\"");
    }

    @Log
    void cleanupRender() {
        environment.pop(CurrentBeanContext.class);
    }
}