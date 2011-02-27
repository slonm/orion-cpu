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
    private String veryLongStringField;

    public Entity() {
    }

    public Entity(int id, String stringField, String veryLongStringField) {
        setId(id);
        setStringField(stringField);
        setVeryLongStringField(veryLongStringField);
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

    public String getVeryLongStringField() {
        return veryLongStringField;
    }

    public void setVeryLongStringField(String veryLongStringField) {
        this.veryLongStringField = veryLongStringField;
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
        if ((this.veryLongStringField == null) ? (other.veryLongStringField != null) : !this.veryLongStringField.equals(other.veryLongStringField)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 53 * hash + (this.stringField != null ? this.stringField.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Entity{" + "id=" + id + ", stringField=" + stringField + '}';
    }

}
