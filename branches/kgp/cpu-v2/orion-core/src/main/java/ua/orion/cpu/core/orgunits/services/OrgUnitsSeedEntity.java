package ua.orion.cpu.core.orgunits.services;

import ua.orion.cpu.core.orgunits.OrgUnitsSymbols;
import ua.orion.cpu.core.orgunits.entities.Chair;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.core.services.EntityService;
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
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "Chair:read,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseAppender", SubjectType.ROLE, "Chair:read,insert,update,menu"));
        //---Кафедры, выполняющие обучение по лицензиям----------
//            Chair kafPIT = es.findUniqueOrPersist(new Chair("кафедра програмування та інформаційних технологій", "КПІТ"));
//            Chair kafEICPHS = es.findUniqueOrPersist(new Chair("кафедра управління навчальними закладами та педагогіки вищої школи", "КУНЗПВШ"));
//            Chair kafSAVM = es.findUniqueOrPersist(new Chair("кафедра системного аналізу та вищої математики", "КСАВМ"));
           
                        //---Кафедры, выполняющие обучение по лицензиям----------
            //---Інститут управління----------
            Chair kafMO = es.findUniqueOrPersist(new Chair("Кафедра менеджменту організацій", "КМО"));
            Chair kafMZD = es.findUniqueOrPersist(new Chair("Кафедра менеджменту зовнішньоекономічної діяльності", "КМЗЕД"));
            Chair kafGD = es.findUniqueOrPersist(new Chair("Кафедра гуманітарних дисциплін", "КГД"));
            Chair kafRT = es.findUniqueOrPersist(new Chair("Кафедра релігієзнавства та теології", "КРТ"));
            Chair kafIAM = es.findUniqueOrPersist(new Chair("Кафедра інвестиційного та аграрного менеджменту", "КІАМ"));
            Chair kafAMBA = es.findUniqueOrPersist(new Chair("Кафедра адміністративного менеджменту та бізнес-адміністрування", "КАМБА"));
            //---Інститут економіки----------
            Chair kafEP = es.findUniqueOrPersist(new Chair("Кафедра економіки підприємства", "КЕП"));
            Chair kafEKS = es.findUniqueOrPersist(new Chair("Кафедра економічної кібернетики та статистики", "КЕКС"));
            Chair kafETNPE = es.findUniqueOrPersist(new Chair("Кафедра економічної теорії, національної та прикладної економіки", "КЕТНПЕ"));
            Chair kafM = es.findUniqueOrPersist(new Chair("Кафедра маркетингу", "КМК"));
            Chair kafME = es.findUniqueOrPersist(new Chair("Кафедра міжнародної економіки", "КМЕ"));
            Chair kafOA = es.findUniqueOrPersist(new Chair("Кафедра обліку і аудиту", "КОА"));
            Chair kafFK = es.findUniqueOrPersist(new Chair("Кафедра фінансів та кредиту", "КФК"));
            //---Інститут права ім. В.Сташиса----------
            Chair kafKAP = es.findUniqueOrPersist(new Chair("Кафедра конституційного та адміністративного права", "ККАП"));
            Chair kafKP = es.findUniqueOrPersist(new Chair("Кафедра кримінального права", "ККП"));
            Chair kafKPK = es.findUniqueOrPersist(new Chair("Кафедра кримінального процесу та криміналістики", "ККПК"));
            Chair kafMP = es.findUniqueOrPersist(new Chair("Кафедра міжнародного права", "КМП"));
            Chair kafTZGP = es.findUniqueOrPersist(new Chair("Кафедра трудового, земельного та господарського права", "КТЗГП"));
            Chair kafGPP = es.findUniqueOrPersist(new Chair("Кафедра цивільного права і процесу", "КЦПП"));
            Chair kafTIDP = es.findUniqueOrPersist(new Chair("Кафедра теорії та історії держави та права", "КТІДП"));
            Chair kafVPCO = es.findUniqueOrPersist(new Chair("Кафедра військової підготовки та цивільної оборони", "КВПЦО"));
            //---Інститут журналістики й масової комунікації----------
            Chair kafZSK = es.findUniqueOrPersist(new Chair("Кафедра журналістики і соціальних комунікацій", "КЖСК"));
            Chair kafVSRUF = es.findUniqueOrPersist(new Chair("Кафедра видавничої справи, редагування та української філології", "КВСРУФ"));
            Chair kafRZG = es.findUniqueOrPersist(new Chair("Кафедра реклами і з'язків із громадкістю", "КРЗГ"));
            //---Інститут здоров'я, спорту та туризму----------
            Chair kafTGG = es.findUniqueOrPersist(new Chair("Кафедра туризму та готельного господарства", "КТГГ"));
            Chair kafTOFAV = es.findUniqueOrPersist(new Chair("Кафедра теоретичних основ фізичного та адаптивного виховання", "КТОФАВ"));
            Chair kafFR = es.findUniqueOrPersist(new Chair("Кафедра фізичної реабілітації", "КФР"));
            Chair kafV = es.findUniqueOrPersist(new Chair("Кафедра фізичного виховання", "КФВ"));
            //---Інститут іноземної філології----------
            Chair kafAFZL = es.findUniqueOrPersist(new Chair("Кафедра англійської філології та зарубіжної літератури", "КАФЗЛ"));
//            Chair kafPF       = es.findUniqueOrPersist(new Chair("Кафедра перекладу за фахом","КПФ",iIPh));
            Chair kafRGF = es.findUniqueOrPersist(new Chair("Кафедра романо–германської філології", "КРГФ"));
            Chair kafTPP = es.findUniqueOrPersist(new Chair("Кафедра теорії та практики  перекладу", "КТПП"));
            Chair kafIM = es.findUniqueOrPersist(new Chair("Кафедра іноземних мов", "КІМ"));
            //---Інститут інформаційних та соціальних технологій ----------
            Chair kafD = es.findUniqueOrPersist(new Chair("Кафедра дизайну", "КД"));
            Chair kafPP = es.findUniqueOrPersist(new Chair("Кафедра практичної психології", "КПП"));
            Chair kafPIT = es.findUniqueOrPersist(new Chair("Кафедра програмування та інформаційних технологій", "КПІТ"));
            Chair kafSAVM = es.findUniqueOrPersist(new Chair("Кафедра системного аналізу та вищої математики", "КСАВМ"));
            Chair kafSSR = es.findUniqueOrPersist(new Chair("Кафедра соціології та соціальної роботи", "КССР"));
            Chair kafDUZK = es.findUniqueOrPersist(new Chair("Кафедра державного управління та земельного кадастру", "КДУЗК"));
            Chair kafEICPHS = es.findUniqueOrPersist(new Chair("Кафедра управління навчальними закладами та педагогіки вищої школи", "КПВШ"));
        }
    }
}
