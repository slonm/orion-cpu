package orion.cpu.views.tapestry.encoders.primarykey;

import java.util.List;

import org.apache.tapestry5.PrimaryKeyEncoder;

import br.com.arsmachina.controller.Controller;
import orion.cpu.baseentities.BaseEntity;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Partial {@link PrimaryKeyEncoder} implementation using {@link Controller}.
 *
 * @param <T> the entity class related to this controller.
 *
 * @author sl
 */
public class BasePrimaryKeyEncoder<T extends BaseEntity<?>> implements
        PrimaryKeyEncoder<Integer, T> {

    final private Controller<T, Integer> controller;

    /**
     * Constructor meant to be used when instantiating this class directly.
     * @param controller a {@link Controller}. It cannot be null.
     */
    public BasePrimaryKeyEncoder(Controller<T, Integer> controller) {
        this.controller = Defense.notNull(controller, "controller");
    }

    /**
     * @see org.apache.tapestry.PrimaryKeyEncoder#toKey(java.lang.Object)
     */
    @Override
    public Integer toKey(T value) {
        return value.getId();
    }

    /**
     * Invokes <code>controller.findByIds((K[]) keys.toArray());</code>.
     *
     * @param keys
     * @see org.apache.tapestry.PrimaryKeyEncoder#prepareForKeys(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void prepareForKeys(List<Integer> keys) {
        controller.findByIds((Integer[]) keys.toArray());
    }

    /**
     * Returns <code>controller.findById(key)</code>. {@inheritDoc}
     *
     * @see org.apache.tapestry.PrimaryKeyEncoder#toValue(java.io.Serializable)
     */
    @Override
    public T toValue(Integer key) {
        T value = null;
        if (key != null) {
            value = controller.findById(key);
        }
        return value;
    }

    @Override
    public Class<Integer> getKeyType() {
        return Integer.class;
    }
}
