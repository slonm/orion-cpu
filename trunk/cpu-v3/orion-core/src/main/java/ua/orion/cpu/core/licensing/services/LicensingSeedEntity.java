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
            KnowledgeAreaOrTrainingDirection kaotdSpecCateg = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Специфічні категорії", null, "0000", false, false));
            KnowledgeAreaOrTrainingDirection kaotdPhysTrainSport = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Фізичне виховання і спорт", null, "0102", false, false));
            KnowledgeAreaOrTrainingDirection kaotdZhurn = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Журналістика", null, "0302", false, false));
            KnowledgeAreaOrTrainingDirection kaotdInternRel = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Міжнародні відносини", null, "0304", false, false));
            KnowledgeAreaOrTrainingDirection kaotdPhilol = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Філологія", null, "0305", false, false));
            KnowledgeAreaOrTrainingDirection kaotdPs = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Психологія", null, "0401", false, false));
            KnowledgeAreaOrTrainingDirection kaotdSocial = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Соціологія", null, "0402", false, false));
            KnowledgeAreaOrTrainingDirection kaotdEkonBus = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Економіка і підприємництво", null, "0501", false, false));
            KnowledgeAreaOrTrainingDirection kaotdManeg = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Менеджмент", null, "0502", false, false));
            KnowledgeAreaOrTrainingDirection kaotdTur = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Туризм", null, "0504", false, false));
            KnowledgeAreaOrTrainingDirection kaotdRight = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Право", null, "0601", false, false));
            KnowledgeAreaOrTrainingDirection kaotdGeodMapZem = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Геодезія, картографія та землевпорядкування", null, "0709", false, false));
            KnowledgeAreaOrTrainingDirection kaotdAppMath = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Прикладна математика", null, "0802", false, false));
            KnowledgeAreaOrTrainingDirection kaotdCompSci = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Комп'ютерні науки", null, "0804", false, false));
            KnowledgeAreaOrTrainingDirection kaotdElect = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Електроніка", null, "0908", false, false));
            KnowledgeAreaOrTrainingDirection kaotdGover = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Державне управління", null, "1501", false, false));

            //Переподготовка специалистов (Перелік 1997р - напрями навчання) - додатковх до основних напрямів немає

            //Для коледжу КПУ (Перелік 1997г - напрями навчання) - додаткові до основних напрями
            KnowledgeAreaOrTrainingDirection kaotdTrade = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Торгівля", null, "0503", false, false));

            //Подготовка бакалавров (Перелік 2006р - галузі знань)
            KnowledgeAreaOrTrainingDirection kaotdPhysTrainSportHealth = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Фізичне виховання, спорт і здоров'я людини", null, "0102", true, false));
            KnowledgeAreaOrTrainingDirection kaotdCult = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Культура", null, "0201", true, false));
            KnowledgeAreaOrTrainingDirection kaotdHumanSci = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Гуманітарні науки", null, "0203", true, false));
            KnowledgeAreaOrTrainingDirection kaotdSocPolSci = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Соціально-політичні науки", null, "0301", true, false));
            KnowledgeAreaOrTrainingDirection kaotdIntRelat = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Міжнародні відносини", null, "0302", true, false));
            KnowledgeAreaOrTrainingDirection kaotdZhurnInf = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Журналістика та інформація", null, "0303", true, false));
            KnowledgeAreaOrTrainingDirection kaotdLaw = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Право", null, "0304", true, false));
            KnowledgeAreaOrTrainingDirection kaotdEconEnterprise = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Економіка та підприємництво", null, "0305", true, false));
            KnowledgeAreaOrTrainingDirection kaotdManegAdmin = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Менеджмент та адміністрування", null, "0306", true, false));
            KnowledgeAreaOrTrainingDirection kaotdSysSciCyber = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Системні науки та кібернетика", null, "0403", true, false));
            KnowledgeAreaOrTrainingDirection kaotdInfComp = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Інформатика та обчислювальна техніка", null, "0501", true, false));
            KnowledgeAreaOrTrainingDirection kaotdElectron = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Електроніка", null, "0508", true, false));
            KnowledgeAreaOrTrainingDirection kaotdGeodLandReg = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Геодезія та землеустрій", null, "0801", true, false));

            //Подготовка младших специалистов (Перелік 2006р - галузі знань) - додаткові до основних напрями
            KnowledgeAreaOrTrainingDirection kaotdSocServ = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Соціальне забезпечення", null, "1301", true, false));
            KnowledgeAreaOrTrainingDirection kaotdServSect = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Сфера обслуговування", null, "1401", true, false));
            KnowledgeAreaOrTrainingDirection kaotdMst = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Мистецтво", null, "0202", true, false));

            //---Направления подготовки или специальности суффиксы _JS, _B, _S, _M обозначают квалификационные уровни
            //младшего специалиста, бакалавра, специалиста/магистра, соответственно
            //Допускается комбинация суффиксов при одинаковых именах и кодах записей (или кратких именах и кодах),
            //например, часто совпадают записи для специалистов и магистров, в этом случае суффикс _SM
            //(образовательно-квалификационный уровень добавляется уже для записей лицензий)

            //Підготовка бакалаврів, спеціалістів, магістрів (Перелік 1997р - спеціальності)
            //Специфічні категорії
            TrainingDirectionOrSpeciality tdosPVSH_M = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Педагогіка вищої школи", "ПВШ", "05", false, kaotdSpecCateg, false));
            TrainingDirectionOrSpeciality tdosAM_M = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Адміністративний менеджмент", "АМ", "07", false, kaotdSpecCateg, false));
            TrainingDirectionOrSpeciality tdosUNZ_M = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Управління навчальним закладом", "УНЗ", "09", false, kaotdSpecCateg, false));
            TrainingDirectionOrSpeciality tdosPE_M = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Прикладна економіка", "ПЕ", "11", false, kaotdSpecCateg, false));
            TrainingDirectionOrSpeciality tdosBA_M = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Бізнес-адміністрування", "БА", "13", false, kaotdSpecCateg, false));
            //Фізичне виховання і спорт
            TrainingDirectionOrSpeciality tdosPVS_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Фізичне виховання і спорт", "ФВС", "00", false, kaotdPhysTrainSport, false));
            TrainingDirectionOrSpeciality tdosPhysV_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Фізичне виховання", "ФВ", "01", false, kaotdPhysTrainSport, false));
            TrainingDirectionOrSpeciality tdosPhysR_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Фізична реабілітація", "ФР", "02", false, kaotdPhysTrainSport, false));
            //Журналістика
            TrainingDirectionOrSpeciality tdosZhurn_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Журналістика", "Ж", "00", false, kaotdZhurn, false));
            TrainingDirectionOrSpeciality tdosZhurn_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Журналістика", "Ж", "01", false, kaotdZhurn, false));
            TrainingDirectionOrSpeciality tdosVSR_S = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Видавнича справа та редагування", "ВСР", "03", false, kaotdZhurn, false));
            //Міжнародні відносини
            TrainingDirectionOrSpeciality tdosMP_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Міжнародні відносини", "МВ", "00", false, kaotdInternRel, false));
            TrainingDirectionOrSpeciality tdosMP_S = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Міжнародне право", "МП", "02", false, kaotdInternRel, false));
            //Філологія
            TrainingDirectionOrSpeciality tdosPhyl_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Філологія", "Ф", "00", false, kaotdPhilol, false));
            TrainingDirectionOrSpeciality tdosLangLit_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Мова та література (англійська)", "МЛ", "02", false, kaotdPhilol, false));
            TrainingDirectionOrSpeciality tdosTransl_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Переклад", "ПР", "07", false, kaotdPhilol, false));
            TrainingDirectionOrSpeciality tdosLT_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Літературна творчість", "ЛТ", "00", false, kaotdPhilol, false));
            //Психологія
            TrainingDirectionOrSpeciality tdosPS_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Психологія", "ПС", "00", false, kaotdPs, false));
            TrainingDirectionOrSpeciality tdosPS_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Психологія", "ПС", "01", false, kaotdPs, false));
            //Соціологія
            TrainingDirectionOrSpeciality tdosSR_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Соціологія", "СР", "00", false, kaotdSocial, false));
            TrainingDirectionOrSpeciality tdosSR_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Соціальна робота", "СР", "02", false, kaotdSocial, false));
            //Економіка і підприємництво
            TrainingDirectionOrSpeciality tdosEIP_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Економіка і підприємництво", "ЕІП", "00", false, kaotdEkonBus, false));
            TrainingDirectionOrSpeciality tdosEKib_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Економічна кібернетика", "ЕК", "02", false, kaotdEkonBus, false));
            TrainingDirectionOrSpeciality tdosME_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Міжнародна економіка", "МЕ", "03", false, kaotdEkonBus, false));
            TrainingDirectionOrSpeciality tdosF_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Фінанси", "Ф", "04", false, kaotdEkonBus, false));
            TrainingDirectionOrSpeciality tdosOA_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Облік і аудит", "ОА", "06", false, kaotdEkonBus, false));
            TrainingDirectionOrSpeciality tdosBS_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Банківська справа", "БС", "05", false, kaotdEkonBus, false));
            TrainingDirectionOrSpeciality tdosEP_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Економіка підприємства", "ЕП", "07", false, kaotdEkonBus, false));
            TrainingDirectionOrSpeciality tdosM_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Маркетинг", "М", "08", false, kaotdEkonBus, false));
            TrainingDirectionOrSpeciality tdosES_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Економічна статистика", "ЕС", "10", false, kaotdEkonBus, false));
            //Менеджмент
            TrainingDirectionOrSpeciality tdosMN_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Менеджмент", "М", "00", false, kaotdManeg, false));
            TrainingDirectionOrSpeciality tdosMO_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Менеджмент організації", "МО", "01", false, kaotdManeg, false));
            TrainingDirectionOrSpeciality tdosMZED_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Менеджмент зовнішньо-економічної діяльності", "МЗЕД", "06", false, kaotdManeg, false));
            //Туризм
            TrainingDirectionOrSpeciality tdosTR_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Туризм", "Т", "00", false, kaotdTur, false));
            TrainingDirectionOrSpeciality tdosTR_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Туризм", "Т", "01", false, kaotdTur, false));
            TrainingDirectionOrSpeciality tdosGGos_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Готельне господарство", "ГГ", "02", false, kaotdTur, false));
            //Право
            TrainingDirectionOrSpeciality tdosPZ_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Право", "П", "00", false, kaotdRight, false));
            TrainingDirectionOrSpeciality tdosPZ_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Правознавство", "П", "01", false, kaotdRight, false));
            //Геодезія, картографія та землевпорядкування
            TrainingDirectionOrSpeciality tdosZK_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Землевпорядаткування та кадастр", "ЗК", "00", false, kaotdGeodMapZem, false));
            //Прикладна математика
            TrainingDirectionOrSpeciality tdosPM_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Прикладна математика", "ПМ", "00", false, kaotdAppMath, false));
            TrainingDirectionOrSpeciality tdosSAU_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Системний аналіз і управління", "САУ", "03", false, kaotdAppMath, false));
            TrainingDirectionOrSpeciality tdosI_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Інформатика", "Інф", "00", false, kaotdAppMath, false));
            //Комп'ютерні науки
            TrainingDirectionOrSpeciality tdosPZAS_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Програмне забезпечення автоматизованих систем", "ПЗАС", "00", false, kaotdCompSci, false));
            TrainingDirectionOrSpeciality tdosPZAS_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Програмне забезпечення автоматизованих систем", "ПЗАС", "03", false, kaotdCompSci, false));
            //Електроніка
            TrainingDirectionOrSpeciality tdosFBE_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Фізична та біомедична електроніка", "ФБЕ", "00", false, kaotdElect, false));
            TrainingDirectionOrSpeciality tdosFBE_SM = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Фізична та біомедична електроніка", "ФБЕ", "04", false, kaotdElect, false));
            //Державне управління
            TrainingDirectionOrSpeciality tdosDS_M = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Державна служба", "ДС", "01", false, kaotdGover, false));

            //Перепідготовка спеціалістів (Перелік 1997р - спеціальності) - додаткових до основних спеціальностей немає

            //Для коледжу КПУ (Перелік 1997р - спеціальності) - додаткові до основних спеціальності
            //Менеджмент
            TrainingDirectionOrSpeciality tdosOON_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Організація обслуговування населення", "ООН", "03", false, kaotdManeg, false));
            //Торгівля
            TrainingDirectionOrSpeciality tdosTKD_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Товарознавство та комерційна діяльність", "ТКД", "01", false, kaotdTrade, false));
            //Туризм
            TrainingDirectionOrSpeciality tdosOOGTK_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Організація обслуговування в готелях та туристичних комплексах", "ООГТК", "03", false, kaotdTur, false));
            //Прикладна математика
            TrainingDirectionOrSpeciality tdosPM_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Прикладна математика", "ПМ", "02", false, kaotdAppMath, false));
            //Комп'ютерні науки
            TrainingDirectionOrSpeciality tdosPECTAS_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Програмування для електронно-обчислювальної техніки і автоматизованих систем", "ПЗАС", "05", false, kaotdCompSci, false));

            //Подготовка бакалавров (Перелік 2006р - напрями навчання)
            //Фізичне виховання, спорт і здоров'я людини
            TrainingDirectionOrSpeciality tdosPhysV_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Фізичне виховання", "ФВ", "01", true, kaotdPhysTrainSportHealth, false));
            TrainingDirectionOrSpeciality tdosZL_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Здоров'я людини", "ЗЛ", "03", true, kaotdPhysTrainSportHealth, false));
            //Культура
            TrainingDirectionOrSpeciality tdosT_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Туризм", "Т", "07", true, kaotdCult, false));
            //Гуманітарні науки
            TrainingDirectionOrSpeciality tdosPhilol_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Філологія", "ФЛ", "03", true, kaotdHumanSci, false));
            //Соціально-політичні науки
            TrainingDirectionOrSpeciality tdosS_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Соціологія", "С", "01", true, kaotdSocPolSci, false));
            TrainingDirectionOrSpeciality tdosPSH_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Психологія", "П", "02", true, kaotdSocPolSci, false));
            //Журналістика та інформація
            TrainingDirectionOrSpeciality tdosZhu_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Журналістика", "Ж", "01", true, kaotdZhurnInf, false));
            TrainingDirectionOrSpeciality tdosRZG_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Реклама і зв'язки з громадкістю (за видами)", "РЗГ", "02", true, kaotdZhurnInf, false));
            TrainingDirectionOrSpeciality tdosVSR_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Видавнича справа та редагування", "ВСР", "03", true, kaotdZhurnInf, false));
            //Право
            TrainingDirectionOrSpeciality tdosP_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Право", "П", "01", true, kaotdLaw, false));
            //Економіка і підприємництво
            TrainingDirectionOrSpeciality tdosEKib_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Економічна кібернетика", "ЕК", "02", true, kaotdEconEnterprise, false));
            TrainingDirectionOrSpeciality tdosME_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Міжнародна економіка", "МЕ", "03", true, kaotdEconEnterprise, false));
            TrainingDirectionOrSpeciality tdosEP_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Економіка підприємства", "ЕП", "04", true, kaotdEconEnterprise, false));
            TrainingDirectionOrSpeciality tdosPRS_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Прикладна статистика", "ПС", "06", true, kaotdEconEnterprise, false));
            TrainingDirectionOrSpeciality tdosM_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Маркетинг", "М", "07", true, kaotdEconEnterprise, false));
            TrainingDirectionOrSpeciality tdosFK_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Фінанси і кредит", "ФК", "08", true, kaotdEconEnterprise, false));
            TrainingDirectionOrSpeciality tdosOA_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Облік і аудит", "ОА", "09", true, kaotdEconEnterprise, false));
            //Менеджмент та адміністрування
            TrainingDirectionOrSpeciality tdosMG_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Менеджмент", "МА", "01", true, kaotdManegAdmin, false));
            //Системні науки та кібернетика
            TrainingDirectionOrSpeciality tdosIF_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Інформатика", "І", "02", true, kaotdSysSciCyber, false));
            TrainingDirectionOrSpeciality tdosSA_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Системний аналіз", "СА", "03", true, kaotdSysSciCyber, false));
            //Інформатика та обчислювальна техника
            TrainingDirectionOrSpeciality tdosPI_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Програмна інженерія", "ПІ", "03", true, kaotdInfComp, false));
            //Електроніка
            TrainingDirectionOrSpeciality tdosMNE_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Мікро- та наноелектроніка", "МНЕ", "01", true, kaotdElectron, false));
            //Геодезія та землеустрій
            TrainingDirectionOrSpeciality tdosGKZ_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Геодезія, картографія та землеустрій", "ГКЗ", "01", true, kaotdGeodLandReg, false));
            //Мистецтво (NEW)
            TrainingDirectionOrSpeciality tdosDZ_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Дизайн", "ДЗ", "07", true, kaotdMst, false));
            //Сфера обслуговування (NEW)
            TrainingDirectionOrSpeciality tdosGRS_B = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Готельно ресторанна справа", "ГРС", "01", true, kaotdServSect, false));

            //Подготовка младших специалистов (Перелік 2006р - спеціальності)
            //Культура
            TrainingDirectionOrSpeciality tdosOTO_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Організація туристичного обслуговування", "ОТО", "0701", false, kaotdCult, false));
            //Журналістика та інформація
            TrainingDirectionOrSpeciality tdosVSTR_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Видавнича справа та редагування", "ВСР", "0301", false, kaotdZhurnInf, false));
            //Економіка та підприємництво
            TrainingDirectionOrSpeciality tdosEKP_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Економіка підприємства", "ЕП", "0401", false, kaotdEconEnterprise, false));
            TrainingDirectionOrSpeciality tdosTZKD_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Товарознавство та комерційна діяльність", "ТЗКД", "1001", false, kaotdEconEnterprise, false));
            //Системні науки та кібернетика
            TrainingDirectionOrSpeciality tdosPRM_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Прикладна математика", "ПМ", "0101", false, kaotdSysSciCyber, false));
            //Інформатика та обчислювальна техника
            TrainingDirectionOrSpeciality tdosRPZ_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Розробка програмного забезпечення", "РПЗ", "0301", false, kaotdInfComp, false));
            //Соціальне забезпечення
            TrainingDirectionOrSpeciality tdosSOCR_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Соціальна робота", "СР", "0101", false, kaotdSocServ, false));
            //Сфера обслуговування
            TrainingDirectionOrSpeciality tdosOONAS_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Організація обслуговування населення", "ООН", "0202", false, kaotdServSect, false));
            TrainingDirectionOrSpeciality tdosTO_JS = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Туристичне обслуговування", "ТО", "0301", false, kaotdServSect, false));


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
            saveOrUpdateLR(licenseCPU, tdosPVSH_M, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEICPHS, bsmtrain_LRG, es);
            //Адміністративний менеджмент - магістри
            saveOrUpdateLR(licenseCPU, tdosAM_M, master_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafAMBA, bsmtrain_LRG, es);
            //Управління навчальним закладом - магістри
            saveOrUpdateLR(licenseCPU, tdosUNZ_M, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEICPHS, bsmtrain_LRG, es);
            //Прикладна економіка - магістри
            saveOrUpdateLR(licenseCPU, tdosPE_M, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20120701, kafETNPE, bsmtrain_LRG, es);
            //Бізнес-адміністрування - магістри
            saveOrUpdateLR(licenseCPU, tdosBA_M, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafAMBA, bsmtrain_LRG, es);

            //Фізичне виховання і спорт
            //Фізичне виховання і спорт - бакалаври
            saveOrUpdateLR(licenseCPU, tdosPVS_B, bach_EQL, stat_EF, 110, corr_EF, 110, lrCal20150701, kafTOFAV, bsmtrain_LRG, es);
            //Фізичне виховання - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPhysV_SM, spec_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafTOFAV, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosPhysV_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafTOFAV, bsmtrain_LRG, es);
            //Фізична реабілітація - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPhysR_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafFR, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosPhysR_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafFR, bsmtrain_LRG, es);

            //Журналістика
            //Журналістика - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosZhurn_B, bach_EQL, stat_EF, 110, corr_EF, 100, lrCal20150701, kafZSK, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosZhurn_SM, spec_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafZSK, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosZhurn_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafZSK, bsmtrain_LRG, es);

            //Видавнича справа та редагування - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosVSR_S, spec_EQL, stat_EF, 50, corr_EF, 50, lrCal20110701, kafVSRUF, bsmtrain_LRG, es);

            //Міжнародні відносини
            //Міжнародне право - бакалаври, спеціалісти
            saveOrUpdateLR(licenseCPU, tdosMP_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafMP, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosMP_S, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafMP, bsmtrain_LRG, es);

            //Філологія
            //Філологія - бакалаври
            saveOrUpdateLR(licenseCPU, tdosPhyl_B, bach_EQL, stat_EF, 80, corr_EF, 55, lrCal20150701, kafAFZL, bsmtrain_LRG, es);
            //Мова та література(англійська) - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosLangLit_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafAFZL, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosLangLit_SM, master_EQL, stat_EF, 12, corr_EF, 12, lrCal20150701, kafAFZL, bsmtrain_LRG, es);
            //Переклад - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosTransl_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafTPP, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosTransl_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafTPP, bsmtrain_LRG, es);
            //Літературна творчість - бакалаври
            saveOrUpdateLR(licenseCPU, tdosLT_B, bach_EQL, stat_EF, 25, corr_EF, 0, lrCal20100701, kafAFZL, bsmtrain_LRG, es);

            //Психологія
            //Психологія - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPS_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPP, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosPS_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPP, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosPS_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPP, bsmtrain_LRG, es);

            //Соціологія
            //Соціальна робота - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosSR_B, bach_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafSSR, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosSR_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20120701, kafSSR, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosSR_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSSR, bsmtrain_LRG, es);

            //Економіка і підприємництво
            //Економіка і підприємництво - бакалаври
            saveOrUpdateLR(licenseCPU, tdosEIP_B, bach_EQL, stat_EF, 430, corr_EF, 460, lrCal20150701, kafEP, bsmtrain_LRG, es);
            //Економічна кібернетика - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosEKib_SM, spec_EQL, stat_EF, 20, corr_EF, 20, lrCal20150701, kafEKS, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosEKib_SM, master_EQL, stat_EF, 25, corr_EF, 15, lrCal20150701, kafEKS, bsmtrain_LRG, es);
            //Міжнародна економіка - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosME_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafME, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosME_SM, master_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafME, bsmtrain_LRG, es);
            //Фінанси
            saveOrUpdateLR(licenseCPU, tdosF_SM, spec_EQL, stat_EF, 30, corr_EF, 60, lrCal20150701, kafFK, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosF_SM, master_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafFK, bsmtrain_LRG, es);
            //Облік і аудит - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosOA_SM, spec_EQL, stat_EF, 100, corr_EF, 100, lrCal20150701, kafOA, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosOA_SM, master_EQL, stat_EF, 30, corr_EF, 80, lrCal20130701, kafOA, bsmtrain_LRG, es);
            //Банківська справа - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosBS_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafFK, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosBS_SM, master_EQL, stat_EF, 20, corr_EF, 20, lrCal20150701, kafFK, bsmtrain_LRG, es);
            //Економіка підприємства - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosEP_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosEP_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, bsmtrain_LRG, es);
            //Маркетинг - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosM_SM, spec_EQL, stat_EF, 30, corr_EF, 20, lrCal20150701, kafM, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosM_SM, master_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafM, bsmtrain_LRG, es);
            //Прикладна статистика - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosES_SM, spec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafEKS, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosES_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafEKS, bsmtrain_LRG, es);

            //Менеджмент
            //Менеджмент - бакалаври
            saveOrUpdateLR(licenseCPU, tdosMN_B, bach_EQL, stat_EF, 140, corr_EF, 250, lrCal20150701, kafMO, bsmtrain_LRG, es);
            //Менеджмент організацій - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosMO_SM, spec_EQL, stat_EF, 90, corr_EF, 150, lrCal20150701, kafMO, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosMO_SM, master_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafMO, bsmtrain_LRG, es);
            //Менеджмент зовнішньо-економічної діяльності - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosMZED_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafMZD, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosMZED_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafMZD, bsmtrain_LRG, es);

            //Туризм
            //Туризм - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosTR_B, bach_EQL, stat_EF, 90, corr_EF, 90, lrCal20130701, kafTGG, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosTR_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafTGG, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosTR_SM, master_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafTGG, bsmtrain_LRG, es);
            //Готельне господарство - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosGGos_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20130701, kafTGG, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosGGos_SM, master_EQL, stat_EF, 15, corr_EF, 15, lrCal20130701, kafTGG, bsmtrain_LRG, es);

            //Право
            //Право - бакалаври
            saveOrUpdateLR(licenseCPU, tdosPZ_B, bach_EQL, stat_EF, 150, corr_EF, 250, lrCal20150701, kafTZGP, bsmtrain_LRG, es);
            //Правознавство - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPZ_SM, spec_EQL, stat_EF, 115, corr_EF, 210, lrCal20120701, kafTZGP, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosPZ_SM, master_EQL, stat_EF, 40, corr_EF, 60, lrCal20150701, kafTZGP, bsmtrain_LRG, es);

            //Геодезія, картографія та землевпорядкування
            //Землевпорядкування та кадастр - бакалаври
            saveOrUpdateLR(licenseCPU, tdosZK_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafDUZK, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosZK_B, spec_EQL, stat_EF, 30, corr_EF, 0, lrCal20150701, kafDUZK, bsmtrain_LRG, es);

            //Прикладна математика
            //Прикладна математика - бакалаври
            saveOrUpdateLR(licenseCPU, tdosPM_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSAVM, bsmtrain_LRG, es);
            //Системний аналіз і управління - спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosSAU_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafSAVM, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosSAU_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafSAVM, bsmtrain_LRG, es);
            //Інформатика - бакалаври
            saveOrUpdateLR(licenseCPU, tdosI_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20110701, kafSAVM, bsmtrain_LRG, es);

            //Комп'ютерні науки
            //Програмне забезпечення автоматизованих систем - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosPZAS_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafPIT, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosPZAS_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPIT, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosPZAS_SM, master_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafPIT, bsmtrain_LRG, es);

            //Електроніка
            //Фізична та біомедична електроніка - бакалаври, спеціалісти, магістри
            saveOrUpdateLR(licenseCPU, tdosFBE_B, bach_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafSAVM, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosFBE_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafSAVM, bsmtrain_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosFBE_SM, master_EQL, stat_EF, 10, corr_EF, 0, lrCal20150701, kafSAVM, bsmtrain_LRG, es);

            //Державне управління
            //Державна служба - магістри
            saveOrUpdateLR(licenseCPU, tdosDS_M, master_EQL, stat_EF, 60, corr_EF, 90, lrCal20120701, kafDUZK, bsmtrain_LRG, es);


            //Перепідготовка спеціалістів
            //Фізичне виховання і спорт
            //Фізичне виховання  - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosPhysV_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafTOFAV, sretrain_LRG, es);
            //Фізична реабілітація  - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosPhysR_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafFR, sretrain_LRG, es);

            //Журналістика
            //Журналістика - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosZhurn_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafZSK, sretrain_LRG, es);

            //Філологія
            //Мова та література (англійська) - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosLangLit_SM, spec_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafAFZL, sretrain_LRG, es);
            //Переклад - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosTransl_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafTPP, sretrain_LRG, es);

            //Психологія
            //Психологія - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosPS_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafPP, sretrain_LRG, es);

            //Соціологія
            //Соціальна робота - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosSR_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20120701, kafSSR, sretrain_LRG, es);

            //Економіка і підприємництво
            //Міжнародна економіка - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosME_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafME, sretrain_LRG, es);
            //Фінанси - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosF_SM, spec_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafFK, sretrain_LRG, es);
            //Облік і аудит - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosOA_SM, spec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafOA, sretrain_LRG, es);
            //Банківська справа - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosBS_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafFK, sretrain_LRG, es);
            //Економіка підприємства - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosEP_SM, spec_EQL, stat_EF, 25, corr_EF, 25, lrCal20150701, kafEP, sretrain_LRG, es);

            //Менеджмент
            //Менеджмент організацій - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosMO_SM, spec_EQL, stat_EF, 40, corr_EF, 40, lrCal20150701, kafMO, sretrain_LRG, es);

            //Комп'ютерні науки
            //Програмне забезпечення автоматизованих систем
            saveOrUpdateLR(licenseCPU, tdosPZAS_SM, spec_EQL, stat_EF, 15, corr_EF, 15, lrCal20150701, kafPIT, sretrain_LRG, es);


            //Для коледжу КПУ
            //Журналістика
            //Видавнича справа та редагування - молодші спеціалісти
            saveOrUpdateLR(licenseCPU, tdosVSR_S, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafVSRUF, forcol_LRG, es);

            //Соціологія
            //Соціальна робота - спеціалісти
            saveOrUpdateLR(licenseCPU, tdosSR_SM, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafSSR, forcol_LRG, es);

            //Економіка і підприємництво
            //Економіка підприємства
            saveOrUpdateLR(licenseCPU, tdosEP_SM, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, forcol_LRG, es);

            //Менеджмент
            //Організація обслуговування населення
            saveOrUpdateLR(licenseCPU, tdosOON_JS, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafMO, forcol_LRG, es);

            //Торгівля
            //Товарознавство та комерційна діяльність
            saveOrUpdateLR(licenseCPU, tdosTKD_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafMO, forcol_LRG, es);

            //Туризм
            //Організація обслуговування в готелях та туристичних комплексах
            saveOrUpdateLR(licenseCPU, tdosOOGTK_JS, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20150701, kafTGG, forcol_LRG, es);

            //Прикладна математика
            //Прикладна математика
            saveOrUpdateLR(licenseCPU, tdosPM_JS, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20110701, kafSAVM, forcol_LRG, es);

            //Комп'ютерні науки
            //Програмування для електроно-обчислювальної техніки і автоматизованих систем
            saveOrUpdateLR(licenseCPU, tdosPECTAS_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPIT, forcol_LRG, es);

            //Новая лицензия
            //Подготовка бакалавров
            //Фізичне виховання, спорт і здоров'я людини
            //Фізичне виховання
            saveOrUpdateLR(licenseCPU, tdosPhysV_B, bach_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafTOFAV, btrain_LRG, es);
            //Здоров'я людини
            saveOrUpdateLR(licenseCPU, tdosZL_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafFR, btrain_LRG, es);

            //Мистецтво
            //Дизайн
            saveOrUpdateLR(licenseCPU, tdosDZ_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20140701, kafTGG, btrain_LRG, es);

            //Гуманітарні науки
            //Філологія
            saveOrUpdateLR(licenseCPU, tdosPhilol_B, bach_EQL, stat_EF, 105, corr_EF, 55, lrCal20150701, kafAFZL, btrain_LRG, es);

            //Соціально-політичні науки
            //Соціологія
            saveOrUpdateLR(licenseCPU, tdosS_B, bach_EQL, stat_EF, 10, corr_EF, 10, lrCal20150701, kafSSR, btrain_LRG, es);
            //Психологія
            saveOrUpdateLR(licenseCPU, tdosPSH_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPP, btrain_LRG, es);

            //Журналістика та інформація
            //Журналістика
            saveOrUpdateLR(licenseCPU, tdosZhu_B, bach_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafZSK, btrain_LRG, es);
            //Реклама і зв'язки з громадкістю (за видами)
            saveOrUpdateLR(licenseCPU, tdosRZG_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20110701, kafRZG, btrain_LRG, es);
            //Видавнича справа та редагування
            saveOrUpdateLR(licenseCPU, tdosVSR_B, bach_EQL, stat_EF, 30, corr_EF, 50, lrCal20110701, kafVSRUF, btrain_LRG, es);

            //Право
            //Право
            saveOrUpdateLR(licenseCPU, tdosP_B, bach_EQL, stat_EF, 150, corr_EF, 250, lrCal20150701, kafTZGP, btrain_LRG, es);

            //Економіка і підприємництво
            //Економічна кібернетика
            saveOrUpdateLR(licenseCPU, tdosEKib_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEKS, btrain_LRG, es);
            //Міжнародна економіка
            saveOrUpdateLR(licenseCPU, tdosME_B, bach_EQL, stat_EF, 50, corr_EF, 30, lrCal20150701, kafME, btrain_LRG, es);
            //Економіка підприємства
            saveOrUpdateLR(licenseCPU, tdosEP_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafEP, btrain_LRG, es);
            //Прикладна статистика
            saveOrUpdateLR(licenseCPU, tdosPRS_B, bach_EQL, stat_EF, 20, corr_EF, 20, lrCal20150701, kafEKS, btrain_LRG, es);
            //Маркетинг
            saveOrUpdateLR(licenseCPU, tdosM_B, bach_EQL, stat_EF, 50, corr_EF, 50, lrCal20150701, kafM, btrain_LRG, es);
            //Фінанси і кредит
            saveOrUpdateLR(licenseCPU, tdosFK_B, bach_EQL, stat_EF, 120, corr_EF, 120, lrCal20150701, kafFK, btrain_LRG, es);
            //Облік і аудит
            saveOrUpdateLR(licenseCPU, tdosOA_B, bach_EQL, stat_EF, 100, corr_EF, 150, lrCal20150701, kafOA, btrain_LRG, es);

            //Менеджмент і адміністрування
            //Менеджмент
            saveOrUpdateLR(licenseCPU, tdosMG_B, bach_EQL, stat_EF, 140, corr_EF, 250, lrCal20150701, kafMO, btrain_LRG, es);

            //Системні науки та кібернетика
            //Інформатика
            saveOrUpdateLR(licenseCPU, tdosIF_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20110701, kafSAVM, btrain_LRG, es);
            //Системний аналіз
            saveOrUpdateLR(licenseCPU, tdosSA_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSAVM, btrain_LRG, es);

            //Інформатика та обчислювальна техніка
            //Програмна інженерія
            saveOrUpdateLR(licenseCPU, tdosPI_B, bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20150701, kafPIT, btrain_LRG, es);

            //Електроніка
            //Мікро- та наноелектроніка
            saveOrUpdateLR(licenseCPU, tdosMNE_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSAVM, btrain_LRG, es);

            //Геодезія та землеустрій
            //Геодезія, картографія та землеустрій
            saveOrUpdateLR(licenseCPU, tdosGKZ_B, bach_EQL, stat_EF, 30, corr_EF, 0, lrCal20150701, kafDUZK, btrain_LRG, es);

            //Соціальне забезпечення
            //Соціальна робота
            saveOrUpdateLR(licenseCPU, tdosSR_B, bach_EQL, stat_EF, 40, corr_EF, 40, lrCal20150701, kafSSR, forcol_LRG, es);

            //Сфера обслуговування
            //Готельно-ресторанна справа
            saveOrUpdateLR(licenseCPU, tdosGRS_B, bach_EQL, stat_EF, 30, corr_EF, 30, lrCal20130701, kafTGG, forcol_LRG, es);
            //Туризм
            saveOrUpdateLR(licenseCPU, tdosT_B,   bach_EQL, stat_EF, 60, corr_EF, 60, lrCal20130701, kafTGG, btrain_LRG, es);


            //Подготовка младших специалистов
            //Журналістика та інформація
            //Видавнича справа та редагування
            saveOrUpdateLR(licenseCPU, tdosVSTR_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafVSRUF, forcol_LRG, es);

            //Економіка та підприємництво
            //Економіка підприємства
            saveOrUpdateLR(licenseCPU, tdosEKP_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, forcol_LRG, es);
            //Товарознавство та комерційна діяльність
            saveOrUpdateLR(licenseCPU, tdosTZKD_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafEP, forcol_LRG, es);

            //Системні науки та кібернетика
            //Прикладна математика
            saveOrUpdateLR(licenseCPU, tdosPRM_JS, jSpec_EQL, stat_EF, 0, corr_EF, 30, lrCal20110701, kafSAVM, forcol_LRG, es);

            //Інформатика та обчислювальна техника
            //Розробка програмного забезпечення
            saveOrUpdateLR(licenseCPU, tdosRPZ_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafPIT, forcol_LRG, es);

            //Соціальне забезпечення
            //Соціальна робота
            saveOrUpdateLR(licenseCPU, tdosSOCR_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSSR, forcol_LRG, es);

            //Сфера обслуговування
            //Організація обслуговування населення
            saveOrUpdateLR(licenseCPU, tdosOONAS_JS, jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSSR, forcol_LRG, es);
            saveOrUpdateLR(licenseCPU, tdosTO_JS,    jSpec_EQL, stat_EF, 30, corr_EF, 30, lrCal20150701, kafSSR, forcol_LRG, es);
        }
    }

    //---------Метод сохранения/обновления записи лицензии----------
    private void saveOrUpdateLR(
            License license,
            TrainingDirectionOrSpeciality trainingDirectionOrSpeciality,
            EducationalQualificationLevel educationalQualificationLevel,
            EducationForm statEduForm,
            int statLicenseQuantity,
            EducationForm corrEduForm,
            int corrLicenseQuantity,
            //            Map<EducationForm, Integer> licenseQuantityByEducationForm,
            Calendar terminationDate,
            Chair chair,
            LicenseRecordGroup licenseRecordGroup,
            EntityService es) {

        //---Создание образца записи лицензии
        LicenseRecord lrSample = new LicenseRecord();
        SortedMap<EducationForm, Integer> licenseQuantityByEducationForm = new TreeMap();
        licenseQuantityByEducationForm.put(statEduForm, statLicenseQuantity);
        licenseQuantityByEducationForm.put(corrEduForm, corrLicenseQuantity);
        lrSample.setLicense(license);
        lrSample.setTrainingDirectionOrSpeciality(trainingDirectionOrSpeciality);
        lrSample.setEducationalQualificationLevel(educationalQualificationLevel);
        lrSample.setLicenseQuantityByEducationForm(licenseQuantityByEducationForm);
        lrSample.setTermination(terminationDate);
        lrSample.setOrgUnit(chair);
        lrSample.setLicenseRecordGroup(licenseRecordGroup);
        //Сохранение лицензионной записи
        es.findUniqueOrPersist(lrSample);
    }
}
