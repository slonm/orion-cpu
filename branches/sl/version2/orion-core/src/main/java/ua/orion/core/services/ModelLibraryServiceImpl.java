package ua.orion.core.services;

import java.util.*;
import ua.orion.core.annotations.AfterLibrary;
import ua.orion.core.annotations.BeforeLibrary;
import ua.orion.core.utils.Defense;
import ua.orion.core.ModelLibraryInfo;

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
        final Map<Class<?>, String> map = new HashMap();
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
                map.put(Class.forName(sb.toString()), lib.getLibraryName());
            } catch (ClassNotFoundException ex) {
            }
        }
        List<Class<?>> result = new ArrayList();
        result.addAll(map.keySet());
        Collections.sort(result, new Comparator<Class<?>>()      {

            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                return - compare(o1, o2, false);
            }

//TODO Добавить проверку непротиворечивости условий
            public int compare(Class<?> o1, Class<?> o2, boolean swap) {
                BeforeLibrary beforeLibrary = o1.getAnnotation(BeforeLibrary.class);
                if (beforeLibrary != null) {
                    List<String> libs = Arrays.asList(beforeLibrary.value());
                    if (libs.contains(map.get(o2))) {
                        return -1;
                    }
                }
                AfterLibrary afterLibrary = o1.getAnnotation(AfterLibrary.class);
                if (afterLibrary != null) {
                    List<String> libs = Arrays.asList(afterLibrary.value());
                    if (libs.contains(map.get(o2))) {
                        return 1;
                    }
                }
                if (swap) {
                    return 0;
                } else {
                    return compare(o2, o1, true);
                }
            }
        });
        return result;
    }
}
