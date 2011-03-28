package orion.cpu.views.birt;

import java.net.URL;
import org.apache.tapestry5.ioc.internal.services.ClassNameLocatorImpl;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;

//import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.*;
import org.hibernate.cfg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    public static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class.getName());
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration cfg = new Configuration();
            cfg.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            String packageNames[] = {"br.com.arsmachina.authentication.entity", "orion.cpu.entities"};
            for (String packageName : packageNames) {
                //cfg.addPackage(packageName);
                ClassNameLocator locator = new ClassNameLocatorImpl(new ClasspathURLConverter() {

                    @Override
                    public URL convert(URL url) {
                        return url;
                    }
                });
                for (String className : locator.locateClassNames(packageName)) {
                    try {
                        Class entityClass = contextClassLoader.loadClass(className);
                        cfg.addAnnotatedClass(entityClass);
                        logger.info(entityClass.getName());
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            sessionFactory = cfg.configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log exception!
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession()
            throws HibernateException {
        return sessionFactory.openSession();
    }

    public static void main(String[] argv){}
}


