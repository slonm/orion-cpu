package orion.tapestry.grid.lib.model;

import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;

/**
 * Класс для назначения подписей на заданном языке.
 * Модифицирует все текстовые метки в модели {@link GridModelInterface}
 * @author dobro
 */
public class GridModelLocalizator {

    public GridModelInterface localize(GridModelInterface gridModel){
        // set field labels
        for(GridFieldAbstract fld:gridModel.getFields()){
            fld.setLabel(this.getLabel(fld.getLabel()));
        }

        // set sort form element labels
        for(GridFieldSort gfs:gridModel.getFieldSortList()){
            gfs.setLabel(this.getLabel(gfs.getLabel()));
        }

        // set view form element labels
        for(GridFieldView gfv:gridModel.getFieldViewList()){
            gfv.setLabel(this.getLabel(gfv.getLabel()));
        }

        //gridModel.setFilterElementList(gridModel.getFilterElementList());
        for(FilterElementAbstract fe:gridModel.getFilterElementList()){
            String[] uid=fe.getLabel().split(" +");
            String label=this.getLabel(uid[0]);
            //System.out.println(uid[0] +">>>> "+label+fe.getLabel().substring(uid[0].length()));
            //System.out.println(uid[0] +">>"+uid[1]+ " >> "+this.getLabel(uid[0]));
            fe.setLabel(label+fe.getLabel().substring(uid[0].length()));
        }
        return gridModel;
    }

    protected String getLabel(String uid){
        return "msg:"+uid;
    }
}
