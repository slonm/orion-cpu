package orion.cpu.services;

import br.com.arsmachina.authentication.entity.*;
import br.com.arsmachina.module.service.EntitySource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.*;
import orion.cpu.entities.sys.SubSystem;
import orion.cpu.security.OperationTypes;
import orion.cpu.services.impl.InitializeDatabaseSupport;
import orion.cpu.utils.PossibleOperations;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Начальная инициализация данных.
 * @author sl
 */
public class InitializeDatabase extends OperationTypes implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(InitializeDatabase.class);
    private final InitializeDatabaseSupport initDBSupport;
    private final EntitySource entitySource;

    public InitializeDatabase(InitializeDatabaseSupport initDBSupport, EntitySource entitySource) {
        this.initDBSupport = Defense.notNull(initDBSupport, "initDBSupport");
        this.entitySource = Defense.notNull(entitySource, "entitySource");
    }

    @Override
    public void run() {
        //Создание объектов - прав доступа для всех сущностей
        //по аннотации PossibleOperations
        for (Class<?> c : entitySource.getEntityClasses()) {
            while (c != null) {
                PossibleOperations po = c.getAnnotation(PossibleOperations.class);
                if (po != null) {
                    for (String permissionType : po.value()) {
                        initDBSupport.getPermissionsMap(c, permissionType);
                    }
                    if (!po.inherit()) {
                        break;
                    }
                }
                c = c.getSuperclass();
            }
        }
        SubSystem subSystem = initDBSupport.saveOrUpdateSubSystem("Admin");
        //Сохранение в базе пользователя SYSTEM
        User SYS = saveOrUpdateUser(User.SYSTEM_USER.getName(), User.SYSTEM_USER.getPassword(),
                User.SYSTEM_USER.getName(), User.SYSTEM_USER.getEmail(), null);
        SYS.setEnabled(User.SYSTEM_USER.isEnabled());
        initDBSupport.getUserController().update(SYS);
        if (initDBSupport.isFillTestData()) {
            //---------Группы прав----------
            //Права управления всеми справочниками
            Set<Permission> allReference = new HashSet<Permission>();
            for (Class<?> e : entitySource.getEntityClasses()) {
                javax.persistence.Table a = e.getAnnotation(javax.persistence.Table.class);
                if (a != null && "ref".equals(a.schema())) {
                    Map<String, Permission> permissions = initDBSupport.getPermissionsMap(e, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
                    allReference.addAll(permissions.values());
                }
            }
            PermissionGroup pg=null;
            for (Permission p : allReference) {
                pg = initDBSupport.saveOrUpdatePermissionGroup("Управління довідниками", p);
            }
            //---------Роли----------
            Role role = initDBSupport.saveOrUpdateRole("Developer",
                    "Розробник", subSystem, pg);
            //---------Пользователи----------
            User user = saveOrUpdateUser("sl", "123456", "Адміністратор ІС КПУ", "admin@cpu.edu", "uk");
            saveOrUpdateUser("TII", "123456", "Оператор підсистем ІС КПУ", "operator@cpu.edu", "ru");
            saveOrUpdateUser("guest", "123456", "Гість ІС КПУ", "guest@cpu.edu", null);
            user.add(role);
            initDBSupport.getUserController().saveOrUpdate(user);
        }
    }

    private User saveOrUpdateUser(String login, String password, String name, String email, String locale) {
        User p = initDBSupport.getUserController().findByLogin(login);
        if (p == null) {
            p = new User(login, password, name, email);
        } else {
            p.setName(name);
            p.setPassword(password);
            p.setEmail(email);
        }
        p.setLocale(locale);
        return initDBSupport.getUserController().saveOrUpdate(p);
    }
}
