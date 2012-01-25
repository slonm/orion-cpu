package orion.tapestry.grid.lib.model.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.beaneditor.RelativePosition;
import org.apache.tapestry5.internal.services.CoercingPropertyConduitWrapper;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.ioc.util.AvailableValues;
import org.apache.tapestry5.ioc.util.UnknownValueException;
import org.apache.tapestry5.services.PropertyConduitSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.grid.lib.model.filter.GridFilterModel;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.model.sort.GridSortModelImpl;
import orion.tapestry.grid.lib.model.view.GridPropertyView;
import orion.tapestry.grid.lib.model.view.GridPropertyViewModel;
import orion.tapestry.grid.services.GridPropertyModelSource;

/**
 *
 * @author dobro
 */
public class GridBeanModelImpl<T> implements GridBeanModel<T> {

    private Logger logger = LoggerFactory.getLogger(GridBeanModelImpl.class);
    /**
     * Класс, на основе которого построена модель
     */
    private Class<T> beanType;
    /**
     * Источник объектов, ответственных за чтение/запись свойств отдельных атрибутов
     */
    private PropertyConduitSource propertyConduitSource;
    /**
     * Использует {@link org.apache.tapestry5.ioc.services.Coercion}s чтобы
     * превратить входные значения некоторого типа в выходные значения
     * другого типа. Может выполнять преобразование типов в несколько этапов.
     * Например, через промежуточное строковое значение.
     */
    private TypeCoercer typeCoercer;
    /**
     * сообщения на заданном языке
     */
    private Messages messages;
    /**
     * Создатель объектов
     */
    private ObjectLocator locator;
    /**
     * список атрибутов
     */
    private Map<String, GridPropertyModelInterface> properties = CollectionFactory.newCaseInsensitiveMap();
    private GridPropertyViewModel gridPropertyViewModel;
    private GridSortModelImpl gridSortModelImpl;
    private GridFilterModel gridFilterModel;
    private final GridPropertyModelSource gridPropertyModelSource;
    private static PropertyConduit NULL_PROPERTY_CONDUIT = null;

    public GridBeanModelImpl(Class<T> beanType,
            PropertyConduitSource propertyConduitSource,
            TypeCoercer typeCoercer,
            Messages messages,
            ObjectLocator locator,
            GridPropertyModelSource gridPropertyModelSource) {
        this.beanType = beanType;
        this.propertyConduitSource = propertyConduitSource;
        this.typeCoercer = typeCoercer;
        this.messages = messages;
        this.locator = locator;
        this.gridPropertyModelSource = gridPropertyModelSource;
    }

    @Override
    public Class<T> getBeanType() {
        return beanType;
    }

    @Override
    public T newInstance() {
        return locator.autobuild("Instantiating new instance of " + beanType.getName(), beanType);
    }

    @Override
    public List<String> getPropertyNames() {
        List<String> propertyNames = new ArrayList<String>();
        for (GridPropertyModelInterface model : this.properties.values()) {
            if (model.getGridPropertyView() != null) {
                propertyNames.add(model.getPropertyName());
            }
        }
        return propertyNames;
    }

    @Override
    public GridPropertyModelInterface get(String propertyName) {
        GridPropertyModelInterface propertyModel = properties.get(propertyName);

        if (propertyModel == null) {
            throw new UnknownValueException(String.format(
                    "Bean editor model for %s does not contain a property named '%s'.", beanType.getName(),
                    propertyName), new AvailableValues("Defined properties", this.properties.keySet()));
        }
        return propertyModel;
    }

    @Override
    public GridPropertyModelInterface getById(String propertyId) {
        for (GridPropertyModelInterface model : properties.values()) {
            if (model.getId().equalsIgnoreCase(propertyId)) {
                return model;
            }
        }

        // Not found, so we throw an exception. A bit of work to set
        // up the exception however.

        List<String> ids = CollectionFactory.newList();

        for (PropertyModel model : properties.values()) {
            ids.add(model.getId());
        }

        throw new UnknownValueException(String.format(
                "Bean editor model for %s does not contain a property with id '%s'.", beanType.getName(), propertyId),
                new AvailableValues("Defined property ids", ids));
    }

