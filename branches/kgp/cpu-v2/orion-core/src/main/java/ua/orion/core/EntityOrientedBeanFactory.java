package ua.orion.core;

import java.util.Set;
import org.apache.tapestry5.ioc.ObjectLocator;
import ua.orion.core.services.ModelLibraryService;
import ua.orion.core.utils.Defense;

/**
 *
 * @author sl
 */
public class EntityOrientedBeanFactory {

    public final ModelLibraryService resolver;
    public final ObjectLocator locator;
    public final String subPackage;
    public final String prefix;
    public final String suffix;

    public EntityOrientedBeanFactory(ModelLibraryService resolver,
            ObjectLocator locator,
            String subPackage, String prefix, String suffix) {
        this.resolver = Defense.notNull(resolver, "resolver");
        this.locator = Defense.notNull(locator, "locator");
        this.subPackage = Defense.notNull(subPackage, "subPackage");
        if ((prefix == null || prefix.isEmpty()) && (suffix == null || suffix.isEmpty())) {
            throw new IllegalArgumentException("'prefix' or 'suffix' must have value");
        }
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public <T> T create(Class<T> beanType, Class<?> entityType) {
        Set<Class<?>> classes = resolver.resolveEntityOrientedBeanClasses(entityType, subPackage, prefix, suffix);
        if (classes.isEmpty()) {
            return null;
        } else if (classes.size() == 1) {
            Class<T> c = classes.toArray(new Class[1])[0];
            return locator.autobuild(c);
        }
        throw new RuntimeException("Exists more when one class for "
                + entityType.getName() + ": " + classes.toString());
    }
}
