package ua.orion.cpu.core.licensing.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.annotations.UserPresentable;
import ua.orion.core.persistence.AbstractReferenceEntity;
import ua.orion.core.persistence.IRangable;

/**
 * Справочник форм обучения
 * @author kgp
 */
@Entity
@Table(schema = "ref")
@AttributeOverrides({
    @AttributeOverride(name = "name", column =
    @Column(unique = true)),
    @AttributeOverride(name = "shortName", column =
    @Column(unique = true))})
@UserPresentable("name")
@Cacheable
public class EducationForm extends AbstractReferenceEntity<EducationForm>
        implements IRangable {

    private static final long serialVersionUID = 1L;
    /**
     * Дневная форма обучения
     */
    public static final String STATIONARY_UKEY = "STATIONARY";
    /**
     * Заочная форма обучения
     */
    public static final String CORRESPONDENCE_UKEY = "CORRESPONDENCE";
    private Integer rang = 0;

    public EducationForm() {
    }

    public EducationForm(String name) {
        setName(name);
    }

    public EducationForm(String name, String shortName, 
            String uKey, Integer rang) {
        setName(name);
        setShortName(shortName);
        setUKey(uKey);
        this.rang=rang;
    }

    /**
     * 
     * @return 
     */
    @Override
    @DataType("rang")
    @NotNull
    public Integer getRang() {
        return rang;
    }

    @Override
    public void setRang(Integer rang) {
        this.rang = rang;
    }

    @Override
    public int compareTo(EducationForm o) {
        return o == null ? -1 : rang.compareTo(o.getRang());
    }
}
