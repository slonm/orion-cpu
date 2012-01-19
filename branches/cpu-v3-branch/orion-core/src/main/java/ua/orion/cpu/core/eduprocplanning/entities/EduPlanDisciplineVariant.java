/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.eduprocplanning.entities;

import javax.persistence.*;
import ua.orion.core.annotations.UserPresentable;
import ua.orion.core.persistence.AbstractReferenceEntity;
import ua.orion.core.persistence.ReferenceBook;

/**
 *
 * @author molodec
 */
@Entity
@ReferenceBook
@AttributeOverrides({
    @AttributeOverride(name = "name", column =
    @Column(unique = true)),
    @AttributeOverride(name = "shortName", column =
    @Column(unique = true))})
@UserPresentable("name")
@Cacheable
public class EduPlanDisciplineVariant extends AbstractReferenceEntity<EduPlanDisciplineVariant> {

    public EduPlanDisciplineVariant() {
    }

    public EduPlanDisciplineVariant(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }


}
