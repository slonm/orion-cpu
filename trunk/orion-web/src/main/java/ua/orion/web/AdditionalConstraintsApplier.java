package ua.orion.web;

import javax.persistence.criteria.*;

/**
 *
 * @author slobodyanuk
 */
public interface AdditionalConstraintsApplier<E> {

    void applyAdditionalConstraints(final CriteriaQuery<E> criteria, final Root<E> root,
            final CriteriaBuilder builder);
}
