package orion.cpu.views.tapestry.services;

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
public class CpuTapestryInitializeDatabase extends OperationTypes implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(CpuTapestryInitializeDatabase.class);
    private final InitializeDatabaseSupport iDBSpt;

    public CpuTapestryInitializeDatabase(InitializeDatabaseSupport initializeDatabaseSupport) {
        this.iDBSpt = Defense.notNull(initializeDatabaseSupport, "initializeDatabaseSupport");
    }

    @Override
    public void run() {
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
