package ua.orion.cpu.core.students.entities;

import java.util.Calendar;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.cpu.core.persons.entities.Person;

/**
 * Здесь хранится информация о том, на какой специальности обучается студент,
 * форме обучения и дата поступления на данную специальность.
 *
 * @author molodec
 */
@Entity
public class SocialPrivelege extends AbstractEntity<SocialPrivelege> {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    private KindPrevelege kindPrevelege;
    @DataType("longText")
    private String documentBasis;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dateStart;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dateEnd;
    @DataType("longText")
    private String addinitional;
    @ManyToOne
    private Person person;

    public SocialPrivelege(KindPrevelege kindPrevelege, String documentBasis, Calendar dateStart, Calendar dateEnd, String addinitional, Person person) {
        this.kindPrevelege = kindPrevelege;
        this.documentBasis = documentBasis;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.addinitional = addinitional;
        this.person = person;
    }

    public SocialPrivelege() {
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Calendar getDateStart() {
        return dateStart;
    }

    public void setDateStart(Calendar dateStart) {
        this.dateStart = dateStart;
    }

    public Calendar getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Calendar dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDocumentBasis() {
        return documentBasis;
    }

    public void setDocumentBasis(String documentBasis) {
        this.documentBasis = documentBasis;
    }

    public KindPrevelege getKindPrevelege() {
        return kindPrevelege;
    }

    public void setKindPrevelege(KindPrevelege kindPrevelege) {
        this.kindPrevelege = kindPrevelege;
    }

    public String getAddinitional() {
        return addinitional;
    }

    public void setAddinitional(String addinitional) {
        this.addinitional = addinitional;
    }

    @Override
    protected boolean entityEquals(SocialPrivelege obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(SocialPrivelege o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
