/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.orion.cpu.core.security.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author sl
 */
public class ActiveDirectoryPrincipal {
    //cn
    private String fio;
    //mail
    private String email;
    //description
    private String description;
    //objectSID
    private String sid;
    //sAMAccountName
    private String login;
    //Groups
    private Set<String> roles=new HashSet();

    public ActiveDirectoryPrincipal() {
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
    
}
