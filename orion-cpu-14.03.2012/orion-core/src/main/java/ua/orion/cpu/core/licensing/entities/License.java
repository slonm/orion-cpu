package ua.orion.cpu.core.licensing.entities;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import ua.orion.cpu.core.entities.Document;

/**
 * Сущность, хранящая серию и номер лицензий, а также набор лицензионных записей
 *
 * @author kgp
 */
@Entity
@Cacheable
public class License extends Document<License> {

    private static final long serialVersionUID = 1L;
    private Set<LicenseRecord> licenseRecords = new HashSet<LicenseRecord>();
    private LicenseState licenseState = LicenseState.NEW;

    public License() {
    }

    public License(String serial, String number, Calendar issue) {
        setIssue(issue);
        setSerial(serial);
        setNumber(number);
    }

    /**
     * Состояние лицензии. При добавлении новой лицензии она принимает состояние NEW.
     * При этом ее можно модифицировать. NEW лицензия может быть только одна или ни одной.
     * Далее лицензия вводится в эксплуатацию и
     * переходит в состояние FORCED. Такую лицензию нельзя модифицировать, но на
     * ее основе можно создавать учебные планы. FORCED лицензия только одна. Если NEW
     * лицензия переводится в состояние FORCED, то прежняя FORCED лицензия переходит 
     * в состояние OBSOLETE.
     */
    @Enumerated(EnumType.STRING)
    public LicenseState getLicenseState() {
        return licenseState;
    }

    public void setLicenseState(LicenseState forced) {
        this.licenseState = forced;
    }

    /**
     * @return the LicenseSerial
     */
    @Pattern(regexp = "([А-Я]{2})")
    @NotNull
    @Override
    public String getSerial() {
        return super.getSerial();
    }

    /**
     * @return the licenseNumber
     */
    @Pattern(regexp = "([0-9]{6})")
    @NotNull
    @Override
    public String getNumber() {
        return super.getNumber();
    }

    /**
     * @return the licenseIssueDate
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    @Override
    public Calendar getIssue() {
        return super.getIssue();
    }

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "license")
    public Set<LicenseRecord> getLicenseRecords() {
        return licenseRecords;
    }

    public void setLicenseRecords(Set<LicenseRecord> licenseRecords) {
        this.licenseRecords = licenseRecords;
    }
}
