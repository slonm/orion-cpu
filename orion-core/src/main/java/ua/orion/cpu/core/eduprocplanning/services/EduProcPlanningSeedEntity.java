package ua.orion.cpu.core.eduprocplanning.services;

import java.util.*;
import ua.orion.cpu.core.eduprocplanning.entities.*;
import ua.orion.cpu.core.eduprocplanning.EduProcPlanningSymbols;
import ua.orion.cpu.core.security.entities.Acl;
import ua.orion.cpu.core.security.entities.SubjectType;
import ua.orion.core.annotations.OrderLibrary;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.core.services.EntityService;
import ua.orion.core.utils.DateTimeUtils;
import ua.orion.cpu.core.licensing.LicensingSymbols;

/**
 *
 * @author kgp
 */
@OrderLibrary("after:" + LicensingSymbols.LICENSING_LIB)
public class EduProcPlanningSeedEntity {

//    @Inject
//    EduProcPlanningService epps;
    public EduProcPlanningSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es, EduProcPlanningService epps) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(EduProcPlanningSymbols.EDUPROC_PLANNING_LIB));
        //---------Заполнение справочников
        //---------Циклы дисциплин учебных планов----------
        EPPCycle humsocecon_DC = es.findUniqueOrPersist(new EPPCycle("Цикл гуманітарної та соціально-економічної підготовки", "ЦГСЭП"));
        EPPCycle mathnatsci_DC = es.findUniqueOrPersist(new EPPCycle("Цикл математичної, природничо-наукової підготовки", "ЦМПНП"));
        EPPCycle prof_DC = es.findUniqueOrPersist(new EPPCycle("Цикл професійної та практичної підготовки", "ЦППП"));
        EPPCycle sochumekonom_DC = es.findUniqueOrPersist(new EPPCycle("Цикл гуманітарних та соціально-економічних дисциплін", "ЦГСЕД"));
        EPPCycle natursci_DC = es.findUniqueOrPersist(new EPPCycle("Цикл природничо-наукової підготовки", "ЦПНП"));
        EPPCycle profnd_DC = es.findUniqueOrPersist(new EPPCycle("Цикл професійної підготовки (нормативні дисципліни)", "ЦППНД"));
        //---------Квалификации----------
        Qualification fRTPZ = es.findUniqueOrPersist(new Qualification("фахівець з розробки та тестування програмного забезпечення", ""));
        Qualification SoftwareEng = es.findUniqueOrPersist(new Qualification("інженер-програміст", ""));
        Qualification SoftwareMaster = es.findUniqueOrPersist(new Qualification("магістр з програмного забезпечення автоматизованих систем,  інженер-програміст", ""));
        Qualification tFGPNT = es.findUniqueOrPersist(new Qualification("технічний фахівець в галузі прикладних наук та техніки", ""));
        Qualification SystemAnalysMaster = es.findUniqueOrPersist(new Qualification("системний аналітик-дослідник", ""));
        //--Заполнение названий дисциплин ПІ
        Discipline foreignLang = es.findUniqueOrPersist(new Discipline("Іноземна мова", "Ін.мова"));
        Discipline pravoznavstvo = es.findUniqueOrPersist(new Discipline("Правознавство", "Правозн."));
        Discipline mathAnalysis = es.findUniqueOrPersist(new Discipline("Математичний аналіз", "Мат.аналіз"));
        Discipline physics = es.findUniqueOrPersist(new Discipline("Фізика", "Фізика"));
        Discipline compDiscrMath = es.findUniqueOrPersist(new Discipline("Комп'ютерна дискретна математика", "КДМ"));
        Discipline discrStruc = es.findUniqueOrPersist(new Discipline("Дискретні структури", "ДС"));
        //--Заполнение названий дисциплин CA
        Discipline ukrainianLang = es.findUniqueOrPersist(new Discipline("Українська мова (за професійним спрямуванням)", "Укр.мова"));
        Discipline politologiya = es.findUniqueOrPersist(new Discipline("Політологія", "Політолог."));
        Discipline algGeom = es.findUniqueOrPersist(new Discipline("Алгебра та геометрія", "АГ"));
        Discipline funcAnalys = es.findUniqueOrPersist(new Discipline("Функціональний аналіз", "Функц.аналіз"));
        Discipline program = es.findUniqueOrPersist(new Discipline("Програмування", "Прогр."));
        Discipline model = es.findUniqueOrPersist(new Discipline("Моделювання економічних, екологічних та соціальних процесів", "МЕЕСП"));
        if (testData) {
            //---Списки доступа----------
            //---------Роли----------
            //LicenseReader
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "EduPlan:read,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "EduPlanDiscipline:read,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "EduPlanRecord:read,menu"));

            //LicenseAppender
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseAppender", SubjectType.ROLE, "Qualification:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "EduPlanDisciplineCycle:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "ePPCycle:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "Discipline:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "EduPlan:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "EduPlanDiscipline:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "EduPlanRecord:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "EduPlanSemester:read,insert,update,menu"));

            //---------Учебные планы----------
            //Извлечение лицензионной записи (на интерфейсе - выбор из списка)

            LicenseRecord pIBach = epps.findLicenseRecordByTrainingDirection("АВ", "529699", DateTimeUtils.createCalendar(5, 11, 2010),
                    EducationalQualificationLevel.BACHELOR_UKEY, "Програмна інженерія",
                    DateTimeUtils.createCalendar(1, 7, 2015));
            LicenseRecord sABach = epps.findLicenseRecordByTrainingDirection("АВ", "529699", DateTimeUtils.createCalendar(5, 11, 2010),
                    EducationalQualificationLevel.BACHELOR_UKEY, "Системний аналіз",
                    DateTimeUtils.createCalendar(1, 7, 2015));

            //--Создание экземпляра учебного планв (для бакалавров ПИ 2009г утверждения)
            //привязка к нему набора циклов и сохранение
            EduPlan pIBach2009 = es.findUniqueOrPersist(new EduPlan(pIBach, 4.0, fRTPZ, DateTimeUtils.createCalendar(1, 9, 2009), DateTimeUtils.createCalendar(2, 6, 2008), "Проректор з навчальної роботи О.І.Гура, Начальник навчального відділу В.М.Кравченко", true));

            //--Создание вариантов дисциплин
            EduPlanDisciplineVariant epdvMandatory = es.findUniqueOrPersist(new EduPlanDisciplineVariant("Обов`язкова", "Об."));
            EduPlanDisciplineVariant epdvSelective = es.findUniqueOrPersist(new EduPlanDisciplineVariant("Вибіркова", "Виб."));
            EduPlanDisciplineVariant epdvFacultative = es.findUniqueOrPersist(new EduPlanDisciplineVariant("Факультативна", "Ф."));
            //--Создание дисциплин учебных планов (для бакалавров ПИ 2009г утверждения) и их сохранение
            EduPlanDiscipline foreignLangPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "01", new HashSet(Arrays.asList(foreignLang)), 11.25, "", "1-4", "", "", 0, 3, 0, 0, 0, 0, 96));
            EduPlanDiscipline pravoznavstvoPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "02", new HashSet(Arrays.asList(pravoznavstvo)), 2.25, "", "7", "", "", 0, 1, 0, 0, 16, 0, 16));
            EduPlanDiscipline mathAnalysisPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "01", new HashSet(Arrays.asList(mathAnalysis)), 10.0, "3", "2", "", "2,3", 1, 1, 0, 2, 32, 0, 32));
            EduPlanDiscipline physicsPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "03", new HashSet(Arrays.asList(physics)), 4.0, "1", "", "", "1", 1, 0, 0, 1, 16, 40, 0));
            EduPlanDiscipline compDiscrMathPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "01", new HashSet(Arrays.asList(compDiscrMath)), 5.0, "2", "", "", "2", 1, 0, 0, 1, 32, 32, 0));
            EduPlanDiscipline discrStrucPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "02", new HashSet(Arrays.asList(discrStruc)), 3.5, "3", "", "", "3", 1, 0, 0, 1, 16, 32, 0));
            //--Создание дисциплин учебных планов (для бакалавров СА 2009г утверждения)
            EduPlanDiscipline ukrainianLangSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "1", new HashSet(Arrays.asList(ukrainianLang)), 3.0, "5", "3,4", "", "", 1, 2, 0, 0, 16, 0, 56));
            EduPlanDiscipline politologiyaSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "4", new HashSet(Arrays.asList(politologiya)), 1.5, "", "3", "", "", 0, 1, 0, 0, 16, 0, 8));
            EduPlanDiscipline algGeomSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "2", new HashSet(Arrays.asList(algGeom)), 11.0, "1,2", "", "", "", 2, 0, 0, 0, 56, 0, 88));
            EduPlanDiscipline funcAnalysSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "6", new HashSet(Arrays.asList(funcAnalys)), 3.0, "", "5", "", "", 0, 1, 0, 0, 24, 0, 16));
            EduPlanDiscipline programSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "3", new HashSet(Arrays.asList(program)), 10.5, "1,2", "", "", "", 2, 0, 0, 0, 32, 96, 16));
            EduPlanDiscipline modelSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009, epdvMandatory, "6", new HashSet(Arrays.asList(model)), 4.0, "8", "", "", "8", 1, 0, 0, 1, 12, 36, 0));

            //--Создание циклов дисциплин учебных планов (для бакалавров ПИ 2009г утверждения), 
            //привязка к ним наборов дисциплин и сохранение
            EduPlanDisciplineCycle pIBach2009HumSocEconom = es.findUniqueOrPersist(new EduPlanDisciplineCycle(pIBach2009, humsocecon_DC, "1", true, 24.0));
            //Привязка цикла к дисциплинам 
            foreignLangPI2009.getEduPlanDisciplineTags().add(pIBach2009HumSocEconom);
            es.persist(foreignLangPI2009);
            pravoznavstvoPI2009.getEduPlanDisciplineTags().add(pIBach2009HumSocEconom);
            es.persist(pravoznavstvoPI2009);

            EduPlanDisciplineCycle pIBach2009MathNatSci = es.findUniqueOrPersist(new EduPlanDisciplineCycle(pIBach2009, mathnatsci_DC, "2", true, 23.5));
            //Привязка цикла к дисциплинам 
            mathAnalysisPI2009.getEduPlanDisciplineTags().add(pIBach2009MathNatSci);
            es.persist(mathAnalysisPI2009);
            physicsPI2009.getEduPlanDisciplineTags().add(pIBach2009MathNatSci);
            es.persist(physicsPI2009);

            EduPlanDisciplineCycle pIBach2009Prof = es.findUniqueOrPersist(new EduPlanDisciplineCycle(pIBach2009, prof_DC, "3", true, 104.0));
            //Привязка цикла к дисциплинам 
            compDiscrMathPI2009.getEduPlanDisciplineTags().add(pIBach2009Prof);
            es.persist(compDiscrMathPI2009);
            discrStrucPI2009.getEduPlanDisciplineTags().add(pIBach2009Prof);
            es.persist(discrStrucPI2009);

            //--Создание циклов дисциплин учебных планов (для бакалавров СА 2009г)
            //привязка к ним наборов дисциплин и сохранение
            EduPlanDisciplineCycle pMBach2009SocHumEconom = es.findUniqueOrPersist(new EduPlanDisciplineCycle(pIBach2009, sochumekonom_DC, "1", true, 24.0));
            //Привязка цикла к дисциплинам 
            ukrainianLangSA2009.getEduPlanDisciplineTags().add(pMBach2009SocHumEconom);
            es.persist(ukrainianLangSA2009);
            politologiyaSA2009.getEduPlanDisciplineTags().add(pMBach2009SocHumEconom);
            es.persist(politologiyaSA2009);

            EduPlanDisciplineCycle pMBach2009NaturSci = es.findUniqueOrPersist(new EduPlanDisciplineCycle(pIBach2009, natursci_DC, "2", true, 60.75));
            //Привязка цикла к дисциплинам 
            algGeomSA2009.getEduPlanDisciplineTags().add(pMBach2009NaturSci);
            es.persist(algGeomSA2009);
            funcAnalysSA2009.getEduPlanDisciplineTags().add(pMBach2009NaturSci);
            es.persist(funcAnalysSA2009);

            EduPlanDisciplineCycle pMBach2009ProfND = es.findUniqueOrPersist(new EduPlanDisciplineCycle(pIBach2009, profnd_DC, "3", true, 57.5));
            //Привязка цикла к дисциплинам 
            programSA2009.getEduPlanDisciplineTags().add(pMBach2009ProfND);
            es.persist(programSA2009);
            modelSA2009.getEduPlanDisciplineTags().add(pMBach2009ProfND);
            es.persist(modelSA2009);
        }
    }
}
