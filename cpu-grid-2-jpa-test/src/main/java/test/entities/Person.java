package test.entities;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author root
 */
@Entity
public class Person {

    @Id
    private long id;
    @Basic
    private String firstName;
    @Basic
    private String lastName;
    @Basic
    private String email;
    @Basic
    private float salary;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDate;

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date _birthDate) {
        this.birthDate = _birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String val) {
        email = val;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String val) {
        firstName = val;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String val) {
        lastName = val;
    }

    public long getId() {
        return id;
    }

    public void setId(long val) {
        id = val;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float val) {
        salary = val;
    }
}
