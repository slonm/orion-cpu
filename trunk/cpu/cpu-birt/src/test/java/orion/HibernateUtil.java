/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import orion.cpu.entities.ref.KnowledgeAreaOrTrainingDirection;
import orion.cpu.entities.ref.TrainingDirectionOrSpeciality;

/**
 *
 * @author sl
 */
public class HibernateUtil {

    private static AnnotationConfiguration configuration = null;
    private static SessionFactory sessionFactory = null;

    private static AnnotationConfiguration buildConfiguration() {
        AnnotationConfiguration ac = new AnnotationConfiguration();
        ac.addAnnotatedClass(KnowledgeAreaOrTrainingDirection.class);
        ac.addAnnotatedClass(TrainingDirectionOrSpeciality.class);
        ac.configure();
        return ac;
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return getConfiguration().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static AnnotationConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = buildConfiguration();
        }
        return configuration;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory == null) {
            return;
        }
        sessionFactory.close();
        sessionFactory = null;
    }
}
