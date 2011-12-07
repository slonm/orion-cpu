package orion.tapestry.grid.lib.model.sort;

import java.util.Comparator;

/**
 * Класс используется для упорядочения колонок
 * @author Gennadiy Dobrovolsky 
 */
public class GridSortComparator implements Comparator<GridSortConstraint>{
        @Override
        public int compare(GridSortConstraint o1, GridSortConstraint o2) {
            int priority1=(o1!=null)?o1.getPriority():1000000;
            int priority2=(o2!=null)?o2.getPriority():1000000;
            return (priority1-priority2);
        }
    }
