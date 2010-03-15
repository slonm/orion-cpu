package orion.cpu.entities.ref;

import orion.cpu.baseentities.ReferenceEntity;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import org.apache.tapestry5.beaneditor.ReorderProperties;
import org.apache.tapestry5.beaneditor.Validate;

/**
 * Сущность KnowledgeAreaOrTrainingDirection
 * @author sl
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames={"name", "isKnowledgeArea"}),
    @UniqueConstraint(columnNames={"shortName", "isKnowledgeArea"}),
    @UniqueConstraint(columnNames={"code", "isKnowledgeArea"})
})
@ReorderProperties("name, shortName, code, isKnowledgeArea, isObsolete")
public class KnowledgeAreaOrTrainingDirection extends ReferenceEntity<KnowledgeAreaOrTrainingDirection> {

    private static final long serialVersionUID = 1L;
    @Validate("regexp=([0-9]{2}|[0-9]{4})")
    private String code;
    @DataType("KnowledgeAreaOrTrainingDirection")
    private Boolean isKnowledgeArea;

    @Column(nullable=false)
    public Boolean getIsKnowledgeArea() {
        return isKnowledgeArea;
    }

    public void setIsKnowledgeArea(Boolean isKnowledgeArea) {
        auditRecord(this.isKnowledgeArea,isKnowledgeArea);
        this.isKnowledgeArea = isKnowledgeArea;
    }

    @Column(nullable=false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        auditRecord(this.code, code);
        this.code = code;
    }

    @Override
    public boolean entityEquals(KnowledgeAreaOrTrainingDirection obj) {
        return aEqualsField(code, obj.code) &&
               aEqualsField(isKnowledgeArea, obj.isKnowledgeArea);
    }
}
