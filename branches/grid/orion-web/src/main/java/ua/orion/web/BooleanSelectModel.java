/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 *
 * @author slobodyanuk
 */
public class BooleanSelectModel extends AbstractSelectModel {

    private final String trueString;
    private final String falseString;

    public BooleanSelectModel(String trueString, String falseString) {
        this.trueString = trueString;
        this.falseString = falseString;
    }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> optionModelList = new ArrayList<OptionModel>();
        optionModelList.add(new AbstractOptionModel() {

            @Override
            public String getLabel() {
                return trueString;
            }

            @Override
            public Object getValue() {
                return true;
            }
        });
        optionModelList.add(new AbstractOptionModel() {

            @Override
            public String getLabel() {
                return falseString;
            }

            @Override
            public Object getValue() {
                return false;
            }
        });
        return optionModelList;
    }
}
