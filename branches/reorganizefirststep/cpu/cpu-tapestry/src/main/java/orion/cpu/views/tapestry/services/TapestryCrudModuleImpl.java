package orion.cpu.views.tapestry.services;

import br.com.arsmachina.module.Module;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.module.DefaultTapestryCrudModule;
import org.apache.tapestry5.ioc.services.SymbolSource;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.views.tapestry.encoders.activationcontext.BaseActivationContextEncoder;
import orion.cpu.views.tapestry.pages.Edit;
import orion.cpu.views.tapestry.pages.ListView;

/**
 * Расширение br.com.arsmachina.module.Module для нужд библиотеки TapestryCRUD
 * Модуль формирует имена объектов на основании символов реестра (symbolSource)
 * @author sl
 */
public class TapestryCrudModuleImpl extends DefaultTapestryCrudModule {

    private final SymbolSource symbolSource;
    private final Module module;

    /**
     * Single constructor of this class.
     * @param module Arsmachina module
     * @param symbolSource сервис SymbolSource
     */
    public TapestryCrudModuleImpl(Module module,
            SymbolSource symbolSource) {
        super(module);
        this.symbolSource = symbolSource;
        this.module = module;
    }

    /**
     * Формирователь имени класса для работы с сущностью.
     * @param entityClass класс сущности
     * @param symbol символ реестра
     * @param suffix суфикс имени класса
     * @return имя класса.
     */
    protected String getClassName(Class<?> entityClass, String symbol, String suffix) {
        return String.format("%s.%s.%s%s",
                getTapestryPackage(),
                symbolSource.valueForSymbol(symbol),
                BaseEntity.getFullClassName(entityClass),
                suffix);
    }

