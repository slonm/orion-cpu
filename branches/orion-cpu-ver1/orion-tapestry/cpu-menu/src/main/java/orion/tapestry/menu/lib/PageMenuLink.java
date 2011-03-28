/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Реализация класса IMenuLink, представляет ссылку на страницу.
 * @author Gennadiy Dobrovolsky
 */
public class PageMenuLink implements IMenuLink {

    /**
     * Page class object
     */
    protected Class<?> pageClass;
    /**
     * anchor part of the URL
     */
    protected String anchor;
    /**
     * Context part of the URL (Tapestry specific)
     * persistent part of the context
     */
    protected Object[] contextPersistent;
    /**
     * Context part of the URL (Tapestry specific)
     * variable part of the context
     */
    protected Object[] contextVariable;

    /**
     * Persistent parameters part of the URL
     */
    protected Map<String, String> parameterPersistent=new HashMap<String, String>();

    /**
     * Variablepersistent parameters part of the URL
     */
    protected Map<String, String> parameterVariable=new HashMap<String, String>();

    /**
     * Конструктор класса.
     * В конструкторе задаётся постоянная часть URL
     */
    public PageMenuLink(Class<?> _pageClass, Object... _context) {
        this.pageClass = _pageClass;
        this.contextPersistent = _context;
    }

    public PageMenuLink() {
    }

    @Override
    public String getAnchor() {
        return anchor;
    }

    @Override
    public IMenuLink setAnchor(String value) {
        anchor = value;
        return this;
    }

    @Override
    public String getParameter(String name) {
        if (parameterPersistent.containsKey(name)) {
            return parameterPersistent.get(name);
        }
        if (parameterVariable.containsKey(name)) {
            return parameterVariable.get(name);
        }
        return null;
    }

    @Override
    public IMenuLink setParameterVariable(String name, String value) {
        parameterVariable.put(name, value);
        return this;
    }

    @Override
    public IMenuLink setParameterVariable(Map<String, String> _paramеters) {
        parameterVariable.putAll(_paramеters);
        return this;
    }

    @Override
    public IMenuLink setParameterPersistent(String name, String value) {
        parameterPersistent.put(name, value);
        return this;
    }

    @Override
    public IMenuLink setParameterPersistent(Map<String, String> _paramеters) {
        parameterPersistent.putAll(_paramеters);
        return this;
    }

    @Override
    public IMenuLink setContextPersistent(Object... _context) {
        this.contextPersistent = _context;
        return this;
    }

    @Override
    public IMenuLink setContextVariable(Object... _context) {
        this.contextVariable = _context;
        return this;
    }

    @Override
    public Object[] getContext() {
        ArrayList<Object> a = new ArrayList<Object>();
        if (contextPersistent != null) {
            for (Object s : contextPersistent) {
                a.add(s);
            }
        }
        if (contextVariable != null) {
            for (Object s : contextVariable) {
                a.add(s);
            }
        }
        return a.toArray();
    }

    @Override
    public IMenuLink setPageClass(Class<?> _pageClass) {
        this.pageClass = _pageClass;
        return this;
    }

    @Override
    public Class<?> getPageClass() {
        return this.pageClass;
    }

    @Override
    public IMenuLink setPath(String pageClassName) {
        try {
            this.pageClass = Class.forName(pageClassName);
        } catch (ClassNotFoundException ex) {
            Logger logger = LoggerFactory.getLogger(PageMenuLink.class);
            logger.info(pageClassName + " - class not found ");
        }
        return this;
    }

    @Override
    public String getPath() {
        return this.pageClass.getCanonicalName();
    }

    @Override
    public Map<String, String> getParameters() {
        @SuppressWarnings("unchecked")
        Map<String, String> m = new TreeMap<String, String>();
        m.putAll(parameterVariable);
        m.putAll(parameterPersistent);
        return m;
    }

    @Override
    public IMenuLink setScheme(String scheme) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getScheme() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IMenuLink setLogin(String login) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLogin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IMenuLink setPassword(String pwd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IMenuLink setHost(String host) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getHost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IMenuLink setPort(int port) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
