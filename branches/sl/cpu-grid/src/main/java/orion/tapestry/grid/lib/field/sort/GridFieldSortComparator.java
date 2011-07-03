package orion.tapestry.grid.lib.field.sort;

import java.util.Comparator;

/**
 * Класс используется для упорядочения колонок
 * @author Gennadiy Dobrovolsky 
 */
public class GridFieldSortComparator implements Comparator<GridFieldSort>{
        @Override
        public int compare(GridFieldSort o1, GridFieldSort o2) {
            return (o1.getOrdering()-o2.getOrdering());
        }
    }
