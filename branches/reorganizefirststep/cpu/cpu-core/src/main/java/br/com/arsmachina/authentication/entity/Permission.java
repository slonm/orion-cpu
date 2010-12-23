package br.com.arsmachina.authentication.entity;

import javax.persistence.*;
import orion.cpu.baseentities.NamedEntity;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Class that represents a single permission.
 * Класс представляет собой определенное право на определенный тип объекта
 * @author Mihail Slobodyanuk
 */
@Entity
@Table(schema = "sec", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
public class Permission extends NamedEntity<Permission> {

    private static final long serialVersionUID = 1L;
    private Class<?> subject;
    private String permissionType;

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public String getPermissionType() {
        return permissionType;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setPermissionType(String type) {
        this.permissionType = type;
    }

    /**
     * Get the value of subject
     * Hibernate вызывает ошибку Unsupported discriminator type null
     * при попытке выборки по значению поля типа Class когда для этого класса есть
     * маппинг см. Unsupported discriminator type null.
     * Поэтому это поле Transient, а в базу сохраняем SubjectClassName
     * @return the value of subject
     */
    @Transient
    public Class<?> getSubject() {
        return subject;
    }

    /**
     * Set the value of subject
     *
     * @param subject new value of subject
     */
    public void setSubject(Class<?> subject) {
        this.subject = subject;
        setName();
    }

    /**
     * Get the value of SubjectClassName
     *
     * @return the value of subject
     */
    public String getSubjectClassName() {
        return subject==null?null:subject.getName();
    }

    /**
     * Set the value of SubjectClassName
     *
     * @param name 
     */
    public void setSubjectClassName(String name) {
        try {
            setSubject(Class.forName(name));
        } catch (ClassNotFoundException ex) {
            setSubject(null);
        }
    }

    /**
     * Установка поля name. Это пустая операция, т.к. name вычислимое.
     *
     * @param name new value of name
     */
    @Override
    public void setName(String name) {
    }

    private void setName() {
        if (subject != null && permissionType != null) {
            String schema = getSchema(subject).toUpperCase();
            super.setName(String.format("ROLE_%s%s_%s", schema.length() > 0 ? schema + "_" : "",
                    subject.getSimpleName().toUpperCase(), permissionType.toUpperCase()));
        }
    }

    /**
     * No-arg constructor.
     */
    public Permission() {
    }

    /**
     * Constructor that receives a name.
     * @param subject значения свойства. It cannot be null.
     * @param type значения свойства. It cannot be null.
     * @throws IllegalArgumentException if <code>name</code> is null.
     */
    public Permission(Class<?> subject, String type) {
        this.subject = Defense.notNull(subject, "subject");
        this.permissionType = Defense.notNull(type, "type");
        setName();
    }
}
