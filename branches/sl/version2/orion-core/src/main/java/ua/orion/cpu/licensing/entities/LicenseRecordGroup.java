package ua.orion.cpu.licensing.entities;

import javax.persistence.*;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 *
 * @author kgp
 */
@Entity
@Table(schema = "ref")
@AttributeOverrides({
    @AttributeOverride(name = "name", column =
    @Column(unique = true)),
    @AttributeOverride(name = "shortName", column =
    @Column(unique = true))})
public class LicenseRecordGroup extends AbstractReferenceEntity<LicenseRecordGroup> {

    private static final long serialVersionUID = 1L;
    /**
     * Підготовка бакалаврів, спеціалістів. магістрів
     */
    public static final String B_S_M_TRAINING_UKEY = "BACH_SPEC_MAG_PREPARE";
    /**
     * Перепідготовка спеціалістів
     */
    public static final String SPEC_RETRAINING_UKEY = "SPEC_RETRAINING";
    /**
     * Для колледжу "Класичного приватного університету"
     */
    public static final String FOR_COLLEDGE_UKEY = "FOR_CPU_COLLEDGE";
    /**
     * Підготовка бакалаврів
     */
    public static final String BACH_TRAINING_UKEY = "BACH_TRAINING";
    /**
     * Підготовка молодших спеціалістів
     */
    public static final String JUN_SPEC_TRAINING_UKEY = "JUNIOR_SPEC_TRAINING";

    public LicenseRecordGroup() {
    }

    public LicenseRecordGroup(String name, String uKey) {
        setName(name);
        setUKey(uKey);
    }
}
