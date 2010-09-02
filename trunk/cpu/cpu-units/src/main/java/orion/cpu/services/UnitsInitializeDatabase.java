package orion.cpu.services;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authentication.entity.PermissionGroup;
import br.com.arsmachina.authentication.entity.Role;
import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.controller.Controller;
import orion.cpu.security.OperationTypes;
import orion.cpu.services.impl.InitializeDatabaseSupport;
import ua.mihailslobodyanuk.utils.Defense;
import orion.cpu.entities.org.*;
import java.util.*;
import org.slf4j.*;

/**
 * Начальная инициализация данных.
 * @author sl
 */
public class UnitsInitializeDatabase extends OperationTypes implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(UnitsInitializeDatabase.class);
    private final InitializeDatabaseSupport iDBSpt;

    public UnitsInitializeDatabase(InitializeDatabaseSupport initializeDatabaseSupport) {
        this.iDBSpt = Defense.notNull(initializeDatabaseSupport, "initializeDatabaseSupport");
    }

    @Override
    public void run() {
        //---------Права----------
        Map<String, Permission> CPermissions = iDBSpt.getPermissionsMap(Chair.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> OUPermissions = iDBSpt.getPermissionsMap(OrgUnit.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
//        //Заполнение тестовых данных
        if (iDBSpt.isFillTestData()) {
            //---------Группы прав----------
            //Права просмотра записей о подразделениях
            PermissionGroup pgReadOrgUnits = iDBSpt.saveOrUpdatePermissionGroup("Подсистема административно-организационной структуры. Просмотр записей о подразделениях",
                    CPermissions.get(READ_OP), OUPermissions.get(READ_OP),
                    CPermissions.get(MENU_OP));

            //Права изменения записей о подразделениях
            PermissionGroup pgManageOrgUnits = iDBSpt.saveOrUpdatePermissionGroup("Подсистема административно-организационной структуры. Управление записями о кафедрах",
                    CPermissions.get(STORE_OP), CPermissions.get(UPDATE_OP), CPermissions.get(REMOVE_OP),
                    OUPermissions.get(STORE_OP), OUPermissions.get(UPDATE_OP), OUPermissions.get(REMOVE_OP),
                    CPermissions.get(MENU_OP));

            //---------Роли----------
            Role roleOUG = iDBSpt.saveOrUpdateRole("OrgUnitGuest",
                    "Просмотр записей об оргподразделениях", pgReadOrgUnits);

            Role roleOUM = iDBSpt.saveOrUpdateRole("OrgUnitOperator",
                    "Оператор управления записями о подразделениях",
                    pgReadOrgUnits, pgManageOrgUnits);

            //---------Пользователи----------
            UserController uCnt = iDBSpt.getUserController();
            User user = uCnt.findByLogin("sl");
            user.add(roleOUM);
            for (PermissionGroup pg : roleOUM.getPermissionGroups()) {
                user.add(pg);
            }
            uCnt.saveOrUpdate(user);

            user = uCnt.findByLogin("TII");
            user.add(roleOUG);
            for (PermissionGroup pg : roleOUG.getPermissionGroups()) {
                user.add(pg);
            }
            uCnt.saveOrUpdate(user);

//        //---Кафедры, выполняющие обучение по лицензиям----------
            Chair kafPIT = saveOrUpdateChair("кафедра програмування та інформаційних технологій", "КПІТ");
            Chair kafEICPHS = saveOrUpdateChair("кафедра управління навчальними закладами та педагогіки вищої школи", "КУНЗПВШ");
        }
    }

    public Chair saveOrUpdateChair(String name, String shortName) {
        Controller<Chair, Integer> ouController;
        ouController = iDBSpt.getControllerSource().get(Chair.class);
        //---Создание образца записи кафедры - наследника абстрактного класса Kafedra
        Chair ouSample = new Chair();
        ouSample.setName(name);
        ouSample.setShortName(shortName);

        //--Выборка списка данных записи кафедры
        List<Chair> ou = ouController.findByExample(ouSample);

        //--Переменная для работы с экземпляром элемента спискка
        Chair p;
        //--Инициализация элемента списка
        if (ou.size() == 0) {
            p = new Chair();
            p.setName(name);
            p.setShortName(shortName);

        } else {
            p = ou.get(0);
        }
        return ouController.saveOrUpdate(p);
    }
}