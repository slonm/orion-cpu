package ua.orion.cpu.core.licensing.entities;

import ua.orion.core.persistence.AbstractReferenceEntity;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Справочник специальностей
 * @author sl
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames={"name", "code"}),
    @UniqueConstraint(columnNames={"shortName", "code"})
})
public class Speciality extends AbstractReferenceEntity<Speciality> {

    private static final long serialVersionUID = 1L;
    private String code;
    private TrainingDirection trainingDirection;
    //Подпись классификации в именительном падеже, единственном числе
    private String classify;

    public Speciality() {
    }

    public Speciality(String name,
            String shortName,
            String code,
            TrainingDirection trainingDirection, String classify,
            Boolean isObsolete) {
                setName(name);
                this.code=code;
                this.trainingDirection=trainingDirection;
                setIsObsolete(isObsolete);
                this.classify=classify;
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

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    @Override
    public boolean entityEquals(Speciality obj) {
        return aEqualsField(getCode(), obj.code) &&
               aEqualsField(getTrainingDirection(), obj.getTrainingDirection());
    }
}
