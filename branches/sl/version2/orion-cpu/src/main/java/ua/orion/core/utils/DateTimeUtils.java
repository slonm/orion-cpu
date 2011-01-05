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
    
    public static Date createDate(int dayOfMonth, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }

}
