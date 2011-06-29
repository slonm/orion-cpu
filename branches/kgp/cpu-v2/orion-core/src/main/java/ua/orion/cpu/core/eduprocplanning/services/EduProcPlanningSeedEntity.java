package ua.orion.cpu.core.eduprocplanning.services;

import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDiscipline;
import ua.orion.cpu.core.eduprocplanning.entities.Discipline;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDisciplineCycle;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
import ua.orion.cpu.core.eduprocplanning.entities.Qualification;
import ua.orion.cpu.core.eduprocplanning.entities.EPPCycle;
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
import ua.orion.cpu.core.orgunits.entities.OrgUnit;

/**
 *
 * @author kgp
 */
@OrderLibrary("after:" + LicensingSymbols.LICENSING_LIB)
public class EduProcPlanningSeedEntity {

    public EduProcPlanningSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(EduProcPlanningSymbols.EDUPROC_PLANNING_LIB));
            //---------Заполнение справочников
            //---------Циклы дисциплин учебных планов----------
            EPPCycle humsocecon_DC = es.findUniqueOrPersist(new EPPCycle("Цикл гуманітарної та соціально-економічної підготовки", "ЦГСЭП"));
            EPPCycle mathnatsci_DC = es.findUniqueOrPersist(new EPPCycle("Цикл математичної, природничо-наукової підготовки", "ЦМПНП"));
            EPPCycle prof_DC = es.findUniqueOrPersist(new EPPCycle("Цикл професійної та практичної підготовки", "ЦППП"));
            EPPCycle sochumekonom_DC = es.findUniqueOrPersist(new EPPCycle("Цикл гуманітарних та соціально-економічних дисциплін", "ЦГСЕД"));
            EPPCycle natursci_DC = es.findUniqueOrPersist(new EPPCycle("ЦЦикл природничо-наукової підготовки", "ЦПНП"));
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
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "EduPlan:read,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "EduPlanDiscipline:read,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "EduPlanRecord:read,menu"));

            //LicenseAppender
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseAppender", SubjectType.ROLE, "Qualification:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "EduPlanDisciplineCycle:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "ePPCycle:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "Discipline:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "EduPlan:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "EduPlanDiscipline:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "EduPlanRecord:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "EduPlanSemester:read,insert,update,menu"));

            //---------Учебные планы----------
            //Извлечение лицензионной записи (на интерфейсе - выбор из списка)
            EducationalQualificationLevel bach = es.findByUKey(EducationalQualificationLevel.class, EducationalQualificationLevel.BACHELOR_UKEY);
            TrainingDirectionOrSpeciality tdosPI_B = es.findByName(TrainingDirectionOrSpeciality.class, "Програмна інженерія");
            TrainingDirectionOrSpeciality tdosSA_B = es.findByName(TrainingDirectionOrSpeciality.class, "Системний аналіз");

            //ПРИ ИСПОЛЬЗОВАНИИ ПОИСКА ПО ОБРАЗЦУ НЕОБХОДИМО В ОБРАЗЦЕ ЗОАПОЛНЯТЬ ПОЛЯ,
            //ПОМЕЧЕННЫЕ АННОТАЦИЕЙ @NotNull!!!
            
            LicenseRecord lrSamplePI = new LicenseRecord();
            lrSamplePI.setLicense(es.findUniqueOrPersist(new License("АВ", "529699", DateTimeUtils.createCalendar(5, 11, 2010))));
            lrSamplePI.setEducationalQualificationLevel(bach);
            lrSamplePI.setTrainingDirectionOrSpeciality(tdosPI_B);
            lrSamplePI.setLicenseRecordGroup(es.findByName(LicenseRecordGroup.class, "Підготовка бакалаврів"));
            lrSamplePI.setOrgUnit(es.findByName(OrgUnit.class, "Кафедра програмування та інформаційних технологій"));
            lrSamplePI.setTermination(DateTimeUtils.createCalendar(1, 7, 2015));
            LicenseRecord pIBach = es.findUniqueOrPersist(lrSamplePI);

            LicenseRecord lrSampleSA = new LicenseRecord();
            lrSampleSA.setLicense(es.findUniqueOrPersist(new License("АВ", "529699", DateTimeUtils.createCalendar(5, 11, 2010))));
            lrSampleSA.setEducationalQualificationLevel(bach);
            lrSampleSA.setTrainingDirectionOrSpeciality(tdosSA_B);
            lrSampleSA.setLicenseRecordGroup(es.findByName(LicenseRecordGroup.class, "Підготовка бакалаврів"));
            lrSampleSA.setOrgUnit(es.findByName(OrgUnit.class, "Кафедра системного аналізу та вищої математики"));
            lrSampleSA.setTermination(DateTimeUtils.createCalendar(1, 7, 2015));
            LicenseRecord sABach = es.findUniqueOrPersist(lrSampleSA);

            //--Заполнение экземпляра учебных планов (для бакалавров ПИ 2009г утверждения)
            EduPlan pIBach2009 = es.findUniqueOrPersist(new EduPlan(pIBach, 4.0, fRTPZ, DateTimeUtils.createCalendar(1, 9, 2009)));
            EduPlan sABach2009 = es.findUniqueOrPersist(new EduPlan(sABach, 4.0, tFGPNT, DateTimeUtils.createCalendar(1, 9, 2009)));

            //--Заполнение циклов дисциплин учебных планов (для бакалавров ПИ 2009г утверждения) - привязка их к учебным планам
            EduPlanDisciplineCycle pIBach2009HumSocEconom = es.findUniqueOrPersist(new EduPlanDisciplineCycle(pIBach2009, humsocecon_DC, "1", true, 24.0));
            EduPlanDisciplineCycle pIBach2009MathNatSci = es.findUniqueOrPersist(new EduPlanDisciplineCycle(pIBach2009, mathnatsci_DC, "2", true, 23.5));
            EduPlanDisciplineCycle pIBach2009Prof = es.findUniqueOrPersist(new EduPlanDisciplineCycle(pIBach2009, prof_DC, "3", true, 104.0));

            //--Заполнение циклов дисциплин учебных планов (для бакалавров СА 2009г)
            EduPlanDisciplineCycle pMBach2009SocHumEconom = es.findUniqueOrPersist(new EduPlanDisciplineCycle(sABach2009, sochumekonom_DC, "1", true, 24.0));
            EduPlanDisciplineCycle pMBach2009NaturSci = es.findUniqueOrPersist(new EduPlanDisciplineCycle(sABach2009, natursci_DC, "2", true, 60.75));
            EduPlanDisciplineCycle pMBach2009ProfND = es.findUniqueOrPersist(new EduPlanDisciplineCycle(sABach2009, profnd_DC, "3", true, 57.5));

            //--Заполнение дисциплин учебных планов (для бакалавров ПИ 2009г утверждения) - привязка их к циклам
            EduPlanDiscipline foreignLangPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009HumSocEconom, true, "01", foreignLang, 11.25, "", "1-4", "", "", 0, 0, 96));
            EduPlanDiscipline pravoznavstvoPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009HumSocEconom, true, "02", pravoznavstvo, 2.25, "", "7", "", "", 16, 0, 16));
            EduPlanDiscipline mathAnalysisPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009MathNatSci, true, "01", mathAnalysis, 10.0, "3", "2", "", "2,3", 32, 0, 32));
            EduPlanDiscipline physicsPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009MathNatSci, true, "03", physics, 4.0, "1", "", "", "1", 16, 40, 0));
            EduPlanDiscipline compDiscrMathPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009Prof, true, "01", compDiscrMath, 5.0, "2", "", "", "2", 32, 32, 0));
            EduPlanDiscipline discrStrucPI2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pIBach2009Prof, true, "02", discrStruc, 3.5, "3", "", "", "3", 16, 32, 0));

            EduPlanDiscipline ukrainianLangSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pMBach2009SocHumEconom, true, "1", ukrainianLang, 3.0, "5", "3,4", "", "", 16, 0, 56));
            EduPlanDiscipline politologiyaSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pMBach2009SocHumEconom, true, "4", politologiya, 1.5, "", "3", "", "", 16, 0, 8));
            EduPlanDiscipline algGeomSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pMBach2009NaturSci, true, "2", algGeom, 11.0, "1,2", "", "", "", 56, 0, 88));
            EduPlanDiscipline funcAnalysSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pMBach2009NaturSci, true, "6", funcAnalys, 3.0, "", "5", "", "", 24, 0, 16));
            EduPlanDiscipline programSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pMBach2009ProfND, true, "3", program, 10.5, "1,2", "", "", "", 32, 96, 16));
            EduPlanDiscipline modelSA2009 = es.findUniqueOrPersist(new EduPlanDiscipline(pMBach2009ProfND, true, "6", model, 4.0, "8", "", "", "8", 12, 36, 0));
        }
    }
}

