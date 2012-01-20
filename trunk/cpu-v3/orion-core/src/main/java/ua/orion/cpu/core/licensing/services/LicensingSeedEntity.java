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
            TrainingDirection tdosPhysV_B = es.findUniqueOrPersist(new TrainingDirection("Фізичне виховання", "ФВ", "01", kaotdPhysTrainSport, null, false));
            TrainingDirection tdosZL_B = es.findUniqueOrPersist(new TrainingDirection("Здоров'я людини", "ЗЛ", "03", kaotdPhysTrainSport, null, false));
            //Культура
            TrainingDirection tdosT_B = es.findUniqueOrPersist(new TrainingDirection("Туризм", "Т", "07", kaotdPhysTrainSport, null, false));
            //Гуманітарні науки
            TrainingDirection tdosPhilol_B = es.findUniqueOrPersist(new TrainingDirection("Філологія", "ФЛ", "03", kaotdPhilol, null, false));
            //Соціально-політичні науки
            TrainingDirection tdosS_B = es.findUniqueOrPersist(new TrainingDirection("Соціологія", "С", "01", kaotdSocial, null, false));
            TrainingDirection tdosPSH_B = es.findUniqueOrPersist(new TrainingDirection("Психологія", "П", "02", kaotdPs, null, false));
            //Журналістика та інформація
            TrainingDirection tdosZhu_B = es.findUniqueOrPersist(new TrainingDirection("Журналістика", "Ж", "01", kaotdZhurn, null, false));
            TrainingDirection tdosRZG_B = es.findUniqueOrPersist(new TrainingDirection("Реклама і зв'язки з громадкістю (за видами)", "РЗГ", "02", kaotdManeg, "вид", false));
            TrainingDirection tdosVSR_B = es.findUniqueOrPersist(new TrainingDirection("Видавнича справа та редагування", "ВСР", "03", kaotdZhurn, null, false));
            //Право
            TrainingDirection tdosP_B = es.findUniqueOrPersist(new TrainingDirection("Право", "П", "01", kaotdRight, null, false));
            //Економіка і підприємництво
            TrainingDirection tdosEKib_B = es.findUniqueOrPersist(new TrainingDirection("Економічна кібернетика", "ЕК", "02", kaotdEkonBus, null, false));
            TrainingDirection tdosME_B = es.findUniqueOrPersist(new TrainingDirection("Міжнародна економіка", "МЕ", "03", kaotdEkonBus, null, false));
            TrainingDirection tdosEP_B = es.findUniqueOrPersist(new TrainingDirection("Економіка підприємства", "ЕП", "04", kaotdEkonBus, null, false));
            TrainingDirection tdosPRS_B = es.findUniqueOrPersist(new TrainingDirection("Прикладна статистика", "ПС", "06", kaotdEkonBus, null, false));
            TrainingDirection tdosM_B = es.findUniqueOrPersist(new TrainingDirection("Маркетинг", "М", "07", kaotdEkonBus, null, false));
            TrainingDirection tdosFK_B = es.findUniqueOrPersist(new TrainingDirection("Фінанси і кредит", "ФК", "08", kaotdEkonBus, null, false));
            TrainingDirection tdosOA_B = es.findUniqueOrPersist(new TrainingDirection("Облік і аудит", "ОА", "09", kaotdEkonBus, null, false));
            //Менеджмент та адміністрування
            TrainingDirection tdosMG_B = es.findUniqueOrPersist(new TrainingDirection("Менеджмент", "МА", "01", kaotdEkonBus, null, false));
            //Системні науки та кібернетика
            TrainingDirection tdosIF_B = es.findUniqueOrPersist(new TrainingDirection("Інформатика", "І", "02", kaotdEkonBus, null, false));
            TrainingDirection tdosSA_B = es.findUniqueOrPersist(new TrainingDirection("Системний аналіз", "СА", "03", kaotdEkonBus, null, false));
            //Інформатика та обчислювальна техника
            TrainingDirection tdosPI_B = es.findUniqueOrPersist(new TrainingDirection("Програмна інженерія", "ПІ", "03", kaotdEkonBus, null, false));
            //Електроніка
            TrainingDirection tdosMNE_B = es.findUniqueOrPersist(new TrainingDirection("Мікро- та наноелектроніка", "МНЕ", "01", kaotdEkonBus, null, false));
            //Геодезія та землеустрій
            TrainingDirection tdosGKZ_B = es.findUniqueOrPersist(new TrainingDirection("Геодезія, картографія та землеустрій", "ГКЗ", "01", kaotdEkonBus, null, false));
            //Мистецтво (NEW)
            TrainingDirection tdosDZ_B = es.findUniqueOrPersist(new TrainingDirection("Дизайн", "ДЗ", "07", kaotdEkonBus, null, false));
            //Сфера обслуговування (NEW)
            TrainingDirection tdosGRS_B = es.findUniqueOrPersist(new TrainingDirection("Готельно ресторанна справа", "ГРС", "01", kaotdEkonBus, null, false));

            //---Направления подготовки или специальности суффиксы _JS, _B, _S, _M обозначают квалификационные уровни
            //младшего специалиста, бакалавра, специалиста/магистра, соответственно
            //Допускается комбинация суффиксов при одинаковых именах и кодах записей (или кратких именах и кодах),
            //например, часто совпадают записи для специалистов и магистров, в этом случае суффикс _SM
            //(образовательно-квалификационный уровень добавляется уже для записей лицензий)

            //Підготовка бакалаврів, спеціалістів, магістрів (Перелік 1997р - спеціальності)
            //Специфічні категорії
            Speciality tdosPVSH_M = es.findUniqueOrPersist(new Speciality("Педагогіка вищої школи", "ПВШ", "05", tdosPhysV_B, null, false));
            Speciality tdosAM_M = es.findUniqueOrPersist(new Speciality("Адміністративний менеджмент", "АМ", "07", tdosPhysV_B, null, false));
            Speciality tdosUNZ_M = es.findUniqueOrPersist(new Speciality("Управління навчальним закладом", "УНЗ", "09", tdosPhysV_B, null, false));
            Speciality tdosPE_M = es.findUniqueOrPersist(new Speciality("Прикладна економіка", "ПЕ", "11", tdosPhysV_B, null, false));
            Speciality tdosBA_M = es.findUniqueOrPersist(new Speciality("Бізнес-адміністрування", "БА", "13", tdosPhysV_B, null, false));
            //Фізичне виховання і спорт
            Speciality tdosPVS_B = es.findUniqueOrPersist(new Speciality("Фізичне виховання і спорт", "ФВС", "00", tdosPhysV_B, null, false));
            Speciality tdosPhysV_SM = es.findUniqueOrPersist(new Speciality("Фізичне виховання", "ФВ", "01", tdosPhysV_B, null, false));
            Speciality tdosPhysR_SM = es.findUniqueOrPersist(new Speciality("Фізична реабілітація", "ФР", "02", tdosPhysV_B, null, false));
            //Журналістика
            Speciality tdosZhurn_B = es.findUniqueOrPersist(new Speciality("Журналістика", "Ж", "00", tdosPhysV_B, null, false));
            Speciality tdosZhurn_SM = es.findUniqueOrPersist(new Speciality("Журналістика", "Ж", "01", tdosPhysV_B, null, false));
            Speciality tdosVSR_S = es.findUniqueOrPersist(new Speciality("Видавнича справа та редагування", "ВСР", "03", tdosPhysV_B, null, false));
            //Міжнародні відносини
            Speciality tdosMP_B = es.findUniqueOrPersist(new Speciality("Міжнародні відносини", "МВ", "00", tdosPhysV_B, null, false));
            Speciality tdosMP_S = es.findUniqueOrPersist(new Speciality("Міжнародне право", "МП", "02", tdosPhysV_B, null, false));
            //Філологія
            Speciality tdosPhyl_B = es.findUniqueOrPersist(new Speciality("Філологія", "Ф", "00", tdosPhysV_B, null, false));
            Speciality tdosLangLit_SM = es.findUniqueOrPersist(new Speciality("Мова та література (англійська)", "МЛ", "02", tdosPhysV_B, null, false));
            Speciality tdosTransl_SM = es.findUniqueOrPersist(new Speciality("Переклад", "ПР", "07", tdosPhysV_B, null, false));
            Speciality tdosLT_B = es.findUniqueOrPersist(new Speciality("Літературна творчість", "ЛТ", "00", tdosPhysV_B, null, false));
            //Психологія
            Speciality tdosPS_B = es.findUniqueOrPersist(new Speciality("Психологія", "ПС", "00", tdosPhysV_B, null, false));
            Speciality tdosPS_SM = es.findUniqueOrPersist(new Speciality("Психологія", "ПС", "01", tdosPhysV_B, null, false));
            //Соціологія
            Speciality tdosSR_B = es.findUniqueOrPersist(new Speciality("Соціологія", "СР", "00", tdosPhysV_B, null, false));
            Speciality tdosSR_SM = es.findUniqueOrPersist(new Speciality("Соціальна робота", "СР", "02", tdosPhysV_B, null, false));
            //Економіка і підприємництво
            Speciality tdosEIP_B = es.findUniqueOrPersist(new Speciality("Економіка і підприємництво", "ЕІП", "00", tdosPhysV_B, null, false));
            Speciality tdosEKib_SM = es.findUniqueOrPersist(new Speciality("Економічна кібернетика", "ЕК", "02", tdosPhysV_B, null, false));
            Speciality tdosME_SM = es.findUniqueOrPersist(new Speciality("Міжнародна економіка", "МЕ", "03", tdosPhysV_B, null, false));
            Speciality tdosF_SM = es.findUniqueOrPersist(new Speciality("Фінанси", "Ф", "04", tdosPhysV_B, null, false));
            Speciality tdosOA_SM = es.findUniqueOrPersist(new Speciality("Облік і аудит", "ОА", "06", tdosPhysV_B, null, false));
            Speciality tdosBS_SM = es.findUniqueOrPersist(new Speciality("Банківська справа", "БС", "05", tdosPhysV_B, null, false));
            Speciality tdosEP_SM = es.findUniqueOrPersist(new Speciality("Економіка підприємства", "ЕП", "07", tdosPhysV_B, null, false));
            Speciality tdosM_SM = es.findUniqueOrPersist(new Speciality("Маркетинг", "М", "08", tdosPhysV_B, null, false));
            Speciality tdosES_SM = es.findUniqueOrPersist(new Speciality("Економічна статистика", "ЕС", "10", tdosPhysV_B, null, false));
            //Менеджмент
            Speciality tdosMN_B = es.findUniqueOrPersist(new Speciality("Менеджмент", "М", "00", tdosPhysV_B, null, false));
            Speciality tdosMO_SM = es.findUniqueOrPersist(new Speciality("Менеджмент організації", "МО", "01", tdosPhysV_B, "за видами економічної діяльності", false));
            Speciality tdosMZED_SM = es.findUniqueOrPersist(new Speciality("Менеджмент зовнішньо-економічної діяльності", "МЗЕД", "06", tdosPhysV_B, null, false));
            //Туризм
            Speciality tdosTR_B = es.findUniqueOrPersist(new Speciality("Туризм", "Т", "00", tdosPhysV_B, null, false));
            Speciality tdosTR_SM = es.findUniqueOrPersist(new Speciality("Туризм", "Т", "01", tdosPhysV_B, null, false));
            Speciality tdosGGos_SM = es.findUniqueOrPersist(new Speciality("Готельне господарство", "ГГ", "02", tdosPhysV_B, null, false));
            //Право
            Speciality tdosPZ_B = es.findUniqueOrPersist(new Speciality("Право", "П", "00", tdosPhysV_B, null, false));
            Speciality tdosPZ_SM = es.findUniqueOrPersist(new Speciality("Правознавство", "П", "01", tdosPhysV_B, null, false));
            //Геодезія, картографія та землевпорядкування
            Speciality tdosZK_B = es.findUniqueOrPersist(new Speciality("Землевпорядаткування та кадастр", "ЗК", "00", tdosPhysV_B, null, false));
            //Прикладна математика
            Speciality tdosPM_B = es.findUniqueOrPersist(new Speciality("Прикладна математика", "ПМ", "00", tdosPhysV_B, null, false));
            Speciality tdosSAU_SM = es.findUniqueOrPersist(new Speciality("Системний аналіз і управління", "САУ", "03", tdosPhysV_B, null, false));
            Speciality tdosI_B = es.findUniqueOrPersist(new Speciality("Інформатика", "Інф", "00", tdosPhysV_B, null, false));
            //Комп'ютерні науки
            Speciality tdosPZAS_B = es.findUniqueOrPersist(new Speciality("Програмне забезпечення автоматизованих систем", "ПЗАС", "00", tdosPhysV_B, null, false));
            Speciality tdosPZAS_SM = es.findUniqueOrPersist(new Speciality("Програмне забезпечення автоматизованих систем", "ПЗАС", "03", tdosPhysV_B, null, false));
            //Електроніка
            Speciality tdosFBE_B = es.findUniqueOrPersist(new Speciality("Фізична та біомедична електроніка", "ФБЕ", "00", tdosPhysV_B, null, false));
            Speciality tdosFBE_SM = es.findUniqueOrPersist(new Speciality("Фізична та біомедична електроніка", "ФБЕ", "04", tdosPhysV_B, null, false));
            //Державне управління
            Speciality tdosDS_M = es.findUniqueOrPersist(new Speciality("Державна служба", "ДС", "01", tdosPhysV_B, null, false));

            //Перепідготовка спеціалістів (Перелік 1997р - спеціальності) - додаткових до основних спеціальностей немає

            //Для коледжу КПУ (Перелік 1997р - спеціальності) - додаткові до основних спеціальності
            //Менеджмент
            Speciality tdosOON_JS = es.findUniqueOrPersist(new Speciality("Організація обслуговування населення", "ООН", "03", tdosPhysV_B, null, false));
            //Торгівля
            Speciality tdosTKD_JS = es.findUniqueOrPersist(new Speciality("Товарознавство та комерційна діяльність", "ТКД", "01", tdosPhysV_B, null, false));
            //Туризм
            Speciality tdosOOGTK_JS = es.findUniqueOrPersist(new Speciality("Організація обслуговування в готелях та туристичних комплексах", "ООГТК", "03", tdosPhysV_B, null, false));
            //Прикладна математика
            Speciality tdosPM_JS = es.findUniqueOrPersist(new Speciality("Прикладна математика", "ПМ", "02", tdosPhysV_B, null, false));
            //Комп'ютерні науки
            Speciality tdosPECTAS_JS = es.findUniqueOrPersist(new Speciality("Програмування для електронно-обчислювальної техніки і автоматизованих систем", "ПЗАС", "05", tdosPhysV_B, null, false));


            //---Серия, номер и дата выдачи лицензии----------
            License licenseCPU = es.findUniqueOrPersist(new License("АВ", "529699", createCalendar(5, 11, 2010)));

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

            //Старая лицензия
            //Подготовка бакалавров, специалистов, магистров
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPVSH_M, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEICPHS, bsmtrain_LRG));
            //Адміністративний менеджмент - магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosAM_M, master_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafAMBA, bsmtrain_LRG));
            //Управління навчальним закладом - магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosUNZ_M, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEICPHS, bsmtrain_LRG));
            //Прикладна економіка - магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPE_M, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20120701, kafETNPE, bsmtrain_LRG));
            //Бізнес-адміністрування - магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosBA_M, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafAMBA, bsmtrain_LRG));

            //Фізичне виховання і спорт
            //Фізичне виховання і спорт - бакалаври
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPVS_B, bach_EQL, stat_EF, 110, corr_EF, 110, lrCal20150701, kafTOFAV, bsmtrain_LRG));
            //Фізичне виховання - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPhysV_SM, spec_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafTOFAV, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPhysV_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafTOFAV, bsmtrain_LRG));
            //Фізична реабілітація - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPhysR_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafFR, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPhysR_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafFR, bsmtrain_LRG));

            //Журналістика
            //Журналістика - бакалаври, спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosZhurn_B, bach_EQL, stat_EF, 110, corr_EF, 100, lrCal20150701, kafZSK, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosZhurn_SM, spec_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafZSK, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosZhurn_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafZSK, bsmtrain_LRG));

            //Видавнича справа та редагування - спеціалісти
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosVSR_S, spec_EQL, stat_EF, 50, corr_EF, 50, lrCal20110701, kafVSRUF, bsmtrain_LRG));

            //Міжнародні відносини
            //Міжнародне право - бакалаври, спеціалісти
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMP_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafMP, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMP_S, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafMP, bsmtrain_LRG));

            //Філологія
            //Філологія - бакалаври
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPhyl_B, bach_EQL, stat_EF, 80, corr_EF, 55, lrCal20150701, kafAFZL, bsmtrain_LRG));
            //Мова та література(англійська) - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosLangLit_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafAFZL, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosLangLit_SM, master_EQL, stat_EF, 12, corr_EF, 12, lrCal20150701, kafAFZL, bsmtrain_LRG));
            //Переклад - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosTransl_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafTPP, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosTransl_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafTPP, bsmtrain_LRG));
            //Літературна творчість - бакалаври
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosLT_B, bach_EQL, stat_EF, 25, corr_EF, 0, lrCal20100701, kafAFZL, bsmtrain_LRG));

            //Психологія
            //Психологія - бакалаври, спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPS_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPP, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPS_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPP, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPS_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPP, bsmtrain_LRG));

            //Соціологія
            //Соціальна робота - бакалаври, спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSR_B, bach_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafSSR, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSR_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20120701, kafSSR, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSR_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSSR, bsmtrain_LRG));

            //Економіка і підприємництво
            //Економіка і підприємництво - бакалаври
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEIP_B, bach_EQL, stat_EF, 430, corr_EF, 460, lrCal20150701, kafEP, bsmtrain_LRG));
            //Економічна кібернетика - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEKib_SM, spec_EQL, stat_EF, 20, corr_EF, 20, lrCal20150701, kafEKS, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEKib_SM, master_EQL, stat_EF, 25, corr_EF, 15, lrCal20150701, kafEKS, bsmtrain_LRG));
            //Міжнародна економіка - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosME_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafME, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosME_SM, master_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafME, bsmtrain_LRG));
            //Фінанси
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosF_SM, spec_EQL, stat_EF, 30, corr_EF, 60, lrCal20150701, kafFK, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosF_SM, master_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafFK, bsmtrain_LRG));
            //Облік і аудит - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosOA_SM, spec_EQL, stat_EF, 100, corr_EF, 100, lrCal20150701, kafOA, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosOA_SM, master_EQL, stat_EF, 30, corr_EF, 80, lrCal20130701, kafOA, bsmtrain_LRG));
            //Банківська справа - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosBS_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafFK, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosBS_SM, master_EQL, stat_EF, 20, corr_EF, 20, lrCal20150701, kafFK, bsmtrain_LRG));
            //Економіка підприємства - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEP_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEP_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, bsmtrain_LRG));
            //Маркетинг - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosM_SM, spec_EQL, stat_EF, 30, corr_EF, 20, lrCal20150701, kafM, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosM_SM, master_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafM, bsmtrain_LRG));
            //Прикладна статистика - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosES_SM, spec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafEKS, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosES_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafEKS, bsmtrain_LRG));

            //Менеджмент
            //Менеджмент - бакалаври
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMN_B, bach_EQL, stat_EF, 140, corr_EF, 250, lrCal20150701, kafMO, bsmtrain_LRG));
            //Менеджмент організацій - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMO_SM, spec_EQL, stat_EF, 90, corr_EF, 150, lrCal20150701, kafMO, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMO_SM, master_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafMO, bsmtrain_LRG));
            //Менеджмент зовнішньо-економічної діяльності - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMZED_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafMZD, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMZED_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafMZD, bsmtrain_LRG));

            //Туризм
            //Туризм - бакалаври, спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosTR_B, bach_EQL, stat_EF, 90, corr_EF, 90, lrCal20130701, kafTGG, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosTR_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafTGG, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosTR_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafTGG, bsmtrain_LRG));
            //Готельне господарство - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosGGos_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20130701, kafTGG, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosGGos_SM, master_EQL, stat_EF, 15, corr_EF, 15, lrCal20130701, kafTGG, bsmtrain_LRG));

            //Право
            //Право - бакалаври
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZ_B, bach_EQL, stat_EF, 150, corr_EF, 250, lrCal20150701, kafTZGP, bsmtrain_LRG));
            //Правознавство - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZ_SM, spec_EQL, stat_EF, 115, corr_EF, 210, lrCal20120701, kafTZGP, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZ_SM, master_EQL, stat_EF, 40, corr_EF, 60, lrCal20150701, kafTZGP, bsmtrain_LRG));

            //Геодезія, картографія та землевпорядкування
            //Землевпорядкування та кадастр - бакалаври
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosZK_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafDUZK, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosZK_B, spec_EQL, stat_EF, 30, corr_EF, 0, lrCal20150701, kafDUZK, bsmtrain_LRG));

            //Прикладна математика
            //Прикладна математика - бакалаври
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPM_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSAVM, bsmtrain_LRG));
            //Системний аналіз і управління - спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSAU_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafSAVM, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSAU_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafSAVM, bsmtrain_LRG));
            //Інформатика - бакалаври
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosI_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20110701, kafSAVM, bsmtrain_LRG));

            //Комп'ютерні науки
            //Програмне забезпечення автоматизованих систем - бакалаври, спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZAS_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafPIT, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZAS_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPIT, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZAS_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafPIT, bsmtrain_LRG));

            //Електроніка
            //Фізична та біомедична електроніка - бакалаври, спеціалісти, магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosFBE_B, bach_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafSAVM, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosFBE_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafSAVM, bsmtrain_LRG));
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosFBE_SM, master_EQL, stat_EF, 10, corr_EF, 0, lrCal20150701, kafSAVM, bsmtrain_LRG));

            //Державне управління
            //Державна служба - магістри
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosDS_M, master_EQL, stat_EF, 60, corr_EF, 90, lrCal20120701, kafDUZK, bsmtrain_LRG));


            //Перепідготовка спеціалістів
            //Фізичне виховання і спорт
            //Фізичне виховання  - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPhysV_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafTOFAV, sretrain_LRG));
