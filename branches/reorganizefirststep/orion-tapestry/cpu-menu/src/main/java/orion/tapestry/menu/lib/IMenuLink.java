/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.lib;

import java.util.Map;

/**
 * @author Gennadiy Dobrovolsky
 * Интерфейс структуры данных для представления ссылки.
 * Возвращает все параметры, необходимые для составления ссылки
 * Используется при составлении навигационного меню.
 */
public interface IMenuLink {

    /**
     * Get anchor part of the URL
     * @return
     */
    public String getAnchor();

    /**
     * Set anchor part of the URL
     * @param value is anchor value
     * @return current object
     */
    public IMenuLink setAnchor(String value);

    /**
     * Get named parameter of the URL
     * @param name parameter name
     * @return parameter value
     */
    public String getParameter(String name);

    /**
     * Get all named parameters of the URL
     * @return parameter value
     */
    public Map<String, String> getParameters();

    /**
     * Set named parameter of the URL
     * Parameters in the URL are grouped in two classes
     * i.e. persistent and variable ones.
     * Persistent parameters are not changed and have biggest priority vs variable
     * @param name parameter name
     * @param value parameter value
     * @return current object
     */
    public IMenuLink setParameterPersistent(String name, String value);

    /**
     * Set named parameter of the URL
     * Parameters in the URL are grouped in two classes
     * i.e. persistent and variable ones.
     * Persistent parameters are not changed and have biggest priority vs variable
     * @param _paramеters list of key-value pairs
     * @return current object
     */
    public IMenuLink setParameterPersistent(Map<String, String> _paramеters);

    /**
     * Set named parameter of the URL
     * Parameters in the URL are grouped in two classes
     * i.e. persistent and variable ones.
     * Persistent parameters are not changed and have biggest priority vs variable
     * @param name parameter name
     * @param value parameter value
     * @return current object
     */
    public IMenuLink setParameterVariable(String name, String value);

    /**
     * Set named parameter of the URL
     * Parameters in the URL are grouped in two classes
     * i.e. persistent and variable ones.
     * Persistent parameters are not changed and have biggest priority vs variable
     * @param _paramеters list of key-value pairs
     * @return current object
     */
    public IMenuLink setParameterVariable(Map<String, String> _paramеters);

    /**
     * Set context objects (Context is Tapestry-specific concept).
     * 
     * @param context
     * @return current object
     */
    public IMenuLink setContextPersistent(Object... context);

    /**
     * Set context objects (Context is Tapestry-specific concept).
     *
     * @param context
     * @return current object
     */
    public IMenuLink setContextVariable(Object... context);

    /**
     * Get context objects. Context is Tapestry-specific concept
     * @return array of context objects
     */
    public Object[] getContext();

    /**
     * set page class
     * @param pageClass tapestry page class
     * @return current object
     */
    public IMenuLink setPageClass(Class<?> pageClass);

    /**
     * get page class
     * @return current object
     */
    public Class<?> getPageClass();

    /**
     * set page class
     * @param path string representation of the
     * @return current object
     */
    public IMenuLink setPath(String path);

    /**
     * get page path
     * @return current object
     */
    public String getPath();

    /**
     * Set URL scheme
     * @param scheme string representing URL scheme like "http", "https" etc.
     * @return current object
     */
    public IMenuLink setScheme(String scheme);

    /**
     * @return current URL scheme like "http", "https" etc.
     */
    public String getScheme();

    /**
     * Set URL login
     * @param login part ot the URL
     * @return current object
     */
    public IMenuLink setLogin(String login);

    /**
     * @return login path of the current URL
     */
    public String getLogin();

    /**
     * Set URL password
     * @param pwd password part ot the URL
     * @return current object
     */
    public IMenuLink setPassword(String pwd);

    /**
     * @return password path of the current URL
     */
    public String getPassword();

    /**
     * Set URL host
     * @param host part ot the URL
     * @return current object
     */
    public IMenuLink setHost(String host);

    /**
     * @return host path of the current URL
     */
    public String getHost();

    /**
     * Set URL port
     * @param port port part ot the URL
     * @return current object
     */
    public IMenuLink setPort(int port);

    /**
     * @return port path of the current URL
     */
    public int getPort();
}
