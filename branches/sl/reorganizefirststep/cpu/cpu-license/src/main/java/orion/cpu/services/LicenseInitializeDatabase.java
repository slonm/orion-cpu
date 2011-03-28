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
            KnowledgeAreaOrTrainingDirection kaotdCompSci = saveOrUpdateKAOTD("Комп'ютерні науки", null, "0804", false, false);
            KnowledgeAreaOrTrainingDirection kaotdInfComp = saveOrUpdateKAOTD("Інформатика та обчислювальна техніка", null, "0501", true, false);
            KnowledgeAreaOrTrainingDirection kaotdSpecCateg = saveOrUpdateKAOTD("Специфічні категорії", null, "0000", false, false);
            KnowledgeAreaOrTrainingDirection kaotdSysSciCyber = saveOrUpdateKAOTD("Системні науки та кібернетика", null, "0403", true, false);

            //---Направления подготовки или специальности суффиксы _С, _B, _SM обозначают квалификационные уровни младшего специалиста, бакалавра, специалиста/магистра, соответственно----------
            LOG.debug("Add Test TrainingDirectionOrSpecialitys...");
            TrainingDirectionOrSpeciality tdosPZAS_B = saveOrUpdateTDOS("Програмне забезпечення автоматизованих систем", "ПЗАС", "00", false, kaotdCompSci, false);
            TrainingDirectionOrSpeciality tdosPZAS_SM = saveOrUpdateTDOS("Програмне забезпечення автоматизованих систем", "ПЗАС", "03", false, kaotdCompSci, false);
            TrainingDirectionOrSpeciality tdosPECTAS_JS = saveOrUpdateTDOS("Програмування для електронно-обчислювальної техніки і автоматизованих систем", "ПЗАС", "05", false, kaotdCompSci, false);
            TrainingDirectionOrSpeciality tdosPI_B = saveOrUpdateTDOS("Програмна інженерія", "ПІ", "03", true, kaotdInfComp, false);
            TrainingDirectionOrSpeciality tdosRPZ_JS = saveOrUpdateTDOS("Розробка програмного забезпечення", "РПЗ", "0301", true, kaotdInfComp, false);
            TrainingDirectionOrSpeciality tdosPVSH_SM = saveOrUpdateTDOS("Педагогіка вищої школи", "ПВШ", "05", false, kaotdSpecCateg, false);
            TrainingDirectionOrSpeciality tdosSA_B = saveOrUpdateTDOS("Системний аналіз та управління", "СА", "03", true, kaotdSysSciCyber, false);

            //---Серия, номер и дата выдачи лицензии----------
            LOG.debug("Add Test License...");
            Calendar licCal = Calendar.getInstance();
            licCal.set(Calendar.YEAR, 2008);
            licCal.set(Calendar.MONTH, Calendar.OCTOBER);
            licCal.set(Calendar.DAY_OF_MONTH, 21);
            License licenseCPU = saveOrUpdateLicense("АВ", "420720", licCal.getTime(), null);

            //---Кафедры, выполняющие обучение по лицензиям----------
            NamedEntityController<OrgUnit> ouCnt = (NamedEntityController<OrgUnit>) (Object) iDBSpt.getControllerSource().get(OrgUnit.class);
            OrgUnit kafPIT = ouCnt.findByNameFirst("кафедра програмування та інформаційних технологій");
            OrgUnit kafEICPHS = ouCnt.findByNameFirst("кафедра управління навчальними закладами та педагогіки вищої школи");
            OrgUnit kafSAVM = ouCnt.findByNameFirst("кафедра системного аналізу та вищої математики");
            //Термін закінчення ліцензій ПЗАС та ПІ
            Calendar lrCal20100701 = Calendar.getInstance();
            lrCal20100701.set(Calendar.YEAR, 2010);
            lrCal20100701.set(Calendar.MONTH, Calendar.JULY);
            lrCal20100701.set(Calendar.DAY_OF_MONTH, 1);
            //Термін закінчення ліцензій ПВШ
            Calendar lrCal20090701 = Calendar.getInstance();
            lrCal20090701.set(Calendar.YEAR, 2009);
            lrCal20090701.set(Calendar.MONTH, Calendar.JULY);
            lrCal20090701.set(Calendar.DAY_OF_MONTH, 1);
            LOG.debug("Add Test LicenseRecords...");
            //---Записи лицензии-суффиксы _JS, _B, _S, _M обозначают
            //квалификационные уровни младшего специалиста, бакалавра, специалиста, магистра, соответственно
            //stat_EF, corr_EF обозначают дневную и заочн формы обучения----------
            //Програмне забезпечення автоматизованих систем - молодші спеціалісти, денна
            LicenseRecord pZASJSpecStat = saveOrUpdateLR(licenseCPU, tdosPECTAS_JS, jSpec_EQL, stat_EF, 0, lrCal20100701.getTime(), kafPIT, forcol_LRG);
            //Програмне забезпечення автоматизованих систем - молодші спеціалісти, заочна
            LicenseRecord pZASJSpecCorr = saveOrUpdateLR(licenseCPU, tdosPECTAS_JS, jSpec_EQL, corr_EF, 30, lrCal20100701.getTime(), kafPIT, forcol_LRG);
            //Програмне забезпечення автоматизованих систем - бакалаври, денна
            LicenseRecord pZASBachStat = saveOrUpdateLR(licenseCPU, tdosPZAS_B, bach_EQL, stat_EF, 60, lrCal20100701.getTime(), kafPIT, bsmtrain_LRG);
            //Програмне забезпечення автоматизованих систем - бакалаври, заочна
            LicenseRecord pZASBachCorr = saveOrUpdateLR(licenseCPU, tdosPZAS_B, bach_EQL, corr_EF, 60, lrCal20100701.getTime(), kafPIT, bsmtrain_LRG);
            //Програмне забезпечення автоматизованих систем - специалісти, денна
            LicenseRecord pZASSpecStat = saveOrUpdateLR(licenseCPU, tdosPZAS_SM, spec_EQL, stat_EF, 30, lrCal20100701.getTime(), kafPIT, bsmtrain_LRG);
            //Програмне забезпечення автоматизованих систем - специалісти, заочна
            LicenseRecord pZASSpecCorr = saveOrUpdateLR(licenseCPU, tdosPZAS_SM, spec_EQL, corr_EF, 30, lrCal20100701.getTime(), kafPIT, bsmtrain_LRG);
            //Програмне забезпечення автоматизованих систем - магістри, денна
            LicenseRecord pZASMasterStat = saveOrUpdateLR(licenseCPU, tdosPZAS_SM, master_EQL, stat_EF, 10, lrCal20100701.getTime(), kafPIT, bsmtrain_LRG);
            //Програмне забезпечення автоматизованих систем - магістри, заочна
            LicenseRecord pZASMasterCorr = saveOrUpdateLR(licenseCPU, tdosPZAS_SM, master_EQL, corr_EF, 10, lrCal20100701.getTime(), kafPIT, bsmtrain_LRG);
            //Розробка програмного забезпечення - молодші спеціалісти, денна
            LicenseRecord rPZJSpecStat = saveOrUpdateLR(licenseCPU, tdosRPZ_JS, jSpec_EQL, stat_EF, 0, lrCal20100701.getTime(), kafPIT, jstrain_LRG);
            //Розробка програмного забезпечення - молодші спеціалісти, заочна
            LicenseRecord rPZJSpecCorr = saveOrUpdateLR(licenseCPU, tdosRPZ_JS, jSpec_EQL, corr_EF, 30, lrCal20100701.getTime(), kafPIT, jstrain_LRG);
            //Програмна інженерія- бакалаври, денна
            LicenseRecord pIBachStat = saveOrUpdateLR(licenseCPU, tdosPI_B, bach_EQL, stat_EF, 60, lrCal20100701.getTime(), kafPIT, btrain_LRG);
            //Програмна інженерія- бакалаври, заочна
            LicenseRecord pIBachCorr = saveOrUpdateLR(licenseCPU, tdosPI_B, bach_EQL, corr_EF, 60, lrCal20100701.getTime(), kafPIT, btrain_LRG);
            //Педагогіка вищої школи- магістри, денна
            LicenseRecord pVSHMasterStat = saveOrUpdateLR(licenseCPU, tdosPVSH_SM, master_EQL, stat_EF, 30, lrCal20090701.getTime(), kafEICPHS, bsmtrain_LRG);
            ////Педагогіка вищої школи- магістри, заочна
            LicenseRecord pVSHMasterCorr = saveOrUpdateLR(licenseCPU, tdosPVSH_SM, master_EQL, corr_EF, 30, lrCal20090701.getTime(), kafEICPHS, bsmtrain_LRG);
            //Програмна інженерія- бакалаври, денна
            LicenseRecord sABachStat = saveOrUpdateLR(licenseCPU, tdosSA_B, bach_EQL, stat_EF, 30, lrCal20090701.getTime(), kafSAVM, btrain_LRG);
            //Програмна інженерія- бакалаври, заочна
            LicenseRecord sABachCorr = saveOrUpdateLR(licenseCPU, tdosSA_B, bach_EQL, corr_EF, 30, lrCal20090701.getTime(), kafSAVM, btrain_LRG);
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
