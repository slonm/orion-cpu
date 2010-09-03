package orion.tapestry.internal.utils;

import org.apache.tapestry5.internal.TapestryInternalUtils;

/**
 *
 * @author sl
 */
public class TapestryInternalUtilsWrapper {

//Сдублироан алгоритм вычисления надписи из TapestryInternalUtils.defaultLabel
    public static String defaultLabel(String id, String propertyExpression)
    {
        return TapestryInternalUtils.toUserPresentable(TapestryInternalUtils.extractIdFromPropertyExpression(TapestryInternalUtils.lastTerm(propertyExpression)));
    }

}
