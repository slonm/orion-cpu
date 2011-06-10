package orion.tapestry.grid.lib.field.impl;

import java.util.ArrayList;
import java.util.List;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.filter.impl.*;
import orion.tapestry.grid.lib.field.filter.validator.ValidatorRequireList;

/**
 * Класс, который описывает поле типа Byte
 * @author Gennadiy Dobrovolsky
 */
public class GridFieldNumberAbstractInt<B> extends GridFieldAbstract<B> {

    @Override
    public List<FilterElementAbstract> createFilterElementList() {
        ArrayList<FilterElementAbstract> list = new ArrayList<FilterElementAbstract>();
        list.add(new FilterElementEQ(this.getAttributeName(), this.getLabel() + " == "));
        list.add(new FilterElementGE(this.getAttributeName(), this.getLabel() + " >= "));
        list.add(new FilterElementGT(this.getAttributeName(), this.getLabel() + " > "));
        list.add(new FilterElementISNOTNULL(this.getAttributeName(), this.getLabel() + " is not null "));
        list.add(new FilterElementISNULL(this.getAttributeName(), this.getLabel() + " is null "));
        list.add(new FilterElementLE(this.getAttributeName(), this.getLabel() + " <= "));
        list.add(new FilterElementLT(this.getAttributeName(), this.getLabel() + " < "));
        list.add(new FilterElementNEQ(this.getAttributeName(), this.getLabel() + " != "));

        for (FilterElementAbstract fe : list) {
            fe.setValidator(this.getValidator());
        }



        return list;
    }
}
