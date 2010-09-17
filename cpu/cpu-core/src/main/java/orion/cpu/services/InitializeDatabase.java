package orion.cpu.services;

import br.com.arsmachina.authentication.entity.*;
import br.com.arsmachina.module.service.EntitySource;
import org.slf4j.*;
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
        //Сохранение в базе пользователя SYSTEM
        User SYS = saveOrUpdateUser(User.SYSTEM_USER.getName(), User.SYSTEM_USER.getPassword(),
                User.SYSTEM_USER.getName(), User.SYSTEM_USER.getEmail(), null);
        SYS.setEnabled(User.SYSTEM_USER.isEnabled());
        initDBSupport.getUserController().update(SYS);
        if (initDBSupport.isFillTestData()) {
            //---------Пользователи----------
            saveOrUpdateUser("sl", "123456", "Михаил Слободянюк", "slobodyanukma@ukr.net", "uk");
            saveOrUpdateUser("TII", "123456", "Ирина Тесленко", "bbb@aaa.net", "ru");
            saveOrUpdateUser("guest", "", "Гость информационной системы КПУ", "guest@cpu.edu", null);
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
