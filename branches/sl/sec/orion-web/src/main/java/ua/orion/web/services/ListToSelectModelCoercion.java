/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.services;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.util.AbstractSelectModel;
import ua.orion.core.services.StringValueProvider;

/**
 *
 * @author slobodyanuk
 */
public class ListToSelectModelCoercion implements Coercion<List, SelectModel> {

    private final StringValueProvider svp;

    public ListToSelectModelCoercion(StringValueProvider svp) {
        this.svp = svp;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SelectModel coerce(final List input) {
        return new AbstractSelectModel() {

            @Override
            public List<OptionGroupModel> getOptionGroups() {
                return null;
            }

            @Override
            public List<OptionModel> getOptions() {
                List<OptionModel> options = new ArrayList<>();
                for (final Object option : input) {
                    options.add(new AbstractOptionModel() {

                        @Override
                        public String getLabel() {
                            return svp.getStringValue(option);
                        }

                        @Override
                        public Object getValue() {
                            return option;
                        }
                    });
                }
                return options;
            }
        };
    }
}