//            //Фізична реабілітація  - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPhysR_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafFR, sretrain_LRG));
//
//            //Журналістика
//            //Журналістика - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosZhurn_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafZSK, sretrain_LRG));
//
//            //Філологія
//            //Мова та література (англійська) - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosLangLit_SM, spec_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafAFZL, sretrain_LRG));
//            //Переклад - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosTransl_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafTPP, sretrain_LRG));
//
//            //Психологія
//            //Психологія - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPS_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafPP, sretrain_LRG));
//
//            //Соціологія
//            //Соціальна робота - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSR_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20120701, kafSSR, sretrain_LRG));
//
//            //Економіка і підприємництво
//            //Міжнародна економіка - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosME_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafME, sretrain_LRG));
//            //Фінанси - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosF_SM, spec_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafFK, sretrain_LRG));
//            //Облік і аудит - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosOA_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafOA, sretrain_LRG));
//            //Банківська справа - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosBS_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafFK, sretrain_LRG));
//            //Економіка підприємства - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEP_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafEP, sretrain_LRG));
//
//            //Менеджмент
//            //Менеджмент організацій - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMO_SM, spec_EQL, stat_EF, 40, corr_EF, 40, lrCal20150701, kafMO, sretrain_LRG));
//
//            //Комп'ютерні науки
//            //Програмне забезпечення автоматизованих систем
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPZAS_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafPIT, sretrain_LRG));
//
//
//            //Для коледжу КПУ
//            //Журналістика
//            //Видавнича справа та редагування - молодші спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosVSR_S, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafVSRUF, forcol_LRG));
//
//            //Соціологія
//            //Соціальна робота - спеціалісти
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSR_SM, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafSSR, forcol_LRG));
//
//            //Економіка і підприємництво
//            //Економіка підприємства
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEP_SM, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, forcol_LRG));
//
//            //Менеджмент
//            //Організація обслуговування населення
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosOON_JS, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafMO, forcol_LRG));
//
//            //Торгівля
//            //Товарознавство та комерційна діяльність
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosTKD_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafMO, forcol_LRG));
//
//            //Туризм
//            //Організація обслуговування в готелях та туристичних комплексах
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosOOGTK_JS, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafTGG, forcol_LRG));
//
//            //Прикладна математика
//            //Прикладна математика
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPM_JS, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20110701, kafSAVM, forcol_LRG));
//
//            //Комп'ютерні науки
//            //Програмування для електроно-обчислювальної техніки і автоматизованих систем
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPECTAS_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPIT, forcol_LRG));
//
//            //Новая лицензия
//            //Подготовка бакалавров
//            //Фізичне виховання, спорт і здоров'я людини
//            //Фізичне виховання
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPhysV_B, bach_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafTOFAV, btrain_LRG));
//            //Здоров'я людини
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosZL_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafFR, btrain_LRG));
//
//            //Мистецтво
//            //Дизайн
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosDZ_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20140701, kafTGG, btrain_LRG));
//
//            //Гуманітарні науки
//            //Філологія
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPhilol_B, bach_EQL, stat_EF, 105, corr_EF, 55, lrCal20150701, kafAFZL, btrain_LRG));
//
//            //Соціально-політичні науки
//            //Соціологія
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosS_B, bach_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafSSR, btrain_LRG));
//            //Психологія
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPSH_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPP, btrain_LRG));
//
//            //Журналістика та інформація
//            //Журналістика
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosZhu_B, bach_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafZSK, btrain_LRG));
//            //Реклама і зв'язки з громадкістю (за видами)
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosRZG_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20110701, kafRZG, btrain_LRG));
//            //Видавнича справа та редагування
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosVSR_B, bach_EQL, stat_EF, 30, corr_EF, 50, lrCal20110701, kafVSRUF, btrain_LRG));
//
//            //Право
//            //Право
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosP_B, bach_EQL, stat_EF, 150, corr_EF, 250, lrCal20150701, kafTZGP, btrain_LRG));
//
//            //Економіка і підприємництво
//            //Економічна кібернетика
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEKib_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEKS, btrain_LRG));
//            //Міжнародна економіка
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosME_B, bach_EQL, stat_EF, 50, corr_EF, 30, lrCal20150701, kafME, btrain_LRG));
//            //Економіка підприємства
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEP_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafEP, btrain_LRG));
//            //Прикладна статистика
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPRS_B, bach_EQL, stat_EF, 20, corr_EF, 20, lrCal20150701, kafEKS, btrain_LRG));
//            //Маркетинг
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosM_B, bach_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafM, btrain_LRG));
//            //Фінанси і кредит
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosFK_B, bach_EQL, stat_EF, 120, corr_EF, 120, lrCal20150701, kafFK, btrain_LRG));
//            //Облік і аудит
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosOA_B, bach_EQL, stat_EF, 100, corr_EF, 150, lrCal20150701, kafOA, btrain_LRG));
//
//            //Менеджмент і адміністрування
//            //Менеджмент
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMG_B, bach_EQL, stat_EF, 140, corr_EF, 250, lrCal20150701, kafMO, btrain_LRG));
//
//            //Системні науки та кібернетика
//            //Інформатика
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosIF_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20110701, kafSAVM, btrain_LRG));
//            //Системний аналіз
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSA_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSAVM, btrain_LRG));
//
//            //Інформатика та обчислювальна техніка
//            //Програмна інженерія
            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPI_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafPIT, btrain_LRG));
