package orion.tapestry.pages;

import br.com.arsmachina.authentication.entity.Role;
import javax.naming.event.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.*;
import orion.tapestry.components.BooleanSelectField;
import br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.corelib.components.PasswordField;

/**
 * <p>
 * A page that holds the editing and viewing blocks provided by Orion Tapestry for
 * {@link BeanEditor}, {@link BeanEditForm}, and {@link Grid}.
 * </p>
 * @author sl
 */
@SuppressWarnings("unused")
public class PropertyBlocks {

    /**
     * Иньектируется фабрика для получения SelectModel, необходимого
     * в качестве параметра для компонентов (Palette)
     */
    @Inject
    private SelectModelFactory selectModelFactory;
    /**
     * Иньектируется valueEncoderSource для получения конвертера серверных данных
     * в данные для клиента - строки.
     */
    @Inject
    private ValueEncoderSource valueEncoderSource;
    /**
     * Контекст для редактирования свойств объекта.
     * {@link org.apache.tapestry5.corelib.components.BeanEditor}
     */
    @Environmental
    @Property(write = false)
    private PropertyEditContext editContext;
    /**
     * Контекст, необходимый для отображения значения.
     * {@link Grid}
     */
    @Environmental
    @Property(write = false)
    private PropertyOutputContext outputContext;
    @Component(parameters = {"value=editContext.propertyValue",
        "trueLabel=message:true",
        "falseLabel=message:false",
        "validate=prop:booleanSelectFieldValidator"
    })
    private BooleanSelectField booleanSelectField;

    public FieldValidator<?> getBooleanSelectFieldValidator() {
        return editContext.getValidator(booleanSelectField);
    }
    /**
     * Присваивание ролей пользователям должно осуществляться с помощью
     * компонента Palette. Параметры: model возвращает метод getRoleSM(),
     * encoder - getRoleVE(), данные, которые передаются при открытии beaneditor
     * и возвращаются при нажатии на кнопку сохранения хранятся в selected. Для
     * него организованы специализированные get и set.
     */
    @Component(parameters = {"label=prop:editContext.label",
        "selectedLabel=message:selected-options",
        "availableLabel=message:available-options",
        "clientId=prop:editContext.propertyId",
        "encoder=roleVE",
        "model=roleSM",
        "selected=propertyValues"})
    private Palette userRoleField;
    /**
     * Пароль должен быть скрытым при вводе, поэтому используется компонент
     * PasswordField, в качестве значения ему подается значение поля в
     * editcontext, в качестве транслятора - тип данных String.
     */
    @Component(parameters = {"label=prop:editContext.label",
        "value=editContext.propertyValue",
        "translate=String"
    })
    private PasswordField passwordField;

    /**
     * Метод, который возвращает список ролей пользователя для заполнения правой
     * колонки Palette. Так как editContext.propertyValue возвращает в данном
     * случае Set, а selected принимает List, то необходимо осуществить
     * пребразование, что и организовано в данном методе.
     * @return список ролей выбранного пользователя.
     */
    public List<Role> getPropertyValues() {
        return new ArrayList((Set) editContext.getPropertyValue());
    }

    /**
     * Метод обеспечивающий сохранение выбранных ролей в объект.
     * @param propertyValue - список с выбранными ролями.
     */
    public void setPropertyValues(List<Role> propertyValue) {
        Set<Role> set = (Set<Role>) editContext.getPropertyValue();
        set.clear();
        set.addAll(propertyValue);
    }

//******************************************************************
// Попытки решить проблему с заполнением поля с паролем при загрузке
//******************************************************************
//    public FieldTranslator getFieldTranslator(){
//        return editContext.getTranslator(passwordField);
//    }
//
//    public String getPassword() {
//        return (String) editContext.getPropertyValue();
//    }
//
//    public void setPassword(String password) {
//        String value = editContext.getPropertyValue().toString();
//        value = password;
//    }
//******************************************************************

    /**
     * Получение SelectModel для компонента Palette с идентификатором userRoleField
     * @return SelectModel
     */
    public SelectModel getRoleSM() {
        return selectModelFactory.create(Role.class);
    }

    /**
     * Получение ValueEncoder для класса Role
     * @return ValueEncoder<Role>
     */
    @Cached
    public ValueEncoder<Role> getRoleVE() {
        return valueEncoderSource.getValueEncoder(Role.class);
    }

    /**
     * Запрещает явное открытие этой псевдо-страницы.
     * @param ec Page activation context
     * @return the empty string
     * @author sl
     */
    public String onActivate(EventContext ec) {
        return "";
    }
}
