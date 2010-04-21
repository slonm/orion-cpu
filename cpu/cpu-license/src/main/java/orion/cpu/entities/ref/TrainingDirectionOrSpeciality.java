package orion.cpu.entities.ref;

import orion.cpu.baseentities.ReferenceEntity;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import org.apache.tapestry5.beaneditor.ReorderProperties;
import org.apache.tapestry5.beaneditor.Validate;

/**
 * Сущность TrainingDirectionOrSpeciality
 * @author sl
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames={"name", "code", "isTrainingDirection"}),
    @UniqueConstraint(columnNames={"shortName", "code", "isTrainingDirection"})
})
@ReorderProperties("name, shortName, code, isTrainingDirection, knowledgeAreaOrTrainingDirection, isObsolete")
public class TrainingDirectionOrSpeciality extends ReferenceEntity<TrainingDirectionOrSpeciality> {

    private static final long serialVersionUID = 1L;
    @Validate("regexp=([0-9]{2}|[0-9]{4})")
    private String code;
    private KnowledgeAreaOrTrainingDirection knowledgeAreaOrTrainingDirection;
    @DataType("TrainingDirectionOrSpeciality")
    private Boolean isTrainingDirection;

    @Column(nullable=false)
    public Boolean getIsTrainingDirection() {
        return isTrainingDirection;
    }

    public void setIsTrainingDirection(Boolean isTrainingDirection) {
        auditRecord(this.isTrainingDirection,isTrainingDirection);
        this.isTrainingDirection = isTrainingDirection;
    }

    @Column(nullable=false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        auditRecord(this.code, code);
        this.code = code;
    }

    @JoinColumn(nullable=false)
    @ManyToOne
    public KnowledgeAreaOrTrainingDirection getKnowledgeAreaOrTrainingDirection() {
        return knowledgeAreaOrTrainingDirection;
    }

    public void setKnowledgeAreaOrTrainingDirection(KnowledgeAreaOrTrainingDirection knowledgeAreaOrTrainingDirection) {
        auditRecord(this.knowledgeAreaOrTrainingDirection, knowledgeAreaOrTrainingDirection);
        this.knowledgeAreaOrTrainingDirection = knowledgeAreaOrTrainingDirection;
    }

    @Override
    public boolean entityEquals(TrainingDirectionOrSpeciality obj) {
        return aEqualsField(getCode(), obj.code) &&
               aEqualsField(getKnowledgeAreaOrTrainingDirection(), obj.getKnowledgeAreaOrTrainingDirection()) &&
               aEqualsField(isTrainingDirection, obj.isTrainingDirection);
    }
}
