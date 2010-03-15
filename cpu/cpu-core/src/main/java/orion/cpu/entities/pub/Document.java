package orion.cpu.entities.pub;

import orion.cpu.baseentities.*;
import java.util.*;
import javax.persistence.*;

/**
 * Абстрактная реализация документа.
 * Реализует общие для всех документов поля: Серия, Номер, Дата издания, тело в
 * виде текста и сканированный вариант.
 * @param <T>
 * @author kgp
 */
//TODO Указать обязательность номера или серии
@Entity
@Table(schema = "pub", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"serial", "number", "issue"})
})
@Inheritance(strategy = InheritanceType.JOINED)
public class Document<T extends Document<?>> extends BaseEntity<T> {

    private String serial;
    private String number;
    private Date issue;
    private String body;
    private Map<String, DocumentImage> images = new HashMap<String, DocumentImage>();

    @Lob
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @OneToMany
    @JoinColumn(name="document")
    @MapKey(name = "name")
    public Map<String, DocumentImage> getImages() {
        return images;
    }

    public void setImages(Map<String, DocumentImage> images) {
        this.images = images;
    }

    /**
     * @return the Serial
     */
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
    @Column(nullable = false)
    public Date getIssue() {
        return issue;
    }

    /**
     * @param issue the issue to set
     */
    public void setIssue(Date issue) {
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
