package test.licensing.entities;



import javax.persistence.*;
import javax.validation.constraints.*;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 * Сущность-справочник EducationalQualificationLevel
 * @author kgp
 */
@Entity
@AttributeOverrides({
    @AttributeOverride(name = "name", column = @Column(unique = true)),
    @AttributeOverride(name = "shortName", column = @Column(unique = true))})
public class EducationalQualificationLevel extends AbstractReferenceEntity<EducationalQualificationLevel> {

    private static final long serialVersionUID = 1L;
    /**
     * Младший специалист
     */
    public final static String JUNIOR_SPECIALIST_UKEY = "JUNIOR_SPECIALIST";
    /**
     * Бакалавр
     */
    public final static String BACHELOR_UKEY = "BACHELOR";
    /**
     * Специалист
     */
    public final static String SPECIALIST_UKEY = "SPECIALIST";
    /**
     * Магистр
     */
    public final static String MASTER_UKEY = "MASTER";

    private String code;

    public EducationalQualificationLevel() {
    }

    public EducationalQualificationLevel(String name, String code) {
        setName(name);
        this.code = code;
    }

    public EducationalQualificationLevel(String name, String shortName, String code,
            String uKey) {
        setName(name);
        setShortName(shortName);
        setUKey(uKey);
        this.code = code;
    }

    @Column(nullable=false, unique=true)
    @Pattern(regexp="([5-8]{1})")
    @NotNull
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean entityEquals(EducationalQualificationLevel obj) {
        return aEqualsField(code, obj.code);
    }
}