//
//            //Електроніка
//            //Мікро- та наноелектроніка
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosMNE_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSAVM, btrain_LRG));
//
//            //Геодезія та землеустрій
//            //Геодезія, картографія та землеустрій
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosGKZ_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20150701, kafDUZK, btrain_LRG));
//
//            //Соціальне забезпечення
//            //Соціальна робота
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSR_B, bach_EQL, stat_EF, 40, corr_EF, 40, lrCal20150701, kafSSR, forcol_LRG));
//
//            //Сфера обслуговування
//            //Готельно-ресторанна справа
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosGRS_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafTGG, forcol_LRG));
//            //Туризм
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosT_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20130701, kafTGG, btrain_LRG));
//
//
//            //Подготовка младших специалистов
//            //Журналістика та інформація
//            //Видавнича справа та редагування
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosVSTR_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafVSRUF, forcol_LRG));
//
//            //Економіка та підприємництво
//            //Економіка підприємства
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosEKP_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, forcol_LRG));
//            //Товарознавство та комерційна діяльність
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosTZKD_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, forcol_LRG));
//
//            //Системні науки та кібернетика
//            //Прикладна математика
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosPRM_JS, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20110701, kafSAVM, forcol_LRG));
//
//            //Інформатика та обчислювальна техника
//            //Розробка програмного забезпечення
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosRPZ_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPIT, forcol_LRG));
//
//            //Соціальне забезпечення
//            //Соціальна робота
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosSOCR_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSSR, forcol_LRG));
//
//            //Сфера обслуговування
//            //Організація обслуговування населення
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosOONAS_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSSR, forcol_LRG));
//            es.findUniqueOrPersist(new LicenseRecord(licenseCPU, tdosTO_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSSR, forcol_LRG));
        }
    }

}
