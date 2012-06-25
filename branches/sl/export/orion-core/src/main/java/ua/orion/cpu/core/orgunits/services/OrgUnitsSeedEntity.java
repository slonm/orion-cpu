package ua.orion.cpu.core.orgunits.services;

import ua.orion.cpu.core.orgunits.OrgUnitsSymbols;
import ua.orion.cpu.core.orgunits.entities.Chair;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.orgunits.entities.Department;
import ua.orion.cpu.core.orgunits.entities.Institute;
import ua.orion.cpu.core.orgunits.entities.University;
import ua.orion.cpu.core.security.entities.Acl;
import ua.orion.cpu.core.security.entities.SubjectType;

/**
 *
 * @author sl
 */
//@AfterLibrary(OrgUnitsSymbols.ORG_UNITS)
public class OrgUnitsSeedEntity {
    public OrgUnitsSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(OrgUnitsSymbols.ORG_UNITS_LIB));
        if (testData) {
            //---Списки доступа----------
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "Chair:read,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseAppender", SubjectType.ROLE, "Chair:read,insert,update,menu"));

            //Структура КПУ відповідно до наказів ректора #67 від 16.05.2011 та #74 від 16.05.2011
            //---Университет
            University ou_CPU = es.findUniqueOrPersist(new University("Класичний приватний університет", "КПУ"));
            //---Институты
            Institute iU = es.findUniqueOrPersist(new Institute("Інститут управління", "ІУ", ou_CPU));
            Institute iE = es.findUniqueOrPersist(new Institute("Інститут економіки", "ІЕ", ou_CPU));
            Institute iP = es.findUniqueOrPersist(new Institute("Інститут права ім. В.Сташиса", "ІП", ou_CPU));
            Institute iZhMK = es.findUniqueOrPersist(new Institute("Інститут журналістики й масової комунікації", "ІЖМК", ou_CPU));
            Institute iZST = es.findUniqueOrPersist(new Institute("Інститут здоров'я, спорту та туризму", "ІЗСТ", ou_CPU));
            Institute iIPh = es.findUniqueOrPersist(new Institute("Інститут іноземної філології", "ІІФ", ou_CPU));
            Institute iIST = es.findUniqueOrPersist(new Institute("Інститут інформаційних та соціальних технологій", "ІІСТ", ou_CPU));
            Institute bIDMU = es.findUniqueOrPersist(new Institute("Бердянський інститут державного та муніципального управління", "БІДМУ", ou_CPU));
            //---Отделы
            Department dptPorfRetrainingCenter = es.findUniqueOrPersist(new Department("Центр професійної перепідготовки та підвищення кваліфікації", "ЦПППК", ou_CPU));
            Department dptColledgeCPU = es.findUniqueOrPersist(new Department("Колледж КПУ", "кол.КПУ", ou_CPU));
            Department dptPreVocationalTraining = es.findUniqueOrPersist(new Department("Відділення допрофесійної підготовки", "ДПП", ou_CPU));
            Department dptIITO = es.findUniqueOrPersist(new Department("Інститут інтерактивних технологій в освіті", "ІІТО", ou_CPU));
            Department dptPersManag = es.findUniqueOrPersist(new Department("Відділ управління персоналом", "ВУП", ou_CPU));

            //---Кафедры
            //---Інститут управління----------
            Chair kafMO = es.findUniqueOrPersist(new Chair("Кафедра менеджменту організацій", "КМО", iU));
            Chair kafMZD = es.findUniqueOrPersist(new Chair("Кафедра менеджменту зовнішньоекономічної діяльності", "КМЗЕД", iU));
            Chair kafGD = es.findUniqueOrPersist(new Chair("Кафедра гуманітарних дисциплін", "КГД", iU));
            Chair kafRT = es.findUniqueOrPersist(new Chair("Кафедра релігієзнавства та теології", "КРТ", iU));
            Chair kafIAM = es.findUniqueOrPersist(new Chair("Кафедра інвестиційного та аграрного менеджменту", "КІАМ", iU));
            Chair kafAMBA = es.findUniqueOrPersist(new Chair("Кафедра адміністративного менеджменту та бізнес-адміністрування", "КАМБА", iU));
            //---Інститут економіки----------
            Chair kafEP = es.findUniqueOrPersist(new Chair("Кафедра економіки підприємства", "КЕП", iE));
            Chair kafEKS = es.findUniqueOrPersist(new Chair("Кафедра економічної кібернетики та статистики", "КЕКС", iE));
            Chair kafETNPE = es.findUniqueOrPersist(new Chair("Кафедра економічної теорії, національної та прикладної економіки", "КЕТНПЕ", iE));
            Chair kafM = es.findUniqueOrPersist(new Chair("Кафедра маркетингу", "КМК", iE));
            Chair kafME = es.findUniqueOrPersist(new Chair("Кафедра міжнародної економіки", "КМЕ", iE));
            Chair kafOA = es.findUniqueOrPersist(new Chair("Кафедра обліку і аудиту", "КОА", iE));
            Chair kafFK = es.findUniqueOrPersist(new Chair("Кафедра фінансів та кредиту", "КФК", iE));
            //---Інститут права ім. В.Сташиса----------
            Chair kafKAP = es.findUniqueOrPersist(new Chair("Кафедра конституційного та адміністративного права", "ККАП", iP));
            Chair kafKP = es.findUniqueOrPersist(new Chair("Кафедра кримінального права", "ККП", iP));
            Chair kafKPK = es.findUniqueOrPersist(new Chair("Кафедра кримінального процесу та криміналістики", "ККПК", iP));
            Chair kafMP = es.findUniqueOrPersist(new Chair("Кафедра міжнародного права", "КМП", iP));
            Chair kafTZGP = es.findUniqueOrPersist(new Chair("Кафедра трудового, земельного та господарського права", "КТЗГП", iP));
            Chair kafGPP = es.findUniqueOrPersist(new Chair("Кафедра цивільного права і процесу", "КЦПП", iP));
            Chair kafTIDP = es.findUniqueOrPersist(new Chair("Кафедра теорії та історії держави та права", "КТІДП", iP));
            Chair kafVPCO = es.findUniqueOrPersist(new Chair("Кафедра військової підготовки та цивільної оборони", "КВПЦО", iP));
            //---Інститут журналістики й масової комунікації----------
            Chair kafZSK = es.findUniqueOrPersist(new Chair("Кафедра журналістики і соціальних комунікацій", "КЖСК", iZhMK));
            Chair kafVSRUF = es.findUniqueOrPersist(new Chair("Кафедра видавничої справи, редагування та української філології", "КВСРУФ", iZhMK));
            Chair kafRZG = es.findUniqueOrPersist(new Chair("Кафедра реклами і з'язків із громадкістю", "КРЗГ", iZhMK));
            //---Інститут здоров'я, спорту та туризму----------
            Chair kafTGG = es.findUniqueOrPersist(new Chair("Кафедра туризму та готельного господарства", "КТГГ", iZST));
            Chair kafTOFAV = es.findUniqueOrPersist(new Chair("Кафедра теоретичних основ фізичного та адаптивного виховання", "КТОФАВ", iZST));
            Chair kafFR = es.findUniqueOrPersist(new Chair("Кафедра фізичної реабілітації", "КФР", iZST));
            Chair kafV = es.findUniqueOrPersist(new Chair("Кафедра фізичного виховання", "КФВ", iZST));
            //---Інститут іноземної філології----------
            Chair kafAFZL = es.findUniqueOrPersist(new Chair("Кафедра англійської філології та зарубіжної літератури", "КАФЗЛ", iIPh));
