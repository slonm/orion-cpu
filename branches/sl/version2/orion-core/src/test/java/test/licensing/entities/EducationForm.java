package test.licensing.entities;

import javax.persistence.*;
import ua.orion.core.annotations.UserPresentable;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 *Сущность-справочник EducationForm
 * @author kgp
 */
@Entity
@AttributeOverrides({
    @AttributeOverride(name = "name", column = @Column(unique = true)),
    @AttributeOverride(name = "shortName", column = @Column(unique = true))})
@UserPresentable("name")
public class EducationForm extends AbstractReferenceEntity<EducationForm> {

    private static final long serialVersionUID = 1L;
    /**
     * Дневная форма обучения
     */
    public static final String STATIONARY_UKEY = "STATIONARY";
    /**
     * Заочная форма обучения
     */
    public static final String CORRESPONDENCE_UKEY = "CORRESPONDENCE";

    public EducationForm() {
    }

    public EducationForm(String name) {
        setName(name);
    }
    
    public EducationForm(String name, String shortName, String uKey) {
        setName(name);
        setShortName(shortName);
        setUKey(uKey);
    }
}
