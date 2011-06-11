/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.mihailslobodyanuk.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static ua.mihailslobodyanuk.utils.Defense.*;

/**
 *
 * @author sl
 */
public class BeanUtils {
    /**
     * Pattern used to eliminate leading and trailing underscores and dollar signs.
     */
    private static final Pattern NAME_PATTERN = Pattern.compile("^[_|$]*([\\w|$]+?)[_|$]*$", Pattern.CASE_INSENSITIVE);
    /**
     * Strips leading "_" and "$" and trailing "_" from the name.
     */
    public static String stripMemberName(String memberName) {
        notBlank(memberName, "memberName");

        Matcher matcher = NAME_PATTERN.matcher(memberName);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Input '%s' is not a valid Java identifier.", memberName));
        }

        return matcher.group(1);
    }

}
