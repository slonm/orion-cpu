/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.cpu.entities.grid;

import javax.persistence.*;

/**
 *
 * @author dobro
 */
@Entity
@Table(schema = "sys")
public class GridSetting {
    /**
     * Saved setting set identifier
     */
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Saved setting set owner
     */
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    /**
     * Saved setting set label
     */
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String _label) {
        this.label = _label;
    }

    /**
     * Saved setting set encoded as JSON string
     */
    private String settingJSON;

    public String getSettingJSON() {
        return settingJSON;
    }

    public void setSettingJSON(String _settingJSON) {
        this.settingJSON = _settingJSON;
    }
}
