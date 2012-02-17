package test.licensing.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.persistence.*;

/**
 * Класс-сущность кафедра
 * @author kgp
 */
@Entity
public class Chair extends OrgUnit<Chair> {

   private static final long serialVersionUID = 1L;

    public Chair() {
    }

    public Chair(String name, String shortName) {
        setName(name);
        setShortName(shortName);
    }
   
}
