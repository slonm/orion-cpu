package ua.orion.cpu.core.licensing.entities;

import ua.orion.core.persistence.AbstractReferenceEntity;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Справочник специальностей
 * @author sl
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames={"name", "code"}),
    @UniqueConstraint(columnNames={"shortName", "code"})
})
public class Speciality extends AbstractReferenceEntity<Speciality> {

    private static final long serialVersionUID = 1L;
    private String code;
    private TrainingDirection trainingDirection;

    public Speciality() {
    }

    public Speciality(String name,
            String shortName,
            String code,
            TrainingDirection trainingDirection) {
                setName(name);
                this.code=code;
                this.trainingDirection=trainingDirection;
                setIsObsolete(false);
    }

    @Pattern(regexp="([0-9]{2})")
    @NotNull
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Transient
    public KnowledgeArea getKnowledgeArea() {
        return trainingDirection.getKnowledgeArea();
    }
    
    @NotNull
    @ManyToOne
    public TrainingDirection getTrainingDirection() {
        return trainingDirection;
    }

    public void setTrainingDirection(TrainingDirection trainingDirection) {
        this.trainingDirection = trainingDirection;
    }

    @Override
    public boolean entityEquals(Speciality obj) {
        return aEqualsField(getCode(), obj.code) &&
               aEqualsField(getTrainingDirection(), obj.getTrainingDirection());
    }
}
