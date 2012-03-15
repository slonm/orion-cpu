package ua.orion.web;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;

/**
 * A simple implementation of {@link org.apache.tapestry5.grid.GridDataSource} based on a
 * {@linkplain javax.persistence.EntityManager} and a known
 * entity class. This implementation does support multiple
 * {@link org.apache.tapestry5.grid.SortConstraint sort
 * constraints}.
 * <p/>
 * This class is <em>not</em> thread-safe; it maintains internal state.
 * <p/>
 * Typically, an instance of this object is created fresh as needed (that is, it is not stored
 * between requests).
 * Этот источник получает все данные удовлетворяющие ограничениям без учета страниц и сортировок.
 * Разбиение на страницы и сортировка происходит уже на клиентской стороне.
 * @since 5.3
 */
public class FetchAllJpaGridDataSource<E> implements GridDataSource {

    private final EntityManager entityManager;
    private final Class<E> entityType;
    private int startIndex;
    private List<E> preparedResults;
    private LinkedList<E> allResults;
    private final ClassPropertyAdapter cpa;

    public FetchAllJpaGridDataSource(final EntityManager entityManager,
            final Class<E> entityType,
            PropertyAccess pa) {
        super();
        this.entityManager = entityManager;
        this.entityType = entityType;
        cpa = pa.getAdapter(entityType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAvailableRows() {
        prepare();
        return allResults.size();
    }

    private void prepare() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<E> criteria = builder.createQuery(entityType);
        final Root<E> root = criteria.from(entityType);
        applyAdditionalConstraints(criteria.select(root), root, builder);
        final TypedQuery<E> query = entityManager.createQuery(criteria);
        allResults = new LinkedList<>(query.getResultList());
    }

    private int compare(Object o1, Object o2, String propertyName) {
        PropertyAdapter pa = cpa.getPropertyAdapter(propertyName);
        if (Comparable.class.isAssignableFrom(pa.getDeclaringClass())) {
            Comparable c1 = (Comparable) pa.get(o1);
            Comparable c2 = (Comparable) pa.get(o2);
            if (c1 == null && c2 == null) {
                return 0;
            }
            return c1 == null ? 1 : (c2 == null ? -1 : c1.compareTo(c2));
        }
        return 0;
    }

    @Override
    public void prepare(final int startIndex, final int endIndex,
            final List<SortConstraint> sortConstraints) {
        final List<Comparator> sortChain = new ArrayList<>();
        for (final SortConstraint constraint : sortConstraints) {

            final String propertyName = constraint.getPropertyModel().getPropertyName();
            switch (constraint.getColumnSort()) {
                case ASCENDING:
                    sortChain.add(new Comparator() {

                        @Override
                        public int compare(Object o1, Object o2) {
                            return FetchAllJpaGridDataSource.this.compare(o1, o2, propertyName);
                        }
                    });
                    break;

                case DESCENDING:
                    sortChain.add(new Comparator() {

                        @Override
                        public int compare(Object o1, Object o2) {
                            return -FetchAllJpaGridDataSource.this.compare(o1, o2, propertyName);
                        }
                    });
                    break;

                default:
            }
        }
        Collections.sort(allResults, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                for (Comparator c : sortChain) {
                    int res = c.compare(o1, o2);
                    if (res != 0) {
                        return res;
                    }
                }
                return 0;
            }
        });

        preparedResults = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            preparedResults.add(allResults.get(i));
        }
        this.startIndex = startIndex;
    }

    protected void applyAdditionalConstraints(final CriteriaQuery<?> criteria, final Root<E> root,
            final CriteriaBuilder builder) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRowValue(final int index) {
        return preparedResults.get(index - startIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<E> getRowType() {
        return entityType;
    }
}
