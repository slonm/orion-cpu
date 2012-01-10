package ua.orion.cpu.core.students.entities;

import java.util.Calendar;
import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
import ua.orion.cpu.core.licensing.entities.EducationForm;

/**
 * Здесь хранится информация о том, на какой специальности обучается студент,
 * форме обучения и дата поступления на данную специальность.
 *
 * @author molodec
 */
@Entity
public class EducationStudent extends AbstractEntity<EducationStudent> {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    private EduPlan eduPlan;
    @ManyToOne
    private EducationForm educationForm;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar entranceDate;
    @OneToOne
    private ContractStudent contractStudent;

    public EducationStudent() {
    }

    /**
     * Создание записи о поступлении человека на определнную специальность на
     * основе:
     *
     * @param eduPlan - учебный план
     * @param educationForm - форма обучения
     * @param entranceDate - дата поступления
     * @param contractStudent - контракт(договор)
     */
    public EducationStudent(EduPlan eduPlan, EducationForm educationForm, Calendar entranceDate, ContractStudent contractStudent) {
        this.eduPlan = eduPlan;
        this.educationForm = educationForm;
        this.entranceDate = entranceDate;
        this.contractStudent = contractStudent;
    }


    public EduPlan getEduPlan() {
        return eduPlan;
    }

    public void setEduPlan(EduPlan eduPlan) {
        this.eduPlan = eduPlan;
    }

    /**
     * Конкретная форма обучения для данной записи
     *
     * @return
     */
    public EducationForm getEducationForm() {
        return educationForm;
    }

    public void setEducationForm(EducationForm educationForm) {
        this.educationForm = educationForm;
    }

    /**
     * Дата поступления на данную специальность
     *
     * @return
     */
    public Calendar getEntranceDate() {
        return entranceDate;
    }

    public void setEntranceDate(Calendar entranceDate) {
        this.entranceDate = entranceDate;
    }

    public ContractStudent getContractStudent() {
        return contractStudent;
    }

    public void setContractStudent(ContractStudent contractStudent) {
        this.contractStudent = contractStudent;
    }

    @Override
    protected boolean entityEquals(EducationStudent obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(EducationStudent o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Переопределенный метод toString. Служит для компактного вывода записи
     * EducationStudent
     *
     * @return
     */
    @Override
    public String toString() {
        return "Навчальний план: " + eduPlan + ", Форма навчання: " + educationForm + ", Дата надходження: " + entranceDate.getTime().toString();
    }
}
