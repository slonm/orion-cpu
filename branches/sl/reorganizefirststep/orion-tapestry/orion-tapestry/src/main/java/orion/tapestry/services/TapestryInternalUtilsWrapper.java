/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.tapestry.services;

/**
 *
 * @author sl
 */
public interface TapestryInternalUtilsWrapper {
//Сдублироан алгоритм вычисления надписи из TapestryInternalUtils.defaultLabel
    public String defaultLabel(String id, String propertyExpression);
}
