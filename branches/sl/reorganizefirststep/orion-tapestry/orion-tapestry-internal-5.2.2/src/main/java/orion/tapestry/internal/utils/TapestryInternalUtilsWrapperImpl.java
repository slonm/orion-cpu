package orion.tapestry.internal.utils;

import org.apache.tapestry5.internal.TapestryInternalUtils;
import orion.tapestry.services.TapestryInternalUtilsWrapper;

/**
 *
 * @author sl
 */
public class TapestryInternalUtilsWrapperImpl implements TapestryInternalUtilsWrapper{

//Сдублироан алгоритм вычисления надписи из TapestryInternalUtils.defaultLabel
    public String defaultLabel(String id, String propertyExpression)
    {
        return TapestryInternalUtils.toUserPresentable(TapestryInternalUtils.extractIdFromPropertyExpression(TapestryInternalUtils.lastTerm(propertyExpression)));
    }

}
