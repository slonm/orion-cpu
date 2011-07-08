
package orion.tapestry.grid.lib.savedsettings;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dobro
 */
public class SampleGridSettingStore implements IGridSettingStore {

    // construct sample store
    HashMap<Long, SampleGridSettingStoreRecord> sampleStore = new HashMap<Long, SampleGridSettingStoreRecord>();


    @Override
    public String getSavedSetting(Long id) {
        return this.sampleStore.get(id).setting;
    }

    @Override
    public String getSavedSettingListJSON() {
        StringBuilder s = new StringBuilder();
        String delim="";
        s.append("[");
        for (SampleGridSettingStoreRecord rec : this.sampleStore.values()) {
            JSONObject recJSON=new JSONObject();
            try {
                recJSON.append("id", rec.id);
                recJSON.append("label", rec.label);
                s.append(delim);
                s.append(recJSON.toString());
                delim=",";
            } catch (JSONException ex) {
                Logger.getLogger(SampleGridSettingStore.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        s.append("]");
        System.out.println("getSavedSettingListJSON:"+s.toString());
        return s.toString();
    }

    @Override
    public boolean saveSetting( String label, String setting) {
        System.out.println("Save setting:"+label + " >>>>>>>> "+ setting);
        Long id=null;
        for(Long k:sampleStore.keySet()){
            if(id==null || id<k){
                id=k;
            }
        }
        if(id==null){
            id=1L;
        }else{
            id=id+1;
        }
        sampleStore.put(id, new SampleGridSettingStoreRecord(id, label,setting ));
        return true;
    }

    @Override
    public boolean deleteSavedSetting(Long id) {
        sampleStore.remove(id);
        return true;
    }
}
