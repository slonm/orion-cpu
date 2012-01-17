package orion.tapestry.grid.lib.savedsettings;

/**
 *
 * @author dobro
 */
public class SampleGridSettingStoreRecord {
    Long id;
    String label;
    String setting;
    public SampleGridSettingStoreRecord(Long _id, String _label, String _setting){
        this.id=_id;
        this.label=_label;
        this.setting=_setting;
    }
}
