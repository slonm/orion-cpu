package ua.orion.cpu.security.entities;

import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Общий список ACL.
 * Максимально абстрагирован от конкретных типов разрешений, пользователей, ролей
 * и т.п.
 * @author sl
 */
@Entity
public class ACL extends AbstractEntity<ACL> {

    /**
     * Субъект разрешений. Им может быть пользователь или роль
     * У роль должен быть префикс ROLE_. Запрещенный символ во всех полях ":"
     */
    private String subject;
    /**
     * Объект разрешений. Им может быть класс сущности, сущность, веб страница, пункт меню и т.п.
     * Класс сущности: ua.orion.entity.Entity
     * Сущность: ua.orion.entity.Entity$12
     * Страница: PAGE_Index
     * Пункт меню: MENUITEM_Start>Licensing>LicenseRecord
     */
    private String securedObject;
    /**
     * Разрешение. операция с объектом. read, write, menu, etc.
     */
    private String permission;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getSecuredObject() {
        return securedObject;
    }

    public void setSecuredObject(String securedObject) {
        this.securedObject = securedObject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return subject + ":" + securedObject + ":" + permission;
    }

    @Override
    protected boolean entityEquals(ACL obj) {
        return toString().equalsIgnoreCase(obj.toString());
    }

    @Override
    public int compareTo(ACL o) {
        return toString().compareTo(o.toString());
    }
}
