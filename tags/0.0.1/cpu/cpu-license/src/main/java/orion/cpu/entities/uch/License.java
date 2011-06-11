package orion.cpu.entities.uch;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import orion.cpu.entities.pub.Document;

/**
 * Сущность, хранящая серию и номер лицензий, 
 * а также набор лицензионных записей
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class License extends Document<License> {

    private Set<LicenseRecord> licenseRecords=new HashSet<LicenseRecord>();

    /**
     * @return the LicenseSeria
     */
    @Column(length = 2, nullable = false)
    @Pattern(regexp="([А-Я]{2})")
    @Override
    public String getSerial() {
        return super.getSerial();
    }

    /**
     * @return the licenseNumber
     */
    @Column(nullable = false)
   // @Validate("regexp=([0-9]{6})")
    //@Size(min = 2, max = 6, message="Number must be from 2 to 6 letters length")
    @Override
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
