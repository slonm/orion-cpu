package test.grid.lib;

import java.util.Date;

/**
 *
 * @author root
 */
public class Contact {

    private String firstName;
    private String lastName;
    private String email;
    private float salary;
    private Date birthDate;
    private long id;

    public Date getBirthDate(){
        return this.birthDate;
    }

    public void setBirthDate(Date _birthDate){
        this.birthDate=_birthDate;
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
