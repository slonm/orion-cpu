package ua.orion.cpu.core.licensing.entities;

import ua.orion.core.persistence.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.apache.tapestry5.beaneditor.DataType;

/**
 * Справочник направлений обучения или специальностей (старая/новая классификация)
 * @author sl
 */
@Entity
@ReferenceBook
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames={"name", "code", "isTrainingDirection"}),
    @UniqueConstraint(columnNames={"shortName", "code", "isTrainingDirection"})
})
public class TrainingDirectionOrSpeciality extends AbstractReferenceEntity<TrainingDirectionOrSpeciality> {

    private static final long serialVersionUID = 1L;
    private String code;
    private KnowledgeAreaOrTrainingDirection knowledgeAreaOrTrainingDirection;
    private Boolean isTrainingDirection;

    public TrainingDirectionOrSpeciality() {
    }

    public TrainingDirectionOrSpeciality(String name,
            String shortName,
            String code,
            Boolean isTrainingDirection,
            KnowledgeAreaOrTrainingDirection knowledgeAreaOrTrainingDirection,
            Boolean isObsolete) {
                setName(name);
                this.code=code;
                this.isTrainingDirection=isTrainingDirection;
                this.knowledgeAreaOrTrainingDirection=knowledgeAreaOrTrainingDirection;
                setIsObsolete(isObsolete);
    }

    @Pattern(regexp="([0-9]{2}|[0-9]{4})")
    @NotNull
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
     /**
     * Позволяет в бинэдиторе и гриде выводить вместо true TrainingDirectionOrSpeciality
     */
    @DataType("TrainingDirectionOrSpeciality")
    @NotNull
    public Boolean getIsTrainingDirection() {
        return isTrainingDirection;
    }

    public void setIsTrainingDirection(Boolean isTrainingDirection) {
        this.isTrainingDirection = isTrainingDirection;
    }

    @NotNull
    @ManyToOne
    public KnowledgeAreaOrTrainingDirection getKnowledgeAreaOrTrainingDirection() {
        return knowledgeAreaOrTrainingDirection;
    }

    public void setKnowledgeAreaOrTrainingDirection(KnowledgeAreaOrTrainingDirection knowledgeAreaOrTrainingDirection) {
        this.knowledgeAreaOrTrainingDirection = knowledgeAreaOrTrainingDirection;
    }

    @Override
    public boolean entityEquals(TrainingDirectionOrSpeciality obj) {
        return aEqualsField(getCode(), obj.code) &&
               aEqualsField(getKnowledgeAreaOrTrainingDirection(), obj.getKnowledgeAreaOrTrainingDirection()) &&
               aEqualsField(isTrainingDirection, obj.isTrainingDirection);
    }
}
