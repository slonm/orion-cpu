package orion.tapestry.grid.lib.model.property.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.GridFilterAbstract;
import orion.tapestry.grid.lib.model.filter.GridFilterDataType;
import orion.tapestry.grid.lib.model.filter.GridFilterGUIType;
import orion.tapestry.grid.lib.model.filter.impl.*;
import orion.tapestry.grid.lib.model.property.GridPropertyModelAdapter;

/**
 *
 * @author dobro
 */
public class GridPropertyModelDate extends GridPropertyModelAdapter {

    //    /**
    //     * Форматы дат, традиционные для разных стран
    //     */
    //    public static final SimpleDateFormat[] sdf = {
    //        // new SimpleDateFormat("dd.MM.yyyy"), //4 Финляндия, Чехия, Болгария, Германия, Норвегия, Румыния, Россия Турция
    //        // new SimpleDateFormat("d.M.yyyy"),  //6  Финляндия, Чехия
    //        new SimpleDateFormat("d.M.y"), //1
    //        new SimpleDateFormat("d.M.y H:m:s"), //2
    //        new SimpleDateFormat("d.M.y H:m"), //3
    //        new SimpleDateFormat("d.M.y K:m a"), //4
    //        new SimpleDateFormat("d.M.y K:m:s a"),//5
    //
    //        //new SimpleDateFormat("yyyy.MM.dd"),//1 Венгрия
    //        new SimpleDateFormat("y.M.d"), //1
    //        new SimpleDateFormat("y.M.d H:m:s"), //2
    //        new SimpleDateFormat("y.M.d H:m"), //3
    //        new SimpleDateFormat("y.M.d K:m a"), //4
    //        new SimpleDateFormat("y.M.d K:m:s a"),//5
    //
    //        // new SimpleDateFormat("yyyy-MM-dd"),//2  Польша, Швеция, Литва, Китай
    //        // new SimpleDateFormat("yyyy-M-d"), //4  Китай
    //        new SimpleDateFormat("y-M-d"), //6
    //        new SimpleDateFormat("y-M-d H:m:s"), //7
    //        new SimpleDateFormat("y-M-d H:m"), //8
    //        new SimpleDateFormat("y-M-d K:m a"), //9
    //        new SimpleDateFormat("y-M-d K:m:s a"),//10
    //
    //        // new SimpleDateFormat("MM/dd/yyyy"), //7 США, Канада
    //        // new SimpleDateFormat("M/d/yyyy"),   //10 США, Канада
    //        new SimpleDateFormat("MM/dd/yyyy"), //11
    //        new SimpleDateFormat("MM/dd/yyyy H:m:s"), //12
    //        new SimpleDateFormat("MM/dd/yyyy H:m"), //13
    //        new SimpleDateFormat("MM/dd/yyyy K:m a"), //14
    //        new SimpleDateFormat("MM/dd/yyyy K:m:s a"), //15
    //
    //        // new SimpleDateFormat("d/M/yyyy"),  //7 Бразилия, Греция, Таиланд
    //        // new SimpleDateFormat("dd/MM/yyyy"), //6 Бразилия, Греция, Таиланд, Великобритания, Вьетнам, Израиль, Индонезия, Испания, Италия, Франция
    //        new SimpleDateFormat("d/M/yyyy"), //11
    //        new SimpleDateFormat("d/M/yyyy H:m:s"), //12
    //        new SimpleDateFormat("d/M/yyyy H:m"), //13
    //        new SimpleDateFormat("d/M/yyyy K:m a"), //14
    //        new SimpleDateFormat("d/M/yyyy K:m:s a"), //15
    //
    //        // new SimpleDateFormat("dd-MM-yyyy"), //5 Нидерланды, Дания, Португалия
    //        // new SimpleDateFormat("d-M-yyyy"),  //6 Нидерланды
    //        // new SimpleDateFormat("dd-MMM-yy"), //8
    //        // new SimpleDateFormat("dd-MM-yy") //9
    //        new SimpleDateFormat("d-M-yyyy"), //6
    //        new SimpleDateFormat("d-M-yyyy H:m:s"), //7
    //        new SimpleDateFormat("d-M-yyyy H:m"), //8
    //        new SimpleDateFormat("d-M-yyyy K:m a"), //9
    //        new SimpleDateFormat("d-M-yyyy K:m:s a"),//10
    //
    //        // new SimpleDateFormat("yyyy/MM/dd"), //3  Иран, Япония, Гонконг, Тайвань
    //        // new SimpleDateFormat("yyyy/M/d"), //5  Гонконг, Тайвань        
    //        new SimpleDateFormat("y/M/d"), //11
    //        new SimpleDateFormat("y/M/d H:m:s"), //12
    //        new SimpleDateFormat("y/M/d H:m"), //13
    //        new SimpleDateFormat("y/M/d K:m a"), //14
    //        new SimpleDateFormat("y/M/d K:m:s a") //15
    //    };

    public GridPropertyModelDate(
            GridBeanModel _gridBeanModel,
            String _propertyName,
            PropertyConduit _propertyConduit,
            Messages messages) {
        super(_gridBeanModel, _propertyName, _propertyConduit, messages);

        GridFilterDataType datatype = new GridFilterDataTypeDate();
        GridFilterAbstract filterElement;

        filterElement = new GridFilterEQ(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterGE(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterGT(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterLE(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterLT(this);
        filterElement.setGUIType(GridFilterGUIType.DATE);
        filterElement.setDataType(datatype);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterISNOTNULL(this);
        this.gridFilterList.add(filterElement);

        filterElement = new GridFilterISNULL(this);
        this.gridFilterList.add(filterElement);
    }

    //    @Override
    //    public Object fromString(String value) {
    //        Date date = null;
    //        String val = value.toString();
    //        for (Format formatter : sdf) {
    //            try {
    //                date = (Date) formatter.parseObject(val);
    //                return date;
    //            } catch (java.text.ParseException exPars) {
    //            }
    //        }
    //        return date;
    //    }
}
