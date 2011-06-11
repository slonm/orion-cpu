package orion.tapestry.grid.lib.rows.impl;

import java.util.HashMap;
import java.util.Map;
import orion.tapestry.grid.lib.rows.GridRow;

/**
 * Простой контейнер строки
 * Строка хранится как коллекция объектов Map&lt;String, Object&gt; data
 * @author Gennadiy Dobrovolsky
 */
public class GridRowMap implements GridRow {

    private Map<String, Object> data;

    public GridRowMap() {
    }

    public GridRowMap(Map<String, Object> rowData) {
        data = new HashMap<String, Object>();
        data.putAll(rowData);
    }
    public GridRowMap(Object[][] rowData) {
        int i, i_cnt;
        i_cnt = rowData.length;
        data = new HashMap<String, Object>();
        for (i = 0; i < i_cnt; i++) {
            data.put(rowData[i][0].toString(), rowData[i][1]);
        }
    }
    @Override
    public Object getValue(String uid) {
        return data.get(uid);
    }

    @Override
    public void setValue(String uid, Object value) {
        data.put(uid, value);
    }

    @Override
    public GridRow _setValue(String uid, Object value) {
        this.setValue(uid, value);
        return this;
    }
}
