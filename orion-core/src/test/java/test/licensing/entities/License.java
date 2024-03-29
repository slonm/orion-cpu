package test.licensing.entities;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Сущность, хранящая общие сведения (шапку) лицензий
 * @author kgp
 */
@Entity
public class License extends Document<License> {

    private static final long serialVersionUID = 1L;
    private Set<LicenseRecord> licenseRecords=new HashSet<LicenseRecord>();

    public License() {
    }

    public License(String serial, String number, Calendar issue) {
        setIssue(issue);
        setSerial(serial);
        setNumber(number);
    }

    /**
     * @return the LicenseSerial
     */
    @Pattern(regexp="([А-Я]{2})")
    @NotNull
    @Override
    @Transient
    public String getSerial() {
        return super.getSerial();
    }

    /**
     * @return the licenseNumber
     */
    @Pattern(regexp="([0-9]{6})")
    @NotNull
    @Override
    @Transient
    public String getNumber() {
        return super.getNumber();
    }

    @OneToMany(cascade={CascadeType.ALL}, mappedBy="license")
    public Set<LicenseRecord> getLicenseRecords(){
        return licenseRecords;
    }

    public void setLicenseRecords(Set<LicenseRecord> licenseRecords) {
        this.licenseRecords = licenseRecords;
    }

}
