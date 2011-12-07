package orion.tapestry.grid.lib.model.view;

import java.util.Comparator;

/**
 * Класс используется для упорядочения колонок
 * @author Gennadiy Dobrovolsky 
 */
public class GridPropertyViewComparator implements Comparator<GridPropertyView>{
        @Override
        public int compare(GridPropertyView o1, GridPropertyView o2) {
            int ord1=(o1!=null)?o1.getOrdering():0;
            int ord2=(o2!=null)?o2.getOrdering():0;
            return (ord1-ord2);
        }
    }
