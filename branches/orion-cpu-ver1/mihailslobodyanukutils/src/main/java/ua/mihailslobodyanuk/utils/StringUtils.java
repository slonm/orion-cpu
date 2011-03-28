/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.mihailslobodyanuk.utils;

/**
 *
 * @author sl
 */
public class StringUtils {

    private StringUtils() {
    }
    /**
     * Capitalizes a string, converting the first character to uppercase.
     */
    public static String capitalize(String input) {
        if (input.length() == 0) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
