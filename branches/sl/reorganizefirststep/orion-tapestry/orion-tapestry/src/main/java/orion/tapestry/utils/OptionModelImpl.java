package orion.tapestry.utils;

import org.apache.tapestry5.AbstractOptionModel;

/**
 * Реализация OptionModel. Скопирована с внутреннего класса Tapestry
 * @author sl
 */
public class OptionModelImpl extends AbstractOptionModel {

    private final String label;
    private final Object value;

    /**
     * Constructor for when the value and the label are the same.
     * @param value значение
     */
    public OptionModelImpl(String value) {
        this(value, value);
    }

    public OptionModelImpl(String label, Object value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("OptionModel[%s %s]", label, value);
    }
}
