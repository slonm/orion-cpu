package orion.tapestry.beaneditor;

import java.util.ArrayList;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.services.ValidationConstraintGenerator;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

/**
 * Checks for JPA annotations, and extracts its value to form the result.
 */
//TODO Сделать что-бы hibernate validator обрабатывал аннотации hibernate
public class JPAAnnotationsConstraintGenerator implements ValidationConstraintGenerator {

    /**
     * @see ValidationConstraintGenerator#buildConstraints
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> buildConstraints(Class propertyType, AnnotationProvider annotationProvider) {

        ArrayList<String> constraints = new ArrayList<String>();
        Column annColumn = annotationProvider.getAnnotation(Column.class);
        if (annColumn != null) {
            if (!annColumn.nullable()) {
                constraints.add("required");
            }
            constraints.add("maxlength=" + annColumn.length());
        }

        JoinColumn annJoinColumn = annotationProvider.getAnnotation(JoinColumn.class);
        if (annJoinColumn != null && !annJoinColumn.nullable()) {
            constraints.add("required");
        }

        return constraints;
    }
}
