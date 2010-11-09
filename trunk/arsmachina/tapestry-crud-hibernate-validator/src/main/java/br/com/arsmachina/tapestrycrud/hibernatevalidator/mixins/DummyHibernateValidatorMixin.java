// Copyright 2008-2009 Thiago H. de Paula Figueiredo
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package br.com.arsmachina.tapestrycrud.hibernatevalidator.mixins;

import java.lang.reflect.Method;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import br.com.arsmachina.tapestrycrud.services.FormValidationSupport;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;

/**
 * Mixin that is not meant to be used as a mixin, just as an easy way to invoke
 * the
 * {@link DummyHibernateValidatorMixin#validate(Object, Form, ComponentResources)}
 * method inside page classes. This class exists because it is not possible to
 * pass a {@link Form} instance (a Tapestry-transformed classs) to a
 * Tapestry-IoC service (not Tapestry-transformed).
 * 
 * @sse {@link HibernateValidatorMixin}
 * @author Thiago H. de Paula Figueiredo
 */
public class DummyHibernateValidatorMixin {

    /**
     * Default error message format.
     */
    private static final String DEFAULT_ERROR_MESSAGE_FORMAT = "%s: %s.";
    /**
     * Error message format key.
     */
    public static final String ERROR_MESSAGE_FORMAT =
            "tapestry.crud.hibernate.validator.format";
    @Inject
    private FormValidationSupport formValidationSupport;
    @Inject
    private Validator validator;
    @Inject
    private Messages messages;
    @Inject
    private ThreadLocale threadLocale;

    /**
     * Validates an object and records its erros in a given form. The same as
     * <code>validate(object, form, componentResources, true)</code>.
     * 
     * @param object aon {@link Object}. It cannot be null.
     * @param form a {@link Form}. It cannot be null.
     * @param the {@link ComponentResources} from the page where the form
     *            belongs.
     * 
     * @return <code>true</code> if no validation errors were fond,
     *         <code>false</code> otherwise.
     */
    public boolean validate(final Object object, final Form form,
            ComponentResources componentResources) {
        return validate(object, form, componentResources, true);
    }

    /**
     * Validates an object and records its erros in a given form.
     * 
     * @param object aon {@link Object}. It cannot be null.
     * @param form a {@link Form}. It cannot be null.
     * @param the {@link ComponentResources} from the page where the form
     *            belongs.
     * @param singleErrorMessagePerField a <code>boolean</code>. If
     *            <code>true</code>, it will record at most one error message
     *            per field.
     * 
     * @return <code>true</code> if no validation errors were fond,
     *         <code>false</code> otherwise.
     */
    @SuppressWarnings("unchecked")
    public boolean validate(final Object object, final Form form,
            ComponentResources componentResources,
            boolean singleErrorMessagePerField) {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        final ValidationTracker tracker = form.getDefaultTracker();
        boolean addMessage;

        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            Path.Node node = null;
            Field field = null;
            if (constraintViolation.getPropertyPath().iterator().hasNext()) {
                node = constraintViolation.getPropertyPath().iterator().next();
                field = formValidationSupport.getField(node.getName(), componentResources);
            }

            if (field != null) {
                addMessage = singleErrorMessagePerField == false
                        || tracker.inError(field) == false;
            } else {
                addMessage = true;
            }

            if (addMessage) {
//                String message = createMessage(field, invalidValue);
                String message = constraintViolation.getMessage();
                if (field != null) {
                    form.recordError(field, message);
                } else {
                    form.recordError(message);
                }
            }
        }
        return constraintViolations.isEmpty();

    }

    /**
     * Creates a message for a given <code>field</code> and
     * <code>invalidValue</code>.
     * 
     * @param invalidValue an {@link InvalidValue}. It cannot be null.
     * @param field a {@link Field}. It cannot be null.
     * @return a {@link String}. It cannot be null.
     * @todo: handle @@AssertTrue and @@AssertFalse
     */
//    protected String createMessage(Field field, InvalidValue invalidValue) {
//
//        final String propertyName = invalidValue.getPropertyName();
//        String message = null;
//
//        // if the value type is boolean, we consider that it isn't a property,
//        // we check if it is a real property or not (method with @AssertFalse or
//        // @AssertTrue).
//        final Object value = invalidValue.getValue();
//
//        if (value != null && value.getClass().equals(Boolean.class)) {
//
//            boolean testProperty =
//                    isTestProperty(invalidValue, propertyName, "is")
//                    || isTestProperty(invalidValue, propertyName, "get");
//
//            if (testProperty) {
//                message = invalidValue.getMessage();
//            }
//
//        }
//
//        if (message == null) {
//
//            String fieldLabel;
//
//            if (field != null) {
//                fieldLabel = field.getLabel();
//            } else {
//
//                final String key = propertyName + "-label";
//                fieldLabel =
//                        messages.contains(key) ? messages.get(key) : propertyName;
//
//            }
//
//            String format;
//
//            if (messages.contains(ERROR_MESSAGE_FORMAT)) {
//                format = messages.get(ERROR_MESSAGE_FORMAT);
//            } else {
//                format = DEFAULT_ERROR_MESSAGE_FORMAT;
//            }
//
//            message =
//                    String.format(threadLocale.getLocale(), format, fieldLabel,
//                    invalidValue.getMessage());
//
//        }
//
//        return message;
//
//    }

    /**
     * @param invalidValue
     * @param propertyName
     * @param message
     * @param prefix
     * @return
     */
//    private boolean isTestProperty(InvalidValue invalidValue,
//            final String propertyName, final String prefix) {
//
//        String getter =
//                prefix + Character.toUpperCase(propertyName.charAt(0))
//                + propertyName.substring(1);
//        final Class<?> beanType = invalidValue.getBeanClass();
//        boolean testProperty = false;
//
//        Method method = getterMethod(getter, beanType);
//
//        if (method == null) {
//            testProperty = true;
//        } else {
//
//            if (method.isAnnotationPresent(AssertTrue.class)
//                    || method.isAnnotationPresent(AssertFalse.class)) {
//
//                testProperty = true;
//
//            }
//
//        }
//
//        return testProperty;
//
//    }

    private Method getterMethod(String name, final Class<?> clasz) {

        Method method = null;

        try {
            method = clasz.getMethod(name);
        } catch (NoSuchMethodException e) {
        }

        return method;

    }
}
