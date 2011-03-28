package orion.cpu.services;

import br.com.arsmachina.module.DefaultModule;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.ioc.services.SymbolSource;
import orion.cpu.baseentities.BaseEntity;

/**
 * {@see br.com.arsmachina.module.Module}
 * Модуль формирует имена объектов на основании символов реестра (symbolSource)
 * @author sl
 */
public class OrionModuleImpl extends DefaultModule {

    private final SymbolSource symbolSource;

    /**
     * Конструктор.
     * @param id Идентификатор модуля. Если он отличен от null то в пакете TapestryCRUD будет регистрироватся библиотека компонентов с таким именем
     * @param classNameLocator сервис ClassNameLocator
     * @param symbolSource сервис SymbolSource
     */
    public OrionModuleImpl(String id, ClassNameLocator classNameLocator,
            SymbolSource symbolSource) {
        super(id, symbolSource.valueForSymbol("orion.root-package"), classNameLocator, "");
        this.symbolSource = symbolSource;
    }

    /**
     * Формирователь имени класса для работы с сущностью.
     * @param entityClass класс сущности
     * @param symbol символ реестра
     * @param prefix префикс имени класса
     * @param suffix суфикс имени класса
     * @return имя класса.
     */
    protected String getClassName(Class<?> entityClass, String symbol, String prefix, String suffix) {
        String schema=BaseEntity.getSchema(entityClass);
        return String.format("%s.%s.%s%s%s%s",
                getRootPackage(),
                symbolSource.valueForSymbol(symbol),
                schema.length() != 0 ? schema + "." : "",
                prefix,
                entityClass.getSimpleName(),
                suffix);
    }

    /**
     * Вычисляет имя пакета с сущностями по формуле
     * Symbol("orion.root-package")+"."+Symbol("orion.entities-package")
     * @return a {@link String}.
     */
    @Override
    public String getEntityPackage() {
        return String.format("%s.%s", getRootPackage(), symbolSource.valueForSymbol("orion.entities-package"));
    }

    /**
     * Returns the fully-qualified name of the controller class implementation for a given entity
     * class.
     * По формуле Symbol("orion.root-package")+"."+Symbol("orion.entities-package")+"."+
     * Symbol("orion.controllers-implementation-package")+"."+schema+"."+entityClassName+"ControllerImpl"
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    public String getControllerImplementationClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.controllers-implementation-package", "", "ControllerImpl");
    }

    /**
     * Returns the fully-qualified name of the controller definition (interface) for a given entity
     * class.
     * По формуле Symbol("orion.root-package")+"."+Symbol("orion.entities-package")+"."+
     * Symbol("orion.controllers-package")+"."+schema+"."+entityClassName+"Controller"
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    public String getControllerDefinitionClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.controllers-package", "", "Controller");
    }

    /**
     * Returns the fully-qualified name of the DAO class implementation for a given entity class.
     * По формуле Symbol("orion.root-package")+"."+Symbol("orion.entities-package")+"."+
     * Symbol("orion.dao-implementation-package")+"."+schema+"."+entityClassName+"DAOImpl"
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    public String getDAOImplementationClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.dao-implementation-package", "", "DAOImpl");
    }

    /**
     * Returns the fully-qualified name of the DAO definition (interface) for a given entity class.
     * По формуле Symbol("orion.root-package")+"."+Symbol("orion.entities-package")+"."+
     * Symbol("orion.dao-package")+"."+schema+"."+entityClassName+"DAO"
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    public String getDAODefinitionClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.dao-package", "", "DAO");
    }

    /**
     * Returns the fully-qualified name of the SingleTypeAuthorizationService for a given entity class.
     * По формуле Symbol("orion.root-package")+"."+Symbol("orion.entities-package")+"."+
     * Symbol("orion.authorizer-package")+"."+schema+"."+entityClassName+"AuthorizationService"
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    public String getSingleTypeAuthorizationServiceClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.authorizer-package", "", "AuthorizationService");
    }

    /**
     * Returns the fully-qualified name of the Authorizer for a given entity class.
     * По формуле Symbol("orion.root-package")+"."+Symbol("orion.entities-package")+"."+
     * Symbol("orion.authorizer-package")+"."+schema+"."+entityClassName+"Authorizer"
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    public String getAuthorizerClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.authorizer-package", "", "Authorizer");
    }

    /**
     * Показывает будет ли обслуживатся указанный класс сущности этим модулем.
     * Возвращает true если для класса загружен маппинг Hibernate или если класс наследует BaseEntity
     * @param clasz a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    protected boolean accept(Class<?> clasz) {
        return super.accept(clasz) || (BaseEntity.class.isAssignableFrom(clasz));
    }
}
