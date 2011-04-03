package orion.tapestry.grid.lib.model;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Map;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.GridFieldFactory;
import orion.tapestry.grid.lib.field.filter.FilterAggregator;
import orion.tapestry.grid.lib.paging.Pager;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorHumanReadable;
import orion.tapestry.grid.lib.rows.GridRow;
import orion.tapestry.grid.lib.rows.impl.GridRowMap;
import orion.tapestry.grid.lib.field.sort.SortEditor;
import orion.tapestry.grid.lib.field.sort.SortEditorSample;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class GridModelBeanSample extends GridModelAdapter<String>{
    /**
     * Метод возвращает набор видимых строк
     */
    @Override
    public List<GridRow> getRows() {

        // Как будто бы используем условие фильтрации
        System.out.println("GridModelBeanSample:"+this.getHumanReadableFilterInfo());

        //  Как будто бы используем условие сортировки
        SortEditor<String> sortEditor = new SortEditorSample();
        sortEditor.createNew();
        for (GridFieldSort fs : this.fieldSortList) {
            sortEditor.addFieldSort(fs);
        }
        System.out.println("GridModelBeanSample:"+sortEditor.getValue());

        // Количество строк можно узнать только после связи с БД.
        // Надо устанавливать этот параметр в правильном месте
        // Как будто вычисляем общее количество найденных строк
        this.pager.setRowsFound(555);

        ArrayList<GridRow> rows = new ArrayList<GridRow>();
        int i, cnt;

        // Как бы используем номер страницы
        int shift;
        shift = this.pager.getVisiblePage().getFirstRow();

        cnt = this.getPager().getRowsPerPage();
        GridRow row;

        // извлекаем строки
        int i_field, n_fields=this.fieldList.size();
        GridFieldAbstract fld;
        Object[][] data = new Object[n_fields][2];
        for (i = 0; i < cnt; i++) {
            for(i_field=0;i_field<n_fields; i_field++){
                fld=this.fieldList.get(i_field);
                data[i_field][0]=fld.getUid();
                data[i_field][1]=getSampleValue(fld.getClass().cast(fld));
            }
            row = new GridRowMap(data);
            rows.add(row);
        }
        return rows;
    }

    /**
     * Конструктор составляет список полей таблицы
     * и задаёт их начальные свойства
     * @param forClass класс сущности, его атрибуты будут колонками в таблице
     * @param configuration конфигурация, соответствие между типами данных Java и свойствами колонки
     * @throws IntrospectionException возникает, если интроспекция не удалась
     */
    public GridModelBeanSample(
            Class forClass,
            Map<String, Class> configuration) throws IntrospectionException {


        // объявляем поля таблицы
        // поля надо объявлять обязательно, а то в таблице не будет колонок
           this.fieldList = GridFieldFactory.getFields( forClass,configuration);
        // =========== объявляем поля таблицы - конец ==========================

        /*
         * Создаём агрегатор фильтров -
         * объект, который хранит редактор фильтров
         * и умеет составлять из набора фильтров единое условие фильтрации
         *
         * агрегатор используется при извлечении строк из базы данных
         */
        this.filterAggregator=new FilterAggregator(this.getFilterElementList());

        /**
         * этот агрегатор используется для фильтрации строк в таблице
         */
        this.restrictionEditor=new RestrictionEditorHumanReadable();

        /**
         * этот агрегатор используется для отображения условия фильтрации
         */
        this.restrictionEditorHumanReadable=new RestrictionEditorHumanReadable();

        /*
         * Создаём объект,  содержащий информацию о разбивке списка на страницы
         */
        this.pager = new Pager();
        this.pager.setRowsPerPage(10);
        this.pager.setVisiblePage(1);
    }


    private Object getSampleValue(GridFieldAbstract fld){
        if(fld.getClass()==orion.tapestry.grid.lib.field.impl.GridFieldNumberLong.class){
            return new Integer(Math.round((float)Math.random()*1000));
        }
        if(fld.getClass()==orion.tapestry.grid.lib.field.impl.GridFieldNumberDouble.class){
            return new Double(Math.random()*1000);
        }
        if(fld.getClass()==orion.tapestry.grid.lib.field.impl.GridFieldNumberFloat.class){
            return new Float(Math.random()*1000);
        }
        if(fld.getClass()==orion.tapestry.grid.lib.field.impl.GridFieldDate.class){
            return new Date();
        }
        return Math.random()*1000 +"";
    }

    
}
