package orion.cpu.views.tapestry.utils;

/**
 *
 * @author sl
 */
public class CpuTapestryUtils {

    private CpuTapestryUtils() {
    }

    public static String subSystemNameByMenupath(String menupath) {
        int startLength = "Start>".length();
        if (menupath == null || menupath.length() <= startLength) {
            return null;
        }
        String subKey = menupath.substring(startLength);
        int gtpos = subKey.indexOf(">");
        if (gtpos > 0) {
            subKey = subKey.substring(0, subKey.indexOf(">"));
        }
        return subKey;
    }
}
