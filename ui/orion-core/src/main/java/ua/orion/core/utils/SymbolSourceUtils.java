/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.utils;

import java.util.Enumeration;
import java.util.ResourceBundle;
import org.apache.tapestry5.ioc.MappedConfiguration;

/**
 *
 * @author sl
 */
public class SymbolSourceUtils {

    public static void readDefaultsFromResourceBundle(String resourceBundleName,
            MappedConfiguration<String, String> configuration) {
        ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName);
        Enumeration<String> e = bundle.getKeys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            String value = bundle.getString(key);
            configuration.add(key, value);
        }
    }
}
