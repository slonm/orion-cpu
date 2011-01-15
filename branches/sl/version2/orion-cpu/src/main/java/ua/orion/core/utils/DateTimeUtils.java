/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.orion.core.utils;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author sl
 */
public class DateTimeUtils {

    private DateTimeUtils() {
    }
    
    /**
     * 
     * @return Calendar с заданными днем месяца, месяцем и годом
     * FIXME: Hibernate не отрезает часы, минуты, секунды и миллисекунды в 
     * параметрах запросов несмотря на то, что поле отмаплено как
     * javax.persistence.TemporalType.DATE. Вожможно это баг Hibernate
     */
    public static Calendar createCalendar(int dayOfMonth, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
