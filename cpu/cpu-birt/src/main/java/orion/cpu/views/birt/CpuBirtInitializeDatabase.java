package orion.cpu.views.birt;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.*;
import java.util.*;
import org.slf4j.*;
import orion.cpu.entities.sys.*;
import orion.cpu.security.OperationTypes;
import orion.cpu.services.impl.InitializeDatabaseSupport;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Начальная инициализация данных.
 * @author sl
 */
public class CpuBirtInitializeDatabase extends OperationTypes implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(CpuBirtInitializeDatabase.class);
    private final InitializeDatabaseSupport iDBSpt;

    public CpuBirtInitializeDatabase(InitializeDatabaseSupport initializeDatabaseSupport) {
        this.iDBSpt = Defense.notNull(initializeDatabaseSupport, "initializeDatabaseSupport");
    }

    @Override
    public void run() {
        //---------Права----------
        Map<String, Permission> permissions = iDBSpt.getPermissionsMap(ReportTemplate.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        if (iDBSpt.isFillTestData()) {
            //---------Группы прав----------
            //Права изменения шаблонов
            PermissionGroup pg = iDBSpt.saveOrUpdatePermissionGroup("Администрирование. Управление шаблонами отчетов",
                    permissions.get(STORE_OP), permissions.get(UPDATE_OP), permissions.get(REMOVE_OP),
                    permissions.get(READ_OP), permissions.get(MENU_OP));
            //---------Роли----------
            Role role = iDBSpt.saveOrUpdateRole("RPTDesignAdmin",
                    "Администратор макетов отчетов", pg);
            //---------Пользователи----------
            UserController uCnt = iDBSpt.getUserController();
            User user = uCnt.findByLogin("sl");
            user.add(role);
            for (PermissionGroup pg1 : role.getPermissionGroups()) {
                user.add(pg1);
            }
            uCnt.saveOrUpdate(user);
        }
    }
}