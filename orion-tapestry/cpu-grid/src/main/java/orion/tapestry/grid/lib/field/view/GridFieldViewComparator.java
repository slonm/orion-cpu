package orion.tapestry.grid.lib.field.view;

import orion.tapestry.grid.lib.field.view.GridFieldView;
import java.util.Comparator;

/**
 * Класс используется для упорядочения колонок
 * @author Gennadiy Dobrovolsky 
 */
public class GridFieldViewComparator implements Comparator<GridFieldView>{
        @Override
        public int compare(GridFieldView o1, GridFieldView o2) {
            return (o1.getOrdering()-o2.getOrdering());
        }
    }
