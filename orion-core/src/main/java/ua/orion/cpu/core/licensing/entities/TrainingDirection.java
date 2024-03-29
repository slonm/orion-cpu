package ua.orion.cpu.core.licensing.entities;

import ua.orion.core.persistence.AbstractReferenceEntity;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Справочник направлений обучения
 * @author sl
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames={"name", "code"}),
    @UniqueConstraint(columnNames={"shortName", "code"})
})
public class TrainingDirection extends AbstractReferenceEntity<TrainingDirection> {

    private static final long serialVersionUID = 1L;
    private String code;
    private KnowledgeArea knowledgeArea;

    public TrainingDirection() {
    }

    public TrainingDirection(String name,
            String shortName,
            String code,
            KnowledgeArea knowledgeArea) {
                setName(name);
                this.code=code;
                this.knowledgeArea=knowledgeArea;
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

    @NotNull
    @ManyToOne
    public KnowledgeArea getKnowledgeArea() {
        return knowledgeArea;
    }

    public void setKnowledgeArea(KnowledgeArea knowledgeArea) {
        this.knowledgeArea = knowledgeArea;
    }

    @Override
    public boolean entityEquals(TrainingDirection obj) {
        return aEqualsField(getCode(), obj.code) &&
               aEqualsField(getKnowledgeArea(), obj.getKnowledgeArea());
    }
}
