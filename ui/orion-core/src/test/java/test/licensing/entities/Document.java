package test.licensing.entities;



import ua.orion.cpu.core.entities.*;
import ua.orion.core.persistence.AbstractEntity;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.ScriptAssert;
/**
 * Абстрактная реализация документа.
 * Реализует общие для всех документов поля: Серия, Номер, Дата издания, тело в
 * виде текста.
 * @param <T>
 * @author kgp
 */
//TODO Реализовать как JCR Node
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"serial", "number", "issue"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@ScriptAssert(lang = "javascript", 
        script = "_this.serial!=null||_this.number!=null")
public abstract class Document<T extends Document<?>> extends AbstractEntity<T> {

    private String serial;
    private String number;
    private Calendar issue;
    private String body;

    @Lob
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the Serial
     */
    @Size(min=1)
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial the Serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the licenseNumber
     */
    @Size(min=1)
    public String getNumber() {
        return number;
    }

    /**
     * @param number the Number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the licenseIssueDate
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    public Calendar getIssue() {
        return issue;
    }

    /**
     * @param issue the issue to set
     */
    public void setIssue(Calendar issue) {
        this.issue = issue;
    }

    /**
     * Сортрирует по issue, serial, number
     */
    @Override
    public String toString() {
        return (serial + number + " (" + issue + ")");
    }

    @Override
    protected boolean entityEquals(Document obj) {
        return aEqualsField(serial, obj.serial) && aEqualsField(number, obj.number) && aEqualsField(issue, obj.issue);
    }

    @Override
    public int compareTo(Document o) {
        int ret = issue.compareTo(o.issue);
        if (ret != 0) {
            return ret;
        }
        ret = serial.compareTo(o.serial);
        if (ret != 0) {
            return ret;
        }
        ret = number.compareTo(o.number);
        if (ret != 0) {
            return ret;
        }
        return 0;
    }
}
