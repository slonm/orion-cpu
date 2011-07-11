package orion.tapestry.grid.lib.savedsettings;

import java.util.HashMap;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dobro
 */
public class SampleGridSettingStore implements IGridSettingStore {

    private static final Logger log = LoggerFactory.getLogger(SampleGridSettingStore.class);
    // construct sample store
    HashMap<Long, SampleGridSettingStoreRecord> sampleStore = new HashMap<Long, SampleGridSettingStoreRecord>();

    @Override
    public String getSavedSetting(Long id) {
        return this.sampleStore.get(id).setting;
    }

    @Override
    public String getSavedSettingListJSON() {
        StringBuilder s = new StringBuilder();
        String delim = "";
        s.append("[");
        for (SampleGridSettingStoreRecord rec : this.sampleStore.values()) {
            JSONObject recJSON = new JSONObject();
            recJSON.append("id", rec.id);
            recJSON.append("label", rec.label);
            s.append(delim);
            s.append(recJSON.toString());
            delim = ",";

        }
        s.append("]");
        System.out.println("getSavedSettingListJSON:" + s.toString());
        return s.toString();
    }

    @Override
    public boolean saveSetting(String label, String setting) {
        System.out.println("Save setting:" + label + " >>>>>>>> " + setting);
        Long id = null;
        for (Long k : sampleStore.keySet()) {
            if (id == null || id < k) {
                id = k;
            }
        }
        if (id == null) {
            id = 1L;
        } else {
            id = id + 1;
        }
        sampleStore.put(id, new SampleGridSettingStoreRecord(id, label, setting));
        return true;
    }

    @Override
    public boolean deleteSavedSetting(Long id) {
        sampleStore.remove(id);
        return true;
    }
}