    /**
     * Adds a new, synthetic property to the model, returning its mutable model for further refinement.
     *
     * @param propertyName name of property to add
     * @param conduit      the conduit used to read or update the property; this may be null for a synthetic or
     *                     placeholder property
     * @return the model for the property
     * @throws RuntimeException if the property already exists
     */
    @Override
    public GridPropertyModelInterface add(String propertyName, PropertyConduit conduit) {
        validateNewPropertyName(propertyName);

        GridPropertyModelInterface propertyModel = this.gridPropertyModelSource.getGridPropertyModel(this, propertyName, conduit, messages);

        // get maximal priority
        int maxPriority = 0;
        for (GridPropertyModelInterface pmd : this.properties.values()) {
            int priority = pmd.getGridPropertyView().getOrdering();
            if (priority > maxPriority) {
                maxPriority = priority;
            }
        }
        // set maximal priority to new poperty
        propertyModel.getGridPropertyView().setOrdering(maxPriority + 1);

        // add new property to the list
        properties.put(propertyName, propertyModel);

        return propertyModel;
    }

    /**
     * Adds a new property to the model, returning its mutable model for further refinement. The property is added to
     * the <em>end</em> of the list of properties. The property must be real (but may have been excluded if there was no
     * {@linkplain org.apache.tapestry5.beaneditor.DataType datatype} associated with the property). To add a synthetic
     * property, use {@link #add(String, org.apache.tapestry5.PropertyConduit)}
     *
     * @param propertyName name of property to add
     * @return the new property model (for further configuration)
     * @throws RuntimeException if the property already exists
     */
    @Override
    public GridPropertyModelInterface add(String propertyName) {
        PropertyConduit conduit = null;
        try {
            conduit = createConduit(propertyName);
        } catch (org.apache.tapestry5.internal.services.PropertyExpressionException ex) {
        }
        return add(propertyName, conduit);
    }

    /**
     * Adds a new property to the model (as with {@link #add(String)}), ordered before or after an existing property.
     *
     * @param position             controls whether the new property is ordered before or after the existing property
     * @param existingPropertyName the name of an existing property (this must exist)
     * @param propertyName         the new property to add
     * @return the new property model (for further configuration)
     * @throws RuntimeException if the existing property does not exist, or if the new property already does exist
     */
    @Override
    public GridPropertyModelInterface add(
            RelativePosition position,
            String existingPropertyName,
            String propertyName,
            PropertyConduit conduit) {
        assert position != null;
        validateNewPropertyName(propertyName);

        // Locate the existing one.
        GridPropertyModelInterface existing = get(existingPropertyName);

        // get position
        int viewPosition = existing.getGridPropertyView().getOrdering();

        // Use the case normalized property name.
        GridPropertyModelInterface propertyModel = this.gridPropertyModelSource.getGridPropertyModel(this, propertyName, conduit, messages);

        // calculate new property position
        int shiftAfter = 0;
        if (position == RelativePosition.AFTER) {
            propertyModel.getGridPropertyView().setOrdering(viewPosition + 1);
            shiftAfter = viewPosition + 1;
        } else {
            propertyModel.getGridPropertyView().setOrdering(viewPosition);
            shiftAfter = viewPosition;
        }

        // update other properties priority
        for (GridPropertyModelInterface pmd : this.properties.values()) {
            int pos = pmd.getGridPropertyView().getOrdering();
            if (pos >= shiftAfter) {
                pmd.getGridPropertyView().setOrdering(pos + 1);
            }
        }

        // add new property to the list
        properties.put(propertyName, propertyModel);

        return propertyModel;
    }

    @Override
    public GridPropertyModelInterface add(RelativePosition position, String existingPropertyName, String propertyName) {
        PropertyConduit conduit = createConduit(propertyName);
        return add(position, existingPropertyName, propertyName, conduit);
    }

