/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.cpu.entities.ref;

import javax.persistence.*;
import orion.cpu.baseentities.ReferenceEntity;

/**
 * Справочник названий груп лицензионных записей (их назначения)
 * @author kgp
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}),
    @UniqueConstraint(columnNames = {"shortName"})
})
public class LicenseRecordGroup extends ReferenceEntity<LicenseRecordGroup>{
    private static final long serialVersionUID = 1L;
    /**
     * Підготовка бакалаврів, спеціалістів. магістрів
     */
    public static final String BSMTRAINING_KEY = "BACHSPECMAGPREPARE";
    /**
     * Перепідготовка спеціалістів
     */
    public static final String SPECRETRAINING_KEY = "SPECRETRAINING";
    /**
     * Для колледжу "Класичного приватного університету"
     */
    public static final String FORCOLLEDGE_KEY = "FORCPUCOLLEDGE";
    /**
     * Підготовка бакалаврів
     */
    public static final String BACHTRAINING_KEY = "BACHTRAINING";
    /**
     * Підготовка молодших спеціалістів
     */
    public static final String JUNSPECTRAINING_KEY = "JUNIORSPECTRAINING";

    public LicenseRecordGroup() {
    }

    public LicenseRecordGroup(String name) {
        setName(name);
    }
    

}
