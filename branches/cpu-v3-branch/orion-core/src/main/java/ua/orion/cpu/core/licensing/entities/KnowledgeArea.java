package ua.orion.cpu.core.licensing.entities;



import ua.orion.core.persistence.AbstractReferenceEntity;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Справочник областей знаний
 * @author sl
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames={"name"}),
    @UniqueConstraint(columnNames={"shortName"}),
    @UniqueConstraint(columnNames={"code"})
})
public class KnowledgeArea extends AbstractReferenceEntity<KnowledgeArea> {

    private static final long serialVersionUID = 1L;
    private String code;

    public KnowledgeArea() {
    }

    public KnowledgeArea(String name, String shortName, String code, Boolean isObsolete) {
        setName(name);
        setShortName(shortName);
        this.code = code;
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

    @Override
    public boolean entityEquals(KnowledgeArea obj) {
        return aEqualsField(code, obj.code);
    }
}
