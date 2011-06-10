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
import orion.cpu.entities.sys.SubSystem;

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
        LOG.debug("Add subsystem...");
        SubSystem subSystem=iDBSpt.saveOrUpdateSubSystem("Units");
      
        //---------Права----------
        LOG.debug("Add Permissions Maps...");
//        Map<String, Permission> CPermissions = iDBSpt.getPermissionsMap(Chair.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> OUPermissions = iDBSpt.getPermissionsMap(OrgUnit.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
//        //Заполнение тестовых данных
        if (iDBSpt.isFillTestData()) {
            //---------Группы прав----------
            LOG.debug("Add Test PermissionGroups...");
            //Права просмотра записей о подразделениях
            PermissionGroup pgReadOrgUnits = iDBSpt.saveOrUpdatePermissionGroup("Подсистема административно-организационной структуры. Просмотр записей о подразделениях",
                    OUPermissions.get(READ_OP), OUPermissions.get(MENU_OP));

            //Права изменения записей о подразделениях
            PermissionGroup pgManageOrgUnits = iDBSpt.saveOrUpdatePermissionGroup("Подсистема административно-организационной структуры. Управление записями о кафедрах",
                    OUPermissions.get(STORE_OP), OUPermissions.get(UPDATE_OP), OUPermissions.get(REMOVE_OP),
                    OUPermissions.get(MENU_OP));

            //---------Роли----------
            LOG.debug("Add Test Roles...");
            Role roleOUG = iDBSpt.saveOrUpdateRole("OrgUnitGuest",
                    "Перегляд записів про оргпідрозділи", subSystem, pgReadOrgUnits);

            Role roleOUM = iDBSpt.saveOrUpdateRole("OrgUnitOperator",
                    "Оператор управління записами про оргпідрозділи", subSystem,
                    pgReadOrgUnits, pgManageOrgUnits);

            //---------Пользователи----------
            LOG.debug("Add Test Roles to Users...");
            UserController uCnt = iDBSpt.getUserController();
            User user = uCnt.findByLogin("sl");
            user.add(roleOUM);
            uCnt.saveOrUpdate(user);

            user = uCnt.findByLogin("TII");
            user.add(roleOUG);
            uCnt.saveOrUpdate(user);

            user = uCnt.findByLogin("guest");
            user.add(roleOUG);
            uCnt.saveOrUpdate(user);
            
            //Структура КПУ відповідно до наказів ректора #67 від 16.05.2011 та #74 від 16.05.2011
            //---Университет
            University ou_CPU=saveOrUpdateUniversity("Класичний приватний університет", "КПУ", null);
            //---Институты
            Institute iU=saveOrUpdateInstitute("Інститут управління","ІУ",ou_CPU);
            Institute iE=saveOrUpdateInstitute("Інститут економіки","ІЕ",ou_CPU);
            Institute iP=saveOrUpdateInstitute("Інститут права ім. В.Сташиса","ІП",ou_CPU);
            Institute iZhMK=saveOrUpdateInstitute("Інститут журналістики й масової комунікації","ІЖМК",ou_CPU);
            Institute iZST=saveOrUpdateInstitute("Інститут здоров'я, спорту та туризму","ІЗСТ",ou_CPU);
            Institute iIPh=saveOrUpdateInstitute("Інститут іноземної філології","ІІФ",ou_CPU);
            Institute iIST=saveOrUpdateInstitute("Інститут інформаційних та соціальних технологій","ІІСТ",ou_CPU);
            Institute bIDMU=saveOrUpdateInstitute("Бердянський інститут державного та муніципального управління","БІДМУ",ou_CPU);
            
            //---Отделы
            Department dptPorfRetrainingCenter=saveOrUpdateDepartment("Центр професійної перепідготовки та підвищення кваліфікації","ЦПППК",ou_CPU);
            Department dptColledgeCPU=saveOrUpdateDepartment("Колледж КПУ","кол.КПУ",ou_CPU);
            Department dptPreVocationalTraining=saveOrUpdateDepartment("Відділення допрофесійної підготовки","ДПП",ou_CPU);
            Department dptIITO=saveOrUpdateDepartment("Інститут інтерактивних технологій в освіті","ІІТО",ou_CPU);
            Department dptPersManag=saveOrUpdateDepartment("Відділ управління персоналом","ВУП",ou_CPU);
            
            //---Кафедры
                        //---Інститут управління----------
            Chair kafMO       = saveOrUpdateChair("Кафедра менеджменту організацій","КМО",iU);
            Chair kafMZD      = saveOrUpdateChair("Кафедра менеджменту зовнішньоекономічної діяльності","КМЗЕД",iU);
            Chair kafGD       = saveOrUpdateChair("Кафедра гуманітарних дисциплін","КГД",iU);
            Chair kafRT       = saveOrUpdateChair("Кафедра релігієзнавства та теології","КРТ",iU);
            Chair kafIAM      = saveOrUpdateChair("Кафедра інвестиційного та аграрного менеджменту","КІАМ",iU);
            Chair kafAMBA     = saveOrUpdateChair("Кафедра адміністративного менеджменту та бізнес-адміністрування","КАМБА",iU);
            //---Інститут економіки----------
            Chair kafEP       = saveOrUpdateChair("Кафедра економіки підприємства","КЕП",iE);
            Chair kafEKS      = saveOrUpdateChair("Кафедра економічної кібернетики та статистики","КЕКС",iE);
            Chair kafETNPE    = saveOrUpdateChair("Кафедра економічної теорії, національної та прикладної економіки","КЕТНПЕ",iE);
            Chair kafM        = saveOrUpdateChair("Кафедра маркетингу","КМК",iE);
            Chair kafME       = saveOrUpdateChair("Кафедра міжнародної економіки","КМЕ",iE);
            Chair kafOA       = saveOrUpdateChair("Кафедра обліку і аудиту","КОА",iE);
            Chair kafFK       = saveOrUpdateChair("Кафедра фінансів та кредиту","КФК",iE);
            //---Інститут права ім. В.Сташиса----------
            Chair kafKAP      = saveOrUpdateChair("Кафедра конституційного та адміністративного права","ККАП",iP);
            Chair kafKP       = saveOrUpdateChair("Кафедра кримінального права","ККП",iP);
            Chair kafKPK      = saveOrUpdateChair("Кафедра кримінального процесу та криміналістики","ККПК",iP);
            Chair kafMP       = saveOrUpdateChair("Кафедра міжнародного права","КМП",iP);
            Chair kafTZGP     = saveOrUpdateChair("Кафедра трудового, земельного та господарського права","КТЗГП",iP);
            Chair kafGPP      = saveOrUpdateChair("Кафедра цивільного права і процесу","КЦПП",iP);
            Chair kafTIDP     = saveOrUpdateChair("Кафедра теорії та історії держави та права","КТІДП",iP);
            Chair kafVPCO     = saveOrUpdateChair("Кафедра військової підготовки та цивільної оборони","КВПЦО",iP);
            //---Інститут журналістики й масової комунікації----------
            Chair kafZSK      = saveOrUpdateChair("Кафедра журналістики і соціальних комунікацій","КЖСК",iZhMK);
            Chair kafVSRUF    = saveOrUpdateChair("Кафедра видавничої справи, редагування та української філології","КВСРУФ",iZhMK);
            Chair kafRZG      = saveOrUpdateChair("Кафедра реклами і з'язків із громадкістю","КРЗГ",iZhMK);
            //---Інститут здоров'я, спорту та туризму----------
            Chair kafTGG      = saveOrUpdateChair("Кафедра туризму та готельного господарства","КТГГ",iZST);
            Chair kafTOFAV    = saveOrUpdateChair("Кафедра теоретичних основ фізичного та адаптивного виховання","КТОФАВ",iZST);
            Chair kafFR       = saveOrUpdateChair("Кафедра фізичної реабілітації","КФР",iZST);
            Chair kafV        = saveOrUpdateChair("Кафедра фізичного виховання","КФВ",iZST);
            //---Інститут іноземної філології----------
            Chair kafAFZL     = saveOrUpdateChair("Кафедра англійської філології та зарубіжної літератури","КАФЗЛ",iIPh);
//            Chair kafPF       = saveOrUpdateChair("Кафедра перекладу за фахом","КПФ",iIPh);
            Chair kafRGF      = saveOrUpdateChair("Кафедра романо–германської філології","КРГФ",iIPh);
            Chair kafTPP      = saveOrUpdateChair("Кафедра теорії та практики  перекладу","КТПП",iIPh);
            Chair kafIM       = saveOrUpdateChair("Кафедра іноземних мов","КІМ",iIPh);
            //---Інститут інформаційних та соціальних технологій ----------
            Chair kafD        = saveOrUpdateChair("Кафедра дизайну","КД",iIST);
            Chair kafPP       = saveOrUpdateChair("Кафедра практичної психології","КПП",iIST);
            Chair kafPIT      = saveOrUpdateChair("Кафедра програмування та інформаційних технологій", "КПІТ", iIST);
            Chair kafSAVM     = saveOrUpdateChair("Кафедра системного аналізу та вищої математики", "КСАВМ",iIST);
            Chair kafSSR      = saveOrUpdateChair("Кафедра соціології та соціальної роботи","КССР",iIST);
            Chair kafDUZK     = saveOrUpdateChair("Кафедра державного управління та земельного кадастру","КДУЗК",iIST);
            Chair kafEICPHS   = saveOrUpdateChair("Кафедра управління навчальними закладами та педагогіки вищої школи","КПВШ",iIST);
        }
    }

    public University saveOrUpdateUniversity(String name, String shortName, OrgUnit parent) {
        Controller<University, Integer> ouController;
        ouController = iDBSpt.getControllerSource().get(University.class);
        //---Создание образца записи института - наследника абстрактного класса OrgUnit
        University ouSample = new University();
        ouSample.setName(name);
        ouSample.setShortName(shortName);
        ouSample.setParent(parent);

        //--Выборка списка данных записи кафедры
        List<University> ou = ouController.findByExample(ouSample);

        //--Переменная для работы с экземпляром элемента спискка
        University p;
        //--Инициализация элемента списка
        if (ou.isEmpty()) {
            p = new University();
            p.setName(name);
            p.setShortName(shortName);
            p.setParent(parent);

        } else {
            p = ou.get(0);
        }
        return ouController.saveOrUpdate(p);
    }

    public Institute saveOrUpdateInstitute(String name, String shortName, OrgUnit parent) {
        Controller<Institute, Integer> ouController;
        ouController = iDBSpt.getControllerSource().get(Institute.class);
        //---Создание образца записи института - наследника абстрактного класса OrgUnit
        Institute ouSample = new Institute();
        ouSample.setName(name);
        ouSample.setShortName(shortName);
        ouSample.setParent(parent);

        //--Выборка списка данных записи кафедры
        List<Institute> ou = ouController.findByExample(ouSample);

        //--Переменная для работы с экземпляром элемента спискка
        Institute p;
        //--Инициализация элемента списка
        if (ou.isEmpty()) {
            p = new Institute();
            p.setName(name);
            p.setShortName(shortName);
            p.setParent(parent);

        } else {
            p = ou.get(0);
        }
        return ouController.saveOrUpdate(p);
    }

    public Chair saveOrUpdateChair(String name, String shortName, OrgUnit parent) {
        Controller<Chair, Integer> ouController;
        ouController = iDBSpt.getControllerSource().get(Chair.class);
        //---Создание образца записи кафедры - наследника абстрактного класса OrgUnit
        Chair ouSample = new Chair();
        ouSample.setName(name);
        ouSample.setShortName(shortName);
        ouSample.setParent(parent);

        //--Выборка списка данных записи кафедры
        List<Chair> ou = ouController.findByExample(ouSample);

        //--Переменная для работы с экземпляром элемента спискка
        Chair p;
        //--Инициализация элемента списка
        if (ou.isEmpty()) {
            p = new Chair();
            p.setName(name);
            p.setShortName(shortName);
            p.setParent(parent);

        } else {
            p = ou.get(0);
        }
        return ouController.saveOrUpdate(p);
    }

    public Department saveOrUpdateDepartment(String name, String shortName, OrgUnit parent) {
        Controller<Department, Integer> ouController;
        ouController = iDBSpt.getControllerSource().get(Department.class);
        //---Создание образца записи кафедры - наследника абстрактного класса OrgUnit
        Department ouSample = new Department();
        ouSample.setName(name);
        ouSample.setShortName(shortName);
        ouSample.setParent(parent);

        //--Выборка списка данных записи кафедры
        List<Department> ou = ouController.findByExample(ouSample);

        //--Переменная для работы с экземпляром элемента спискка
        Department p;
        //--Инициализация элемента списка
        if (ou.isEmpty()) {
            p = new Department();
            p.setName(name);
            p.setShortName(shortName);
            p.setParent(parent);

        } else {
            p = ou.get(0);
        }
        return ouController.saveOrUpdate(p);
    }
}
