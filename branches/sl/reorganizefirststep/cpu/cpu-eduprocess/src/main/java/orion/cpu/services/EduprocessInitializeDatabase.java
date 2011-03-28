package orion.cpu.services;

import orion.cpu.security.OperationTypes;
import orion.cpu.services.impl.InitializeDatabaseSupport;
import ua.mihailslobodyanuk.utils.Defense;
import orion.cpu.entities.ref.*;
import orion.cpu.entities.sys.SubSystem;
import java.util.*;
import br.com.arsmachina.authentication.entity.*;
import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.controller.Controller;
import org.slf4j.*;
import orion.cpu.controllers.NamedEntityController;
import orion.cpu.entities.uch.*;

/**
 * Начальная инициализация данных.
 * @author sl
 */
public class EduprocessInitializeDatabase extends OperationTypes implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(EduprocessInitializeDatabase.class);
    private final InitializeDatabaseSupport iDBSpt;

    public EduprocessInitializeDatabase(InitializeDatabaseSupport initializeDatabaseSupport) {
        this.iDBSpt = Defense.notNull(initializeDatabaseSupport, "initializeDatabaseSupport");
    }

    @Override
    public void run() {
        LOG.debug("Add SubSystem...");
        SubSystem subSystem = iDBSpt.saveOrUpdateSubSystem("EduProcess");
        //---------Константы
        //---------Циклы дисциплин в ОПП "Программная инженерия" (в других могут отличаться!!!)---------
        LOG.debug("Add EPPCycle...");
        EPPCycle humsocecon_DC = iDBSpt.getStoredConstantsSource().get(EPPCycle.class, EPPCycle.SOCIALHUMANITARY_KEY);
        if (humsocecon_DC == null) {
            humsocecon_DC = new EPPCycle("Цикл гуманітарної та соціально-економічної підготовки", "ЦГСЭП");
            iDBSpt.getStoredConstantsSource().put(EPPCycle.class, EPPCycle.SOCIALHUMANITARY_KEY, humsocecon_DC);
        }
        EPPCycle mathnatsci_DC = iDBSpt.getStoredConstantsSource().get(EPPCycle.class, EPPCycle.NATUREFUNDAMENTALECONOMICAL_KEY);
        if (mathnatsci_DC == null) {
            mathnatsci_DC = new EPPCycle("Цикл математичної, природничо-наукової підготовки", "ЦМПНП");
            iDBSpt.getStoredConstantsSource().put(EPPCycle.class, EPPCycle.NATUREFUNDAMENTALECONOMICAL_KEY, mathnatsci_DC);
        }
        EPPCycle prof_DC = iDBSpt.getStoredConstantsSource().get(EPPCycle.class, EPPCycle.PROFESSIONALPRACTICAL_KEY);
        if (prof_DC == null) {
            prof_DC = new EPPCycle("Цикл професійної та практичної підготовки", "ЦППП");
            iDBSpt.getStoredConstantsSource().put(EPPCycle.class, EPPCycle.PROFESSIONALPRACTICAL_KEY, prof_DC);

            //---------Циклы дисциплин в ОПП "Системный анализ и управление" ---------
        EPPCycle sochumekonom_DC = iDBSpt.getStoredConstantsSource().get(EPPCycle.class, EPPCycle.SOCIALHUMANITARYECONOMIC_KEY);
        if (sochumekonom_DC == null) {
            sochumekonom_DC = new EPPCycle("Цикл гуманітарних та соціально-економічних дисциплін", "ЦГСЕД");
            iDBSpt.getStoredConstantsSource().put(EPPCycle.class, EPPCycle.SOCIALHUMANITARYECONOMIC_KEY, sochumekonom_DC);
        }
        EPPCycle natursci_DC = iDBSpt.getStoredConstantsSource().get(EPPCycle.class, EPPCycle.NATURESCIENCE_KEY);
        if (natursci_DC == null) {
            natursci_DC = new EPPCycle("Цикл природничо-наукової підготовки", "ЦПНП");
            iDBSpt.getStoredConstantsSource().put(EPPCycle.class, EPPCycle.NATURESCIENCE_KEY, natursci_DC);
        }
        EPPCycle profnd_DC = iDBSpt.getStoredConstantsSource().get(EPPCycle.class, EPPCycle.PROFESSIONALNORMATIVE_KEY);
        if (profnd_DC == null) {
            profnd_DC = new EPPCycle("Цикл професійної підготовки (нормативні дисципліни)", "ЦППНД");
            iDBSpt.getStoredConstantsSource().put(EPPCycle.class, EPPCycle.PROFESSIONALNORMATIVE_KEY, profnd_DC);
        }

        //---------Права----------
        LOG.debug("Add Permissions Maps...");
        Map<String, Permission> DPermissions = iDBSpt.getPermissionsMap(Discipline.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> EPPCPermissions = iDBSpt.getPermissionsMap(EPPCycle.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> QPermissions = iDBSpt.getPermissionsMap(Qualification.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);

        Map<String, Permission> EPPermissions = iDBSpt.getPermissionsMap(EduPlan.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> EPDPermissions = iDBSpt.getPermissionsMap(EduPlanDiscipline.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> EPDCPermissions = iDBSpt.getPermissionsMap(EduPlanDisciplineCycle.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> EPRPermissions = iDBSpt.getPermissionsMap(EduPlanRecord.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> EPSPermissions = iDBSpt.getPermissionsMap(EduPlanSemester.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);


        if (iDBSpt.isFillTestData()) {
            //---------Группы прав----------
            //Права просмотра записей учебного плана
            LOG.debug("Add Test PermissionGroups...");
            PermissionGroup pgReadEduPlanRecords = iDBSpt.saveOrUpdatePermissionGroup("Подсистема организации учебного процесса. Просмотр учебных планов",
                    EPPermissions.get(READ_OP), EPDPermissions.get(READ_OP), EPDCPermissions.get(READ_OP),
                    EPRPermissions.get(READ_OP), EPSPermissions.get(READ_OP),
                    EPPermissions.get(MENU_OP), EPDPermissions.get(MENU_OP), EPDCPermissions.get(MENU_OP),
                    EPRPermissions.get(MENU_OP), EPSPermissions.get(MENU_OP));

            //Права изменения, добавления, удаления с сохранением записей учебного плана
            PermissionGroup pgManageEduPlanRecords = iDBSpt.saveOrUpdatePermissionGroup("Подсистема организации учебного процесса. Управление учебными планами",
                    EPPermissions.get(UPDATE_OP), EPPermissions.get(STORE_OP), EPPermissions.get(REMOVE_OP),
                    EPDPermissions.get(UPDATE_OP), EPDPermissions.get(STORE_OP), EPDPermissions.get(REMOVE_OP),
                    EPDCPermissions.get(UPDATE_OP), EPDCPermissions.get(STORE_OP), EPDCPermissions.get(REMOVE_OP),
                    EPRPermissions.get(UPDATE_OP), EPRPermissions.get(STORE_OP), EPRPermissions.get(REMOVE_OP),
                    EPSPermissions.get(UPDATE_OP), EPSPermissions.get(STORE_OP), EPSPermissions.get(REMOVE_OP));

            //Права чтения записей справочников подсистемы организации учебного процесса
            PermissionGroup pgReadEduPlanReference = iDBSpt.saveOrUpdatePermissionGroup("Подсистема организации учебного процесса. Просмотр записей справочников",
                    DPermissions.get(READ_OP), DPermissions.get(MENU_OP),
                    EPPCPermissions.get(READ_OP), EPPCPermissions.get(MENU_OP),
                    QPermissions.get(READ_OP), QPermissions.get(MENU_OP));

            //Права изменения, добавления, удаления с сохранением записей справочников подсистемы организации учебного процесса
            PermissionGroup pgManageEduPlanReference = iDBSpt.saveOrUpdatePermissionGroup("Подсистема организации учебного процесса. Изменение/удаление записей справочников",
                    DPermissions.get(UPDATE_OP), DPermissions.get(STORE_OP), DPermissions.get(REMOVE_OP),
                    EPPCPermissions.get(UPDATE_OP), EPPCPermissions.get(STORE_OP), EPPCPermissions.get(REMOVE_OP),
                    QPermissions.get(UPDATE_OP), QPermissions.get(STORE_OP), QPermissions.get(REMOVE_OP));

            //Права просмотра экземпляров записи лицензии
            PermissionGroup pgReadLicenseRecords = iDBSpt.getPermissionGroupController().findByName("Подсистема лицензий. Просмотр лицензий");

            //Права просмотра справочников для подсистемы лицензий
            PermissionGroup pgReadLicenseRecordsReference = iDBSpt.getPermissionGroupController().findByName("Подсистема лицензий. Просмотр справочников");

            //Права просмотра записей о подразделениях
            PermissionGroup pgReadOrgUnits = iDBSpt.getPermissionGroupController().findByName("Подсистема административно-организационной структуры. Просмотр записей о подразделениях");

            //---------Роли----------
            LOG.debug("Add Test Roles...");
            Role roleEPG = iDBSpt.saveOrUpdateRole("EduPlanGuest",
                    "Просмотр записей учебных планов", subSystem, pgReadEduPlanRecords);

            Role roleEPO = iDBSpt.saveOrUpdateRole("EduPlanOperator",
                    "Оператор управления записями учебных планов", subSystem,
                    pgReadEduPlanRecords, pgManageEduPlanRecords,
                    pgReadEduPlanReference, pgReadLicenseRecords, pgReadLicenseRecordsReference, pgReadOrgUnits);

            Role roleEPM = iDBSpt.saveOrUpdateRole("EduPlanManager",
                    "Менеджер управления записями учебных планов", subSystem, 
                    pgReadEduPlanRecords, pgManageEduPlanRecords,
                    pgReadEduPlanReference, pgManageEduPlanReference, pgReadLicenseRecords,
                    pgReadLicenseRecordsReference, pgReadOrgUnits);

            //---------Пользователи----------
            LOG.debug("Add Test Users Roles...");
            UserController uCnt = iDBSpt.getUserController();
            User user = uCnt.findByLogin("sl");
            user.add(roleEPM);
            uCnt.saveOrUpdate(user);

            user = uCnt.findByLogin("TII");
            user.add(roleEPO);
            uCnt.saveOrUpdate(user);

//            user = uCnt.findByLogin("guest");
//            user.add(roleEPG);
//            uCnt.saveOrUpdate(user);

            //---------Квалификации----------
            LOG.debug("Add Test Qualifications...");
            Qualification fRTPZ = saveOrUpdateQ("фахівець з розробки та тестування програмного забезпечення", "", "");
            Qualification SoftwareEng = saveOrUpdateQ("інженер-програміст", "", "");
            Qualification SoftwareMaster = saveOrUpdateQ("магістр з програмного забезпечення автоматизованих систем,  інженер-програміст", "", "");

            Qualification tFGPNT = saveOrUpdateQ("технічний фахівець в галузі прикладних наук та техніки", "", "");
            Qualification SystemAnalysMaster = saveOrUpdateQ("системний аналітик-дослідник", "", "");

            //---------Учебные планы----------
            Calendar ePCal20090901 = Calendar.getInstance();
            ePCal20090901.set(Calendar.YEAR, 2009);
            ePCal20090901.set(Calendar.MONTH, Calendar.SEPTEMBER);
            ePCal20090901.set(Calendar.DAY_OF_MONTH, 1);

            //--Создание контроллера, работающего с экземплярами записи лицензии
            //--Создание контроллера, работающего с экземплярами образовательно-квалификационного уровня
            NamedEntityController<EducationalQualificationLevel> lrEQLcnt = (NamedEntityController<EducationalQualificationLevel>) (Object) iDBSpt.getControllerSource().get(EducationalQualificationLevel.class);
            EducationalQualificationLevel bach = lrEQLcnt.findByNameFirst("Бакалавр");
            //--Создание контроллера, работающего с экземплярами направления обучения или специальности
            NamedEntityController<TrainingDirectionOrSpeciality> lrTDSPIcnt = (NamedEntityController<TrainingDirectionOrSpeciality>) (Object) iDBSpt.getControllerSource().get(TrainingDirectionOrSpeciality.class);
            TrainingDirectionOrSpeciality tdosPI_B = lrTDSPIcnt.findByNameFirst("Програмна інженерія");

            NamedEntityController<TrainingDirectionOrSpeciality> lrTDScntSA = (NamedEntityController<TrainingDirectionOrSpeciality>) (Object) iDBSpt.getControllerSource().get(TrainingDirectionOrSpeciality.class);
            TrainingDirectionOrSpeciality tdosSA_B = lrTDScntSA.findByNameFirst("Системний аналіз та управління");

            //--Создание контроллера, работающего с формами обучения
            NamedEntityController<EducationForm> lrEFcnt = (NamedEntityController<EducationForm>) (Object) iDBSpt.getControllerSource().get(EducationForm.class);
            EducationForm efStat = lrEFcnt.findByNameFirst("Денна");

            //--Создание контроллера, работающего с экземплярами записей лицензии
            Controller<LicenseRecord, Integer> lrCnt = iDBSpt.getControllerSource().get(LicenseRecord.class);
            //Для "Программная инженерия" бакалавр Ден.
            LicenseRecord lrSample = new LicenseRecord();
            lrSample.setEducationalQualificationLevel(bach);
            lrSample.setTrainingDirectionOrSpeciality(tdosPI_B);
            lrSample.setEducationForm(efStat);
             //--Заполнение экземпляра лицензионной записи (для бакалавров ПИ)
            List<LicenseRecord> pIBachList = (List<LicenseRecord>)lrCnt.findByExample(lrSample);
            LicenseRecord pIBach=pIBachList.get(0);

            //Для "Системний аналіз та управління" бакалавр Ден.
            LicenseRecord lrSampleSA = new LicenseRecord();
            lrSampleSA.setEducationalQualificationLevel(bach);
            lrSampleSA.setTrainingDirectionOrSpeciality(tdosSA_B);
            lrSampleSA.setEducationForm(efStat);
            //--Заполнение экземпляра лицензионной записи (для бакалавров СА)
            List<LicenseRecord> sABachList = (List<LicenseRecord>)lrCnt.findByExample(lrSampleSA);
            LicenseRecord sABach=sABachList.get(0);

            LOG.debug("Add Test EduPlans...");
            //--Заполнение экземпляра учебных планов (для бакалавров ПИ 2009г утверждения)
            EduPlan pIBach2009 = saveOrUpdateEP(pIBach, 4.0, fRTPZ, ePCal20090901.getTime());
            EduPlan sABach2009 = saveOrUpdateEP(sABach, 4.0, tFGPNT, ePCal20090901.getTime());

            LOG.debug("Add Test EduPlanDisciplineCycles...");
            //--Заполнение циклов дисциплин учебных планов (для бакалавров ПИ 2009г утверждения)
            EduPlanDisciplineCycle pIBach2009HumSocEconom = saveOrUpdateEPDC(humsocecon_DC, "1", pIBach2009, true, 864);
            EduPlanDisciplineCycle pIBach2009MathNatSci = saveOrUpdateEPDC(mathnatsci_DC, "2", pIBach2009, true, 846);
            EduPlanDisciplineCycle pIBach2009Prof = saveOrUpdateEPDC(prof_DC, "3", pIBach2009, true, 3744);

            //--Заполнение циклов дисциплин учебных планов (для бакалавров СА 2009г)
            EduPlanDisciplineCycle sABach2009SocHumEconom = saveOrUpdateEPDC(sochumekonom_DC, "1", sABach2009, true, 864);
            EduPlanDisciplineCycle sABach2009NaturSci = saveOrUpdateEPDC(natursci_DC, "2", sABach2009, true, 2187);
            EduPlanDisciplineCycle sABach2009ProfND = saveOrUpdateEPDC(profnd_DC, "3", sABach2009, true, 2070);

            //--Заполнение номеров семестров учебных планов с количество недель теоретических занятий
            LOG.debug("Add Test EduPlanSemesters...");
            EduPlanSemester firstSem16 = saveOrUpdateEPS("1","",16.0);
            EduPlanSemester secondSem16 = saveOrUpdateEPS("2","",16.0);
            EduPlanSemester thirdSem16 = saveOrUpdateEPS("3","",16.0);
            EduPlanSemester fourthSem16 = saveOrUpdateEPS("4","",16.0);
            EduPlanSemester fifthSem16 = saveOrUpdateEPS("5","",16.0);
            EduPlanSemester sixthSem16 = saveOrUpdateEPS("6","",16.0);
            EduPlanSemester seventhSem16 = saveOrUpdateEPS("7","",16.0);
            EduPlanSemester eighthSem14 = saveOrUpdateEPS("8","",14.0);
            EduPlanSemester eighthSem12 = saveOrUpdateEPS("8","",12.0);

            //--Заполнение названий дисциплин
            LOG.debug("Add Test Disciplines...");
            Discipline foreignLang = saveOrUpdateDiscName("Іноземна мова", "Ін.мова");
            Discipline pravoznavstvo = saveOrUpdateDiscName("Правознавство", "Правозн.");
            Discipline mathAnalysis = saveOrUpdateDiscName("Математичний аналіз", "Мат.аналіз");
            Discipline physics = saveOrUpdateDiscName("Фізика", "Фізика");
            Discipline compDiscrMath = saveOrUpdateDiscName("Комп'ютерна дискретна математика", "КДМ");
            Discipline discrStruc = saveOrUpdateDiscName("Дискретні структури", "ДС");

            //--Заполнение названий дисциплин CA
            Discipline ukrainianLang = saveOrUpdateDiscName("Українська мова (за професійним спрямуванням)", "Укр.мова");
            Discipline politologiya = saveOrUpdateDiscName("Політологія", "Політолог.");
            Discipline algGeom = saveOrUpdateDiscName("Алгебра та геометрія", "АГ");
            Discipline funcAnalys = saveOrUpdateDiscName("Функціональний аналіз", "Функц.аналіз");
            Discipline program = saveOrUpdateDiscName("Програмування", "Прогр.");
            Discipline model = saveOrUpdateDiscName("Моделювання економічних, екологічних та соціальних процесів", "МЕЕСП");

            //--Заполнение дисциплин учебных планов
            LOG.debug("Add Test EduPlanDisciplines...");
            EduPlanDiscipline foreignLangPI2009 = saveOrUpdateEPDisc(foreignLang, pIBach2009HumSocEconom, 180, true);
            EduPlanDiscipline pravoznavstvoPI2009 = saveOrUpdateEPDisc(pravoznavstvo, pIBach2009HumSocEconom, 54, true);
            EduPlanDiscipline mathAnalysisPI2009 = saveOrUpdateEPDisc(mathAnalysis, pIBach2009MathNatSci, 360, true);
            EduPlanDiscipline physicsPI2009 = saveOrUpdateEPDisc(physics, pIBach2009MathNatSci, 144, true);
            EduPlanDiscipline compDiscrMathPI2009 = saveOrUpdateEPDisc(compDiscrMath, pIBach2009Prof, 180, true);
            EduPlanDiscipline discrStrucPI2009 = saveOrUpdateEPDisc(discrStruc, pIBach2009Prof, 126, true);

            //--Заполнение дисциплин учебных планов СА
            EduPlanDiscipline ukrainianLangSA2009 = saveOrUpdateEPDisc(ukrainianLang, sABach2009SocHumEconom, 108, true);
            EduPlanDiscipline politologiyaSA2009 = saveOrUpdateEPDisc(politologiya, sABach2009SocHumEconom, 54, true);
            EduPlanDiscipline algGeomSA2009 = saveOrUpdateEPDisc(algGeom, sABach2009NaturSci, 396, true);
            EduPlanDiscipline funcAnalysSA2009 = saveOrUpdateEPDisc(funcAnalys, sABach2009NaturSci, 108, true);
            EduPlanDiscipline programSA2009 = saveOrUpdateEPDisc(program, sABach2009ProfND, 378, true);
            EduPlanDiscipline modelSA2009 = saveOrUpdateEPDisc(model, sABach2009ProfND, 144, true);

            //--Заполнение записей учебных планов
            LOG.debug("Add Test EduPlanRecords...");
            EduPlanRecord foreignLangPI2009_1 = saveOrUpdateEPRecord("01", foreignLangPI2009, firstSem16, false, true, false, false, 72, 0, 0, 24);
            EduPlanRecord foreignLangPI2009_2 = saveOrUpdateEPRecord("01", foreignLangPI2009, secondSem16, false, true, false, false, 72, 0, 0, 24);
            EduPlanRecord foreignLangPI2009_3 = saveOrUpdateEPRecord("01", foreignLangPI2009, thirdSem16, false, true, false, false, 63, 0, 0, 24);
            EduPlanRecord foreignLangPI2009_4 = saveOrUpdateEPRecord("01", foreignLangPI2009, fourthSem16, false, true, false, false, 63, 0, 0, 24);
            EduPlanRecord pravoznavstvoPI2009_7 = saveOrUpdateEPRecord("02", pravoznavstvoPI2009, seventhSem16, false, true, false, false, 81, 16, 0, 16);
            EduPlanRecord mathAnalysisPI2009_2 = saveOrUpdateEPRecord("01", mathAnalysisPI2009, secondSem16, false, true, false, false, 180, 32, 0, 32);
            EduPlanRecord mathAnalysisPI2009_3 = saveOrUpdateEPRecord("01", mathAnalysisPI2009, thirdSem16, true, false, false, false, 180, 32, 0, 32);
            EduPlanRecord physicsPI2009_1 = saveOrUpdateEPRecord("03", physicsPI2009, firstSem16, true, false, false, false, 144, 16, 40, 0);
            EduPlanRecord compDiscrMathPI2009_2 = saveOrUpdateEPRecord("01", compDiscrMathPI2009, secondSem16, true, false, false, false, 180, 32, 32, 0);
            EduPlanRecord discrStrucPI2009_2 = saveOrUpdateEPRecord("02", discrStrucPI2009, thirdSem16, true, false, false, false, 126, 16, 32, 0);

            //--Заполнение записей учебных планов
            EduPlanRecord ukrainianLangSA2009_3 = saveOrUpdateEPRecord("1", ukrainianLangSA2009, thirdSem16, false, true, false, false, 36, 0, 0, 16);
            EduPlanRecord ukrainianLangSA2009_4 = saveOrUpdateEPRecord("1", ukrainianLangSA2009, fourthSem16, false, true, false, false, 36, 0, 0, 16);
            EduPlanRecord ukrainianLangSA2009_5 = saveOrUpdateEPRecord("1", ukrainianLangSA2009, fifthSem16, true, false, false, false, 36, 0, 0, 16);
            EduPlanRecord politologiyaSA2009_3 = saveOrUpdateEPRecord("4", politologiyaSA2009, thirdSem16, false, true, false, false, 54, 16, 0, 8);
            EduPlanRecord algGeomSA2009_1 = saveOrUpdateEPRecord("2", algGeomSA2009, firstSem16, true, false, false, true, 234, 32, 0, 48);
            EduPlanRecord algGeomSA2009_2 = saveOrUpdateEPRecord("2", algGeomSA2009, secondSem16, true, false, false, true, 162, 24, 0, 40);
            EduPlanRecord funcAnalysSA2009_5 = saveOrUpdateEPRecord("6", funcAnalysSA2009, fifthSem16, false, true, false, true, 108, 24, 0, 16);
            EduPlanRecord programSA2009_1 = saveOrUpdateEPRecord("3", programSA2009, firstSem16, true, false, false, true, 189, 16, 40, 8);
            EduPlanRecord programSA2009_2 = saveOrUpdateEPRecord("3", programSA2009, secondSem16, true, false, false, true, 189, 16, 56, 8);
            EduPlanRecord modelSA2009_8 = saveOrUpdateEPRecord("6", modelSA2009, eighthSem12, true, false, false, true, 144, 12, 36, 0);
        }
    }
    }

     //---------Метод сохранения/обновления номеров семестров учебных планов с количеством недель теоретических занятий ----------
    private EduPlanSemester saveOrUpdateEPS(String name, String shortName, Double eduWeekAmount) {

        //--Создание контроллера, работающего с экземплярами областей знаний или направлений подготовки
        Controller<EduPlanSemester, Integer> ePSController = iDBSpt.getControllerSource().get(EduPlanSemester.class);

        //---Создание образца области знаний или направления подготовки
        EduPlanSemester qSample = new EduPlanSemester();
        qSample.setName(name);
        qSample.setShortName(shortName);
        qSample.setEduWeekAmount(eduWeekAmount);

        //--Выборка списка областей знаний или направлений подготовки
        List<EduPlanSemester> q = ePSController.findByExample(qSample);
        //--Переменная для работы с экземпляром элемента спискка
        EduPlanSemester p;
        //--Инициализация элемента списка
        if (q.size() == 0) {
            p = new EduPlanSemester();
            p.setName(name);
            p.setShortName(shortName);
            p.setEduWeekAmount(eduWeekAmount);
        } else {
            p = q.get(0);
        }
        return ePSController.saveOrUpdate(p);
    }

    //---------Метод сохранения/обновления квалификаций ----------
    private Qualification saveOrUpdateQ(String name, String shortName, String qualificationCode) {

        //--Создание контроллера, работающего с экземплярами областей знаний или направлений подготовки
        Controller<Qualification, Integer> qController = iDBSpt.getControllerSource().get(Qualification.class);

        //---Создание образца области знаний или направления подготовки
        Qualification qSample = new Qualification();
        qSample.setName(name);
        qSample.setShortName(shortName);
        qSample.setQualificationCode(qualificationCode);

        //--Выборка списка областей знаний или направлений подготовки
        List<Qualification> q = qController.findByExample(qSample);
        //--Переменная для работы с экземпляром элемента спискка
        Qualification p;
        //--Инициализация элемента списка
        if (q.size() == 0) {
            p = new Qualification();
            p.setName(name);
            p.setShortName(shortName);
            p.setQualificationCode(qualificationCode);
        } else {
            p = q.get(0);
        }
        return qController.saveOrUpdate(p);
    }

    //---------Метод сохранения/обновления учебных планов (шапки) ----------
    private EduPlan saveOrUpdateEP(LicenseRecord licenseRecord, Double trainingTerm, Qualification qualification, Date introducingDate) {
        //--Создание контроллера, работающего с экземплярами областей знаний или направлений подготовки
        Controller<EduPlan, Integer> ePController = iDBSpt.getControllerSource().get(EduPlan.class);

        //---Создание образца области знаний или направления подготовки
        EduPlan ePSample = new EduPlan();
        ePSample.setLicenseRecord(licenseRecord);
        ePSample.setTrainingTerm(trainingTerm);
        ePSample.setQualification(qualification);
        ePSample.setIntroducingDate(introducingDate);

        //--Выборка списка областей знаний или направлений подготовки
        List<EduPlan> q = ePController.findByExample(ePSample);
        //--Переменная для работы с экземпляром элемента спискка
        EduPlan p;
        //--Инициализация элемента списка
        if (q.size() == 0) {
            p = new EduPlan();
            p.setLicenseRecord(licenseRecord);
            p.setTrainingTerm(trainingTerm);
            p.setQualification(qualification);
            p.setIntroducingDate(introducingDate);

        } else {
            p = q.get(0);
        }
        return ePController.saveOrUpdate(p);
    }

    //---------Метод сохранения/обновления циклов дисциплин учебных планов ----------
    private EduPlanDisciplineCycle saveOrUpdateEPDC(EPPCycle ePPCycle, String eduPlanDisciplineCycleNumber, EduPlan eduPlan, Boolean isRegulatory, Integer cycleTotalHours) {
        //--Создание контроллера, работающего с экземплярами циклов дисциплин учебных планов
        Controller<EduPlanDisciplineCycle, Integer> ePDCController = iDBSpt.getControllerSource().get(EduPlanDisciplineCycle.class);

        //---Создание образца цикла дисциплин учебных планов
        EduPlanDisciplineCycle ePDCSample = new EduPlanDisciplineCycle();
        ePDCSample.setEPPCycle(ePPCycle);
        ePDCSample.setEduPlanDisciplineCycleNumber(eduPlanDisciplineCycleNumber);
        ePDCSample.setEduplan(eduPlan);
        ePDCSample.setIsRegulatory(isRegulatory);
        ePDCSample.setePPCycleTotalHours(cycleTotalHours);

        //--Выборка списка циклов дисциплин учебных планов
        List<EduPlanDisciplineCycle> q = ePDCController.findByExample(ePDCSample);
        //--Переменная для работы с экземпляром элемента спискка
        EduPlanDisciplineCycle p;
        //--Инициализация элемента списка
        if (q.size() == 0) {
            p = new EduPlanDisciplineCycle();
            p.setEPPCycle(ePPCycle);
            p.setEduPlanDisciplineCycleNumber(eduPlanDisciplineCycleNumber);
            p.setEduplan(eduPlan);
            p.setIsRegulatory(isRegulatory);
            p.setePPCycleTotalHours(cycleTotalHours);
        } else {
            p = q.get(0);
        }
        return ePDCController.saveOrUpdate(p);
    }

    //---------Метод сохранения/обновления названий дисциплин ----------
    private Discipline saveOrUpdateDiscName(String name, String shortName) {

        //--Создание контроллера, работающего с экземплярами названий дисциплин
        Controller<Discipline, Integer> discNameController = iDBSpt.getControllerSource().get(Discipline.class);

        //---Создание образца названий дисциплин
        Discipline discNameSample = new Discipline();
        discNameSample.setName(name);
        discNameSample.setShortName(shortName);

        //--Выборка списка названий дисциплин
        List<Discipline> discName = discNameController.findByExample(discNameSample);
        //--Переменная для работы с экземпляром элемента спискка
        Discipline p;
        //--Инициализация элемента списка
        if (discName.size() == 0) {
            p = new Discipline();
            p.setName(name);
            p.setShortName(shortName);

        } else {
            p = discName.get(0);
        }
        return discNameController.saveOrUpdate(p);
    }

    //---------Метод сохранения/обновления дисциплин учебных планов----------
    private EduPlanDiscipline saveOrUpdateEPDisc(Discipline discipline, EduPlanDisciplineCycle eduPlanDisciplineCycle,
            Integer disciplineTotalHoursVolume, Boolean isMandatory) {

        //--Создание контроллера, работающего с экземплярами дисциплин учебных планов
        Controller<EduPlanDiscipline, Integer> ePDiscController = iDBSpt.getControllerSource().get(EduPlanDiscipline.class);

        //---Создание образца названий дисциплин
        EduPlanDiscipline ePDiscSample = new EduPlanDiscipline();
        ePDiscSample.setDiscipline(discipline);
        ePDiscSample.setEduPlanDisciplineCycle(eduPlanDisciplineCycle);
        ePDiscSample.setDisciplineTotalHourVolume(disciplineTotalHoursVolume);
        ePDiscSample.setIsMandatory(isMandatory);

        //--Выборка списка дисциплин учебных планов
        List<EduPlanDiscipline> ePDisc = ePDiscController.findByExample(ePDiscSample);
        //--Переменная для работы с экземпляром элемента спискка
        EduPlanDiscipline p;
        //--Инициализация элемента списка
        if (ePDisc.size() == 0) {
            p = new EduPlanDiscipline();
            p.setDiscipline(discipline);
            p.setEduPlanDisciplineCycle(eduPlanDisciplineCycle);
            p.setDisciplineTotalHourVolume(disciplineTotalHoursVolume);
            p.setIsMandatory(isMandatory);

        } else {
            p = ePDisc.get(0);
        }
        return ePDiscController.saveOrUpdate(p);
    }

    //---------Метод сохранения/обновления записей учебных планов----------
    private EduPlanRecord saveOrUpdateEPRecord(String disciplineNumber, EduPlanDiscipline eduPlanDiscipline,
            EduPlanSemester eduPlanSemester, Boolean isExam, Boolean isCredit,
            Boolean isCourseWork, Boolean isControlWork, Integer disciplineTotalHoursPerRecord,
            Integer lecturesHours, Integer labsHours, Integer practicesHours) {

        //--Создание контроллера, работающего с экземплярами записей учебных планов
        Controller<EduPlanRecord, Integer> ePRecordController = iDBSpt.getControllerSource().get(EduPlanRecord.class);

        //---Создание образца записей учебных планов
        EduPlanRecord ePRecordSample = new EduPlanRecord();
        ePRecordSample.setDisciplineNumber(disciplineNumber);
        ePRecordSample.setEduPlanDiscipline(eduPlanDiscipline);
        ePRecordSample.setEduPlanSemester(eduPlanSemester);
        ePRecordSample.setIsExam(isExam);
        ePRecordSample.setIsCredit(isCredit);
        ePRecordSample.setIsCourseWork(isCourseWork);
        ePRecordSample.setIsControlWork(isControlWork);
        ePRecordSample.setDisciplineTotalHoursPerRecord(disciplineTotalHoursPerRecord);
//        ePRecordSample.setEctsCreditAmount();
        ePRecordSample.setLecturesHours(lecturesHours);
        ePRecordSample.setLabsHours(labsHours);
        ePRecordSample.setPracticesHours(practicesHours);
//        ePRecordSample.setTotalHoursWithTeacher();
//        ePRecordSample.setStudentIndependentWorkHours();
//        ePRecordSample.setHoursWithTeacherPerWeek();
        
        //--Выборка списка записей учебных планов
        List<EduPlanRecord> ePRecord = ePRecordController.findByExample(ePRecordSample);
        //--Переменная для работы с экземпляром элемента спискка
        EduPlanRecord p;
        //--Инициализация элемента списка
        if (ePRecord.size() == 0) {
            p = new EduPlanRecord();
            p.setDisciplineNumber(disciplineNumber);
            p.setEduPlanDiscipline(eduPlanDiscipline);
            p.setEduPlanSemester(eduPlanSemester);
            p.setIsExam(isExam);
            p.setIsCredit(isCredit);
            p.setIsCourseWork(isCourseWork);
            p.setIsControlWork(isControlWork);
            p.setDisciplineTotalHoursPerRecord(disciplineTotalHoursPerRecord);
//            p.setEctsCreditAmount();
            p.setLecturesHours(lecturesHours);
            p.setLabsHours(labsHours);
            p.setPracticesHours(practicesHours);
//            p.setTotalHoursWithTeacher();
//            p.setStudentIndependentWorkHours();
//            p.setHoursWithTeacherPerWeek();
        } else {
            p = ePRecord.get(0);
        }
        return ePRecordController.saveOrUpdate(p);
    }
}
