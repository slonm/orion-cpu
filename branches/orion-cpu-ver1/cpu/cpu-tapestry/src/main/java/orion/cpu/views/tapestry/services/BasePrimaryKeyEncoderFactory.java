package orion.cpu.views.tapestry.services;

import java.io.Serializable;
import org.apache.tapestry5.PrimaryKeyEncoder;
import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.service.ControllerSource;
import br.com.arsmachina.tapestrycrud.factory.PrimaryKeyEncoderFactory;
import br.com.arsmachina.tapestrycrud.hibernate.encoder.HibernatePrimaryKeyEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.views.tapestry.encoders.primarykey.BasePrimaryKeyEncoder;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link PrimaryKeyEncoderFactory} implementation using Hibernate.
 *
 * @see HibernatePrimaryKeyEncoder
 * @author sl
 */
public class BasePrimaryKeyEncoderFactory implements PrimaryKeyEncoderFactory {
    final private Logger LOG = LoggerFactory.getLogger(BasePrimaryKeyEncoderFactory.class);
    final private ControllerSource controllerSource;

    /**
     * Single constructor of this class.
     *
     * @param controllerSource a {@link ControllerSource}. It cannot be null.
     */
    public BasePrimaryKeyEncoderFactory(ControllerSource controllerSource) {
        this.controllerSource = Defense.notNull(controllerSource, "controllerSource");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, K extends Serializable> PrimaryKeyEncoder<K, T> build(Class<T> entityClass) {
        PrimaryKeyEncoder<K, T> encoder = null;
        LOG.debug("Check then entityClass ({}) extends BaseEntity", entityClass.getSimpleName());
        if (BaseEntity.class.isAssignableFrom(entityClass)) {
            LOG.debug("{} realy extends BaseEntity", entityClass.getSimpleName());
            Controller<T, ?> controller = controllerSource.get(entityClass);
            if (controller != null) {
                encoder = new BasePrimaryKeyEncoder(controller);
            }
        }
        return encoder;
    }
}
