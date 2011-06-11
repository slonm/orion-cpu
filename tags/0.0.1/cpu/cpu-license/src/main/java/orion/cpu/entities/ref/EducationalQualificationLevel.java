package orion.cpu.entities.ref;

import javax.persistence.*;
import org.apache.tapestry5.beaneditor.Validate;
import orion.cpu.baseentities.ReferenceEntity;

/**
 * Справочник образовательно-квалификационных уровней
 * @author kgp
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}),
    @UniqueConstraint(columnNames = {"shortName"})
})
public class EducationalQualificationLevel extends ReferenceEntity<EducationalQualificationLevel> {

    private static final long serialVersionUID = 1L;
    /**
     * Младший специалист
     */
    public final static String JUNIOR_SPECIALIST_KEY = "JUNIOR_SPECIALIST";
    /**
     * Бакалавр
     */
    public final static String BACHELOR_KEY = "BACHELOR";
    /**
     * Специалист
     */
    public final static String SPECIALIST_KEY = "SPECIALIST";
    /**
     * Магистр
     */
    public final static String MASTER_KEY = "MASTER";

    @Validate("regexp=([5-8]{1})")
    private String code;

    public EducationalQualificationLevel() {
    }

    public EducationalQualificationLevel(String name, String code) {
        setName(name);
        this.code = code;
    }

    /**
     * @return the code
     */
    @Column(nullable=false, unique=true)
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean entityEquals(EducationalQualificationLevel obj) {
        return aEqualsField(code, obj.code);
    }
}
