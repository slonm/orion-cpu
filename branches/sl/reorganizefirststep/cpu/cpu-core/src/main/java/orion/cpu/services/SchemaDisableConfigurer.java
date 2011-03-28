package orion.cpu.services;

import java.util.Iterator;
import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

/**
 * Заменяет схему на префикс имени таблицы - для СУБД которые не поддерживают схемы
 * @author sl
 */
public class SchemaDisableConfigurer implements HibernateConfigurer {

    private DatabaseSchemaObjectCreator databaseSchemaObjectCreator;

    public SchemaDisableConfigurer(DatabaseSchemaObjectCreator databaseSchemaObjectCreator) {
        this.databaseSchemaObjectCreator = databaseSchemaObjectCreator;
    }

    @Override
    public void configure(Configuration configuration) {
        if (!databaseSchemaObjectCreator.getCanUseSchema()) {
        }
    }
}
