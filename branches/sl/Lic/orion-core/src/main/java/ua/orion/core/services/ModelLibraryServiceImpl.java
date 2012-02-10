package ua.orion.core.services;

import java.util.*;
import ua.orion.core.annotations.OrderLibrary;
import ua.orion.core.utils.Defense;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.core.utils.Orderer;

/**
 *
 * @author sl
 */
public class ModelLibraryServiceImpl implements ModelLibraryService {

    private final Set<ModelLibraryInfo> libs = new HashSet();

    public ModelLibraryServiceImpl(Collection<ModelLibraryInfo> libs) {
        this.libs.addAll(Defense.notNull(libs, "libs"));
    }

    @Override
    public Set<Class<?>> resolveEntityOrientedBeanClasses(Class<?> entityType, String subPackage, String prefix, String suffix) {
        Defense.notNull(entityType, "entityType");
        return resolveEntityOrientedBeanClasses(entityType.getSimpleName(), subPackage, prefix, suffix);
    }

    @Override
    public Set<Class<?>> resolveEntityOrientedBeanClasses(String entity, String subPackage, String prefix, String suffix) {
        Defense.notBlank(entity, "entity");
        Defense.notBlank(subPackage, "subPackage");
        if ((prefix == null || prefix.isEmpty()) && (suffix == null || suffix.isEmpty())) {
            throw new IllegalArgumentException("'prefix' or 'suffix' must have value");
        }
        Set<Class<?>> result = new HashSet();
        for (ModelLibraryInfo lib : libs) {
            StringBuilder sb = new StringBuilder(lib.getLibraryPackage());
            sb.append(".").append(subPackage).append(".");
            if (prefix != null) {
                sb.append(prefix);
            }
            sb.append(entity);
            if (suffix != null) {
                sb.append(suffix);
            }
            try {
                result.add(Class.forName(sb.toString()));
            } catch (ClassNotFoundException ex) {
            }
        }
        return result;
    }

    @Override
    public Set<ModelLibraryInfo> getModelLibraryInfos() {
        return Collections.unmodifiableSet(libs);
    }

    @Override
    public List<Class<?>> resolveLibraryOrientedBeanClasses(String subPackage, String prefix, String suffix) {
        Defense.notBlank(subPackage, "subPackage");
        if ((prefix == null || prefix.isEmpty()) && (suffix == null || suffix.isEmpty())) {
            throw new IllegalArgumentException("'prefix' or 'suffix' must have value");
        }
        Orderer<Class<?>> orderer = new Orderer();
        for (ModelLibraryInfo lib : libs) {
            StringBuilder sb = new StringBuilder(lib.getLibraryPackage());
            sb.append(".").append(subPackage).append(".");
            if (prefix != null) {
                sb.append(prefix);
            }
            sb.append(lib.getLibraryName());
            if (suffix != null) {
                sb.append(suffix);
            }
            try {
                Class<?> clasz = Class.forName(sb.toString());
                OrderLibrary ann = clasz.getAnnotation(OrderLibrary.class);
                if (ann != null) {
                    orderer.add(lib.getLibraryName(), clasz, ann.value());
                } else {
                    orderer.add(lib.getLibraryName(), clasz);
                }
            } catch (ClassNotFoundException ex) {
            }
        }
        return orderer.getOrdered();
    }
}
