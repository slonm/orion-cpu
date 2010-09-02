package orion.cpu.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.persistence.Table;
import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateEntityPackageManager;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.slf4j.*;

/**
 * Создатель схем базы данных. Только для PostgreSQL. Имена схем берутся из аннотаций javax.persistence.Table
 * всех Hibernate сущностей.
 * Класс является реализацией интерфейса HibernateConfigurer только для того что-бы
 * создать схемы до создания SessionFactory.
 * Такое решение продиктовано нерешенностью проблеммы автосоздания схем
 * см. http://opensource.atlassian.com/projects/hibernate/browse/HHH-1853
 * @author sl
 */
public class DatabaseSchemaObjectCreator implements HibernateConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSchemaObjectCreator.class);
    private final HibernateEntityPackageManager packageManager;
    private final ClassNameLocator classNameLocator;
    private String dbmsName = null;
    private String dbName = null;

    public DatabaseSchemaObjectCreator(HibernateEntityPackageManager packageManager,
            ClassNameLocator classNameLocator) {
        this.packageManager = packageManager;
        this.classNameLocator = classNameLocator;
    }

    /**
     * Метод не использует переданную конфигурацию, а создает новую, соединяется с базой
     * и создает недостающие схемы.
     * @param configuration не используется
     */
    @Override
    public void configure(Configuration configuration) {
        Set<String> schemas = new TreeSet<String>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (String packageName : packageManager.getPackageNames()) {
            for (String className : classNameLocator.locateClassNames(packageName)) {
                try {
                    Class entityClass = contextClassLoader.loadClass(className);
                    Table JPATable = (Table) entityClass.getAnnotation(Table.class);
                    if (JPATable != null && JPATable.schema() != null && JPATable.schema().trim().length() > 0) {
                        schemas.add(JPATable.schema());
                    }
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if (schemas.size() > 0) {
            SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    dbmsName = connection.getMetaData().getDatabaseProductName();
                    dbName = connection.getMetaData().getURL();
                    dbName = dbName.substring(dbName.lastIndexOf("/") + 1);
                }
            });
            //создаем схемы только для postgresql
            if ("PostgreSQL".equalsIgnoreCase(dbmsName)) {
                Transaction transaction = session.beginTransaction();
                List<String> list = session.createSQLQuery("select schema_name from information_schema.schemata where catalog_name='" + dbName + "'").list();
                for (String schemaName : list) {
                    if (schemas.contains(schemaName)) {
                        schemas.remove(schemaName);
                    }
                }
                transaction.rollback();
                for (final String schema : schemas) {
                    transaction = session.beginTransaction();
                    try {
                        session.createSQLQuery(String.format("CREATE SCHEMA %s", schema)).executeUpdate();
                        transaction.commit();
                        LOG.debug("Created schema {}", schema);
                    } catch (HibernateException ex) {
                        transaction.rollback();
                    }
                }
            }
            session.close();
            sessionFactory.close();
        }
    }
}