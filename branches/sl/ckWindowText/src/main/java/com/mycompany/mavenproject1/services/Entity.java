/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.mavenproject1.services;

/**
 *
 * @author slobodyanuk
 */
public class Entity {
    private Integer id;
    private String stringField;

    public Entity(int i, String string) {
        setId(i);
        setStringField(string);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entity other = (Entity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.stringField == null) ? (other.stringField != null) : !this.stringField.equals(other.stringField)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.stringField != null ? this.stringField.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Entity{" + "id=" + id + ", stringField=" + stringField + '}';
    }
    
}
