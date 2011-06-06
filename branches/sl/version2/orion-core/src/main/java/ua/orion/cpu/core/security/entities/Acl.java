package ua.orion.cpu.core.security.entities;

import javax.persistence.*;
import org.apache.shiro.authz.permission.WildcardPermission;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Общий список ACL.
 * Максимально абстрагирован от конкретных типов разрешений, пользователей, ролей
 * и т.п.
 * @author sl
 */
@Entity
@Table(schema="sec", uniqueConstraints=@UniqueConstraint(columnNames={"subject", "subjectType", "permission"}))
public class Acl extends AbstractEntity<Acl> {

    /**
     * Субъект разрешений. Им может быть пользователь или роль
     */
    private String subject;
    private SubjectType subjectType;
    /**
     * Объект разрешений. Им может быть класс сущности, сущность, веб страница, пункт меню и т.п.
     * Класс сущности: Entity:edit
     * Сущность: Entity:edit:12
     * Страница: page_index:view
     * Пункт меню: menu_Start>Licensing>LicenseRecord:show
     * @see WildcardPermission
     */
    private String permission;

    public Acl() {
    }

    public Acl(String subject, SubjectType subjectType, String permission) {
        this.subject = subject;
        this.subjectType = subjectType;
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    @Override
    public String toString() {
        return "[" + subjectType.toString() + "." + subject + "][" + permission + "]";
    }

    @Override
    protected boolean entityEquals(Acl obj) {
        return toString().equalsIgnoreCase(obj.toString());
    }

    @Override
    public int compareTo(Acl o) {
        return toString().compareTo(o.toString());
    }
}
