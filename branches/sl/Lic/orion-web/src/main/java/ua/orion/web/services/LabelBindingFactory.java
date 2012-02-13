package ua.orion.web.services;

import java.lang.annotation.Annotation;
import javax.inject.Inject;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import ua.orion.core.services.EntityService;
import ua.orion.core.services.ModelLabelSource;

/**
 *
 * @author slobodyanuk
 */
public class LabelBindingFactory implements BindingFactory {

    @Inject
    private ModelLabelSource mls;
    @Inject
    private EntityService entityService;

    private String incorrectLabelKey(String key) {
        return String.format("[[missing key: %s]]", key);
    }

    public Binding newBinding(final String description, final ComponentResources container,
            final ComponentResources component,
            final String expression, final Location location) {
        final String value;
        String[] parts = expression.split(".");
        //Нужно хотя бы две части
        if (parts.length < 2) {
            value = incorrectLabelKey(expression);
        } else if ("entity".equalsIgnoreCase(parts[0])) {
            value = mls.getEntityLabel(entityService.getMetaEntity(parts[1]).getClass(), component.getMessages());
        } else if ("property".equalsIgnoreCase(parts[0])) {
            value = mls.getPropertyLabel(entityService.getMetaEntity(parts[1]).getClass(), parts[2], component.getMessages());
        } else if ("property-cell".equalsIgnoreCase(parts[0])) {
            value = mls.getCellPropertyLabel(entityService.getMetaEntity(parts[1]).getClass(), parts[2], component.getMessages());
        } else value = incorrectLabelKey(expression);

//
        return new Binding() {

            @Override
            public Object get() {
                return value;
            }

            @Override
            public void set(Object value) {
                throw new RuntimeException("Binding " + this + " Is Read Only");
            }

            @Override
            public boolean isInvariant() {
                return true;
            }

            @Override
            public Class getBindingType() {
                return String.class;
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }

            @Override
            public String toString() {
                return String.format("LabelBinding[%s: %s]", description, expression);
            }
        };
    }
}
