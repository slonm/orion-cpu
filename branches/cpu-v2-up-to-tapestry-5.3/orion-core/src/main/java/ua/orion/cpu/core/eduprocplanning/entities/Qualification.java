package ua.orion.cpu.core.eduprocplanning.entities;

import javax.persistence.*;
import ua.orion.core.annotations.UserPresentable;
import ua.orion.core.persistence.*;

/**
 * Справочник названий и кодов квалификаций для учебного плана
 * берется из НАЦІОНАЛЬНИЙ КЛАСИФІКАТОР УКРАЇНИ КЛАСИФІКАТОР ПРОФЕСІЙ
 * ДК 003:2005
 * @author kgp
 */
@Entity
@ReferenceBook
@AttributeOverrides({
    @AttributeOverride(name = "name", column =
    @Column(unique = true)),
    @AttributeOverride(name = "qualigicationCode", column =
    @Column(unique = true))})
@UserPresentable("name")
@Cacheable
public class Qualification extends AbstractReferenceEntity<Qualification> {

    private static final long serialVersionUID = 1L;
    
    protected String qualificationCode;

    public Qualification() {
    }

    public Qualification(String name, String qualigicationCode) {
        this.setName(name);
        this.setQualificationCode(qualificationCode);
    }

    /**
     * Get the value of QualigicationCode
     *
     * @return the value of QualigicationCode from КЛАСИФІКАТОР ПРОФЕСІЙ
     */
    public String getQualificationCode() {
        return qualificationCode;
    }

    /**
     * Set the value of QualigicationCode
     *
     * @param QualigicationCode new value of QualigicationCode from КЛАСИФІКАТОР ПРОФЕСІЙ
     */
    public void setQualificationCode(String qualificationCode) {
        this.qualificationCode = qualificationCode;
    }
}
