package orion.tapestry.grid.lib.field.sort;

import java.util.ArrayList;
import java.util.Collections;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.sort.GridFieldSortComparator;
import orion.tapestry.grid.lib.field.sort.SortEditor;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class SortEditorSample implements SortEditor<String> {

    ArrayList<GridFieldSort> fieldSortList;

    @Override
    public void createNew() {
        this.fieldSortList = new ArrayList<GridFieldSort>();
    }

    @Override
    public void addFieldSort(GridFieldSort fs) {
        this.fieldSortList.add(fs);
    }

    @Override
    public void delFieldSort(String uid) {
        int i, cnt;
        cnt = fieldSortList.size();
        for (i = 0; i < cnt; i++) {
            if (uid.equalsIgnoreCase(fieldSortList.get(i).getUid())) {
                fieldSortList.remove(i);
                return;
            }
        }
        return;
    }

    @Override
    public String getValue() {
        StringBuffer sb=new StringBuffer();
        String dec="";
        Collections.sort(fieldSortList, new GridFieldSortComparator());
        for(GridFieldSort fs:this.fieldSortList){
            switch(fs.getSortType()){
                case ASC :
                    sb.append(dec + fs.getAttributeName()+" ASC");
                    dec=", ";
                break;
                case DESC :
                    sb.append(dec + fs.getAttributeName()+" DESC");
                    dec=", ";
                break;
            }
        }
        return sb.toString();
    }
}
