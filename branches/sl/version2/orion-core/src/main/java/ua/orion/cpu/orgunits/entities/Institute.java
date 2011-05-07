package ua.orion.cpu.orgunits.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.persistence.*;

/**
 * Класс-сущность институт
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class Institute extends OrgUnit<Institute> {

   private static final long serialVersionUID = 1L;
}
