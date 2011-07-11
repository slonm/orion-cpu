/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.birt.services;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 *
 * @author sl
 */
public abstract class ServletConfigWrapper implements ServletConfig {

    public static final Map<String, String> initParametrs = new HashMap<String, String>();
    private ServletContext servletContext;

    public ServletConfigWrapper(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContextWrapper;
    }

    @Override
    public String getInitParameter(String name) {
        return servletContextWrapper.getInitParameter(name);
    }

    @Override
    public Enumeration getInitParameterNames() {
        return servletContextWrapper.getInitParameterNames();
    }
    public final ServletContext servletContextWrapper = new ServletContext() {

        public void setAttribute(String name, Object object) {
            servletContext.setAttribute(name, object);
        }

        public void removeAttribute(String name) {
            servletContext.removeAttribute(name);
        }

        public void log(String message, Throwable throwable) {
            servletContext.log(message, throwable);
        }

        public void log(Exception exception, String msg) {
            servletContext.log(exception, msg);
        }

        public void log(String msg) {
            servletContext.log(msg);
        }

        public Enumeration getServlets() {
            return servletContext.getServlets();
        }

        public Enumeration getServletNames() {
            return servletContext.getServletNames();
        }

        public String getServletContextName() {
            return servletContext.getServletContextName();
        }

        public Servlet getServlet(String name) throws ServletException {
            return servletContext.getServlet(name);
        }

        public String getServerInfo() {
            return servletContext.getServerInfo();
        }

        public Set getResourcePaths(String path) {
            return servletContext.getResourcePaths(path);
        }

        public InputStream getResourceAsStream(String path) {
            return servletContext.getResourceAsStream(path);
        }

        public URL getResource(String path) throws MalformedURLException {
            return servletContext.getResource(path);
        }

        public RequestDispatcher getRequestDispatcher(String path) {
            return servletContext.getRequestDispatcher(path);
        }

        public String getRealPath(String path) {
            return servletContext.getRealPath(path);
        }

        public RequestDispatcher getNamedDispatcher(String name) {
            return servletContext.getNamedDispatcher(name);
        }

        public int getMinorVersion() {
            return servletContext.getMinorVersion();
        }

        public String getMimeType(String file) {
            return servletContext.getMimeType(file);
        }

        public int getMajorVersion() {
            return servletContext.getMajorVersion();
        }

        public Enumeration getInitParameterNames() {
            final Iterator<String> it = initParametrs.keySet().iterator();
            return new Enumeration() {

                @Override
                public boolean hasMoreElements() {
                    return it.hasNext();
                }

                @Override
                public Object nextElement() {
                    return it.next();
                }
            };
        }

        public String getInitParameter(String name) {
            return initParametrs.get(name);
        }

        public ServletContext getContext(String uripath) {
            return servletContext.getContext(uripath);
        }

        public Enumeration getAttributeNames() {
            return servletContext.getAttributeNames();
        }

        public Object getAttribute(String name) {
            return servletContext.getAttribute(name);
        }

        @Override
        public String getContextPath() {
            return servletContext.getContextPath();
        }
    };
}
