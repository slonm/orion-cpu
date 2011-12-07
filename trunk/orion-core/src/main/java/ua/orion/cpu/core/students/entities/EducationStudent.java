package ua.orion.cpu.core.students.entities;

import java.util.Calendar;
import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.cpu.core.licensing.entities.EducationForm;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;

/**
 * Здесь хранится информация о том, на какой специальности обучается студент,
 * форме обучения и дата поступления на данную специальность.
 *
 * @author molodec
 */
@Entity
@Table(schema = "stu")
public class EducationStudent extends AbstractEntity<EducationStudent> {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    private LicenseRecord licenseRecord;
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
     * @param licenseRecord - лицензионная запись
     * @param educationForm - форма обучения
     * @param entranceDate - дата поступления
     * @param contractStudent - контракт(договор)
     */
    public EducationStudent(LicenseRecord licenseRecord, EducationForm educationForm, Calendar entranceDate, ContractStudent contractStudent) {
        this.licenseRecord = licenseRecord;
        this.educationForm = educationForm;
        this.entranceDate = entranceDate;
        this.contractStudent = contractStudent;
    }

    /**
     * Лицензионная запись. Каждая запись в EducationStudent имеет ссылку на
     * лицензионную запись, так как там хранится - специальность, кол-во мест,
     * кафедры и т.д.
     *
     * @return
     */
    public LicenseRecord getLicenseRecord() {
        return licenseRecord;
    }

    public void setLicenseRecord(LicenseRecord licenseRecord) {
        this.licenseRecord = licenseRecord;
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
        return "Ліцензія: " + licenseRecord + ", Форма навчання: " + educationForm + ", Дата надходження: " + entranceDate.getTime().toString();
    }
}
