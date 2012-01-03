package org.tynamo.jpa.internal;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.persistence.Table;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.jdbc.Work;
import org.slf4j.*;
import org.tynamo.jpa.*;

/**
 * Создатель схем базы данных. Имена схем берутся из аннотаций javax.persistence.Table
 * всех Hibernate сущностей.
 * Класс является реализацией интерфейса Ejb3HibernateConfigurer только для того что-бы
 * создать схемы до создания EntityManagerFactory.
 * Такое решение продиктовано нерешенностью проблеммы автосоздания схем
 * см. http://opensource.atlassian.com/projects/hibernate/browse/HHH-1853
 * @author sl
 */
public class DatabaseSchemaObjectCreator implements Ejb3HibernateConfigurer {

    private final Logger LOG;
    private final JPAEntityPackageManager packageManager;
    private final ClassNameLocator classNameLocator;
    private final String createSchemaStatement;
    private final String hibernateCfgLocation;

    public DatabaseSchemaObjectCreator(Logger logger, JPAEntityPackageManager packageManager,
            ClassNameLocator classNameLocator,
            @Symbol(Ejb3HibernateSymbols.CREATE_SCHEMA_STATEMENT) String createSchemaStatement,
            @Symbol(Ejb3HibernateSymbols.HIBERNATE_CONFIG_LOCATION) String hibernateCfgLocation) {
        this.LOG = logger;
        this.packageManager = packageManager;
        this.classNameLocator = classNameLocator;
        this.createSchemaStatement = createSchemaStatement;
        this.hibernateCfgLocation = hibernateCfgLocation;
    }

    /**
     * Метод не использует переданную конфигурацию, а создает новую, соединяется с базой
     * и создает недостающие схемы.
     * @param configuration не используется
     */
    @Override
    public void configure(Ejb3Configuration configuration) {
        final Set<String> schemas = new TreeSet<String>();
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
            Configuration cfg = new Configuration().configure(hibernateCfgLocation);

            SessionFactory sessionFactory = cfg.buildSessionFactory();
            Session session = sessionFactory.openSession();
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    DatabaseMetaData mData = connection.getMetaData();
                    if (mData.supportsSchemasInTableDefinitions()) {
                        ResultSet rs = mData.getSchemas();
                        while (rs.next()) {
                            String schema = rs.getString("TABLE_SCHEM");
                            Iterator<String> it = schemas.iterator();
                            while (it.hasNext()) {
                                String expectedSchema = it.next();
                                boolean isQuoted = false;
                                if (expectedSchema.startsWith("`")) {
                                    expectedSchema = expectedSchema.substring(1, expectedSchema.length() - 1);
                                    if (!(mData.storesLowerCaseQuotedIdentifiers() && expectedSchema.equals(expectedSchema.toLowerCase()))
                                            && !(mData.storesUpperCaseQuotedIdentifiers() && expectedSchema.equals(expectedSchema.toUpperCase()))) {
                                        isQuoted = true;
                                    }
                                }


                                if (isQuoted) {
                                    if (mData.storesLowerCaseQuotedIdentifiers()
                                            || mData.storesUpperCaseQuotedIdentifiers()) {
                                        if (expectedSchema.equalsIgnoreCase(schema)) {
                                            it.remove();
                                        }
                                    } else {
                                        if (expectedSchema.equals(schema)) {
                                            it.remove();
                                        }
                                    }
                                } else {
                                    if (mData.storesLowerCaseIdentifiers()
                                            || mData.storesUpperCaseIdentifiers()) {
                                        if (expectedSchema.equalsIgnoreCase(schema)) {
                                            it.remove();
                                        }
                                    } else {
                                        if (expectedSchema.equals(schema)) {
                                            it.remove();
                                        }
                                    }
                                }
                            }
                        }
                        rs.close();

                        connection.setAutoCommit(true);
                        Statement st = connection.createStatement();
                        String quotes = mData.getIdentifierQuoteString();
                        for (String schema : schemas) {
                            try {
                                st.execute(String.format(createSchemaStatement,
                                        schema.replace("`".subSequence(0, 1),
                                        quotes.subSequence(0, quotes.length()))));
                                LOG.debug("Created schema {}", schema);
                            } catch (HibernateException ex) {
                                LOG.warn(ex.getMessage());
                            }
                        }
                    }
                }
            });
            session.close();
            sessionFactory.close();
        }
    }
}
