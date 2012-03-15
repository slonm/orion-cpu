package ua.orion.cpu.core.licensing.services;

import ua.orion.cpu.core.security.entities.Acl;
import ua.orion.cpu.core.security.entities.SubjectType;
import ua.orion.cpu.core.licensing.LicensingSymbols;
import java.util.*;
import ua.orion.cpu.core.orgunits.OrgUnitsSymbols;
import ua.orion.core.annotations.OrderLibrary;
import ua.orion.cpu.core.orgunits.entities.Chair;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.core.services.EntityService;
import static ua.orion.core.utils.DateTimeUtils.*;

/**
 *
 * @author sl
 */
@OrderLibrary("after:" + OrgUnitsSymbols.ORG_UNITS_LIB)
public class LicensingSeedEntity {

    public LicensingSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(LicensingSymbols.LICENSING_LIB));
        //---------Заполнение справочников---------
        //---------Формы обучения---------
        EducationForm stat_EF = es.findUniqueOrPersist(new EducationForm("Денна", "Ден.", EducationForm.STATIONARY_UKEY, 1));
        EducationForm corr_EF = es.findUniqueOrPersist(new EducationForm("Заочна", "Заоч.", EducationForm.CORRESPONDENCE_UKEY, 2));
        //---------Квалификационные уровни---------
        EducationalQualificationLevel jSpec_EQL = es.findUniqueOrPersist(new EducationalQualificationLevel("Молодший спеціаліст", "мол.сп.", "5", EducationalQualificationLevel.JUNIOR_SPECIALIST_UKEY));
        EducationalQualificationLevel bach_EQL = es.findUniqueOrPersist(new EducationalQualificationLevel("Бакалавр", "бак.", "6", EducationalQualificationLevel.BACHELOR_UKEY));
        EducationalQualificationLevel spec_EQL = es.findUniqueOrPersist(new EducationalQualificationLevel("Спеціаліст", "спец.", "7", EducationalQualificationLevel.SPECIALIST_UKEY));
        EducationalQualificationLevel master_EQL = es.findUniqueOrPersist(new EducationalQualificationLevel("Магістр", "маг.", "8", EducationalQualificationLevel.MASTER_UKEY));
        //---------Группы лицензионных записей (область действия) ---------
        LicenseRecordGroup bsmtrain_LRG = es.findUniqueOrPersist(new LicenseRecordGroup("Підготовка бакалаврів, спеціалістів. магістрів", LicenseRecordGroup.B_S_M_TRAINING_UKEY));
        LicenseRecordGroup sretrain_LRG = es.findUniqueOrPersist(new LicenseRecordGroup("Перепідготовка спеціалістів", LicenseRecordGroup.SPEC_RETRAINING_UKEY));
        LicenseRecordGroup forcol_LRG = es.findUniqueOrPersist(new LicenseRecordGroup("Для колледжу Класичного приватного університету", LicenseRecordGroup.FOR_COLLEDGE_UKEY));
        LicenseRecordGroup btrain_LRG = es.findUniqueOrPersist(new LicenseRecordGroup("Підготовка бакалаврів", LicenseRecordGroup.BACH_TRAINING_UKEY));
        LicenseRecordGroup jstrain_LRG = es.findUniqueOrPersist(new LicenseRecordGroup("Підготовка молодших спеціалістів", LicenseRecordGroup.JUN_SPEC_TRAINING_UKEY));
        if (testData) {
            //---Списки доступа----------
            //---------Роли----------
            //LicenseReader
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "LicenseRecordGroup:read,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "License:read,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "LicenseRecord:read,menu"));

            //LicenseAppender
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseAppender", SubjectType.ROLE, "EducationForm:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "License:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis/Licensing/LicenseReader", SubjectType.ROLE, "LicenseRecord:read,insert,update,menu"));
            //---------Области знаний или направления подготовки----------
            //Подготовка бакалавров, специалистов, магистров (Перелік 1997р - напрями навчання)
            KnowledgeArea kaotdSpecCateg = es.findUniqueOrPersist(new KnowledgeArea("Специфічні категорії", null, "0000", false));
            KnowledgeArea kaotdPhysTrainSport = es.findUniqueOrPersist(new KnowledgeArea("Фізичне виховання і спорт", null, "0102", false));
            KnowledgeArea kaotdZhurn = es.findUniqueOrPersist(new KnowledgeArea("Журналістика", null, "0302", false));
            KnowledgeArea kaotdInternRel = es.findUniqueOrPersist(new KnowledgeArea("Міжнародні відносини", null, "0304", false));
            KnowledgeArea kaotdPhilol = es.findUniqueOrPersist(new KnowledgeArea("Філологія", null, "0305", false));
            KnowledgeArea kaotdPs = es.findUniqueOrPersist(new KnowledgeArea("Психологія", null, "0401", false));
            KnowledgeArea kaotdSocial = es.findUniqueOrPersist(new KnowledgeArea("Соціологія", null, "0402", false));
            KnowledgeArea kaotdEkonBus = es.findUniqueOrPersist(new KnowledgeArea("Економіка і підприємництво", null, "0501", false));
            KnowledgeArea kaotdManeg = es.findUniqueOrPersist(new KnowledgeArea("Менеджмент", null, "0502", false));
            KnowledgeArea kaotdTur = es.findUniqueOrPersist(new KnowledgeArea("Туризм", null, "0504", false));
            KnowledgeArea kaotdRight = es.findUniqueOrPersist(new KnowledgeArea("Право", null, "0601", false));
            KnowledgeArea kaotdGeodMapZem = es.findUniqueOrPersist(new KnowledgeArea("Геодезія, картографія та землевпорядкування", null, "0709", false));
            KnowledgeArea kaotdAppMath = es.findUniqueOrPersist(new KnowledgeArea("Прикладна математика", null, "0802", false));
            KnowledgeArea kaotdCompSci = es.findUniqueOrPersist(new KnowledgeArea("Комп'ютерні науки", null, "0804", false));
            KnowledgeArea kaotdElect = es.findUniqueOrPersist(new KnowledgeArea("Електроніка", null, "0908", false));
            KnowledgeArea kaotdGover = es.findUniqueOrPersist(new KnowledgeArea("Державне управління", null, "1501", false));

            //Подготовка бакалавров (Перелік 2006р - напрями навчання)
            //Фізичне виховання, спорт і здоров'я людини
            TrainingDirection tdosPhysV_B = es.findUniqueOrPersist(new TrainingDirection("Фізичне виховання", "ФВ", "01", kaotdPhysTrainSport));
            TrainingDirection tdosZL_B = es.findUniqueOrPersist(new TrainingDirection("Здоров'я людини", "ЗЛ", "03", kaotdPhysTrainSport));
            //Культура
            TrainingDirection tdosT_B = es.findUniqueOrPersist(new TrainingDirection("Туризм", "Т", "07", kaotdPhysTrainSport));
            //Гуманітарні науки
            TrainingDirection tdosPhilol_B = es.findUniqueOrPersist(new TrainingDirection("Філологія", "ФЛ", "03", kaotdPhilol));
            //Соціально-політичні науки
            TrainingDirection tdosS_B = es.findUniqueOrPersist(new TrainingDirection("Соціологія", "С", "01", kaotdSocial));
            TrainingDirection tdosPSH_B = es.findUniqueOrPersist(new TrainingDirection("Психологія", "П", "02", kaotdPs));
            //Журналістика та інформація
            TrainingDirection tdosZhu_B = es.findUniqueOrPersist(new TrainingDirection("Журналістика", "Ж", "01", kaotdZhurn));
            TrainingDirection tdosRZG_B = es.findUniqueOrPersist(new TrainingDirection("Реклама і зв'язки з громадкістю (за видами)", "РЗГ", "02", kaotdManeg));
            TrainingDirection tdosVSR_B = es.findUniqueOrPersist(new TrainingDirection("Видавнича справа та редагування", "ВСР", "03", kaotdZhurn));
            //Право
            TrainingDirection tdosP_B = es.findUniqueOrPersist(new TrainingDirection("Право", "П", "01", kaotdRight));
            //Економіка і підприємництво
            TrainingDirection tdosEKib_B = es.findUniqueOrPersist(new TrainingDirection("Економічна кібернетика", "ЕК", "02", kaotdEkonBus));
            TrainingDirection tdosME_B = es.findUniqueOrPersist(new TrainingDirection("Міжнародна економіка", "МЕ", "03", kaotdEkonBus));
            TrainingDirection tdosEP_B = es.findUniqueOrPersist(new TrainingDirection("Економіка підприємства", "ЕП", "04", kaotdEkonBus));
            TrainingDirection tdosPRS_B = es.findUniqueOrPersist(new TrainingDirection("Прикладна статистика", "ПС", "06", kaotdEkonBus));
            TrainingDirection tdosM_B = es.findUniqueOrPersist(new TrainingDirection("Маркетинг", "М", "07", kaotdEkonBus));
            TrainingDirection tdosFK_B = es.findUniqueOrPersist(new TrainingDirection("Фінанси і кредит", "ФК", "08", kaotdEkonBus));
            TrainingDirection tdosOA_B = es.findUniqueOrPersist(new TrainingDirection("Облік і аудит", "ОА", "09", kaotdEkonBus));
            //Менеджмент та адміністрування
            TrainingDirection tdosMG_B = es.findUniqueOrPersist(new TrainingDirection("Менеджмент", "МА", "01", kaotdEkonBus));
            //Системні науки та кібернетика
            TrainingDirection tdosIF_B = es.findUniqueOrPersist(new TrainingDirection("Інформатика", "І", "02", kaotdEkonBus));
            TrainingDirection tdosSA_B = es.findUniqueOrPersist(new TrainingDirection("Системний аналіз", "СА", "03", kaotdEkonBus));
            //Інформатика та обчислювальна техника
            TrainingDirection tdosPI_B = es.findUniqueOrPersist(new TrainingDirection("Програмна інженерія", "ПІ", "03", kaotdEkonBus));
            //Електроніка
            TrainingDirection tdosMNE_B = es.findUniqueOrPersist(new TrainingDirection("Мікро- та наноелектроніка", "МНЕ", "01", kaotdEkonBus));
            //Геодезія та землеустрій
            TrainingDirection tdosGKZ_B = es.findUniqueOrPersist(new TrainingDirection("Геодезія, картографія та землеустрій", "ГКЗ", "01", kaotdEkonBus));
            //Мистецтво (NEW)
            TrainingDirection tdosDZ_B = es.findUniqueOrPersist(new TrainingDirection("Дизайн", "ДЗ", "07", kaotdEkonBus));
            //Сфера обслуговування (NEW)
            TrainingDirection tdosGRS_B = es.findUniqueOrPersist(new TrainingDirection("Готельно ресторанна справа", "ГРС", "01", kaotdEkonBus));

            //---Направления подготовки или специальности суффиксы _JS, _B, _S, _M обозначают квалификационные уровни
            //младшего специалиста, бакалавра, специалиста/магистра, соответственно
            //Допускается комбинация суффиксов при одинаковых именах и кодах записей (или кратких именах и кодах),
            //например, часто совпадают записи для специалистов и магистров, в этом случае суффикс _SM
            //(образовательно-квалификационный уровень добавляется уже для записей лицензий)

            //Підготовка бакалаврів, спеціалістів, магістрів (Перелік 1997р - спеціальності)
            //Специфічні категорії
            Speciality tdosPVSH_M = es.findUniqueOrPersist(new Speciality("Педагогіка вищої школи", "ПВШ", "05", tdosPhysV_B));
            Speciality tdosAM_M = es.findUniqueOrPersist(new Speciality("Адміністративний менеджмент", "АМ", "07", tdosPhysV_B));
            Speciality tdosUNZ_M = es.findUniqueOrPersist(new Speciality("Управління навчальним закладом", "УНЗ", "09", tdosPhysV_B));
            Speciality tdosPE_M = es.findUniqueOrPersist(new Speciality("Прикладна економіка", "ПЕ", "11", tdosPhysV_B));
            Speciality tdosBA_M = es.findUniqueOrPersist(new Speciality("Бізнес-адміністрування", "БА", "13", tdosPhysV_B));
            //Фізичне виховання і спорт
            Speciality tdosPVS_B = es.findUniqueOrPersist(new Speciality("Фізичне виховання і спорт", "ФВС", "00", tdosPhysV_B));
            Speciality tdosPhysV_SM = es.findUniqueOrPersist(new Speciality("Фізичне виховання", "ФВ", "01", tdosPhysV_B));
            Speciality tdosPhysR_SM = es.findUniqueOrPersist(new Speciality("Фізична реабілітація", "ФР", "02", tdosPhysV_B));
            //Журналістика
            Speciality tdosZhurn_B = es.findUniqueOrPersist(new Speciality("Журналістика", "Ж", "00", tdosPhysV_B));
            Speciality tdosZhurn_SM = es.findUniqueOrPersist(new Speciality("Журналістика", "Ж", "01", tdosPhysV_B));
            Speciality tdosVSR_S = es.findUniqueOrPersist(new Speciality("Видавнича справа та редагування", "ВСР", "03", tdosPhysV_B));
            //Міжнародні відносини
            Speciality tdosMP_B = es.findUniqueOrPersist(new Speciality("Міжнародні відносини", "МВ", "00", tdosPhysV_B));
            Speciality tdosMP_S = es.findUniqueOrPersist(new Speciality("Міжнародне право", "МП", "02", tdosPhysV_B));
            //Філологія
            Speciality tdosPhyl_B = es.findUniqueOrPersist(new Speciality("Філологія", "Ф", "00", tdosPhysV_B));
            Speciality tdosLangLit_SM = es.findUniqueOrPersist(new Speciality("Мова та література (англійська)", "МЛ", "02", tdosPhysV_B));
            Speciality tdosTransl_SM = es.findUniqueOrPersist(new Speciality("Переклад", "ПР", "07", tdosPhysV_B));
            Speciality tdosLT_B = es.findUniqueOrPersist(new Speciality("Літературна творчість", "ЛТ", "00", tdosPhysV_B));
            //Психологія
            Speciality tdosPS_B = es.findUniqueOrPersist(new Speciality("Психологія", "ПС", "00", tdosPhysV_B));
            Speciality tdosPS_SM = es.findUniqueOrPersist(new Speciality("Психологія", "ПС", "01", tdosPhysV_B));
            //Соціологія
            Speciality tdosSR_B = es.findUniqueOrPersist(new Speciality("Соціологія", "СР", "00", tdosPhysV_B));
            Speciality tdosSR_SM = es.findUniqueOrPersist(new Speciality("Соціальна робота", "СР", "02", tdosPhysV_B));
            //Економіка і підприємництво
            Speciality tdosEIP_B = es.findUniqueOrPersist(new Speciality("Економіка і підприємництво", "ЕІП", "00", tdosPhysV_B));
            Speciality tdosEKib_SM = es.findUniqueOrPersist(new Speciality("Економічна кібернетика", "ЕК", "02", tdosPhysV_B));
            Speciality tdosME_SM = es.findUniqueOrPersist(new Speciality("Міжнародна економіка", "МЕ", "03", tdosPhysV_B));
            Speciality tdosF_SM = es.findUniqueOrPersist(new Speciality("Фінанси", "Ф", "04", tdosPhysV_B));
            Speciality tdosOA_SM = es.findUniqueOrPersist(new Speciality("Облік і аудит", "ОА", "06", tdosPhysV_B));
            Speciality tdosBS_SM = es.findUniqueOrPersist(new Speciality("Банківська справа", "БС", "05", tdosPhysV_B));
            Speciality tdosEP_SM = es.findUniqueOrPersist(new Speciality("Економіка підприємства", "ЕП", "07", tdosPhysV_B));
            Speciality tdosM_SM = es.findUniqueOrPersist(new Speciality("Маркетинг", "М", "08", tdosPhysV_B));
            Speciality tdosES_SM = es.findUniqueOrPersist(new Speciality("Економічна статистика", "ЕС", "10", tdosPhysV_B));
            //Менеджмент
            TrainingDirection tdosMN_B = es.findUniqueOrPersist(new TrainingDirection("Менеджмент", "М", "00", kaotdEkonBus));
            Speciality tdosMO_SM = es.findUniqueOrPersist(new Speciality("Менеджмент організації", "МО", "01", tdosMN_B));
            Speciality tdosMZED_SM = es.findUniqueOrPersist(new Speciality("Менеджмент зовнішньо-економічної діяльності", "МЗЕД", "06", tdosPhysV_B));
            //Туризм
            Speciality tdosTR_B = es.findUniqueOrPersist(new Speciality("Туризм", "Т", "00", tdosPhysV_B));
            Speciality tdosTR_SM = es.findUniqueOrPersist(new Speciality("Туризм", "Т", "01", tdosPhysV_B));
            Speciality tdosGGos_SM = es.findUniqueOrPersist(new Speciality("Готельне господарство", "ГГ", "02", tdosPhysV_B));
            //Право
            Speciality tdosPZ_B = es.findUniqueOrPersist(new Speciality("Право", "П", "00", tdosPhysV_B));
            Speciality tdosPZ_SM = es.findUniqueOrPersist(new Speciality("Правознавство", "П", "01", tdosPhysV_B));
            //Геодезія, картографія та землевпорядкування
            Speciality tdosZK_B = es.findUniqueOrPersist(new Speciality("Землевпорядаткування та кадастр", "ЗК", "00", tdosPhysV_B));
            //Прикладна математика
            Speciality tdosPM_B = es.findUniqueOrPersist(new Speciality("Прикладна математика", "ПМ", "00", tdosPhysV_B));
            Speciality tdosSAU_SM = es.findUniqueOrPersist(new Speciality("Системний аналіз і управління", "САУ", "03", tdosPhysV_B));
            Speciality tdosI_B = es.findUniqueOrPersist(new Speciality("Інформатика", "Інф", "00", tdosPhysV_B));
            //Комп'ютерні науки
            TrainingDirection tdosPZAS_B = es.findUniqueOrPersist(new TrainingDirection("Програмне забезпечення автоматизованих систем", "ПЗАС", "00", kaotdEkonBus));
            Speciality tdosPZAS_SM = es.findUniqueOrPersist(new Speciality("Програмне забезпечення автоматизованих систем", "ПЗАС", "03", tdosPZAS_B));
            //Електроніка
            Speciality tdosFBE_B = es.findUniqueOrPersist(new Speciality("Фізична та біомедична електроніка", "ФБЕ", "00", tdosPhysV_B));
            Speciality tdosFBE_SM = es.findUniqueOrPersist(new Speciality("Фізична та біомедична електроніка", "ФБЕ", "04", tdosPhysV_B));
            //Державне управління
            Speciality tdosDS_M = es.findUniqueOrPersist(new Speciality("Державна служба", "ДС", "01", tdosPhysV_B));

            //Перепідготовка спеціалістів (Перелік 1997р - спеціальності) - додаткових до основних спеціальностей немає

            //Для коледжу КПУ (Перелік 1997р - спеціальності) - додаткові до основних спеціальності
            //Менеджмент
            Speciality tdosOON_JS = es.findUniqueOrPersist(new Speciality("Організація обслуговування населення", "ООН", "03", tdosPhysV_B));
            //Торгівля
            Speciality tdosTKD_JS = es.findUniqueOrPersist(new Speciality("Товарознавство та комерційна діяльність", "ТКД", "01", tdosPhysV_B));
            //Туризм
            Speciality tdosOOGTK_JS = es.findUniqueOrPersist(new Speciality("Організація обслуговування в готелях та туристичних комплексах", "ООГТК", "03", tdosPhysV_B));
            //Прикладна математика
            Speciality tdosPM_JS = es.findUniqueOrPersist(new Speciality("Прикладна математика", "ПМ", "02", tdosPhysV_B));
            //Комп'ютерні науки
            Speciality tdosPECTAS_JS = es.findUniqueOrPersist(new Speciality("Програмування для електронно-обчислювальної техніки і автоматизованих систем", "ПЗАС", "05", tdosIF_B));


            //---Серия, номер и дата выдачи лицензии----------
            License licenseCPU = es.findUniqueOrPersist(new License("АВ", "529699", createCalendar(5, 11, 2010)));
            licenseCPU.setLicenseState(LicenseState.FORCED);

            //---Кафедры, выполняющие обучение по лицензиям----------
            //---Інститут управління----------
            Chair kafMO = es.findByName(Chair.class, "Кафедра менеджменту організацій");
            assert kafMO != null;
            Chair kafMZD = es.findByName(Chair.class, "Кафедра менеджменту зовнішньоекономічної діяльності");
            assert kafMZD != null;
            Chair kafIAM = es.findByName(Chair.class, "Кафедра інвестиційного та аграрного менеджменту");
            assert kafIAM != null;
            Chair kafAMBA = es.findByName(Chair.class, "Кафедра адміністративного менеджменту та бізнес-адміністрування");
            assert kafAMBA != null;
            //---Інститут економіки----------
            Chair kafEP = es.findByName(Chair.class, "Кафедра економіки підприємства");
            assert kafEP != null;
            Chair kafEKS = es.findByName(Chair.class, "Кафедра економічної кібернетики та статистики");
            assert kafEKS != null;
            Chair kafETNPE = es.findByName(Chair.class, "Кафедра економічної теорії, національної та прикладної економіки");
            assert kafETNPE != null;
            Chair kafM = es.findByName(Chair.class, "Кафедра маркетингу");
            assert kafM != null;
            Chair kafME = es.findByName(Chair.class, "Кафедра міжнародної економіки");
            assert kafME != null;
            Chair kafOA = es.findByName(Chair.class, "Кафедра обліку і аудиту");
            assert kafOA != null;
            Chair kafFK = es.findByName(Chair.class, "Кафедра фінансів та кредиту");
            assert kafFK != null;
            //---Інститут права----------
            Chair kafKAP = es.findByName(Chair.class, "Кафедра конституційного та адміністративного права");
            assert kafKAP != null;
            Chair kafKP = es.findByName(Chair.class, "Кафедра кримінального права");
            assert kafKP != null;
            Chair kafKPK = es.findByName(Chair.class, "Кафедра кримінального процесу та криміналістики");
            assert kafKPK != null;
            Chair kafMP = es.findByName(Chair.class, "Кафедра міжнародного права");
            assert kafMP != null;
            Chair kafTZGP = es.findByName(Chair.class, "Кафедра трудового, земельного та господарського права");
            assert kafTZGP != null;
            Chair kafGPP = es.findByName(Chair.class, "Кафедра цивільного права і процесу");
            assert kafGPP != null;
            Chair kafTIDP = es.findByName(Chair.class, "Кафедра теорії та історії держави та права");
            assert kafTIDP != null;
            //---Інститут журналістики й масової комунікації----------
            Chair kafZSK = es.findByName(Chair.class, "Кафедра журналістики і соціальних комунікацій");
            assert kafZSK != null;
            Chair kafVSRUF = es.findByName(Chair.class, "Кафедра видавничої справи, редагування та української філології");
            assert kafVSRUF != null;
            Chair kafRZG = es.findByName(Chair.class, "Кафедра реклами і з'язків із громадкістю");
            assert kafRZG != null;
            //---Інститут здоров'я, спорту і туризму----------
            Chair kafTGG = es.findByName(Chair.class, "Кафедра туризму та готельного господарства");
            assert kafTGG != null;
            Chair kafTOFAV = es.findByName(Chair.class, "Кафедра теоретичних основ фізичного та адаптивного виховання");
            assert kafTOFAV != null;
            Chair kafFR = es.findByName(Chair.class, "Кафедра фізичної реабілітації");
            assert kafFR != null;
            Chair kafV = es.findByName(Chair.class, "Кафедра фізичного виховання");
            assert kafV != null;
            //---Інститут іноземної філології----------
            Chair kafAFZL = es.findByName(Chair.class, "Кафедра англійської філології та зарубіжної літератури");
            assert kafAFZL != null;
            Chair kafRGF = es.findByName(Chair.class, "Кафедра романо–германської філології");
            assert kafRGF != null;
            Chair kafTPP = es.findByName(Chair.class, "Кафедра теорії та практики  перекладу");
            assert kafTPP != null;
            Chair kafIM = es.findByName(Chair.class, "Кафедра іноземних мов");
            assert kafIM != null;
            //---Інститут інформаційних та соціальних технологій ----------
            Chair kafD = es.findByName(Chair.class, "Кафедра дизайну");
            assert kafD != null;
            Chair kafPP = es.findByName(Chair.class, "Кафедра практичної психології");
            assert kafPP != null;
            Chair kafPIT = es.findByName(Chair.class, "Кафедра програмування та інформаційних технологій");
            assert kafPIT != null;
            Chair kafSAVM = es.findByName(Chair.class, "Кафедра системного аналізу та вищої математики");
            assert kafSAVM != null;
            Chair kafSSR = es.findByName(Chair.class, "Кафедра соціології та соціальної роботи");
            assert kafSSR != null;
            Chair kafDUZK = es.findByName(Chair.class, "Кафедра державного управління та земельного кадастру");
            assert kafDUZK != null;
            Chair kafEICPHS = es.findByName(Chair.class, "Кафедра управління навчальними закладами та педагогіки вищої школи");
            assert kafEICPHS != null;

            //Термін закінчення ліцензій ПЗАС та ПІ
            Calendar lrCal20100701 = createCalendar(1, 7, 2010);
            //Термін закінчення ліцензій ПВШ
            Calendar lrCal20090701 = createCalendar(1, 7, 2009);

            //Універсальні календарі 
            Calendar lrCal20110701 = createCalendar(1, 7, 2011);
            Calendar lrCal20120701 = createCalendar(1, 7, 2012);
            Calendar lrCal20130701 = createCalendar(1, 7, 2013);
            Calendar lrCal20140701 = createCalendar(1, 7, 2014);
            Calendar lrCal20150701 = createCalendar(1, 7, 2015);

            //---Записи лицензии-суффиксы _JS, _B, _S, _M обозначают
            //квалификационные уровни младшего специалиста, бакалавра, специалиста, магистра, соответственно
            //суффиксы _D, _Z обозначают дневную и заочн формы обучения----------

            LicenseRecord lr;
            //Менеджмент
            //Менеджмент - бакалаври
            lr = new LicenseRecord(licenseCPU, tdosMN_B, bach_EQL, stat_EF, 140, corr_EF, 250, lrCal20150701, kafMO, bsmtrain_LRG);
            lr.setClassify("за видами економічної діяльності");
            lr.setTrainingVariants("Аграрний менеджмент\nМенеджмент організації інвестиційної діяльності\nМенеджмент на ринку товарів та послуг");
            es.findUniqueOrPersist(lr);
            //Менеджмент організацій - спеціалісти, магістри
            lr = new LicenseRecord(licenseCPU, tdosMO_SM, spec_EQL, stat_EF, 90, corr_EF, 150, lrCal20150701, kafMO, bsmtrain_LRG);
            lr.setClassify("за видами економічної діяльності");
            lr.setTrainingVariants("Менеджмент організації інвестиційної діяльності\nМенеджмент на ринку товарів та послуг");
            es.findUniqueOrPersist(lr);
            lr = new LicenseRecord(licenseCPU, tdosMO_SM, master_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafMO, bsmtrain_LRG);
            lr.setClassify("за видами економічної діяльності");
            lr.setTrainingVariants("Аграрний менеджмент\nМенеджмент організації інвестиційної діяльності\nМенеджмент на ринку товарів та послуг");
            es.findUniqueOrPersist(lr);


            //Комп'ютерні науки
            //Програмне забезпечення автоматизованих систем - бакалаври, спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZAS_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafPIT, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZAS_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPIT, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZAS_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafPIT, bsmtrain_LRG));

//            //Системний аналіз
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSA_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSAVM, btrain_LRG));
//
//            //Інформатика та обчислювальна техніка
//            //Програмна інженерія
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPI_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafPIT, btrain_LRG));
        }
    }
}