//            Chair kafPF       = es.findUniqueOrPersist(new Chair("Кафедра перекладу за фахом","КПФ",iIPh));
            Chair kafRGF = es.findUniqueOrPersist(new Chair("Кафедра романо–германської філології", "КРГФ", iIPh));
            Chair kafTPP = es.findUniqueOrPersist(new Chair("Кафедра теорії та практики  перекладу", "КТПП", iIPh));
            Chair kafIM = es.findUniqueOrPersist(new Chair("Кафедра іноземних мов", "КІМ", iIPh));
            //---Інститут інформаційних та соціальних технологій ----------
            Chair kafD = es.findUniqueOrPersist(new Chair("Кафедра дизайну", "КД", iIST));
            Chair kafPP = es.findUniqueOrPersist(new Chair("Кафедра практичної психології", "КПП", iIST));
            Chair kafPIT = es.findUniqueOrPersist(new Chair("Кафедра програмування та інформаційних технологій", "КПІТ", iIST));
            Chair kafSAVM = es.findUniqueOrPersist(new Chair("Кафедра системного аналізу та вищої математики", "КСАВМ", iIST));
            Chair kafSSR = es.findUniqueOrPersist(new Chair("Кафедра соціології та соціальної роботи", "КССР", iIST));
            Chair kafDUZK = es.findUniqueOrPersist(new Chair("Кафедра державного управління та земельного кадастру", "КДУЗК", iIST));
            Chair kafEICPHS = es.findUniqueOrPersist(new Chair("Кафедра управління навчальними закладами та педагогіки вищої школи", "КПВШ", iIST));
        }
    }
}
