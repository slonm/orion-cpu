package orion.cpu.entities.emp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.org.Post;

/**
 *
 * @author sl
 */
@Entity
@Table(schema = "emp", uniqueConstraints =
@UniqueConstraint(columnNames = {"post", "employee"}))
public class EmployeePost extends BaseEntity<EmployeePost> {

    @NotNull
    private Post post;
    /**
     * Основная должность или совмещение
     */
    @NotNull
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

    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

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
