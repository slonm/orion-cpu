package orion.tapestry.grid.lib.model.property.impl;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterEQ;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterISNOTNULL;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterISNULL;
import orion.tapestry.grid.lib.model.property.GridPropertyModelAdapter;

/**
 *
 * @author dobro
 */
public class GridPropertyModelBoolean extends GridPropertyModelAdapter{
    public GridPropertyModelBoolean(
            GridBeanModel _gridBeanModel,
            String _propertyName,
            PropertyConduit _propertyConduit,
            Messages messages){
        super(_gridBeanModel, _propertyName, _propertyConduit,messages);
        this.gridFilterList.add(new GridFilterEQ(this));
        this.gridFilterList.add(new GridFilterISNOTNULL(this));
        this.gridFilterList.add(new GridFilterISNULL(this));
    }

}
