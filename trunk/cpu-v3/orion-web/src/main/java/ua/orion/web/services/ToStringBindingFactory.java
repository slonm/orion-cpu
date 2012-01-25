/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.services;

import java.lang.annotation.Annotation;
import javax.inject.Inject;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;
import ua.orion.core.services.StringValueProvider;

/**
 *
 * @author slobodyanuk
 */
public class ToStringBindingFactory implements BindingFactory {

    @Inject
    private BindingSource bindingSource;
    @Inject
    private StringValueProvider provider;

    public Binding newBinding(final String description, final ComponentResources container,
            final ComponentResources component,
            final String expression, final Location location) {

        return new Binding() {

            Binding delegate = bindingSource.newBinding(description, container, component,
                    BindingConstants.PROP, expression, location);

            @Override
            public Object get() {
                Object res = delegate.get();
                return res == null ? null : provider.getStringValue(res);
            }

            @Override
            public void set(Object value) {
                delegate.set(value);
            }

            @Override
            public boolean isInvariant() {
                return true;
            }

            @Override
            public Class getBindingType() {
                return delegate.getBindingType();
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }
        };
    }
}
