package ua.orion.cpu.core.licensing.entities;



import ua.orion.core.persistence.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.apache.tapestry5.beaneditor.DataType;

/**
 * Справочник областей знаний или направлений обучения (старая/новая классификация)
 * @author sl
 */
@Entity
@ReferenceBook
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames={"name", "isKnowledgeArea"}),
    @UniqueConstraint(columnNames={"shortName", "isKnowledgeArea"}),
    @UniqueConstraint(columnNames={"code", "isKnowledgeArea"})
})
public class KnowledgeAreaOrTrainingDirection extends AbstractReferenceEntity<KnowledgeAreaOrTrainingDirection> {

    private static final long serialVersionUID = 1L;
    private String code;
    private Boolean isKnowledgeArea=true;

    public KnowledgeAreaOrTrainingDirection() {
    }

    public KnowledgeAreaOrTrainingDirection(String name, String shortName, String code, Boolean isKnowledgeArea, Boolean isObsolete) {
        setName(name);
        setShortName(shortName);
        this.code = code;
        this.isKnowledgeArea=isKnowledgeArea;
        setIsObsolete(isObsolete);
    }

    @Pattern(regexp="[0-9]{4}")
    @NotNull
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotNull
    /**
     * Позволяет в бинэдиторе и гриде выводить вместо true KnowledgeAreaOrTrainingDirection
     */
    @DataType("KnowledgeAreaOrTrainingDirection")
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
