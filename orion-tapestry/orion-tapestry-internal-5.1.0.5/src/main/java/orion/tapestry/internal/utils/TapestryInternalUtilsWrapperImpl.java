package orion.tapestry.internal.utils;

import org.apache.tapestry5.internal.TapestryInternalUtils;

/**
 *
 * @author sl
 */
public class TapestryInternalUtilsWrapperImpl implements orion.tapestry.services.TapestryInternalUtilsWrapper {

//Сдублироан алгоритм вычисления надписи из TapestryInternalUtils.defaultLabel
    @Override
    public String defaultLabel(String id, String propertyExpression)
    {
        return TapestryInternalUtils.toUserPresentable(TapestryInternalUtils.extractIdFromPropertyExpression(TapestryInternalUtils.lastTerm(propertyExpression)));
    }

}
