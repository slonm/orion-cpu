package ua.orion.core.services;

import java.util.*;
import ua.orion.core.utils.ModelLibraryInfo;

/**
 * Хранилище метаданных о библиотеках моделей данных
 * @author sl
 */
public interface ModelLibraryService {

    /**
     * Возвращает множество описателей библиотек моделей
     * @return
     */
    Set<ModelLibraryInfo> getModelLibraryInfos();

    /**
     * Используется для получения контроллера, дескриптора или любого другого 
     * придатка к классу сущности.
     * Ищет существующие классы во всех пакетах с именем subPackage перебирая все 
     * libraryRootPackage
     * libraryRootPackage.subPackage.prefix+entityType.getSimpleName()+suffix
     * @return class or null, if no class found
     */
    Set<Class<?>> resolveEntityOrientedBeanClasses(Class<?> entityType, String subPackage, String prefix, String suffix);

    /**
     * Используется для получения контроллера, дескриптора или любого другого
     * придатка к классу сущности.
     * Ищет существующие классы во всех пакетах с именем subPackage перебирая все 
     * libraryRootPackage
     * libraryRootPackage.subPackage.prefix+entityType.getSimpleName()+suffix
     *
     * @return class or null, if no class found
     */
    Set<Class<?>> resolveEntityOrientedBeanClasses(String entity, String subPackage, String prefix, String suffix);
    
     /**
     * Используется для получения класса библиотеки
     * Ищет существующие классы во всех пакетах с именем subPackage перебирая все 
     * libraryRootPackage
     * libraryRootPackage.subPackage.prefix+libraryName+suffix
     * Результат сортирован в соответствии с аннотациями ua.orion.AfterLibrary и
     * ua.orion.BeforeLibrary
     * @return class or null, if no class found
     */
    List<Class<?>> resolveLibraryOrientedBeanClasses(String subPackage, String prefix, String suffix);
}
