package orion.cpu.views.tapestry.encoders.activationcontext;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.service.ControllerSource;
import org.apache.tapestry5.EventContext;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.hibernate.HibernateEntityPackageManager;
import orion.cpu.baseentities.BaseEntity;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Реализация {@link ActivationContextEncoder}
 * Помещает в ActivationContext класс справочника и id записи
 *
 * @author sl
 */
public class BaseActivationContextEncoder implements ActivationContextEncoder<BaseEntity<?>> {

    final private ControllerSource controllerSource;
    final private HibernateEntityPackageManager hibernateEntityPackageManager;

    /**
     * Single constructor of this class.
     *
     * @param controllerSource
     * @param hibernateEntityPackageManager
     */
    public BaseActivationContextEncoder(ControllerSource controllerSource,
            HibernateEntityPackageManager hibernateEntityPackageManager) {
        this.controllerSource = Defense.notNull(controllerSource, "controllerSource");
        this.hibernateEntityPackageManager = Defense.notNull(hibernateEntityPackageManager, "hibernateEntityPackageManager");
    }

    @Override
    public Object toActivationContext(BaseEntity<?> object) {
        List<Object> value = null;
        if (object != null) {
            value = new ArrayList<Object>();
            value.add(BaseEntity.getFullClassName(object.getClass()));
            if (object.getId() != null) {
                value.add(object.getId());
            }
        }
        return value;
    }

    /**
     * @param eventContext
     * @return Объект. null, если AC будет содержать меньше одного параметра
     */
    @Override
    public BaseEntity<?> toObject(EventContext eventContext) {
        BaseEntity<?> referenceEntity = null;
        if (eventContext.getCount() >= 2) {
            Controller<?, Integer> controller;
            String entityName = eventContext.get(String.class, 0);
            //Исключение entity.entity
            if (entityName.indexOf("entity.User") > -1) {
                entityName = "User";
            }
            Class<?> entityClass = null;
            for (String pkg : hibernateEntityPackageManager.getPackageNames()) {
                try {
                    entityClass = Class.forName(pkg + "." + entityName);
                    controller = controllerSource.get(entityClass);
                    referenceEntity = (BaseEntity<?>) controller.findById(eventContext.get(Integer.class, 1));
                    break;
                } catch (ClassNotFoundException ex) {
                }
            }
        }
        return referenceEntity;
    }
}
