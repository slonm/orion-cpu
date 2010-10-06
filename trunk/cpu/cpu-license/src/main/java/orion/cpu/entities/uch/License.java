package orion.cpu.entities.uch;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.Validate;
import org.hibernate.validator.Size;
import orion.cpu.entities.pub.Document;

/**
 * Сущность, хранящая общие сведения (шапку) лицензий
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
    @Validate("regexp=([А-Я]{2})")
    public String getSerial() {
        return super.getSerial();
    }

    /**
     * @return the licenseNumber
     */
    @Column(nullable = false)
   // @Validate("regexp=([0-9]{6})")
    //@Size(min = 2, max = 6, message="Number must be from 2 to 6 letters length")
    public String getNumber() {
        return super.getNumber();
    }

    @OneToMany(mappedBy="license")
    public Set<LicenseRecord> getLicenseRecords(){
        return licenseRecords;
    }

    public void setLicenseRecords(Set<LicenseRecord> licenseRecords) {
        this.licenseRecords = licenseRecords;
    }

}
