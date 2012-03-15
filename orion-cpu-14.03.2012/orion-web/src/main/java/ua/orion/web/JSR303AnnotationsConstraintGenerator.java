package ua.orion.web;

import java.util.ArrayList;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.services.ValidationConstraintGenerator;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Checks for JSR303 annotations, and extracts its value to form the result.
 */
public class JSR303AnnotationsConstraintGenerator implements ValidationConstraintGenerator {

    /**
     * @see ValidationConstraintGenerator#buildConstraints
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> buildConstraints(Class propertyType, AnnotationProvider annotationProvider) {

        ArrayList<String> constraints = new ArrayList<String>();
        NotNull annNotNull = annotationProvider.getAnnotation(NotNull.class);
        if (annNotNull != null) {
                constraints.add("required");
        }
        Size annSize = annotationProvider.getAnnotation(Size.class);
        if (annSize != null) {
            constraints.add("maxlength=" + annSize.max());
            constraints.add("minlength=" + annSize.min());
        }

        return constraints;
    }
}
