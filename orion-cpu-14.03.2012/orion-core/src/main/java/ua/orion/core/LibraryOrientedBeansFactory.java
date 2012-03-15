package ua.orion.core;

import java.util.*;
import org.apache.tapestry5.ioc.ObjectLocator;
import ua.orion.core.services.ModelLibraryService;
import ua.orion.core.utils.Defense;

/**
 * Фабрика бинов, обслуживающих библиотеки модели.
 * Фабрика ищет в каждой библиотеке, возвращаемой ModelLibraryService
 * класс в подпакете subPackage с именем prefix+имя_библиотеки+suffix,
 * создает объекты этого типа с помощью ObjectLocator и возвращает список
 * этих объектов
 * @author sl
 */
public class LibraryOrientedBeansFactory {

    public final ModelLibraryService resolver;
    public final ObjectLocator locator;
    public final String subPackage;
    public final String prefix;
    public final String suffix;

    public LibraryOrientedBeansFactory(ModelLibraryService resolver,
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

    /**
     * Возвращает список нетипизированных бинов
     */
    public List<Object> create() {
        return create(Object.class);
    }

    /**
     * Возвращает список бинов реализующих/расширяющих beanType
     */
    public <T> List<T> create(Class<T> beanType) {
        List<Class<?>> classes = resolver.resolveLibraryOrientedBeanClasses(subPackage, prefix, suffix);
        List<T> result = new ArrayList();
        if (classes.isEmpty()) {
            return null;
        } else {
            for (Class<?> c : classes) {
                result.add((T) locator.autobuild(c));
            }
        }
        return result;
    }
}
