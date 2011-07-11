package ua.orion.cpu.core.employees.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import ua.orion.cpu.core.orgunits.entities.Post;
import ua.orion.core.persistence.AbstractEntity;

/**
 *
 * @author sl
 */
@Entity
@Table(schema = "emp", uniqueConstraints =
@UniqueConstraint(columnNames = {"post", "employee"}))
public class EmployeePost extends AbstractEntity<EmployeePost> {

    private Post post;
    /**
     * Основная должность или совмещение
     */
    private Boolean isMain;
    private Employee employee;

    @NotNull
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

    @NotNull
    @ManyToOne
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    protected boolean entityEquals(EmployeePost obj) {
        return aEqualsField(post, obj.post)
                && aEqualsField(isMain, obj.isMain)
                && aEqualsField(employee, obj.employee);
    }

    @Override
    public int compareTo(EmployeePost o) {
        return post.compareTo(o.getPost());
    }
}
