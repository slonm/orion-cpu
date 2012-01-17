package orion.tapestry.grid.lib.model.property.impl;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.GridFilterAbstract;
import orion.tapestry.grid.lib.model.filter.GridFilterDataType;
import orion.tapestry.grid.lib.model.filter.GridFilterGUIType;
import orion.tapestry.grid.lib.model.filter.impl.*;
import orion.tapestry.grid.lib.model.property.GridPropertyModelAdapter;

/**
 *
 * @author dobro
 */
public class GridPropertyModelDate extends GridPropertyModelAdapter {

    public GridPropertyModelDate(
            GridBeanModel _gridBeanModel,
            String _propertyName,
            PropertyConduit _propertyConduit,
            Messages messages) {
        super(_gridBeanModel, _propertyName, _propertyConduit, messages);

        GridFilterDataType datatype = new GridFilterDataTypeDate();
        GridFilterAbstract filterElement;

        filterElement = new GridFilterEQ(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterGE(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterGT(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterLE(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterLT(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterISNOTNULL(this);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterISNULL(this);
        this.gridFilterList.add(filterElement);
    }
}
