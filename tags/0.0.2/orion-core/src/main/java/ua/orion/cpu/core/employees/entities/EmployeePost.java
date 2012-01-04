package ua.orion.cpu.core.employees.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.cpu.core.orgunits.entities.OrgUnitPost;

/**
 *
 * @author sl
 */
@Entity
@Table(schema = "emp")
//@Table(schema = "emp", uniqueConstraints =
//@UniqueConstraint(columnNames = {"post", "employee"}))
public class EmployeePost extends AbstractEntity<EmployeePost> {

    private static final long serialVersionUID = 1L;
    /**
     * Связь с штатным расписанием подразделения
     */
    private OrgUnitPost orgUnitPost;
    /**
     * Основная должность или совмещение
     */
    private Boolean isMain;
    /**
     * Связь с сотрудником
     */
    private Employee employee;

    public EmployeePost() {
    }

    public EmployeePost(OrgUnitPost orgUnitPost, Boolean isMain) {
        this.orgUnitPost = orgUnitPost;
        this.isMain = isMain;
    }

 
    @ManyToOne
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @NotNull
    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }


    @ManyToOne
    public OrgUnitPost getOrgUnitPost() {
        return orgUnitPost;
    }

    public void setOrgUnitPost(OrgUnitPost orgUnitPost) {
        this.orgUnitPost = orgUnitPost;
    }

    @Override
    protected boolean entityEquals(EmployeePost obj) {
        return aEqualsField(orgUnitPost, obj.orgUnitPost)
                && aEqualsField(isMain, obj.isMain)
                && aEqualsField(employee, obj.employee);
    }

    @Override
    public int compareTo(EmployeePost o) {
        return orgUnitPost.compareTo(o.getOrgUnitPost());
    }
}
