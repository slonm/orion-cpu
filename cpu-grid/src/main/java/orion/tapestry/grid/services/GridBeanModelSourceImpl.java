package orion.tapestry.grid.services;

import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.services.ClassFactory;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ComponentLayer;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.apache.tapestry5.services.PropertyConduitSource;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.bean.GridBeanModelImpl;

/**
 *
 * @author dobro
 */
public class GridBeanModelSourceImpl implements GridBeanModelSource {

    private TypeCoercer typeCoercer;
    private PropertyAccess propertyAccess;
    private PropertyConduitSource propertyConduitSource;
    private ClassFactory classFactory;
    private DataTypeAnalyzer dataTypeAnalyzer;
    private ObjectLocator locator;
    private GridPropertyModelSource gridPropertyModelSource;

    public GridBeanModelSourceImpl(TypeCoercer typeCoercer,
            PropertyAccess propertyAccess,
            PropertyConduitSource propertyConduitSource,
            @ComponentLayer ClassFactory classFactory,
            @Primary DataTypeAnalyzer dataTypeAnalyzer,
            ObjectLocator locator,
            GridPropertyModelSource gridPropertyModelSource) {
        this.typeCoercer = typeCoercer;
        this.propertyAccess = propertyAccess;
        this.propertyConduitSource = propertyConduitSource;
        this.classFactory = classFactory;
        this.dataTypeAnalyzer = dataTypeAnalyzer;
        this.locator = locator;
        this.gridPropertyModelSource=gridPropertyModelSource;
    }

    @Override
    public <T> GridBeanModel<T> createDisplayModel(Class<T> beanClass, Messages messages) {
        return create(beanClass, false, messages);
    }

    public <T> GridBeanModel<T> createEditModel(Class<T> beanClass, Messages messages) {
        return create(beanClass, true, messages);
    }

    public <T> GridBeanModel<T> create(
            Class<T> beanClass,
            boolean filterReadOnlyProperties,
            Messages messages) {

        assert beanClass != null;
        assert messages != null;

        // создаём объект для доступа к атрибутам класса
        ClassPropertyAdapter adapter = propertyAccess.getAdapter(beanClass);

        // создаём модель, в которой пока нет полей
        GridBeanModel<T> model = new GridBeanModelImpl<T>(
                beanClass,
                this.propertyConduitSource,
                this.typeCoercer,
                messages,
                this.locator,
                this.gridPropertyModelSource);

        // для каждого атрибута
        for (final String propertyName : adapter.getPropertyNames()) {
            // создаём объект для доступа к свойствам атрибута
            PropertyAdapter pa = adapter.getPropertyAdapter(propertyName);

            // пропускаем атрибуты, которые нельзя читать
            if (!pa.isRead()) {
                continue;
            }

            // пропускаем атрибуты, которые помечены аннотацией @NonVisual
            if (pa.getAnnotation(NonVisual.class) != null) {
                continue;
            }

            // если строится модель для редактирования, но атрибут нельзя изменять
            // то атрибут пропускаем
            if (filterReadOnlyProperties && !pa.isUpdate()) {
                continue;
            }

            // получаем название блока,
            // который используется для отображения атрибута
            final String dataType = dataTypeAnalyzer.identifyDataType(pa);

            // If an unregistered type, then ignore the property.
            if (dataType == null) {
                continue;
            }

            // добавляем опиание свойства в модель
            model.add(propertyName).dataType(dataType);
        }

//        // First, order the properties based on the location of the getter method
//        // within the class.
//
//        List<String> propertyNames = model.getPropertyNames();
//
//        orderProperties(adapter, propertyNames);
//
//        model.reorder(propertyNames.toArray(new String[propertyNames.size()]));
//
//        // Next, check for an annotation with specific ordering information.
//
//        ReorderProperties reorderAnnotation = beanClass.getAnnotation(ReorderProperties.class);
//
//        if (reorderAnnotation != null) {
//            BeanModelUtils.reorder(model, reorderAnnotation.value());
//        }

        return model;
    }
}
