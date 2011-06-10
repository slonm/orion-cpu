package orion.cpu.entities.ref;

import orion.cpu.baseentities.ReferenceEntity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.apache.tapestry5.beaneditor.DataType;

/**
 * Справочник областей знаний или направлений обучения (старая/новая классификация)
 * @author sl
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames={"name", "isKnowledgeArea"}),
    @UniqueConstraint(columnNames={"shortName", "isKnowledgeArea"}),
    @UniqueConstraint(columnNames={"code", "isKnowledgeArea"})
})
public class KnowledgeAreaOrTrainingDirection extends ReferenceEntity<KnowledgeAreaOrTrainingDirection> {

    private static final long serialVersionUID = 1L;
    private String code;
    @DataType("KnowledgeAreaOrTrainingDirection")
    private Boolean isKnowledgeArea;

    @Pattern(regexp="[0-9]{4}")
    @NotNull
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotNull
    public Boolean getIsKnowledgeArea() {
        return isKnowledgeArea;
    }

    public void setIsKnowledgeArea(Boolean isKnowledgeArea) {
        this.isKnowledgeArea = isKnowledgeArea;
    }   

    @Override
    public boolean entityEquals(KnowledgeAreaOrTrainingDirection obj) {
        return aEqualsField(code, obj.code) &&
               aEqualsField(isKnowledgeArea, obj.isKnowledgeArea);
    }
}
