package orion.cpu.views.tapestry.services;

import br.com.arsmachina.authentication.entity.*;
import java.util.*;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.slf4j.*;
import orion.cpu.entities.sys.*;
import orion.cpu.security.OperationTypes;
import orion.cpu.services.impl.InitializeDatabaseSupport;
import orion.tapestry.internal.services.impl.GlobalMessages;
import orion.tapestry.services.FieldLabelSource;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Начальная инициализация данных.
 * @author sl
 */
public class CpuTapestryInitializeDatabase extends OperationTypes implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(CpuTapestryInitializeDatabase.class);
    private final InitializeDatabaseSupport iDBSpt;
    private final Session session;
    private final FieldLabelSource labels;
    private final GlobalMessages messages;
    private final HibernateSessionSource hss;

    public CpuTapestryInitializeDatabase(InitializeDatabaseSupport initializeDatabaseSupport,
            Session session, FieldLabelSource labels, GlobalMessages messages,
            HibernateSessionSource hss) {
        this.iDBSpt = Defense.notNull(initializeDatabaseSupport, "initializeDatabaseSupport");
        this.session = Defense.notNull(session, "session");
        this.labels = Defense.notNull(labels, "labels");
        this.messages = Defense.notNull(messages, "messages");
        this.hss = Defense.notNull(hss, "hss");
    }

    private String prepSQLstr(String s){
        return s.replace("'", "''");
    }

    @Override
    public void run() {
        //Обновим описания в базе даных в соответствии с описаниями в каталоге сообщений
        //Эта часть кода по логике должна быть расположена в ядре, но сервис сообщений объявлен
        //в tapestry-core а ядро от него не зависит
        //TODO Этот код будет верно работать только для PgSQL 
        Iterator<PersistentClass> it = hss.getConfiguration().getClassMappings();
        while (it.hasNext()) {
            PersistentClass pc = it.next();
            Locale locale = new Locale("uk");
            String tableRem = messages.get("reflect." + pc.getMappedClass().getName(), locale);
            String tableName = pc.getTable().getQuotedSchema() == null ? "" : (pc.getTable().getQuotedSchema() + ".") + pc.getTable().getQuotedName();
            if (tableRem != null) {
                SQLQuery query = session.createSQLQuery(String.format("COMMENT ON TABLE %s IS '%s';", tableName, prepSQLstr(tableRem)));
                query.executeUpdate();
            }
            Iterator<Property> pit = pc.getPropertyIterator();
            while (pit.hasNext()) {
                Property p = pit.next();
                String columnRem = labels.get(pc.getMappedClass(), p.getName(), locale);
                if (columnRem != null && p.getColumnIterator().hasNext()) {
                    String columnName = tableName + "." + ((Column) p.getColumnIterator().next()).getQuotedName();
                    SQLQuery query1 = session.createSQLQuery(String.format("COMMENT ON COLUMN %s IS '%s';", columnName, prepSQLstr(columnRem)));
                    query1.executeUpdate();
                }
            }
        }
        if (iDBSpt.isFillTestData()) {
            //---------Права----------
            Map<String, Permission> permissions = iDBSpt.getPermissionsMap(PageTemplate.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
            //---------Группы прав----------
            //Права изменения шаблонов
            PermissionGroup pg = iDBSpt.saveOrUpdatePermissionGroup("Управление шаблонами страниц",
                    permissions.get(STORE_OP), permissions.get(UPDATE_OP), permissions.get(REMOVE_OP),
                    permissions.get(READ_OP), permissions.get(MENU_OP));
            //---------Роли----------
            Role role = iDBSpt.getRoleController().findByLogin("Developer");
            role.add(pg);
            iDBSpt.getRoleController().save(role);
        }
    }
}
