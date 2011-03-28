package orion.tapestry.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.util.AbstractSelectModel;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * SelectModel для булева типа данных.
 * В конструкторе получает подписи всех значений списка
 * @author sl
 */
public class BooleanSelectModel extends AbstractSelectModel {

    private final String trueLabel;
    private final String falseLabel;

    /**
     * Конструктор
     * @param trueLabel подпись значения true
     * @param falseLabel подпись значения false
     */
    public BooleanSelectModel(String trueLabel, String falseLabel) {
        this.trueLabel = Defense.notNull(trueLabel, "trueLabel");
        this.falseLabel = Defense.notNull(falseLabel, "falseLabel");
    }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> optionModelList = new ArrayList<OptionModel>();
        optionModelList.add(new OptionModelImpl(trueLabel + "", true));
        optionModelList.add(new OptionModelImpl(falseLabel + "", false));

        return optionModelList;
    }
}
