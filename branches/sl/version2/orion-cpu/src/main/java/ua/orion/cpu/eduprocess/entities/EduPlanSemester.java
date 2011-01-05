package ua.orion.cpu.eduprocess.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.persistence.*;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 * сущность семестр в учебном плане
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class EduPlanSemester extends AbstractReferenceEntity<EduPlanSemester> {

    private static final long serialVersionUID = 1L;
    //Номера семестров учебного плана
//    public static final String FIRSTSEMESTER_KEY = "1";
//    public static final String SECONDSEMESTER_KEY = "2";
//    public static final String THIRDSEMESTER_KEY = "3";
//    public static final String FOURTHSEMESTER_KEY = "4";
//    public static final String FIFTHSEMESTER_KEY = "5";
//    public static final String SIXTHSEMESTER_KEY = "6";
//    public static final String SEVENTHSEMESTER_KEY = "7";
//    public static final String EIGHTHSEMESTER_KEY = "8";

    public EduPlanSemester() {
    }

    public EduPlanSemester(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }
    /**
     * Количество недель теоретических занятий в данном семестре
     */
    private Double eduWeekAmount;

    public Double getEduWeekAmount() {
        return eduWeekAmount;
    }

    public void setEduWeekAmount(Double eduWeekAmount) {
        this.eduWeekAmount = eduWeekAmount;
    }
}
