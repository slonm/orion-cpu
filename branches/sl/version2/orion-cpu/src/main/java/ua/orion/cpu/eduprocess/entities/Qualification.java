package ua.orion.cpu.eduprocess.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.persistence.*;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 * Сущность-справочник названий и кодов квалификаций для учебного плана
 * берется из НАЦІОНАЛЬНИЙ КЛАСИФІКАТОР УКРАЇНИ КЛАСИФІКАТОР ПРОФЕСІЙ
 * ДК 003:2005
 * @author kgp
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class Qualification extends AbstractReferenceEntity<Qualification> {

    private static final long serialVersionUID = 1L;
    protected String qualificationCode;

    //непараметризированный конструктор
    public Qualification() {
    }

    //параметризированный конструктор
    public Qualification(String name, String qualigicationCode) {
        setName(name);
        setQualificationCode(qualificationCode);
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