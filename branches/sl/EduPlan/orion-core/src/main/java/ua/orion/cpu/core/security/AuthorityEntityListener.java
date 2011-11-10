package ua.orion.cpu.core.security;

import javax.persistence.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author sl
 */
public class AuthorityEntityListener {

    @PostLoad
    public void onPostLoad(Object object) {
        //во время изменяющих операций можно читать из таблиц безопасности
        if (object != null) {
            if (!object.getClass().getAnnotation(Table.class).schema().equals("sec")) {
                Subject subject = SecurityUtils.getSubject();
                if (subject != null) {
                    subject.checkPermission("read");
                }
            }
        }
    }

    @PrePersist
    public void onPrePersist(Object object) {
        if (object != null) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                subject.checkPermission("insert");
            }
        }
    }

    @PreUpdate
    public void onPreUpdate(Object object) {
        if (object != null) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                subject.checkPermission("update");
            }
        }
    }

    @PreRemove
    public void onPreRemove(Object object) {
        if (object != null) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                subject.checkPermission("remove");
            }
        }
    }
}
