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
    private boolean forced = false;

    public License() {
    }

    public License(String serial, String number, Calendar issue) {
        setIssue(issue);
        setSerial(serial);
        setNumber(number);
    }

    public boolean isForced() {
        return forced;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
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
