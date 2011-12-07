package orion.tapestry.grid.lib.model.property.impl;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.GridFilterAbstract;
import orion.tapestry.grid.lib.model.filter.GridFilterDataType;
import orion.tapestry.grid.lib.model.filter.impl.*;
import orion.tapestry.grid.lib.model.property.GridPropertyModelAdapter;

/**
 *
 * @author dobro
 */
public class GridPropertyModelDouble extends GridPropertyModelAdapter{
    public GridPropertyModelDouble(
            GridBeanModel _gridBeanModel,
            String _propertyName,
            PropertyConduit _propertyConduit,
            Messages messages){
        super(_gridBeanModel, _propertyName, _propertyConduit,messages);
        this.gridFilterList.add(new GridFilterGE(this));
        this.gridFilterList.add(new GridFilterGT(this));
        this.gridFilterList.add(new GridFilterLE(this));
        this.gridFilterList.add(new GridFilterLT(this));
        this.gridFilterList.add(new GridFilterISNOTNULL(this));
        this.gridFilterList.add(new GridFilterISNULL(this));

        GridFilterDataType datatype = new GridFilterDataTypeDouble();
        for (GridFilterAbstract fe : this.gridFilterList) {
            fe.setDataType(datatype);
        }
    }

}
