package ua.orion.cpu.orgunits.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.persistence.*;

/**
 * Класс-сущность факультет
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class Faculty extends OrgUnit<Faculty> {

   private static final long serialVersionUID = 1L;
}
