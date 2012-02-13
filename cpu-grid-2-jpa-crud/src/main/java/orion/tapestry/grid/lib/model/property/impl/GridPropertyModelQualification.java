package orion.tapestry.grid.lib.model.property.impl;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterCONTAINS;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterEQ;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterGE;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterGT;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterIN;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterISNOTNULL;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterISNULL;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterLE;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterLIKE;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterLT;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterNEQ;
import orion.tapestry.grid.lib.model.property.GridPropertyModelAdapter;

/**
 *
 * @author dobro
 */
public class GridPropertyModelQualification extends GridPropertyModelAdapter {

    public GridPropertyModelQualification(
            GridBeanModel _gridBeanModel,
            String _propertyName,
            PropertyConduit _propertyConduit,
            Messages messages) {
        super(_gridBeanModel, _propertyName, _propertyConduit, messages);

        String fieldName = this.propertyName + ".name";
        this.gridFilterList.add(new GridFilterCONTAINS(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterEQ(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterGE(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterGT(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterIN(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterISNOTNULL(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterISNULL(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterLE(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterLIKE(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterLT(this, this.getId(), fieldName));
        this.gridFilterList.add(new GridFilterNEQ(this, this.getId(), fieldName));

        //for (GridFilterAbstract fe : this.gridFilterList) {
        //    fe.setDataType(null);
        //}
    }
    //    @Override
    //    public Object fromString(String value) {
    //        return value;
    //    }
}