    @Override
    public GridBeanModel<T> reorder(String... propertyNames) {
        int ordering = properties.size();
        for (GridPropertyModelInterface model : properties.values()) {
            GridPropertyView view = model.getGridPropertyView();
            if (view != null) {
                view.setOrdering(ordering++);
            }
        }
        ordering = 0;
        for (String name : propertyNames) {
            GridPropertyModelInterface model = this.get(name);
            if (model != null) {
                GridPropertyView view = model.getGridPropertyView();
                if (view != null) {
                    view.setOrdering(ordering++);
                }
            }
        }
        return this;
    }

    @Override
    public GridBeanModel<T> exclude(String... propertyNames) {
        for (String propertyName : propertyNames) {
            GridPropertyModelInterface model = properties.get(propertyName);
            if (model == null) {
                continue;
            }
            model.setGridPropertyView(null);
        }
        return this;
    }

    @Override
    public GridBeanModel<T> include(String... propertyNames) {
        List<String> includePropertyNames = Arrays.asList(propertyNames);
        // logger.info(includePropertyNames.toString() + " - set visible");
        int ordering = 0;
        for (GridPropertyModelInterface model : properties.values()) {
            if (includePropertyNames.contains(model.getPropertyName())) {
                GridPropertyView gpv = model.getGridPropertyView();
                if (gpv == null) {
                    model.setGridPropertyView(new GridPropertyView(model, ordering++));
                }
            } else {
                model.setGridPropertyView(null);
                // logger.info(model.getPropertyName() + " - set hidden");
            }
        }
        return this;
    }

    private CoercingPropertyConduitWrapper createConduit(String propertyName) {
        return new CoercingPropertyConduitWrapper(propertyConduitSource.create(beanType, propertyName), typeCoercer);
    }

    private void validateNewPropertyName(String propertyName) {
        if (propertyName == null || propertyName.length() == 0) {
            throw new RuntimeException(String.format("You cannot add empty property name to %s", beanType.getName()));
        }
        if (properties.containsKey(propertyName)) {
            throw new RuntimeException(String.format("Bean editor model for %s already contains a property model for property '%s'.", beanType.getName(), propertyName));
        }
    }

    @Override
    public GridPropertyViewModel getGridPropertyViewModel() {
        if (this.gridPropertyViewModel == null) {
            this.gridPropertyViewModel = new GridPropertyViewModel(this);
        }
        return this.gridPropertyViewModel;
    }

    @Override
    public GridSortModelImpl getGridSortModelImpl() {
        if (this.gridSortModelImpl == null) {
            this.gridSortModelImpl = new GridSortModelImpl(this);
        }
        return this.gridSortModelImpl;
    }

    @Override
    public GridFilterModel getGridFilterModel() {
        if (this.gridFilterModel == null) {
            this.gridFilterModel = new GridFilterModel(this);
        }
        return this.gridFilterModel;
    }

    /**
     * Adds a new synthetic property to the model, returning its mutable model for further refinement. The property is added to
     * the <em>end</em> of the list of properties.
     *
     * @param propertyName name of property to add
     * @param expression   expression for the property
     * @return the new property model (for further configuration)
     * @throws RuntimeException if the property already exists
     * @since 5.3
     */
    @Override
    public PropertyModel addExpression(String propertyName, String expression) {
        PropertyConduit conduit = createConduit(expression);
        return add(propertyName, conduit);
    }

    /**
     * Adds an empty property (one with no property conduit).
     *
     * @param propertyName name of property to add
     * @return the new property model (for further configuration)
     * @throws RuntimeException if the property already exists
     * @since 5.3
     */
    @Override
    public GridPropertyModelInterface addEmpty(String propertyName) {
        return add(propertyName, NULL_PROPERTY_CONDUIT);
    }

    @Override
    public void setMessage(Messages msg) {
        this.messages = msg;
        for(GridPropertyModelInterface gp :this.properties.values()){
            gp.setMessage(msg);
        }
    }
}
