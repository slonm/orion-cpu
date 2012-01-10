/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.utils;

import java.util.*;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * на основе org.apache.tapestry5.ioc.internal.util.Orderer
 * @author slobodyanuk
 */
public class Orderer<T> {

    private final Logger logger=LoggerFactory.getLogger(Orderer.class);
    private final List<Orderable> orderables = new ArrayList();
    private final Map<String, Orderable<T>> idToOrderable = new HashMap();
    private final Map<String, DependencyNode<T>> dependencyNodesById = new HashMap();
    // Special node that is always dead last: all other nodes are a dependency
    // of the trailer.
    private DependencyNode<T> trailer;

    interface DependencyLinker<T> {

        void link(DependencyNode<T> source, DependencyNode<T> target);
    }
    // before: source is added as a dependency of target, so source will
    // appear before target.
    final DependencyLinker<T> _before = new DependencyLinker<T>() {

        @Override
        public void link(DependencyNode<T> source, DependencyNode<T> target) {
            target.addDependency(source);
        }
    };
    // after: target is added as a dependency of source, so source will appear
    // after target.
    final DependencyLinker<T> _after = new DependencyLinker<T>() {

        @Override
        public void link(DependencyNode<T> source, DependencyNode<T> target) {
            source.addDependency(target);
        }
    };

    /**
     * Adds an object to be ordered.
     *
     * @param orderable
     */
    private void add(Orderable<T> orderable) {

        String id = orderable.getId();

        if (idToOrderable.containsKey(id)) {
            logger.warn("Duplicate Orderer {}", id);
            return;
        }

        orderables.add(orderable);

        idToOrderable.put(id, orderable);
    }

    /**
     * Adds an object to be ordered.
     *
     * @param id          unique, qualified id for the target
     * @param target      the object to be ordered (or null as a placeholder)
     * @param constraints optional, variable constraints
     * @see #add(Orderable)
     */
    public void add(String id, T target, String... constraints) {
        add(new Orderable<T>(id, target, constraints));
    }

    public List<T> getOrdered() {
        initializeGraph();

        List<T> result = new ArrayList();

        for (Orderable<T> orderable : trailer.getOrdered()) {
            T target = orderable.getTarget();

            // Nulls are placeholders that are skipped.

            if (target != null) {
                result.add(target);
            }
        }

        return result;
    }

    private void initializeGraph() {
        trailer = new DependencyNode<T>(logger, new Orderable<T>("*-trailer-*", null));

        addNodes();

        addDependencies();
    }

    private void addNodes() {
        for (Orderable<T> orderable : orderables) {
            DependencyNode<T> node = new DependencyNode<T>(logger, orderable);

            dependencyNodesById.put(orderable.getId(), node);

            trailer.addDependency(node);
        }
    }

    private void addDependencies() {
        for (Orderable<T> orderable : orderables) {
            addDependencies(orderable);
        }
    }

    private void addDependencies(Orderable<T> orderable) {
        String sourceId = orderable.getId();

        for (String constraint : orderable.getConstraints()) {
            addDependencies(sourceId, constraint);
        }
    }

    private void addDependencies(String sourceId, String constraint) {
        int colonx = constraint.indexOf(':');

        String type = colonx > 0 ? constraint.substring(0, colonx) : null;

        DependencyLinker<T> linker = null;

        if ("after".equals(type)) {
            linker = _after;
        } else if ("before".equals(type)) {
            linker = _before;
        }

        if (linker == null) {
//            logger.warn(UtilMessages.constraintFormat(constraint, sourceId));
            return;
        }

        String patternList = constraint.substring(colonx + 1);

        linkNodes(sourceId, patternList, linker);
    }

    private void linkNodes(String sourceId, String patternList, DependencyLinker<T> linker) {
        Collection<DependencyNode<T>> nodes = findDependencies(sourceId, patternList);

        DependencyNode<T> source = dependencyNodesById.get(sourceId);

        for (DependencyNode<T> target : nodes) {
            linker.link(source, target);
        }
    }

    private Collection<DependencyNode<T>> findDependencies(String sourceId, String patternList) {
        IdMatcher matcher = buildMatcherForPattern(patternList);

        Collection<DependencyNode<T>> result = new ArrayList();

        for (String id : dependencyNodesById.keySet()) {
            if (sourceId.equals(id)) {
                continue;
            }

            if (matcher.matches(id)) {
                result.add(dependencyNodesById.get(id));
            }
        }

        return result;
    }

    private IdMatcher buildMatcherForPattern(String patternList) {
        List<IdMatcher> matchers = new ArrayList();

        for (String pattern : patternList.split(",")) {
            IdMatcher matcher = new IdMatcherImpl(pattern.trim());

            matchers.add(matcher);
        }

        return matchers.size() == 1 ? matchers.get(0) : new OrIdMatcher(matchers);
    }

