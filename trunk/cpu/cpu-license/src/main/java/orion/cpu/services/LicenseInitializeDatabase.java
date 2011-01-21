package orion.cpu.services;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.*;
import br.com.arsmachina.controller.Controller;
import java.util.*;
import org.slf4j.*;
import orion.cpu.controllers.NamedEntityController;
import orion.cpu.entities.ref.*;
import orion.cpu.entities.uch.*;
import orion.cpu.entities.pub.*;
import orion.cpu.entities.org.*;
import orion.cpu.entities.sys.SubSystem;
import orion.cpu.security.OperationTypes;
import orion.cpu.services.impl.InitializeDatabaseSupport;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Начальная инициализация данных.
 * @author sl
 */
public class LicenseInitializeDatabase extends OperationTypes implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(LicenseInitializeDatabase.class);
    private final InitializeDatabaseSupport iDBSpt;

    public LicenseInitializeDatabase(InitializeDatabaseSupport initializeDatabaseSupport) {
        this.iDBSpt = Defense.notNull(initializeDatabaseSupport, "initializeDatabaseSupport");
    }

    @Override
    public void run() {
        LOG.debug("Add subsystem...");
        SubSystem subSystem = iDBSpt.saveOrUpdateSubSystem("License");
        //---------Константы---------
        //---------Формы обучения---------
        LOG.debug("Add EducationForms...");
        EducationForm stat_EF = iDBSpt.getStoredConstantsSource().get(EducationForm.class, EducationForm.STATIONARY_KEY);
        if (stat_EF == null) {
            stat_EF = new EducationForm("Денна");
            stat_EF.setShortName("Ден.");
            iDBSpt.getStoredConstantsSource().put(EducationForm.class, EducationForm.STATIONARY_KEY, stat_EF);
        }
        EducationForm corr_EF = iDBSpt.getStoredConstantsSource().get(EducationForm.class, EducationForm.CORRESPONDENCE_KEY);
        if (corr_EF == null) {
            corr_EF = new EducationForm("Заочна");
            corr_EF.setShortName("Заоч.");
            iDBSpt.getStoredConstantsSource().put(EducationForm.class, EducationForm.CORRESPONDENCE_KEY, corr_EF);
        }

        //---------Квалификационные уровни---------
        LOG.debug("Add EducationalQualificationLevels...");
        EducationalQualificationLevel jSpec_EQL = iDBSpt.getStoredConstantsSource().get(EducationalQualificationLevel.class, EducationalQualificationLevel.JUNIOR_SPECIALIST_KEY);
        if (jSpec_EQL == null) {
            jSpec_EQL = new EducationalQualificationLevel("Молодший спеціаліст", "5");
            jSpec_EQL.setShortName("мол.сп.");
            iDBSpt.getStoredConstantsSource().put(EducationalQualificationLevel.class, EducationalQualificationLevel.JUNIOR_SPECIALIST_KEY, jSpec_EQL);
        }
        EducationalQualificationLevel bach_EQL = iDBSpt.getStoredConstantsSource().get(EducationalQualificationLevel.class, EducationalQualificationLevel.BACHELOR_KEY);
        if (bach_EQL == null) {
            bach_EQL = new EducationalQualificationLevel("Бакалавр", "6");
            bach_EQL.setShortName("бак.");
            iDBSpt.getStoredConstantsSource().put(EducationalQualificationLevel.class, EducationalQualificationLevel.BACHELOR_KEY, bach_EQL);
        }
        EducationalQualificationLevel spec_EQL = iDBSpt.getStoredConstantsSource().get(EducationalQualificationLevel.class, EducationalQualificationLevel.SPECIALIST_KEY);
        if (spec_EQL == null) {
            spec_EQL = new EducationalQualificationLevel("Спеціаліст", "7");
            spec_EQL.setShortName("спец.");
            iDBSpt.getStoredConstantsSource().put(EducationalQualificationLevel.class, EducationalQualificationLevel.SPECIALIST_KEY, spec_EQL);
        }
        EducationalQualificationLevel master_EQL = iDBSpt.getStoredConstantsSource().get(EducationalQualificationLevel.class, EducationalQualificationLevel.MASTER_KEY);
        if (master_EQL == null) {
            master_EQL = new EducationalQualificationLevel("Магістр", "8");
            master_EQL.setShortName("маг.");
            iDBSpt.getStoredConstantsSource().put(EducationalQualificationLevel.class, EducationalQualificationLevel.MASTER_KEY, master_EQL);
        }

        //---------Группы лицензионных записей (область действия) ---------
        LOG.debug("Add LicenseRecordGroups...");
        LicenseRecordGroup bsmtrain_LRG = iDBSpt.getStoredConstantsSource().get(LicenseRecordGroup.class, LicenseRecordGroup.BSMTRAINING_KEY);
        if (bsmtrain_LRG == null) {
            bsmtrain_LRG = new LicenseRecordGroup("Підготовка бакалаврів, спеціалістів. магістрів");
            iDBSpt.getStoredConstantsSource().put(LicenseRecordGroup.class, LicenseRecordGroup.BSMTRAINING_KEY, bsmtrain_LRG);
        }

        LicenseRecordGroup sretrain_LRG = iDBSpt.getStoredConstantsSource().get(LicenseRecordGroup.class, LicenseRecordGroup.SPECRETRAINING_KEY);
        if (sretrain_LRG == null) {
            sretrain_LRG = new LicenseRecordGroup("Перепідготовка спеціалістів");
            iDBSpt.getStoredConstantsSource().put(LicenseRecordGroup.class, LicenseRecordGroup.SPECRETRAINING_KEY, sretrain_LRG);
        }

        LicenseRecordGroup forcol_LRG = iDBSpt.getStoredConstantsSource().get(LicenseRecordGroup.class, LicenseRecordGroup.FORCOLLEDGE_KEY);
        if (forcol_LRG == null) {
            forcol_LRG = new LicenseRecordGroup("Для колледжу Класичного приватного університету");
            iDBSpt.getStoredConstantsSource().put(LicenseRecordGroup.class, LicenseRecordGroup.FORCOLLEDGE_KEY, forcol_LRG);
        }

        LicenseRecordGroup btrain_LRG = iDBSpt.getStoredConstantsSource().get(LicenseRecordGroup.class, LicenseRecordGroup.BACHTRAINING_KEY);
        if (btrain_LRG == null) {
            btrain_LRG = new LicenseRecordGroup("Підготовка бакалаврів");
            iDBSpt.getStoredConstantsSource().put(LicenseRecordGroup.class, LicenseRecordGroup.BACHTRAINING_KEY, btrain_LRG);
        }

        LicenseRecordGroup jstrain_LRG = iDBSpt.getStoredConstantsSource().get(LicenseRecordGroup.class, LicenseRecordGroup.JUNSPECTRAINING_KEY);
        if (jstrain_LRG == null) {
            jstrain_LRG = new LicenseRecordGroup("Підготовка молодших спеціалістів");
            iDBSpt.getStoredConstantsSource().put(LicenseRecordGroup.class, LicenseRecordGroup.JUNSPECTRAINING_KEY, jstrain_LRG);
        }

        //---------Права----------
        LOG.debug("Add Permissions Maps...");
        Map<String, Permission> EFPermissions = iDBSpt.getPermissionsMap(EducationForm.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> EQLPermissions = iDBSpt.getPermissionsMap(EducationalQualificationLevel.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> KAOTPermissions = iDBSpt.getPermissionsMap(KnowledgeAreaOrTrainingDirection.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> TDOSPermissions = iDBSpt.getPermissionsMap(TrainingDirectionOrSpeciality.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> LRGPermissions = iDBSpt.getPermissionsMap(LicenseRecordGroup.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);

        Map<String, Permission> LPermissions = iDBSpt.getPermissionsMap(License.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> LRPermissions = iDBSpt.getPermissionsMap(LicenseRecord.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> LRVPermissions = iDBSpt.getPermissionsMap(LicenseRecordView.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        if (iDBSpt.isFillTestData()) {
            //---------Группы прав----------
            //Права просмотра записей о лицензиях
            LOG.debug("Add Test PermissionGroups...");
            PermissionGroup pgReadLicenseRecords = iDBSpt.saveOrUpdatePermissionGroup("Подсистема лицензий. Просмотр лицензий",
                    LPermissions.get(READ_OP), LPermissions.get(MENU_OP),
                    LRPermissions.get(READ_OP),
                    LRVPermissions.get(READ_OP), LRVPermissions.get(MENU_OP));

            //Права изменения, добавления, удаления с сохранением записей о лицензиях
            PermissionGroup pgManageLicenseRecords = iDBSpt.saveOrUpdatePermissionGroup("Подсистема лицензий. Управление лицензиями",
                    LPermissions.get(UPDATE_OP), LPermissions.get(STORE_OP), LPermissions.get(REMOVE_OP),
                    LRPermissions.get(UPDATE_OP), LRPermissions.get(STORE_OP), LRPermissions.get(REMOVE_OP),
                    LRVPermissions.get(UPDATE_OP), LRVPermissions.get(STORE_OP), LRVPermissions.get(REMOVE_OP));

            //Права просмотра справочников для подсистемы лицензий
            PermissionGroup pgReadLicenseRecordsReference = iDBSpt.saveOrUpdatePermissionGroup("Подсистема лицензий. Просмотр справочников",
                    EFPermissions.get(READ_OP), EFPermissions.get(MENU_OP),
                    EQLPermissions.get(READ_OP), EQLPermissions.get(MENU_OP),
                    KAOTPermissions.get(READ_OP), KAOTPermissions.get(MENU_OP),
                    TDOSPermissions.get(READ_OP), TDOSPermissions.get(MENU_OP),
                    LRGPermissions.get(READ_OP), LRGPermissions.get(MENU_OP));

            //Права изменения, добавления, удаления с сохранением записей справочников для подсистемы лицензий
            PermissionGroup pgManageLicenseRecordsReference = iDBSpt.saveOrUpdatePermissionGroup("Подсистема лицензий. Управление справочниками",
//Разрешения на редактирование справочников EducationForm и EducatioQualificztionLevel удалены, т.к. записи в них редко изменяются и это можно будет делать Администратору системы
//                    EFPermissions.get(UPDATE_OP), EFPermissions.get(STORE_OP), EFPermissions.get(REMOVE_OP),
//                    EQLPermissions.get(UPDATE_OP), EQLPermissions.get(STORE_OP), EQLPermissions.get(REMOVE_OP),
                    KAOTPermissions.get(UPDATE_OP), KAOTPermissions.get(STORE_OP), KAOTPermissions.get(REMOVE_OP),
                    TDOSPermissions.get(UPDATE_OP), TDOSPermissions.get(STORE_OP), TDOSPermissions.get(REMOVE_OP),
                    LRGPermissions.get(UPDATE_OP), LRGPermissions.get(STORE_OP), LRGPermissions.get(REMOVE_OP));

            //Права просмотра записей о подразделениях
            PermissionGroup pgReadOrgUnits = iDBSpt.getPermissionGroupController().findByName("Подсистема административно-организационной структуры. Просмотр записей о подразделениях");

            //---------Роли----------
            LOG.debug("Add Test Roles...");
            Role roleLG = iDBSpt.saveOrUpdateRole("LicenseGuest",
                    "Перегляд записів про ліцензії", subSystem, pgReadLicenseRecords,pgReadOrgUnits);

            Role roleLO = iDBSpt.saveOrUpdateRole("LicenseOperator",
                    "Оператор управління записами про ліцензії", subSystem,
                    pgReadLicenseRecords, pgManageLicenseRecords,
                    pgReadLicenseRecordsReference, pgReadOrgUnits);

            Role roleLM = iDBSpt.saveOrUpdateRole("LicenseManager",
                    "Менеджер управління записами про ліцензії", subSystem,
                    pgReadLicenseRecords, pgManageLicenseRecords,
                    pgReadLicenseRecordsReference, pgManageLicenseRecordsReference, pgReadOrgUnits);

            //---------Пользователи----------
            LOG.debug("Add Test Roles to Users...");
            UserController uCnt = iDBSpt.getUserController();
            User user = uCnt.findByLogin("sl");
            user.add(roleLM);
            uCnt.saveOrUpdate(user);

            user = uCnt.findByLogin("TII");
            user.add(roleLO);
            uCnt.saveOrUpdate(user);

            user = uCnt.findByLogin("guest");
            user.add(roleLG);
            uCnt.saveOrUpdate(user);

            //---------Области знаний или направления подготовки----------
            LOG.debug("Add Test KnowledgeAreaOrTrainingDirections...");
            //Подготовка бакалавров, специалистов, магистров (Перелік 1997р - напрями навчання)
            KnowledgeAreaOrTrainingDirection kaotdSpecCateg         = saveOrUpdateKAOTD("Специфічні категорії",                        null, "0000", false, false);
            KnowledgeAreaOrTrainingDirection kaotdPhysTrainSport    = saveOrUpdateKAOTD("Фізичне виховання і спорт",                   null, "0102", false, false);
            KnowledgeAreaOrTrainingDirection kaotdZhurn             = saveOrUpdateKAOTD("Журналістика",                                null, "0302", false, false);
            KnowledgeAreaOrTrainingDirection kaotdInternRel         = saveOrUpdateKAOTD("Міжнародні відносини",                        null, "0304", false, false);
            KnowledgeAreaOrTrainingDirection kaotdPhilol            = saveOrUpdateKAOTD("Філологія",                                   null, "0305", false, false);
            KnowledgeAreaOrTrainingDirection kaotdPs                = saveOrUpdateKAOTD("Психологія",                                  null, "0401", false, false);
            KnowledgeAreaOrTrainingDirection kaotdSocial            = saveOrUpdateKAOTD("Соціологія",                                  null, "0402", false, false);
            KnowledgeAreaOrTrainingDirection kaotdEkonBus           = saveOrUpdateKAOTD("Економіка і підприємництво",                  null, "0501", false, false);
            KnowledgeAreaOrTrainingDirection kaotdManeg             = saveOrUpdateKAOTD("Менеджмент",                                  null, "0502", false, false);
            KnowledgeAreaOrTrainingDirection kaotdTur               = saveOrUpdateKAOTD("Туризм",                                      null, "0504", false, false);
            KnowledgeAreaOrTrainingDirection kaotdRight             = saveOrUpdateKAOTD("Право",                                       null, "0601", false, false);
            KnowledgeAreaOrTrainingDirection kaotdGeodMapZem        = saveOrUpdateKAOTD("Геодезія, картографія та землевпорядкування", null, "0709", false, false);
            KnowledgeAreaOrTrainingDirection kaotdAppMath           = saveOrUpdateKAOTD("Прикладна математика",                        null, "0802", false, false);
            KnowledgeAreaOrTrainingDirection kaotdCompSci           = saveOrUpdateKAOTD("Комп'ютерні науки",                           null, "0804", false, false);
            KnowledgeAreaOrTrainingDirection kaotdElect             = saveOrUpdateKAOTD("Електроніка",                                 null, "0908", false, false);
            KnowledgeAreaOrTrainingDirection kaotdGover             = saveOrUpdateKAOTD("Державне управління",                         null, "1501", false, false);
            //Переподготовка специалистов (Перелік 1997р - напрями навчання) - додатковх до основних напрямів немає
            //Для коледжу КПУ (Перелік 1997г - напрями навчання) - додаткові до основних напрями
            KnowledgeAreaOrTrainingDirection kaotdTrade             = saveOrUpdateKAOTD("Торгівля",                                    null, "0503", false, false);
            
            //Подготовка бакалавров (Перелік 2006р - галузі знань)
            KnowledgeAreaOrTrainingDirection kaotdPhysTrainSportHealth  = saveOrUpdateKAOTD("Фізичне виховання, спорт і здоров'я людини",  null, "0102", true, false);
            KnowledgeAreaOrTrainingDirection kaotdCult                  = saveOrUpdateKAOTD("Культура",                                    null, "0201", true, false);
            KnowledgeAreaOrTrainingDirection kaotdHumanSci              = saveOrUpdateKAOTD("Гуманітарні науки",                           null, "0203", true, false);
            KnowledgeAreaOrTrainingDirection kaotdSocPolSci             = saveOrUpdateKAOTD("Соціально-політичні науки",                   null, "0301", true, false);
            KnowledgeAreaOrTrainingDirection kaotdIntRelat              = saveOrUpdateKAOTD("Міжнародні відносини",                        null, "0302", true, false);
            KnowledgeAreaOrTrainingDirection kaotdZhurnInf              = saveOrUpdateKAOTD("Журналістика та інформація",                  null, "0303", true, false);
            KnowledgeAreaOrTrainingDirection kaotdLaw                   = saveOrUpdateKAOTD("Право",                                       null, "0304", true, false);
            KnowledgeAreaOrTrainingDirection kaotdEconEnterprise        = saveOrUpdateKAOTD("Економіка та підприємництво",                 null, "0305", true, false);
            KnowledgeAreaOrTrainingDirection kaotdManegAdmin            = saveOrUpdateKAOTD("Менеджмент та адміністрування",               null, "0306", true, false);
            KnowledgeAreaOrTrainingDirection kaotdSysSciCyber           = saveOrUpdateKAOTD("Системні науки та кібернетика",               null, "0403", true, false);
            KnowledgeAreaOrTrainingDirection kaotdInfComp               = saveOrUpdateKAOTD("Інформатика та обчислювальна техніка",        null, "0501", true, false);
            KnowledgeAreaOrTrainingDirection kaotdElectron              = saveOrUpdateKAOTD("Електроніка",                                 null, "0508", true, false);
            KnowledgeAreaOrTrainingDirection kaotdGeodLandReg           = saveOrUpdateKAOTD("Геодезія та землеустрій",                     null, "0801", true, false);
            //Подготовка младших специалистов (Перелік 2006р - галузі знань) - додаткові до основних напрями
            KnowledgeAreaOrTrainingDirection kaotdSocServ               = saveOrUpdateKAOTD("Соціальне забезпечення",                      null, "1301", true, false);
            KnowledgeAreaOrTrainingDirection kaotdServSect              = saveOrUpdateKAOTD("Сфера обслуговування",                        null, "1401", true, false);

            //---Направления подготовки или специальности суффиксы _JS, _B, _S, _M обозначают квалификационные уровни
            //младшего специалиста, бакалавра, специалиста/магистра, соответственно
            //Допускается комбинация суффиксов при одинаковых именах и кодах записей (или кратких именах и кодах),
            //например, часто совпадают записи для специалистов и магистров, в этом случае суффикс _SM
            //(образовательно-квалификационный уровень добавляется уже для записей лицензий)
            LOG.debug("Add Test TrainingDirectionOrSpecialitys...");
            //Підготовка бакалаврів, спеціалістів, магістрів (Перелік 1997р - спеціальності)
            //Специфічні категорії
            TrainingDirectionOrSpeciality tdosPVSH_M                = saveOrUpdateTDOS("Педагогіка вищої школи",         "ПВШ", "05", false, kaotdSpecCateg, false);
            TrainingDirectionOrSpeciality tdosAM_M                  = saveOrUpdateTDOS("Адміністративний менеджмент",    "АМ",  "07", false, kaotdSpecCateg, false);
            TrainingDirectionOrSpeciality tdosUNZ_M                 = saveOrUpdateTDOS("Управління навчальним закладом", "УНЗ", "09", false, kaotdSpecCateg, false);
            TrainingDirectionOrSpeciality tdosPE_M                  = saveOrUpdateTDOS("Прикладна економіка",            "ПЕ",  "11", false, kaotdSpecCateg, false);
            TrainingDirectionOrSpeciality tdosBA_M                  = saveOrUpdateTDOS("Бізнес-адміністрування",         "БА",  "13", false, kaotdSpecCateg, false);
            //Фізичне виховання і спорт
            TrainingDirectionOrSpeciality tdosPVS_B                 = saveOrUpdateTDOS("Фізичне виховання і спорт", "ФВС", "00", false, kaotdPhysTrainSport, false);
            TrainingDirectionOrSpeciality tdosPhysV_SM              = saveOrUpdateTDOS("Фізичне виховання",         "ФВ",  "01", false, kaotdPhysTrainSport, false);
            TrainingDirectionOrSpeciality tdosPhysR_SM              = saveOrUpdateTDOS("Фізична реабілітація",      "ФР",  "02", false, kaotdPhysTrainSport, false);
            //Журналістика
            TrainingDirectionOrSpeciality tdosZhurn_B               = saveOrUpdateTDOS("Журналістика",                    "Ж",   "00", false, kaotdZhurn, false);
            TrainingDirectionOrSpeciality tdosZhurn_SM              = saveOrUpdateTDOS("Журналістика",                    "Ж",   "01", false, kaotdZhurn, false);
            TrainingDirectionOrSpeciality tdosVSR_S                 = saveOrUpdateTDOS("Видавнича справа та редагування", "ВСР", "03", false, kaotdZhurn, false);
            //Міжнародні відносини
            TrainingDirectionOrSpeciality tdosMP_B                  = saveOrUpdateTDOS("Міжнародні відносини",  "МВ", "00", false, kaotdInternRel, false);
            TrainingDirectionOrSpeciality tdosMP_S                  = saveOrUpdateTDOS("Міжнародне право",      "МП", "02", false, kaotdInternRel, false);
            //Філологія
            TrainingDirectionOrSpeciality tdosPhyl_B                = saveOrUpdateTDOS("Філологія",                       "Ф",  "00", false, kaotdPhilol, false);
            TrainingDirectionOrSpeciality tdosLangLit_SM            = saveOrUpdateTDOS("Мова та література (англійська)", "МЛ", "02", false, kaotdPhilol, false);
            TrainingDirectionOrSpeciality tdosTransl_SM             = saveOrUpdateTDOS("Переклад",                        "ПР", "07", false, kaotdPhilol, false);
            TrainingDirectionOrSpeciality tdosLT_B                  = saveOrUpdateTDOS("Літературна творчість",           "ЛТ", "00", false, kaotdPhilol, false);
            //Психологія
            TrainingDirectionOrSpeciality tdosPS_B                  = saveOrUpdateTDOS("Психологія", "ПС", "00", false, kaotdPs, false);
            TrainingDirectionOrSpeciality tdosPS_SM                 = saveOrUpdateTDOS("Психологія", "ПС", "01", false, kaotdPs, false);
            //Соціологія
            TrainingDirectionOrSpeciality tdosSR_B                  = saveOrUpdateTDOS("Соціологія",       "СР", "00", false, kaotdSocial, false);
            TrainingDirectionOrSpeciality tdosSR_SM                 = saveOrUpdateTDOS("Соціальна робота", "СР", "02", false, kaotdSocial, false);
            //Економіка і підприємництво
            TrainingDirectionOrSpeciality tdosEIP_B                 = saveOrUpdateTDOS("Економіка і підприємництво", "ЕІП", "00", false, kaotdEkonBus, false);
            TrainingDirectionOrSpeciality tdosEKib_SM               = saveOrUpdateTDOS("Економічна кібернетика",     "ЕК",  "02", false, kaotdEkonBus, false);
            TrainingDirectionOrSpeciality tdosME_SM                 = saveOrUpdateTDOS("Міжнародна економіка",       "МЕ",  "03", false, kaotdEkonBus, false);
            TrainingDirectionOrSpeciality tdosF_SM                  = saveOrUpdateTDOS("Фінанси",                    "Ф",   "04", false, kaotdEkonBus, false);
            TrainingDirectionOrSpeciality tdosOA_SM                 = saveOrUpdateTDOS("Облік і аудит",              "ОА",  "06", false, kaotdEkonBus, false);
            TrainingDirectionOrSpeciality tdosBS_SM                 = saveOrUpdateTDOS("Банківська справа",          "БС",  "05", false, kaotdEkonBus, false);
            TrainingDirectionOrSpeciality tdosEP_SM                 = saveOrUpdateTDOS("Економіка підприємства",     "ЕП",  "07", false, kaotdEkonBus, false);
            TrainingDirectionOrSpeciality tdosM_SM                  = saveOrUpdateTDOS("Маркетинг",                  "М",   "08", false, kaotdEkonBus, false);
            TrainingDirectionOrSpeciality tdosES_SM                 = saveOrUpdateTDOS("Економічна статистика",      "ЕС",  "10", false, kaotdEkonBus, false);
            //Менеджмент
            TrainingDirectionOrSpeciality tdosMN_B                  = saveOrUpdateTDOS("Менеджмент",                                  "М",    "00", false, kaotdManeg, false);
            TrainingDirectionOrSpeciality tdosMO_SM                 = saveOrUpdateTDOS("Менеджмент організації",                      "МО",   "01", false, kaotdManeg, false);
            TrainingDirectionOrSpeciality tdosMZED_SM               = saveOrUpdateTDOS("Менеджмент зовнішньо-економічної діяльності", "МЗЕД", "06", false, kaotdManeg, false);
            //Туризм
            TrainingDirectionOrSpeciality tdosTR_B                  = saveOrUpdateTDOS("Туризм",                "Т",  "00", false, kaotdTur, false);
            TrainingDirectionOrSpeciality tdosTR_SM                 = saveOrUpdateTDOS("Туризм",                "Т",  "01", false, kaotdTur, false);
            TrainingDirectionOrSpeciality tdosGGos_SM               = saveOrUpdateTDOS("Готельне господарство", "ГГ", "02", false, kaotdTur, false);
            //Право
            TrainingDirectionOrSpeciality tdosPZ_B                  = saveOrUpdateTDOS("Право",         "П", "00", false, kaotdRight, false);
            TrainingDirectionOrSpeciality tdosPZ_SM                 = saveOrUpdateTDOS("Правознавство", "П", "01", false, kaotdRight, false);
            //Геодезія, картографія та землевпорядкування
            TrainingDirectionOrSpeciality tdosZK_B                  = saveOrUpdateTDOS("Землевпорядаткування та кадастр", "ЗК", "00", false, kaotdGeodMapZem, false);
            //Прикладна математика
            TrainingDirectionOrSpeciality tdosPM_B                  = saveOrUpdateTDOS("Прикладна математика",          "ПМ",  "00", false, kaotdAppMath, false);
            TrainingDirectionOrSpeciality tdosSAU_SM                = saveOrUpdateTDOS("Системний аналіз і управління", "САУ", "03", false, kaotdAppMath, false);
            TrainingDirectionOrSpeciality tdosI_B                   = saveOrUpdateTDOS("Інформатика",                   "Інф",   "00", false, kaotdAppMath, false);
            //Комп'ютерні науки
            TrainingDirectionOrSpeciality tdosPZAS_B                = saveOrUpdateTDOS("Програмне забезпечення автоматизованих систем", "ПЗАС", "00", false, kaotdCompSci, false);
            TrainingDirectionOrSpeciality tdosPZAS_SM               = saveOrUpdateTDOS("Програмне забезпечення автоматизованих систем", "ПЗАС", "03", false, kaotdCompSci, false);
            //Електроніка
            TrainingDirectionOrSpeciality tdosFBE_B                 = saveOrUpdateTDOS("Фізична та біомедична електроніка", "ФБЕ", "00", false, kaotdElect, false);
            TrainingDirectionOrSpeciality tdosFBE_SM                = saveOrUpdateTDOS("Фізична та біомедична електроніка", "ФБЕ", "04", false, kaotdElect, false);
            //Державне управління
            TrainingDirectionOrSpeciality tdosDS_M                  = saveOrUpdateTDOS("Державна служба",                   "ДС",  "01", false, kaotdGover, false);

            //Перепідготовка спеціалістів (Перелік 1997р - спеціальності) - додаткових до основних спеціальностей немає

            //Для коледжу КПУ (Перелік 1997р - спеціальності) - додаткові до основних спеціальності
            //Менеджмент
            TrainingDirectionOrSpeciality tdosOON_JS                = saveOrUpdateTDOS("Організація обслуговування населення",                                         "ООН",  "03", false, kaotdManeg,   false);
            //Торгівля
            TrainingDirectionOrSpeciality tdosTKD_JS                = saveOrUpdateTDOS("Товарознавство та комерційна діяльність",                                      "ТКД",  "01", false, kaotdTrade,   false);
            //Туризм
            TrainingDirectionOrSpeciality tdosOOGTK_JS              = saveOrUpdateTDOS("Організація обслуговування в готелях та туристичних комплексах",               "ООГТК","03", false, kaotdTur,     false);
            //Прикладна математика
            TrainingDirectionOrSpeciality tdosPM_JS                 = saveOrUpdateTDOS("Прикладна математика",                                                         "ПМ",   "02", false, kaotdAppMath, false);
            //Комп'ютерні науки
            TrainingDirectionOrSpeciality tdosPECTAS_JS             = saveOrUpdateTDOS("Програмування для електронно-обчислювальної техніки і автоматизованих систем", "ПЗАС", "05", false, kaotdCompSci, false);

            //Подготовка бакалавров (Перелік 2006р - напрями навчання)
            //Фізичне виховання, спорт і здоров'я людини
            TrainingDirectionOrSpeciality tdosPhysV_B               = saveOrUpdateTDOS("Фізичне виховання",                           "ФВ",  "01", true, kaotdPhysTrainSportHealth, false);
            TrainingDirectionOrSpeciality tdosZL_B                  = saveOrUpdateTDOS("Здоров'я людини",                             "ЗЛ",  "03", true, kaotdPhysTrainSportHealth, false);
            //Культура
            TrainingDirectionOrSpeciality tdosT_B                   = saveOrUpdateTDOS("Туризм",                                      "Т",   "07", true, kaotdCult,           false);
            //Гуманітарні науки
            TrainingDirectionOrSpeciality tdosPhilol_B              = saveOrUpdateTDOS("Філологія",                                   "ФЛ",  "03", true, kaotdHumanSci,          false);
            //Соціально-політичні науки
            TrainingDirectionOrSpeciality tdosS_B                   = saveOrUpdateTDOS("Соціологія",                                  "С",   "01", true, kaotdSocPolSci,      false);
            TrainingDirectionOrSpeciality tdosPSH_B                 = saveOrUpdateTDOS("Психологія",                                  "П",   "02", true, kaotdSocPolSci,      false);
            //Міжнародні відносини
            TrainingDirectionOrSpeciality tdosMV_B                  = saveOrUpdateTDOS("Міжнародні відносини",                        "МВ",  "01", true, kaotdIntRelat,      false);
            //Журналістика та інформація
            TrainingDirectionOrSpeciality tdosZhu_B                 = saveOrUpdateTDOS("Журналістика",                                "Ж",   "01", true, kaotdZhurnInf,       false);
            TrainingDirectionOrSpeciality tdosRZG_B                 = saveOrUpdateTDOS("Реклама і зв'язки з громадкістю (за видами)", "РЗГ", "02", true, kaotdZhurnInf,       false);
            TrainingDirectionOrSpeciality tdosVSR_B                 = saveOrUpdateTDOS("Видавнича справа та редагування",             "ВСР", "03", true, kaotdZhurnInf,       false);
            //Право
            TrainingDirectionOrSpeciality tdosP_B                   = saveOrUpdateTDOS("Право",                                       "П",   "01", true, kaotdLaw,          false);
            //Економіка і підприємництво
            TrainingDirectionOrSpeciality tdosEKib_B                = saveOrUpdateTDOS("Економічна кібернетика",                      "ЕК",  "02", true, kaotdEconEnterprise,        false);
            TrainingDirectionOrSpeciality tdosME_B                  = saveOrUpdateTDOS("Міжнародна економіка",                        "МЕ",  "03", true, kaotdEconEnterprise,        false);
            TrainingDirectionOrSpeciality tdosEP_B                  = saveOrUpdateTDOS("Економіка підприємства",                      "ЕП",  "04", true, kaotdEconEnterprise,        false);
            TrainingDirectionOrSpeciality tdosPRS_B                 = saveOrUpdateTDOS("Прикладна статистика",                        "ПС",  "06", true, kaotdEconEnterprise,        false);
            TrainingDirectionOrSpeciality tdosM_B                   = saveOrUpdateTDOS("Маркетинг",                                   "М",   "07", true, kaotdEconEnterprise,        false);
            TrainingDirectionOrSpeciality tdosFK_B                  = saveOrUpdateTDOS("Фінанси і кредит",                            "ФК",  "08", true, kaotdEconEnterprise,        false);
            TrainingDirectionOrSpeciality tdosOA_B                  = saveOrUpdateTDOS("Облік і аудит",                               "ОА",  "09", true, kaotdEconEnterprise,        false);
            //Менеджмент та адміністрування
            TrainingDirectionOrSpeciality tdosMG_B                  = saveOrUpdateTDOS("Менеджмент",                                  "МА",  "01", true, kaotdManegAdmin,     false);
            //Системні науки та кібернетика
            TrainingDirectionOrSpeciality tdosIF_B                  = saveOrUpdateTDOS("Інформатика",                                 "І",   "02", true, kaotdSysSciCyber,    false);
            TrainingDirectionOrSpeciality tdosSA_B                  = saveOrUpdateTDOS("Системний аналіз",                            "СА",  "03", true, kaotdSysSciCyber,    false);
            //Інформатика та обчислювальна техника
            TrainingDirectionOrSpeciality tdosPI_B                  = saveOrUpdateTDOS("Програмна інженерія",                         "ПІ",  "03", true, kaotdInfComp,        false);
            //Електроніка
            TrainingDirectionOrSpeciality tdosMNE_B                 = saveOrUpdateTDOS("Мікро- та наноелектроніка",                   "МНЕ", "01", true, kaotdElectron,          false);
            //Геодезія та землеустрій
            TrainingDirectionOrSpeciality tdosGKZ_B                 = saveOrUpdateTDOS("Геодезія, картографія та землеустрій",        "ГКЗ", "01", true, kaotdGeodLandReg,     false);

            //Подготовка младших специалистов (Перелік 2006р - спеціальності)
            //Культура
            TrainingDirectionOrSpeciality tdosOTO_JS                = saveOrUpdateTDOS("Організація туристичного обслуговування", "ОТО",  "0701", false, kaotdCult,        false);
            //Журналістика та інформація
            TrainingDirectionOrSpeciality tdosVSTR_JS               = saveOrUpdateTDOS("Видавнича справа та редагування",         "ВСР",  "0301", false, kaotdZhurnInf,    false);
            //Економіка та підприємництво
            TrainingDirectionOrSpeciality tdosEKP_JS                = saveOrUpdateTDOS("Економіка підприємства",                  "ЕП",   "0401", false, kaotdEconEnterprise,     false);
            TrainingDirectionOrSpeciality tdosTZKD_JS               = saveOrUpdateTDOS("Товарознавство та комерційна діяльність", "ТЗКД", "1001", false, kaotdEconEnterprise,     false);
            //Системні науки та кібернетика
            TrainingDirectionOrSpeciality tdosPRM_JS                = saveOrUpdateTDOS("Прикладна математика",                    "ПМ",   "0101", false, kaotdSysSciCyber, false);
            //Інформатика та обчислювальна техника
            TrainingDirectionOrSpeciality tdosRPZ_JS                = saveOrUpdateTDOS("Розробка програмного забезпечення",       "РПЗ",  "0301", false, kaotdInfComp,     false);
            //Соціальне забезпечення
            TrainingDirectionOrSpeciality tdosSOCR_JS               = saveOrUpdateTDOS("Соціальна робота",                        "СР",   "0101", false, kaotdSocServ,     false);
            //Сфера обслуговування
            TrainingDirectionOrSpeciality tdosOONAS_JS              = saveOrUpdateTDOS("Організація обслуговування населення",    "ООН",  "0202", false, kaotdServSect,      false);



            //---Серия, номер и дата выдачи лицензии----------
            LOG.debug("Add Test License...");
            Calendar licCal = Calendar.getInstance();
            licCal.set(Calendar.YEAR, 2008);
            licCal.set(Calendar.MONTH, Calendar.OCTOBER);
            licCal.set(Calendar.DAY_OF_MONTH, 21);
            License licenseCPU = saveOrUpdateLicense("АВ", "420720", licCal.getTime(), null);

            //---Кафедры, выполняющие обучение по лицензиям----------
            NamedEntityController<OrgUnit> ouCnt = (NamedEntityController<OrgUnit>) (Object) iDBSpt.getControllerSource().get(OrgUnit.class);
            OrgUnit kafPIT      = ouCnt.findByNameFirst("Кафедра програмування та інформаційних технологій");
            OrgUnit kafEICPHS   = ouCnt.findByNameFirst("Кафедра управління навчальними закладами та педагогіки вищої школи");
            OrgUnit kafPEBA     = ouCnt.findByNameFirst("Кафедра прикладної економіки та бізнес- адміністрування");
            OrgUnit kafDUAM     = ouCnt.findByNameFirst("Кафедра державного управління та адміністративного менеджменту");
            OrgUnit kafTOFAV    = ouCnt.findByNameFirst("Кафедра теоретичних основ фізичного та адаптивного виховання");
            OrgUnit kafFR       = ouCnt.findByNameFirst("Кафедра фізичної реабілітації");
            OrgUnit kafZSK      = ouCnt.findByNameFirst("Кафедра журналістики і соціальних комунікацій");
            OrgUnit kafVSRUF    = ouCnt.findByNameFirst("Кафедра видавничої справи, редагування та української філології");
            OrgUnit kafRZG      = ouCnt.findByNameFirst("Кафедра реклами та з'язків із громадкістю");
            OrgUnit kafMP       = ouCnt.findByNameFirst("Кафедра міжнародного права");
            OrgUnit kafAF       = ouCnt.findByNameFirst("Кафедра англійської філології");
            OrgUnit kafTPP      = ouCnt.findByNameFirst("Кафедра теорії і практики  перекладу");
            //OrgUnit kafRGF      = ouCnt.findByNameFirst("Кафедра романо-германської філології");
            OrgUnit kafPP       = ouCnt.findByNameFirst("Кафедра практичної психології");
            OrgUnit kafSSR      = ouCnt.findByNameFirst("Кафедра соціології та соціальної роботи");
            OrgUnit kafEKS      = ouCnt.findByNameFirst("Кафедра економічної кібернетики та статистики");
            OrgUnit kafME       = ouCnt.findByNameFirst("Кафедра міжнародної економіки");
            OrgUnit kafFK       = ouCnt.findByNameFirst("Кафедра фінанси і кредит");
            OrgUnit kafOA       = ouCnt.findByNameFirst("Кафедра обліку і аудиту");
            OrgUnit kafEP       = ouCnt.findByNameFirst("Кафедра економіки підприємства");
            OrgUnit kafM        = ouCnt.findByNameFirst("Кафедра маркетингу");
            //OrgUnit kafIAM      = ouCnt.findByNameFirst("Кафедра інвестиційного та аграрного менеджменту");
            OrgUnit kafMZD      = ouCnt.findByNameFirst("Кафедра менеджменту зовнішньоекономічної діяльності");
            OrgUnit kafMG       = ouCnt.findByNameFirst("Кафедра менеджменту");
            OrgUnit kafTGG      = ouCnt.findByNameFirst("Кафедра туризму та готельного господарства");
            OrgUnit kafTZGP     = ouCnt.findByNameFirst("Кафедра трудового, земельного та господарського права");
            OrgUnit kafZK       = ouCnt.findByNameFirst("Кафедра землевпорядкування та кадастру");
            OrgUnit kafSAVM     = ouCnt.findByNameFirst("Кафедра системного аналізу та вищої математики");
            OrgUnit kafPTD      = ouCnt.findByNameFirst("Кафедра природничих і технічних дисциплін");
            //Временно
            OrgUnit kafUND      = ouCnt.findByNameFirst("Неопределенная");

            //Термін закінчення ліцензій 2009.07.01
            Calendar cal_2009_07_01 = Calendar.getInstance();
            cal_2009_07_01.set(Calendar.YEAR, 2009);
            cal_2009_07_01.set(Calendar.MONTH, Calendar.JULY);
            cal_2009_07_01.set(Calendar.DAY_OF_MONTH, 1);

            //Термін закінчення ліцензій 2010.07.01
            Calendar cal_2010_07_01 = Calendar.getInstance();
            cal_2010_07_01.set(Calendar.YEAR, 2010);
            cal_2010_07_01.set(Calendar.MONTH, Calendar.JULY);
            cal_2010_07_01.set(Calendar.DAY_OF_MONTH, 1);

            //Термін закінчення ліцензій 2011.07.01
            Calendar cal_2011_07_01 = Calendar.getInstance();
            cal_2011_07_01.set(Calendar.YEAR, 2011);
            cal_2011_07_01.set(Calendar.MONTH, Calendar.JULY);
            cal_2011_07_01.set(Calendar.DAY_OF_MONTH, 1);

            //Термін закінчення ліцензій 2012.07.01
            Calendar cal_2012_07_01 = Calendar.getInstance();
            cal_2012_07_01.set(Calendar.YEAR, 2012);
            cal_2012_07_01.set(Calendar.MONTH, Calendar.JULY);
            cal_2012_07_01.set(Calendar.DAY_OF_MONTH, 1);

            //Термін закінчення ліцензій 2013.07.01
            Calendar cal_2013_07_01 = Calendar.getInstance();
            cal_2013_07_01.set(Calendar.YEAR, 2013);
            cal_2013_07_01.set(Calendar.MONTH, Calendar.JULY);
            cal_2013_07_01.set(Calendar.DAY_OF_MONTH, 1);


            //---Записи лицензии-суффиксы _JS, _B, _S, _M обозначают
            //квалификационные уровни младшего специалиста, бакалавра, специалиста, магистра, соответственно
            //суффиксы _D, _Z обозначают дневную и заочн формы обучения----------

            //Старая лицензия
            //Подготовка бакалавров, специалистов, магистров
            //Специфічні категорії
            //Педагогіка вищої школи - магістри
            saveOrUpdateLR(licenseCPU, tdosPVSH_M, master_EQL, stat_EF, 30, cal_2009_07_01.getTime(), kafEICPHS, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPVSH_M, master_EQL, corr_EF, 30, cal_2009_07_01.getTime(), kafEICPHS, bsmtrain_LRG);
            //Адміністративний менеджмент - магістри
            saveOrUpdateLR(licenseCPU, tdosAM_M,   master_EQL, stat_EF, 60, cal_2009_07_01.getTime(), kafDUAM,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosAM_M,   master_EQL, corr_EF, 60, cal_2009_07_01.getTime(), kafDUAM,   bsmtrain_LRG);
            //Управління навчальним закладом - магістри
            saveOrUpdateLR(licenseCPU, tdosUNZ_M,  master_EQL, stat_EF, 30, cal_2009_07_01.getTime(), kafEICPHS, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosUNZ_M,  master_EQL, corr_EF, 30, cal_2009_07_01.getTime(), kafEICPHS, bsmtrain_LRG);
            //Прикладна економіка - магістри
            saveOrUpdateLR(licenseCPU, tdosPE_M,   master_EQL, stat_EF, 30, cal_2012_07_01.getTime(), kafPEBA,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPE_M,   master_EQL, corr_EF, 30, cal_2012_07_01.getTime(), kafPEBA,   bsmtrain_LRG);
            //Бізнес-адміністрування - магістри
            saveOrUpdateLR(licenseCPU, tdosBA_M,   master_EQL, stat_EF, 30, cal_2013_07_01.getTime(), kafPEBA,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosBA_M,   master_EQL, corr_EF, 0 , cal_2013_07_01.getTime(), kafPEBA,   bsmtrain_LRG);

            //Фізичне виховання і спорт
            //Фізичне виховання і спорт - бакалаври
            saveOrUpdateLR(licenseCPU, tdosPVS_B,    bach_EQL,    stat_EF, 110, cal_2010_07_01.getTime(), kafTOFAV, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPVS_B,    bach_EQL,    corr_EF, 110, cal_2010_07_01.getTime(), kafTOFAV, bsmtrain_LRG);
            //Фізичне виховання - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPhysV_SM, spec_EQL,    stat_EF, 50,  cal_2010_07_01.getTime(), kafTOFAV, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhysV_SM, spec_EQL,    corr_EF, 50,  cal_2010_07_01.getTime(), kafTOFAV, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhysV_SM, master_EQL,  stat_EF, 10,  cal_2010_07_01.getTime(), kafTOFAV, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhysV_SM, master_EQL,  corr_EF, 10,  cal_2010_07_01.getTime(), kafTOFAV, bsmtrain_LRG);
            //Фізична реабілітація - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPhysR_SM, spec_EQL,    stat_EF, 30,  cal_2010_07_01.getTime(), kafFR,    bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhysR_SM, spec_EQL,    corr_EF, 30,  cal_2010_07_01.getTime(), kafFR,    bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhysR_SM, master_EQL,  stat_EF, 30,  cal_2010_07_01.getTime(), kafFR,    bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhysR_SM, master_EQL,  corr_EF, 30,  cal_2010_07_01.getTime(), kafFR,    bsmtrain_LRG);

            //Журналістика
            //Журналістика - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosZhurn_B,  bach_EQL,    stat_EF, 110, cal_2011_07_01.getTime(), kafZSK,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosZhurn_B,  bach_EQL,    corr_EF, 100, cal_2011_07_01.getTime(), kafZSK,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosZhurn_SM, spec_EQL,    stat_EF, 50,  cal_2010_07_01.getTime(), kafZSK,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosZhurn_SM, spec_EQL,    corr_EF, 50,  cal_2010_07_01.getTime(), kafZSK,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosZhurn_SM, master_EQL,  stat_EF, 10,  cal_2010_07_01.getTime(), kafZSK,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosZhurn_SM, master_EQL,  corr_EF, 10,  cal_2010_07_01.getTime(), kafZSK,   bsmtrain_LRG);
            //Видавнича справа та редагування - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosVSR_S,    spec_EQL,    stat_EF, 50,  cal_2011_07_01.getTime(), kafVSRUF, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosVSR_S,    spec_EQL,    corr_EF, 50,  cal_2011_07_01.getTime(), kafVSRUF, bsmtrain_LRG);

            //Міжнародні відносини
            //Міжнародне право - бакалаври, спеціалісти
            saveOrUpdateLR(licenseCPU, tdosMP_B,     bach_EQL,    stat_EF, 30, cal_2013_07_01.getTime(), kafMP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMP_B,     bach_EQL,    corr_EF, 30, cal_2013_07_01.getTime(), kafMP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMP_S,     spec_EQL,    stat_EF, 30, cal_2013_07_01.getTime(), kafMP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMP_S,     spec_EQL,    corr_EF, 30, cal_2013_07_01.getTime(), kafMP, bsmtrain_LRG);

            //Філологія
            //Філологія - бакалаври
            saveOrUpdateLR(licenseCPU, tdosPhyl_B,     bach_EQL,    stat_EF, 110, cal_2010_07_01.getTime(), kafAF, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhyl_B,     bach_EQL,    corr_EF, 85,  cal_2010_07_01.getTime(), kafAF, bsmtrain_LRG);
            //Мова та література(англійська) - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosLangLit_SM, spec_EQL,    stat_EF, 50,  cal_2009_07_01.getTime(), kafAF, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosLangLit_SM, spec_EQL,    corr_EF, 50,  cal_2009_07_01.getTime(), kafAF, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosLangLit_SM, master_EQL,  stat_EF, 12,  cal_2010_07_01.getTime(), kafAF, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosLangLit_SM, master_EQL,  corr_EF, 12,  cal_2010_07_01.getTime(), kafAF, bsmtrain_LRG);
            //Переклад - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosTransl_SM,  spec_EQL,    stat_EF, 25,  cal_2010_07_01.getTime(), kafTPP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosTransl_SM,  spec_EQL,    corr_EF, 25,  cal_2010_07_01.getTime(), kafTPP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosTransl_SM,  master_EQL,  stat_EF, 10,  cal_2010_07_01.getTime(), kafTPP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosTransl_SM,  master_EQL,  corr_EF, 10,  cal_2010_07_01.getTime(), kafTPP, bsmtrain_LRG);
            //Літературна творчість - бакалаври
            saveOrUpdateLR(licenseCPU, tdosLT_B,       bach_EQL,    stat_EF, 25,  cal_2010_07_01.getTime(), kafUND, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosLT_B,       bach_EQL,    corr_EF, 0,   cal_2010_07_01.getTime(), kafUND, bsmtrain_LRG);

            //Психологія
            //Психологія - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPS_B,   bach_EQL,    stat_EF, 30,  cal_2010_07_01.getTime(), kafPP,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPS_B,   bach_EQL,    corr_EF, 30,  cal_2010_07_01.getTime(), kafPP,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPS_SM , spec_EQL,    stat_EF, 30,  cal_2010_07_01.getTime(), kafPP,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPS_SM , spec_EQL,    corr_EF, 30,  cal_2010_07_01.getTime(), kafPP,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPS_SM , master_EQL,  stat_EF, 30,  cal_2010_07_01.getTime(), kafPP,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPS_SM , master_EQL,  corr_EF, 30,  cal_2010_07_01.getTime(), kafPP,  bsmtrain_LRG);

            //Соціологія
            //Соціальна робота - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosSR_B,   bach_EQL,    stat_EF, 50,  cal_2012_07_01.getTime(), kafSSR, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSR_B,   bach_EQL,    corr_EF, 50,  cal_2012_07_01.getTime(), kafSSR, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSR_SM , spec_EQL,    stat_EF, 30,  cal_2012_07_01.getTime(), kafSSR, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSR_SM , spec_EQL,    corr_EF, 30,  cal_2012_07_01.getTime(), kafSSR, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSR_SM , master_EQL,  stat_EF, 30,  cal_2009_07_01.getTime(), kafSSR, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSR_SM , master_EQL,  corr_EF, 30,  cal_2009_07_01.getTime(), kafSSR, bsmtrain_LRG);

            //Економіка і підприємництво
            //Економіка і підприємництво - бакалаври
            saveOrUpdateLR(licenseCPU, tdosEIP_B,   bach_EQL,    stat_EF, 480, cal_2013_07_01.getTime(), kafEP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEIP_B,   bach_EQL,    corr_EF, 510, cal_2013_07_01.getTime(), kafEP, bsmtrain_LRG);
            //Економічна кібернетика - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosEKib_SM, spec_EQL,    stat_EF, 30, cal_2010_07_01.getTime(), kafEKS, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEKib_SM, spec_EQL,    corr_EF, 30, cal_2010_07_01.getTime(), kafEKS, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEKib_SM, master_EQL,  stat_EF, 30, cal_2010_07_01.getTime(), kafEKS, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEKib_SM, master_EQL,  corr_EF, 30, cal_2010_07_01.getTime(), kafEKS, bsmtrain_LRG);
            //Міжнародна економіка - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosME_SM, spec_EQL,      stat_EF, 30, cal_2010_07_01.getTime(), kafME,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosME_SM, spec_EQL,      corr_EF, 30, cal_2010_07_01.getTime(), kafME,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosME_SM, master_EQL,    stat_EF, 30, cal_2010_07_01.getTime(), kafME,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosME_SM, master_EQL,    corr_EF, 30, cal_2010_07_01.getTime(), kafME,  bsmtrain_LRG);
            //Фінанси
            saveOrUpdateLR(licenseCPU, tdosF_SM, spec_EQL,       stat_EF, 30, cal_2010_07_01.getTime(), kafFK,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosF_SM, spec_EQL,       corr_EF, 70, cal_2010_07_01.getTime(), kafFK,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosF_SM, master_EQL,     stat_EF, 30, cal_2010_07_01.getTime(), kafFK,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosF_SM, master_EQL,     corr_EF, 30, cal_2010_07_01.getTime(), kafFK,  bsmtrain_LRG);
            //Облік і аудит - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosOA_SM, spec_EQL,      stat_EF, 100, cal_2010_07_01.getTime(), kafOA, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosOA_SM, spec_EQL,      corr_EF, 100, cal_2010_07_01.getTime(), kafOA, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosOA_SM, master_EQL,    stat_EF, 30,  cal_2013_07_01.getTime(), kafOA, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosOA_SM, master_EQL,    corr_EF, 80,  cal_2013_07_01.getTime(), kafOA, bsmtrain_LRG);
            //Банківська справа - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosBS_SM, spec_EQL,      stat_EF, 30, cal_2010_07_01.getTime(), kafFK,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosBS_SM, spec_EQL,      corr_EF, 30, cal_2010_07_01.getTime(), kafFK,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosBS_SM, master_EQL,    stat_EF, 20, cal_2010_07_01.getTime(), kafFK,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosBS_SM, master_EQL,    corr_EF, 20, cal_2010_07_01.getTime(), kafFK,  bsmtrain_LRG);
            //Економіка підприємства - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosEP_SM, spec_EQL,      stat_EF, 30, cal_2010_07_01.getTime(), kafEP,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEP_SM, spec_EQL,      corr_EF, 30, cal_2010_07_01.getTime(), kafEP,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEP_SM, master_EQL,    stat_EF, 30, cal_2010_07_01.getTime(), kafEP,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEP_SM, master_EQL,    corr_EF, 30, cal_2010_07_01.getTime(), kafEP,  bsmtrain_LRG);
            //Маркетинг - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosM_SM, spec_EQL,       stat_EF, 30, cal_2010_07_01.getTime(), kafM,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosM_SM, spec_EQL,       corr_EF, 30, cal_2010_07_01.getTime(), kafM,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosM_SM, master_EQL,     stat_EF, 15, cal_2010_07_01.getTime(), kafM,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosM_SM, master_EQL,     corr_EF, 15, cal_2010_07_01.getTime(), kafM,   bsmtrain_LRG);
            //Економічна статистика - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosES_SM, spec_EQL,      stat_EF, 30, cal_2010_07_01.getTime(), kafEKS, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosES_SM, spec_EQL,      corr_EF, 30, cal_2010_07_01.getTime(), kafEKS, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosES_SM, master_EQL,    stat_EF, 10, cal_2010_07_01.getTime(), kafEKS, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosES_SM, master_EQL,    corr_EF, 10, cal_2010_07_01.getTime(), kafEKS, bsmtrain_LRG);

            //Менеджмент
            //Менеджмент - бакалаври
            saveOrUpdateLR(licenseCPU, tdosMN_B,  bach_EQL,      stat_EF, 140, cal_2010_07_01.getTime(), kafMG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMN_B,  bach_EQL,      corr_EF, 250, cal_2010_07_01.getTime(), kafMG, bsmtrain_LRG);
            //Менеджмент організацій - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosMO_SM, spec_EQL,      stat_EF, 90,  cal_2010_07_01.getTime(), kafMG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMO_SM, spec_EQL,      corr_EF, 150, cal_2010_07_01.getTime(), kafMG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMO_SM, master_EQL,    stat_EF, 60,  cal_2010_07_01.getTime(), kafMG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMO_SM, master_EQL,    corr_EF, 60,  cal_2010_07_01.getTime(), kafMG, bsmtrain_LRG);
            //Менеджмент зовнішньо-економічної діяльності - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosMZED_SM, spec_EQL,    stat_EF, 30, cal_2010_07_01.getTime(), kafMZD, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMZED_SM, spec_EQL,    corr_EF, 30, cal_2010_07_01.getTime(), kafMZD, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMZED_SM, master_EQL,  stat_EF, 30, cal_2010_07_01.getTime(), kafMZD, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMZED_SM, master_EQL,  corr_EF, 30, cal_2010_07_01.getTime(), kafMZD, bsmtrain_LRG);

            //Туризм
            //Туризм - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosTR_B,   bach_EQL,     stat_EF, 90, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosTR_B,   bach_EQL,     corr_EF, 90, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosTR_SM,   spec_EQL,     stat_EF, 30, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosTR_SM,   spec_EQL,     corr_EF, 30, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosTR_SM,   master_EQL,   stat_EF, 30, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosTR_SM,   master_EQL,   corr_EF, 30, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);
            //Готельне господарство - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosGGos_SM, spec_EQL,    stat_EF, 15, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosGGos_SM, spec_EQL,    corr_EF, 15, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosGGos_SM, master_EQL,  stat_EF, 15, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosGGos_SM, master_EQL,  corr_EF, 15, cal_2013_07_01.getTime(), kafTGG, bsmtrain_LRG);

            //Право
            //Право - бакалаври
            saveOrUpdateLR(licenseCPU, tdosPZ_B,   bach_EQL,    stat_EF, 150, cal_2012_07_01.getTime(), kafTZGP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZ_B,   bach_EQL,    corr_EF, 250, cal_2012_07_01.getTime(), kafTZGP, bsmtrain_LRG);
            //Правознавство - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPZ_SM,  spec_EQL,    stat_EF, 115, cal_2012_07_01.getTime(), kafTZGP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZ_SM,  spec_EQL,    corr_EF, 210, cal_2012_07_01.getTime(), kafTZGP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZ_SM,  master_EQL,  stat_EF, 40,  cal_2009_07_01.getTime(), kafTZGP, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZ_SM,  master_EQL,  corr_EF, 60,  cal_2009_07_01.getTime(), kafTZGP, bsmtrain_LRG);

            //Геодезія, картографія та землевпорядкування
            //Землевпорядкування та кадастр - бакалаври
            saveOrUpdateLR(licenseCPU, tdosZK_B,   bach_EQL,    stat_EF, 30,  cal_2011_07_01.getTime(), kafZK,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosZK_B,   bach_EQL,    corr_EF,  0,  cal_2011_07_01.getTime(), kafZK,   bsmtrain_LRG);

            //Прикладна математика
            //Прикладна математика - бакалаври
            saveOrUpdateLR(licenseCPU, tdosPM_B,   bach_EQL,    stat_EF, 30, cal_2013_07_01.getTime(),  kafSAVM, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPM_B,   bach_EQL,    corr_EF, 30, cal_2013_07_01.getTime(),  kafSAVM, bsmtrain_LRG);
            //Системний аналіз і управління - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosSAU_SM, spec_EQL,    stat_EF, 30, cal_2013_07_01.getTime(),  kafSAVM, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSAU_SM, spec_EQL,    corr_EF, 30, cal_2013_07_01.getTime(),  kafSAVM, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSAU_SM, master_EQL,  stat_EF, 10, cal_2009_07_01.getTime(),  kafSAVM, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSAU_SM, master_EQL,  corr_EF, 10, cal_2009_07_01.getTime(),  kafSAVM, bsmtrain_LRG);
            //Інформатика - бакалаври
            saveOrUpdateLR(licenseCPU, tdosI_B,    bach_EQL,    stat_EF, 30, cal_2010_07_01.getTime(),  kafSAVM, bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosI_B,    bach_EQL,    corr_EF, 0,  cal_2010_07_01.getTime(),  kafSAVM, bsmtrain_LRG);

            //Комп'ютерні науки
            //Програмне забезпечення автоматизованих систем - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPZAS_B,  bach_EQL,   stat_EF, 60, cal_2010_07_01.getTime(), kafPIT,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZAS_B,  bach_EQL,   corr_EF, 60, cal_2010_07_01.getTime(), kafPIT,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZAS_SM, spec_EQL,   stat_EF, 30, cal_2010_07_01.getTime(), kafPIT,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZAS_SM, spec_EQL,   corr_EF, 30, cal_2010_07_01.getTime(), kafPIT,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZAS_SM, master_EQL, stat_EF, 10, cal_2010_07_01.getTime(), kafPIT,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZAS_SM, master_EQL, corr_EF, 10, cal_2010_07_01.getTime(), kafPIT,   bsmtrain_LRG);

            //Електроніка
            //Фізична та біомедична електроніка - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosFBE_B,   bach_EQL,   stat_EF, 30, cal_2010_07_01.getTime(), kafPTD,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosFBE_B,   bach_EQL,   corr_EF, 30, cal_2010_07_01.getTime(), kafPTD,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosFBE_SM,  spec_EQL,   stat_EF, 30, cal_2010_07_01.getTime(), kafPTD,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosFBE_SM,  spec_EQL,   corr_EF, 30, cal_2010_07_01.getTime(), kafPTD,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosFBE_SM,  master_EQL, stat_EF, 10, cal_2010_07_01.getTime(), kafPTD,   bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosFBE_SM,  master_EQL, corr_EF, 10, cal_2010_07_01.getTime(), kafPTD,   bsmtrain_LRG);

            //Державне управління
            //Державна служба - магістри
            saveOrUpdateLR(licenseCPU, tdosDS_M,    master_EQL, stat_EF, 60, cal_2012_07_01.getTime(), kafDUAM,  bsmtrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosDS_M,    master_EQL, corr_EF, 60,cal_2012_07_01.getTime(),  kafDUAM,  bsmtrain_LRG);



            //Перепідготовка спеціалістів
            //Фізичне виховання і спорт
            //Фізичне виховання  - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosPhysV_SM, spec_EQL,    stat_EF, 15, cal_2010_07_01.getTime(), kafTOFAV, sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhysV_SM, spec_EQL,    corr_EF, 15, cal_2010_07_01.getTime(), kafTOFAV, sretrain_LRG);
            //Фізична реабілітація  - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosPhysR_SM, spec_EQL,    stat_EF, 15, cal_2010_07_01.getTime(), kafFR,    sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhysR_SM, spec_EQL,    corr_EF, 15, cal_2010_07_01.getTime(), kafFR,    sretrain_LRG);

            //Журналістика
            //Журналістика - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosZhurn_SM, spec_EQL,    stat_EF, 15, cal_2010_07_01.getTime(), kafZSK,   sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosZhurn_SM, spec_EQL,    corr_EF, 15, cal_2010_07_01.getTime(), kafZSK,   sretrain_LRG);

            //Філологія
            //Мова та література (англійська) - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosLangLit_SM, spec_EQL,   stat_EF, 10,  cal_2009_07_01.getTime(), kafAF,  sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosLangLit_SM, spec_EQL,   corr_EF, 10,  cal_2009_07_01.getTime(), kafAF,  sretrain_LRG);
            //Переклад - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosTransl_SM,  spec_EQL,   stat_EF, 15,  cal_2010_07_01.getTime(), kafTPP, sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosTransl_SM,  spec_EQL,   corr_EF, 15,  cal_2010_07_01.getTime(), kafTPP, sretrain_LRG);

            //Психологія
            //Психологія - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosPS_SM ,     spec_EQL,   stat_EF, 25,  cal_2010_07_01.getTime(), kafPP,  sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPS_SM ,     spec_EQL,   corr_EF, 25,  cal_2010_07_01.getTime(), kafPP,  sretrain_LRG);

            //Соціологія
            //Соціальна робота - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosSR_SM ,     spec_EQL,   stat_EF, 15,  cal_2012_07_01.getTime(), kafSSR, sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSR_SM ,     spec_EQL,   corr_EF, 15,  cal_2012_07_01.getTime(), kafSSR, sretrain_LRG);

            //Економіка і підприємництво
            //Міжнародна економіка - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosME_SM,      spec_EQL,   stat_EF, 25,  cal_2010_07_01.getTime(), kafME,  sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosME_SM,      spec_EQL,   corr_EF, 25,  cal_2010_07_01.getTime(), kafME,  sretrain_LRG);
            //Фінанси - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosF_SM,       spec_EQL,   stat_EF, 60,  cal_2010_07_01.getTime(), kafFK,  sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosF_SM,       spec_EQL,   corr_EF, 60,  cal_2010_07_01.getTime(), kafFK,  sretrain_LRG);
            //Облік і аудит - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosOA_SM,      spec_EQL,   stat_EF, 30,  cal_2010_07_01.getTime(), kafOA,  sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosOA_SM,      spec_EQL,   corr_EF, 30,  cal_2010_07_01.getTime(), kafOA,  sretrain_LRG);
            //Банківська справа - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosBS_SM,      spec_EQL,    stat_EF, 25, cal_2010_07_01.getTime(), kafFK,  sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosBS_SM,      spec_EQL,    corr_EF, 25, cal_2010_07_01.getTime(), kafFK,  sretrain_LRG);
            //Економіка підприємства - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosEP_SM,      spec_EQL,    stat_EF, 25, cal_2010_07_01.getTime(), kafEP,  sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEP_SM,      spec_EQL,    corr_EF, 25, cal_2010_07_01.getTime(), kafEP,  sretrain_LRG);

            //Менеджмент
            //Менеджмент організацій - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosMO_SM,      spec_EQL,    stat_EF, 40, cal_2010_07_01.getTime(), kafMG,  sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMO_SM,      spec_EQL,    corr_EF, 40, cal_2010_07_01.getTime(), kafMG,  sretrain_LRG);

            //Комп'ютерні науки
            //Програмне забезпечення автоматизованих систем
            saveOrUpdateLR(licenseCPU, tdosPZAS_SM,    spec_EQL,    stat_EF, 15, cal_2010_07_01.getTime(), kafPIT, sretrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPZAS_SM,    spec_EQL,    corr_EF, 15, cal_2010_07_01.getTime(), kafPIT, sretrain_LRG);



            //Для коледжу КПУ
            //Журналістика
            //Видавнича справа та редагування - молодші спеціалісти
            saveOrUpdateLR(licenseCPU, tdosVSR_S,    jSpec_EQL,    stat_EF, 0,   cal_2011_07_01.getTime(), kafVSRUF, forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosVSR_S,    jSpec_EQL,    corr_EF, 30,  cal_2011_07_01.getTime(), kafVSRUF, forcol_LRG);

            //Соціологія
            //Соціальна робота - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosSR_SM,    jSpec_EQL,    stat_EF, 0,   cal_2012_07_01.getTime(), kafSSR,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosSR_SM,    jSpec_EQL,    corr_EF, 30,  cal_2012_07_01.getTime(), kafSSR,   forcol_LRG);

            //Економіка і підприємництво
            //Економіка підприємства
            saveOrUpdateLR(licenseCPU, tdosEP_SM,    jSpec_EQL,    stat_EF, 0,   cal_2012_07_01.getTime(), kafEP,    forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosEP_SM,    jSpec_EQL,    corr_EF, 30,  cal_2012_07_01.getTime(), kafEP,    forcol_LRG);

            //Менеджмент
            //Організація обслуговування населення
            saveOrUpdateLR(licenseCPU, tdosOON_JS,   jSpec_EQL,    stat_EF, 0,   cal_2011_07_01.getTime(), kafUND,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosOON_JS,   jSpec_EQL,    corr_EF, 30,  cal_2011_07_01.getTime(), kafUND,   forcol_LRG);

            //Торгівля
            //Товарознавство та комерційна діяльність
            saveOrUpdateLR(licenseCPU, tdosTKD_JS,   jSpec_EQL,    stat_EF, 0,   cal_2011_07_01.getTime(), kafUND,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosTKD_JS,   jSpec_EQL,    corr_EF, 30,  cal_2011_07_01.getTime(), kafUND,   forcol_LRG);

            //Туризм
            //Організація обслуговування в готелях та туристичних комплексах
            saveOrUpdateLR(licenseCPU, tdosOOGTK_JS, jSpec_EQL,    stat_EF, 0,   cal_2012_07_01.getTime(), kafTGG,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosOOGTK_JS, jSpec_EQL,    corr_EF, 30,  cal_2012_07_01.getTime(), kafTGG,   forcol_LRG);

            //Прикладна математика
            //Прикладна математика
            saveOrUpdateLR(licenseCPU, tdosPM_JS,    jSpec_EQL,    stat_EF, 0,   cal_2011_07_01.getTime(), kafSAVM,  forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosPM_JS,    jSpec_EQL,    corr_EF, 30,  cal_2011_07_01.getTime(), kafSAVM,  forcol_LRG);

            //Комп'ютерні науки
            //Програмування для електроно-обчислювальної техніки і автоматизованих систем
            saveOrUpdateLR(licenseCPU, tdosPECTAS_JS,jSpec_EQL,      stat_EF, 0, cal_2012_07_01.getTime(), kafPIT,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosPECTAS_JS,jSpec_EQL,     corr_EF, 30, cal_2012_07_01.getTime(), kafPIT,   forcol_LRG);



            //Новая лицензия
            //Подготовка бакалавров
            //Фізичне виховання, спорт і здоров'я людини
            //Фізичне виховання
            saveOrUpdateLR(licenseCPU, tdosPhysV_B,  bach_EQL,   stat_EF, 50,  cal_2010_07_01.getTime(), kafTOFAV, btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhysV_B,  bach_EQL,   corr_EF, 50,  cal_2010_07_01.getTime(), kafTOFAV, btrain_LRG);
            //Здоров'я людини
            saveOrUpdateLR(licenseCPU, tdosZL_B,     bach_EQL,   stat_EF, 60,  cal_2010_07_01.getTime(), kafFR,    btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosZL_B,     bach_EQL,   corr_EF, 60,  cal_2010_07_01.getTime(), kafFR,    btrain_LRG);

            //Культура
            //Туризм
            saveOrUpdateLR(licenseCPU, tdosT_B,      bach_EQL,   stat_EF, 90,  cal_2013_07_01.getTime(), kafTGG,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosT_B,      bach_EQL,   corr_EF, 90,  cal_2013_07_01.getTime(), kafTGG,   btrain_LRG);

            //Гуманітарні науки
            //Філологія
            saveOrUpdateLR(licenseCPU, tdosPhilol_B, bach_EQL,   stat_EF, 135, cal_2010_07_01.getTime(), kafUND,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPhilol_B, bach_EQL,   corr_EF, 85,  cal_2010_07_01.getTime(), kafUND,   btrain_LRG);

            //Соціально-політичні науки
            //Соціологія
            saveOrUpdateLR(licenseCPU, tdosS_B,      bach_EQL,   stat_EF, 50,  cal_2012_07_01.getTime(), kafSSR,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosS_B,      bach_EQL,   corr_EF, 50,  cal_2012_07_01.getTime(), kafSSR,   btrain_LRG);
            //Психологія
            saveOrUpdateLR(licenseCPU, tdosPSH_B,    bach_EQL,   stat_EF, 30,  cal_2010_07_01.getTime(), kafPP,    btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPSH_B,    bach_EQL,   corr_EF, 30,  cal_2010_07_01.getTime(), kafPP,    btrain_LRG);

            //Міжнародні відносини
            //Міжнародні відносини
            saveOrUpdateLR(licenseCPU, tdosMV_B,     bach_EQL,   stat_EF, 30,  cal_2013_07_01.getTime(), kafMP,    btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMV_B,     bach_EQL,   corr_EF, 30,  cal_2013_07_01.getTime(), kafMP,    btrain_LRG);

            //Журналістика та інформація
            //Журналістика
            saveOrUpdateLR(licenseCPU, tdosZhu_B,    bach_EQL,   stat_EF, 50,  cal_2011_07_01.getTime(), kafZSK,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosZhu_B,    bach_EQL,   corr_EF, 50,  cal_2011_07_01.getTime(), kafZSK,   btrain_LRG);
            //Реклама і зв'язки з громадкістю (за видами)
            saveOrUpdateLR(licenseCPU, tdosRZG_B,    bach_EQL,   stat_EF, 30,  cal_2011_07_01.getTime(), kafRZG,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosRZG_B,    bach_EQL,   corr_EF, 0,   cal_2011_07_01.getTime(), kafRZG,   btrain_LRG);
            //Видавнича справа та редагування
            saveOrUpdateLR(licenseCPU, tdosVSR_B,    bach_EQL,   stat_EF, 30,  cal_2011_07_01.getTime(), kafVSRUF, btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosVSR_B,    bach_EQL,   corr_EF, 50,  cal_2011_07_01.getTime(), kafVSRUF, btrain_LRG);

            //Право
            //Право
            saveOrUpdateLR(licenseCPU, tdosP_B,      bach_EQL,   stat_EF, 150, cal_2012_07_01.getTime(), kafTZGP,  btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosP_B,      bach_EQL,   corr_EF, 250, cal_2012_07_01.getTime(), kafTZGP,  btrain_LRG);

            //Економіка і підприємництво
            //Економічна кібернетика
            saveOrUpdateLR(licenseCPU, tdosEKib_B,  bach_EQL,   stat_EF, 60,   cal_2010_07_01.getTime(), kafEKS,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEKib_B,  bach_EQL,   corr_EF, 60,   cal_2010_07_01.getTime(), kafEKS,   btrain_LRG);
            //Міжнародна економіка
            saveOrUpdateLR(licenseCPU, tdosME_B,    bach_EQL,   stat_EF, 60,   cal_2010_07_01.getTime(), kafME,    btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosME_B,    bach_EQL,   corr_EF, 30,   cal_2010_07_01.getTime(), kafME,    btrain_LRG);
            //Економіка підприємства
            saveOrUpdateLR(licenseCPU, tdosEP_B,    bach_EQL,   stat_EF, 60,   cal_2010_07_01.getTime(), kafEP,    btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosEP_B,    bach_EQL,   corr_EF, 60,   cal_2010_07_01.getTime(), kafEP,    btrain_LRG);
            //Прикладна статистика
            saveOrUpdateLR(licenseCPU, tdosPRS_B,   bach_EQL,   stat_EF, 20,   cal_2010_07_01.getTime(), kafEKS,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPRS_B,   bach_EQL,   corr_EF, 30,   cal_2010_07_01.getTime(), kafEKS,   btrain_LRG);
            //Маркетинг
            saveOrUpdateLR(licenseCPU, tdosM_B,     bach_EQL,   stat_EF, 60,   cal_2010_07_01.getTime(), kafM,    btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosM_B,     bach_EQL,   corr_EF, 60,   cal_2010_07_01.getTime(), kafM,    btrain_LRG);
            //Фінанси і кредит
            saveOrUpdateLR(licenseCPU, tdosFK_B,    bach_EQL,   stat_EF, 120,  cal_2010_07_01.getTime(), kafFK,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosFK_B,    bach_EQL,   corr_EF, 120,  cal_2010_07_01.getTime(), kafFK,   btrain_LRG);
            //Облік і аудит
            saveOrUpdateLR(licenseCPU, tdosOA_B,    bach_EQL,   stat_EF, 100,  cal_2013_07_01.getTime(), kafOA,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosOA_B,    bach_EQL,   corr_EF, 150,  cal_2013_07_01.getTime(), kafOA,   btrain_LRG);

            //Менеджмент і адміністрування
            //Менеджмент
            saveOrUpdateLR(licenseCPU, tdosMG_B,    bach_EQL,   stat_EF, 140,  cal_2010_07_01.getTime(), kafMG,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMG_B,    bach_EQL,   corr_EF, 250,  cal_2010_07_01.getTime(), kafMG,   btrain_LRG);

            //Системні науки та кібернетика
            //Інформатика
            saveOrUpdateLR(licenseCPU, tdosIF_B,    bach_EQL,   stat_EF, 30,   cal_2009_07_01.getTime(), kafSAVM, btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosIF_B,    bach_EQL,   corr_EF, 0,    cal_2009_07_01.getTime(), kafSAVM, btrain_LRG);
            //Системний аналіз
            saveOrUpdateLR(licenseCPU, tdosSA_B,    bach_EQL,   stat_EF, 30,   cal_2009_07_01.getTime(), kafSAVM, btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosSA_B,    bach_EQL,   corr_EF, 30,   cal_2009_07_01.getTime(), kafSAVM, btrain_LRG);

            //Інформатика та обчислювальна техніка
            //Програмна інженерія
            saveOrUpdateLR(licenseCPU, tdosPI_B,    bach_EQL,   stat_EF, 60,   cal_2010_07_01.getTime(), kafPIT,  btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosPI_B,    bach_EQL,   corr_EF, 60,   cal_2010_07_01.getTime(), kafPIT,  btrain_LRG);

            //Електроніка
            //Мікро- та наноелектроніка
            saveOrUpdateLR(licenseCPU, tdosMNE_B,   bach_EQL,   stat_EF, 30,   cal_2010_07_01.getTime(), kafPTD,  btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosMNE_B,   bach_EQL,   corr_EF, 30,   cal_2010_07_01.getTime(), kafPTD,  btrain_LRG);

            //Геодезія та землеустрій
            //Геодезія, картографія та землеустрій
            saveOrUpdateLR(licenseCPU, tdosGKZ_B,   bach_EQL,   stat_EF, 30,   cal_2011_07_01.getTime(), kafZK,   btrain_LRG);
            saveOrUpdateLR(licenseCPU, tdosGKZ_B,   bach_EQL,   corr_EF, 0,    cal_2011_07_01.getTime(), kafZK,   btrain_LRG);



            //Подготовка младших специалистов
            //Культура
            //Організація туристичного обслуговування
            saveOrUpdateLR(licenseCPU, tdosOTO_JS,  jSpec_EQL,    stat_EF, 0,  cal_2012_07_01.getTime(), kafUND,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosOTO_JS,  jSpec_EQL,    corr_EF, 30, cal_2012_07_01.getTime(), kafUND,   forcol_LRG);

            //Журналістика та інформація
            //Видавнича справа та редагування
            saveOrUpdateLR(licenseCPU, tdosVSTR_JS, jSpec_EQL,    stat_EF, 0,  cal_2011_07_01.getTime(), kafVSRUF, forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosVSTR_JS, jSpec_EQL,    corr_EF, 30, cal_2011_07_01.getTime(), kafVSRUF, forcol_LRG);

            //Економіка та підприємництво
            //Економіка підприємства
            saveOrUpdateLR(licenseCPU, tdosEKP_JS,  jSpec_EQL,    stat_EF, 0,  cal_2012_07_01.getTime(), kafEP,    forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosEKP_JS,  jSpec_EQL,    corr_EF, 30, cal_2012_07_01.getTime(), kafEP,    forcol_LRG);
            //Товарознавство та комерційна діяльність
            saveOrUpdateLR(licenseCPU, tdosTZKD_JS, jSpec_EQL,    stat_EF, 0,  cal_2011_07_01.getTime(), kafUND,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosTZKD_JS, jSpec_EQL,    corr_EF, 30, cal_2011_07_01.getTime(), kafUND,   forcol_LRG);

            //Системні науки та кібернетика
            //Прикладна математика
            saveOrUpdateLR(licenseCPU, tdosPRM_JS,  jSpec_EQL,    stat_EF, 0,  cal_2011_07_01.getTime(), kafSAVM,  forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosPRM_JS,  jSpec_EQL,    corr_EF, 30, cal_2011_07_01.getTime(), kafSAVM,  forcol_LRG);

            //Інформатика та обчислювальна техника
            //Розробка програмного забезпечення
            saveOrUpdateLR(licenseCPU, tdosRPZ_JS,  jSpec_EQL,    stat_EF, 0,  cal_2012_07_01.getTime(), kafPIT,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosRPZ_JS,  jSpec_EQL,    corr_EF, 30, cal_2012_07_01.getTime(), kafPIT,   forcol_LRG);

            //Соціальне забезпечення
            //Соціальна робота
            saveOrUpdateLR(licenseCPU, tdosSOCR_JS, jSpec_EQL,    stat_EF, 0,  cal_2012_07_01.getTime(), kafSSR,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosSOCR_JS, jSpec_EQL,    corr_EF, 30, cal_2012_07_01.getTime(), kafSSR,   forcol_LRG);

            //Сфера обслуговування
            //Організація обслуговування населення
            saveOrUpdateLR(licenseCPU, tdosOONAS_JS,jSpec_EQL,    stat_EF, 0,  cal_2011_07_01.getTime(), kafUND,   forcol_LRG);
            saveOrUpdateLR(licenseCPU, tdosOONAS_JS,jSpec_EQL,    corr_EF, 30, cal_2011_07_01.getTime(), kafUND,   forcol_LRG);

            //TODO Добавить лицензионные записи
        }
    }

    //---------Метод сохранения/обновления областей знаний или направлений подготовки ----------
    private KnowledgeAreaOrTrainingDirection saveOrUpdateKAOTD(String name, String shortName, String code, Boolean isKnowledgeArea, Boolean isObsolete) {

        //--Создание контроллера, работающего с экземплярами областей знаний или направлений подготовки
        Controller<KnowledgeAreaOrTrainingDirection, Integer> kaotdController = iDBSpt.getControllerSource().get(KnowledgeAreaOrTrainingDirection.class);

        //---Создание образца области знаний или направления подготовки
        KnowledgeAreaOrTrainingDirection kaotdSample = new KnowledgeAreaOrTrainingDirection();
        kaotdSample.setName(name);
        kaotdSample.setCode(code);
        kaotdSample.setIsKnowledgeArea(isKnowledgeArea);

        //--Выборка списка областей знаний или направлений подготовки
        List<KnowledgeAreaOrTrainingDirection> kaotd = kaotdController.findByExample(kaotdSample);
        //--Переменная для работы с экземпляром элемента спискка
        KnowledgeAreaOrTrainingDirection p;
        //--Инициализация элемента списка
        if (kaotd.isEmpty()) {
            p = new KnowledgeAreaOrTrainingDirection();
            p.setName(name);
            p.setShortName(shortName);
            p.setCode(code);
            p.setIsKnowledgeArea(isKnowledgeArea);
            p.setIsObsolete(isObsolete);

        } else {
            p = kaotd.get(0);
        }
        return kaotdController.saveOrUpdate(p);
    }

//---------Метод сохранения/обновления направлений подготовки или специальности----------
    private TrainingDirectionOrSpeciality saveOrUpdateTDOS(
            String name,
            String shortName,
            String code,
            Boolean isTrainingDirection,
            KnowledgeAreaOrTrainingDirection knowledgeAreaOrTrainingDirection,
            Boolean isObsolete) {

        //--Создание контроллера, работающего с экземплярами направлений подготовки или специальности
        Controller<TrainingDirectionOrSpeciality, Integer> tdosController = iDBSpt.getControllerSource().get(TrainingDirectionOrSpeciality.class);

        //---Создание образца направления подготовки или специальности
        TrainingDirectionOrSpeciality tdosSample = new TrainingDirectionOrSpeciality();
//        tdosSample.setName(name);
        tdosSample.setShortName(shortName);
        tdosSample.setCode(code);
        tdosSample.setIsTrainingDirection(isTrainingDirection);
        tdosSample.setKnowledgeAreaOrTrainingDirection(knowledgeAreaOrTrainingDirection);

        //--Выборка списка направлений подготовки или специальности
        List<TrainingDirectionOrSpeciality> tdos = tdosController.findByExample(tdosSample);
        //--Переменная для работы с экземпляром элемента спискка
        TrainingDirectionOrSpeciality p;
        //--Инициализация элемента списка
        if (tdos.isEmpty()) {
            p = new TrainingDirectionOrSpeciality();
            p.setName(name);
            p.setShortName(shortName);
            p.setCode(code);
            p.setIsTrainingDirection(isTrainingDirection);
            p.setKnowledgeAreaOrTrainingDirection(knowledgeAreaOrTrainingDirection);
            p.setIsObsolete(isObsolete);

        } else {
            p = tdos.get(0);
        }
        return tdosController.saveOrUpdate(p);
    }

//---------Метод сохранения/обновления серии и номера лицензии----------
    private License saveOrUpdateLicense(
            String serial,
            String number,
            Date issue,
            Map<String, DocumentImage> images) {

        //--Создание контроллера, работающего с экземплярами шапки лицензии
        Controller<License, Integer> licenseController = iDBSpt.getControllerSource().get(License.class);

        //---Создание образца шапки лицензии
        License licenseSample = new License();
        licenseSample.setSerial(serial);
        licenseSample.setNumber(number);
        licenseSample.setIssue(issue);
        licenseSample.setImages(images);

        //--Выборка списка данных шапок лицензий
        List<License> license = licenseController.findByExample(licenseSample);
        //--Переменная для работы с экземпляром элемента спискка
        License p;
        //--Инициализация элемента списка
        if (license.isEmpty()) {
            p = new License();
            p.setSerial(serial);
            p.setNumber(number);
            p.setIssue(issue);
            p.setImages(images);
        } else {
            p = license.get(0);
        }
        return licenseController.saveOrUpdate(p);
    }

//---------Метод сохранения/обновления записи лицензии----------
    private LicenseRecord saveOrUpdateLR(
            License license,
            TrainingDirectionOrSpeciality trainingDirectionOrSpeciality,
            EducationalQualificationLevel educationalQualificationLevel,
            EducationForm educationForm,
            Integer studentLicenseQuantity,
            Date terminationDate,
            OrgUnit orgUnit,
            LicenseRecordGroup licenseRecordGroup) {

        //--Создание контроллера, работающего с экземплярами записи лицензии
        Controller<LicenseRecord, Integer> lrController = iDBSpt.getControllerSource().get(LicenseRecord.class);

        //---Создание образца записи лицензии
        LicenseRecord lrSample = new LicenseRecord();
        lrSample.setLicense(license);
        lrSample.setTrainingDirectionOrSpeciality(trainingDirectionOrSpeciality);
        lrSample.setEducationalQualificationLevel(educationalQualificationLevel);
        lrSample.setEducationForm(educationForm);
        lrSample.setStudentLicenseQuantity(studentLicenseQuantity);
        lrSample.setTerminationDate(terminationDate);
        lrSample.setOrgUnit(orgUnit);
        lrSample.setLicenseRecordGroup(licenseRecordGroup);

        //--Выборка списка данных записи лицензии
        List<LicenseRecord> lr = lrController.findByExample(lrSample);
        //--Переменная для работы с экземпляром элемента спискка
        LicenseRecord p;
        //--Инициализация элемента списка

        if (lr.isEmpty()) {
            p = new LicenseRecord();
            p.setLicense(license);
            p.setTrainingDirectionOrSpeciality(trainingDirectionOrSpeciality);
            p.setEducationalQualificationLevel(educationalQualificationLevel);
            p.setEducationForm(educationForm);
            p.setStudentLicenseQuantity(studentLicenseQuantity);
            p.setTerminationDate(terminationDate);
            p.setOrgUnit(orgUnit);
            p.setLicenseRecordGroup(licenseRecordGroup);
        } else {
            p = lr.get(0);
        }
        return lrController.saveOrUpdate(p);
    }
}