    /**
     * Returns the ActivationContextEncoder class implementation for a given entity
     * class.
     * По формуле Symbol("orion.root-package")+"."+Symbol("orion.web-package")+"."+
     * Symbol("orion.activationcontextencoder-package")+"."+schema+"."+entityClassName+"ActivationContextEncoder"
     * Если такого класса нет, а entityClass наследует {@link BaseEntity} то возвращает {@link BaseActivationContextEncoder}
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link Class}.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<? extends ActivationContextEncoder<T>> getActivationContextEncoderClass(
            Class<T> entityClass) {
        Class<?> ret = null;
        if (contains(entityClass)) {
            String className = getClassName(entityClass, "orion.activationcontextencoder-package",
                    "ActivationContextEncoder");
            ret = getClass(className);
            if (ret == null && BaseEntity.class.isAssignableFrom(entityClass)) {
                ret = BaseActivationContextEncoder.class;
            }
        }
        return (Class<? extends ActivationContextEncoder<T>>) ret;
    }

    /**
     * Returns the fully-qualified name of the encoder for a given entity class.
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String} or null (if no corresponding one is found).
     */
    @Override
    protected String getEncoderClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.encoder-package", "Encoder");
    }

    /**
     * Returns the fully-qualified name of the label encoder for a given entity class.
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String} or null (if no corresponding one is found).
     */
    @Override
    protected String getLabelEncoderClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.labelencoder-package", "LabelEncoder");
    }

    /**
     * Returns the fully-qualified name of the bean model customizer for a given entity class.
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String} or null.
     */
    @Override
    public String getBeanModelCustomizerClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.beanmodelcustomizer-package", "BeanModelCustomizer");
    }

    /**
     * Returns the fully-qualified name of the tree node factory for a given entity class.
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String} or null.
     */
    @Override
    public String getTreeServiceClassName(Class<?> entityClass) {
        return getClassName(entityClass, "orion.treeservice-package", "TreeService");
    }

    /**
     * Возвращает класс страницы редактирования для сущности entityClass.
     * Имя класса страницы берется из getEditPageClassName()
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link Class}.
     */
    @Override
    public Class<?> getEditPageClass(Class<?> entityClass) {
        String pageName = getEditPageClassName(entityClass);
        return pageName == null ? null : getClass(pageName);
    }

    /**
     * Возвращает класс страницы списка для сущности entityClass.
     * Имя класса страницы берется из getListPageClassName()
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link Class}.
     */
    @Override
    public Class<?> getListPageClass(Class<?> entityClass) {
        String pageName = getListPageClassName(entityClass);
        return pageName == null ? null : getClass(pageName);
    }

    /**
     * Возвращает класс страницы просмотра для сущности entityClass.
     * Имя класса страницы берется из getViewPageClassName()
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link Class}.
     */
    @Override
    public Class<?> getViewPageClass(Class<?> entityClass) {
        String pageName = getViewPageClassName(entityClass);
        return pageName == null ? null : getClass(pageName);
    }

    /**
     * Возвращает полное имя класса страницы редактирования указанной сущности по формуле
     * Symbol("orion.root-package")+"."+Symbol("orion.web-package")+".pages."+
     * +schema+"."+entityClassName+".Edit"+entityClassName
     * Если такого класса нет, а entityClass наследует {@link BaseEntity} то возвращает {@link Edit}
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    public String getEditPageClassName(Class<?> entityClass) {
        String className = getPageClassName(entityClass, getEditPagePrefix());
        if (getClass(className) != null) {
            return className;
        } else {
            if (BaseEntity.class.isAssignableFrom(entityClass)) {
                return Edit.class.getName();
            }
        }
        return null;
    }

    /**
     * Возвращает полное имя класса страницы со списком для указанной сущности по формуле
     * Symbol("orion.root-package")+"."+Symbol("orion.web-package")+".pages."+
     * +schema+"."+entityClassName+".List"+entityClassName
     * Если такого класса нет, а entityClass наследует {@link BaseEntity} то возвращает {@link List}
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    public String getListPageClassName(Class<?> entityClass) {
        String className = getPageClassName(entityClass, getListPagePrefix());
        if (getClass(className) != null) {
            return className;
        } else {
            if (BaseEntity.class.isAssignableFrom(entityClass)) {
                return ListView.class.getName();
            }
        }
        return null;
    }

    /**
     * Возвращает полное имя класса страницы проспотра для указанной сущности по формуле
     * Symbol("orion.root-package")+"."+Symbol("orion.web-package")+".pages."+
     * +schema+"."+entityClassName+".View"+entityClassName
     * Если такого класса нет, а entityClass наследует {@link BaseEntity} то возвращает {@link View}
     * @param entityClass a {@link Class}. It cannot be null.
     * @return a {@link String}.
     */
    @Override
    public String getViewPageClassName(Class<?> entityClass) {
        String className = getPageClassName(entityClass, getViewPagePrefix());
        if (getClass(className) != null) {
            return className;
        } else {
            if (BaseEntity.class.isAssignableFrom(entityClass)) {
                return ListView.class.getName();
            }
        }
        return null;
    }

    /**
     * Returns the name of the package where the Tapestry-related packages are located (i.e. under
     * which the <code>pages</code> component is located). This implementation returns
     * <code>[rootPackage].tapestry</code>.
     *
     * @return a {@link String}.
     */
    @Override
    public String getTapestryPackage() {
        return module.getRootPackage() + "." + symbolSource.valueForSymbol("orion.web-package");
    }

    /**
     * Возвращает имя класса страницы с указанным префиксом.
     * Например root.web.pages.sec.ListUser
     * где root.web - пакет tapestry
     *     pages    - константа
     *     sec      - схема
     *     User     - имя редактируемой сущности
     *     List     - префикс
     * @param entityClass a {@link Class} instance.
     * @param prefix a {@link String}.
     * @return a {@link String}.
     */
    protected String getPageClassName(Class<?> entityClass, String prefix) {
        return String.format("%s.pages.%s.%s%s", getTapestryPackage(),
                BaseEntity.getSchema(entityClass).toLowerCase(),
                prefix, entityClass.getSimpleName());
    }
}