    private static class DependencyNode<T> {

        private final Logger logger;
        private final Orderable<T> orderable;
        private final List<DependencyNode<T>> dependencies = new ArrayList();

        DependencyNode(Logger logger, Orderable<T> orderable) {
            this.logger = logger;
            this.orderable = orderable;
        }

        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder(String.format("[%s", getId()));

            boolean first = true;

            for (DependencyNode<T> node : dependencies) {

                buffer.append(first ? ": " : ", ");

                buffer.append(node.toString());

                first = false;
            }

            buffer.append("]");

            return buffer.toString();
        }

        /**
         * Returns the underlying {@link Orderable}'s id.
         */
        public String getId() {
            return orderable.getId();
        }

        void addDependency(DependencyNode<T> node) {
            if (node.isReachable(this)) {
                logger.warn("Cyclic dependency {} {}", node, this);
                return;
            }

            // Make this node depend on the other node.
            // That forces the other node's orderable
            // to appear before this node's orderable.

            dependencies.add(node);
        }

        boolean isReachable(DependencyNode<T> node) {
            if (this == node) {
                return true;
            }

            // Quick fast pass for immediate dependencies

            for (DependencyNode<T> d : dependencies) {
                if (d == node) {
                    return true;
                }
            }

            // Slower second pass looks for
            // indirect dependencies

            for (DependencyNode<T> d : dependencies) {
                if (d.isReachable(node)) {
                    return true;
                }
            }

            return false;
        }

        /**
         * Returns the {@link Orderable} objects for this node ordered based on dependencies.
         */
        List<Orderable<T>> getOrdered() {
            List<Orderable<T>> result = new ArrayList();

            fillOrder(result);

            return result;
        }

        private void fillOrder(List<Orderable<T>> list) {
            if (list.contains(orderable)) {
                return;
            }

            // Recusively add dependencies

            for (DependencyNode<T> node : dependencies) {
                node.fillOrder(list);
            }

            list.add(orderable);
        }
    }

    static private class Orderable<T> {

        private final String id;
        private final T target;
        private final String[] constraints;

        /**
         * @param id     unique identifier for the target object
         * @param target the object to be ordered; this may also be null (in which case the id represents a placeholder)
         */
        public Orderable(String id, T target, String... constraints) {
            assert id != null;
            assert id.length() > 0;
            this.id = id;
            this.target = target;
            this.constraints = constraints;
        }

        public String getId() {
            return id;
        }

        public T getTarget() {
            return target;
        }

        public String[] getConstraints() {
            return constraints;
        }

        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder("Orderable[");

            buffer.append(id);

            for (String c : constraints) {
                buffer.append(" ");
                buffer.append(c);
            }

            buffer.append(" ");
            buffer.append(target.toString());
            buffer.append("]");

            return buffer.toString();
        }
    }

    private static class IdMatcherImpl implements IdMatcher {

        private final GlobPatternMatcher globMatcher;

        public IdMatcherImpl(String pattern) {
            globMatcher = new GlobPatternMatcher(pattern);
        }

        @Override
        public boolean matches(String id) {
            return globMatcher.matches(id);
        }
    }

    private static class OrIdMatcher implements IdMatcher {

        private final IdMatcher[] matchers;

        public OrIdMatcher(Collection<IdMatcher> matchers) {
            this.matchers = matchers.toArray(new IdMatcher[matchers.size()]);
        }

        @Override
        public boolean matches(String id) {
            for (IdMatcher m : matchers) {
                if (m.matches(id)) {
                    return true;
                }
            }

            return false;
        }
    }

    private static class GlobPatternMatcher {

        private final Pattern pattern;
        private final static Pattern oldStyleGlob =
                Pattern.compile("[a-z\\*]+", Pattern.CASE_INSENSITIVE);

        public GlobPatternMatcher(String pattern) {
            this.pattern = compilePattern(pattern);
        }

        private static Pattern compilePattern(String pattern) {
            return Pattern.compile(createRegexpFromGlob(pattern), Pattern.CASE_INSENSITIVE);
        }

        private static String createRegexpFromGlob(String pattern) {
            return oldStyleGlob.matcher(pattern).matches()
                    ? pattern.replace("*", ".*")
                    : pattern;
        }

        public boolean matches(String input) {
            return pattern.matcher(input).matches();
        }
    }

    private static interface IdMatcher {

        /**
         * Returns true if the provided input id matches the pattern defined by this matcher instance.
         *
         * @param id the fully qualfied id
         * @return true on match, false otherwise
         */
        boolean matches(String id);
    }
}
