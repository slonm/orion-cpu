package ua.orion.cpu.licensing.service;

import ua.orion.cpu.licensing.LicensingSymbols;
import java.util.*;
import ua.orion.cpu.orgunits.OrgUnitsSymbols;
import ua.orion.core.annotations.AfterLibrary;
import ua.orion.cpu.orgunits.entities.Chair;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.entities.SubSystem;
import ua.orion.cpu.licensing.entities.*;
import ua.orion.core.services.EntityService;
import static ua.orion.core.utils.DateTimeUtils.*;

/**
 *
 * @author sl
 */
@AfterLibrary(OrgUnitsSymbols.ORG_UNITS)
public class LicensingSeedEntity {
    public LicensingSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService dao) {
        SubSystem subSystem = dao.persistOrGet(SubSystem.class, new SubSystem(LicensingSymbols.LICENSING));
        //---------Формы обучения---------
        EducationForm stat_EF = dao.persistOrGet(EducationForm.class, new EducationForm("Денна", "Ден.", EducationForm.STATIONARY_UKEY));
        EducationForm corr_EF = dao.persistOrGet(EducationForm.class, new EducationForm("Заочна", "Заоч.", EducationForm.CORRESPONDENCE_UKEY));
        //---------Квалификационные уровни---------
        EducationalQualificationLevel jSpec_EQL = dao.persistOrGet(EducationalQualificationLevel.class, new EducationalQualificationLevel("Молодший спеціаліст", "мол.сп.", "5", EducationalQualificationLevel.JUNIOR_SPECIALIST_UKEY));
        EducationalQualificationLevel bach_EQL = dao.persistOrGet(EducationalQualificationLevel.class, new EducationalQualificationLevel("Бакалавр", "бак.", "6", EducationalQualificationLevel.BACHELOR_UKEY));
        EducationalQualificationLevel spec_EQL = dao.persistOrGet(EducationalQualificationLevel.class, new EducationalQualificationLevel("Спеціаліст", "спец.", "7", EducationalQualificationLevel.SPECIALIST_UKEY));
        EducationalQualificationLevel master_EQL = dao.persistOrGet(EducationalQualificationLevel.class, new EducationalQualificationLevel("Магістр", "маг.", "8", EducationalQualificationLevel.MASTER_UKEY));
        //---------Группы лицензионных записей (область действия) ---------
        LicenseRecordGroup bsmtrain_LRG = dao.persistOrGet(LicenseRecordGroup.class, new LicenseRecordGroup("Підготовка бакалаврів, спеціалістів. магістрів", LicenseRecordGroup.B_S_M_TRAINING_UKEY));
        LicenseRecordGroup sretrain_LRG = dao.persistOrGet(LicenseRecordGroup.class,new LicenseRecordGroup("Перепідготовка спеціалістів", LicenseRecordGroup.SPEC_RETRAINING_UKEY));
        LicenseRecordGroup forcol_LRG = dao.persistOrGet(LicenseRecordGroup.class,new LicenseRecordGroup("Для колледжу Класичного приватного університету", LicenseRecordGroup.FOR_COLLEDGE_UKEY));
        LicenseRecordGroup btrain_LRG = dao.persistOrGet(LicenseRecordGroup.class,new LicenseRecordGroup("Підготовка бакалаврів", LicenseRecordGroup.BACH_TRAINING_UKEY));
        LicenseRecordGroup jstrain_LRG = dao.persistOrGet(LicenseRecordGroup.class,new LicenseRecordGroup("Підготовка молодших спеціалістів", LicenseRecordGroup.JUN_SPEC_TRAINING_UKEY));
        if (testData) {
            //---------Области знаний или направления подготовки----------
            KnowledgeAreaOrTrainingDirection kaotdCompSci = dao.persistOrGet(KnowledgeAreaOrTrainingDirection.class, new KnowledgeAreaOrTrainingDirection("Комп'ютерні науки", null, "0804", false, false));
            KnowledgeAreaOrTrainingDirection kaotdInfComp = dao.persistOrGet(KnowledgeAreaOrTrainingDirection.class, new KnowledgeAreaOrTrainingDirection("Інформатика та обчислювальна техніка", null, "0501", true, false));
            KnowledgeAreaOrTrainingDirection kaotdSpecCateg = dao.persistOrGet(KnowledgeAreaOrTrainingDirection.class, new KnowledgeAreaOrTrainingDirection("Специфічні категорії", null, "0000", false, false));
            KnowledgeAreaOrTrainingDirection kaotdSysSciCyber = dao.persistOrGet(KnowledgeAreaOrTrainingDirection.class, new KnowledgeAreaOrTrainingDirection("Системні науки та кібернетика", null, "0403", true, false));
            //---Направления подготовки или специальности суффиксы _С, _B, _SM обозначают квалификационные уровни младшего специалиста, бакалавра, специалиста/магистра, соответственно----------
            TrainingDirectionOrSpeciality tdosPZAS_B = dao.persistOrGet(TrainingDirectionOrSpeciality.class, new TrainingDirectionOrSpeciality("Програмне забезпечення автоматизованих систем", "ПЗАС", "00", false, kaotdCompSci, false));
            TrainingDirectionOrSpeciality tdosPZAS_SM = dao.persistOrGet(TrainingDirectionOrSpeciality.class, new TrainingDirectionOrSpeciality("Програмне забезпечення автоматизованих систем", "ПЗАС", "03", false, kaotdCompSci, false));
            TrainingDirectionOrSpeciality tdosPECTAS_JS = dao.persistOrGet(TrainingDirectionOrSpeciality.class, new TrainingDirectionOrSpeciality("Програмування для електронно-обчислювальної техніки і автоматизованих систем", "ПЗАС", "05", false, kaotdCompSci, false));
            TrainingDirectionOrSpeciality tdosPI_B = dao.persistOrGet(TrainingDirectionOrSpeciality.class, new TrainingDirectionOrSpeciality("Програмна інженерія", "ПІ", "03", true, kaotdInfComp, false));
            TrainingDirectionOrSpeciality tdosRPZ_JS = dao.persistOrGet(TrainingDirectionOrSpeciality.class, new TrainingDirectionOrSpeciality("Розробка програмного забезпечення", "РПЗ", "0301", true, kaotdInfComp, false));
            TrainingDirectionOrSpeciality tdosPVSH_SM = dao.persistOrGet(TrainingDirectionOrSpeciality.class, new TrainingDirectionOrSpeciality("Педагогіка вищої школи", "ПВШ", "05", false, kaotdSpecCateg, false));
            TrainingDirectionOrSpeciality tdosSA_B = dao.persistOrGet(TrainingDirectionOrSpeciality.class, new TrainingDirectionOrSpeciality("Системний аналіз та управління", "СА", "03", true, kaotdSysSciCyber, false));

            //---Серия, номер и дата выдачи лицензии----------
            License licenseCPU = dao.persistOrGet(License.class, new License("АВ", "420720", createDate(21, 10, 2008)));
            
            //---Кафедры, выполняющие обучение по лицензиям----------
            Chair kafPIT = dao.findByName(Chair.class, "кафедра програмування та інформаційних технологій");
            assert kafPIT!=null;
            Chair kafEICPHS = dao.findByName(Chair.class, "кафедра управління навчальними закладами та педагогіки вищої школи");
            assert kafEICPHS!=null;
            Chair kafSAVM = dao.findByName(Chair.class, "кафедра системного аналізу та вищої математики");
            assert kafSAVM!=null;
            //Термін закінчення ліцензій ПЗАС та ПІ
            Date lrCal20100701 = createDate(1, Calendar.JULY, 2010);
            //Термін закінчення ліцензій ПВШ
            Date lrCal20090701 = createDate(1, Calendar.JULY, 2009);
            
            //---Записи лицензии-суффиксы _JS, _B, _S, _M обозначают
            //квалификационные уровни младшего специалиста, бакалавра, специалиста, магистра, соответственно
            //stat_EF, corr_EF обозначают дневную и заочн формы обучения----------

            //Програмне забезпечення автоматизованих систем - молодші спеціалісти, заочна
            //Эта карта будет передана в сущность по ссылке, поэтому ее нельзя использовать повторно
            Map<EducationForm, Integer> pZASJSpecLicQuantity=new HashMap();
            pZASJSpecLicQuantity.put(corr_EF, 30);
            dao.persistOrGet(LicenseRecord.class, new LicenseRecord(licenseCPU, tdosPECTAS_JS, jSpec_EQL, pZASJSpecLicQuantity, lrCal20100701, kafPIT, forcol_LRG));
            
            //Програмне забезпечення автоматизованих систем - бакалаври
            Map<EducationForm, Integer> pZASBachLicQuantity=new HashMap();
            pZASBachLicQuantity.put(stat_EF, 60);
            pZASBachLicQuantity.put(corr_EF, 60);
            dao.persistOrGet(LicenseRecord.class, new LicenseRecord(licenseCPU, tdosPZAS_B, bach_EQL, pZASBachLicQuantity, lrCal20100701, kafPIT, bsmtrain_LRG));

            //Програмне забезпечення автоматизованих систем - специалісти
            Map<EducationForm, Integer> pZASSpecLicQuantity=new HashMap();
            pZASSpecLicQuantity.put(stat_EF, 30);
            pZASSpecLicQuantity.put(corr_EF, 30);
            dao.persistOrGet(LicenseRecord.class, new LicenseRecord(licenseCPU, tdosPZAS_SM, spec_EQL, pZASSpecLicQuantity, lrCal20100701, kafPIT, bsmtrain_LRG));

            //Програмне забезпечення автоматизованих систем - магістри
            Map<EducationForm, Integer> pZASMasterLicQuantity=new HashMap();
            pZASMasterLicQuantity.put(stat_EF, 10);
            pZASMasterLicQuantity.put(corr_EF, 10);
            dao.persistOrGet(LicenseRecord.class, new LicenseRecord(licenseCPU, tdosPZAS_SM, master_EQL, pZASMasterLicQuantity, lrCal20100701, kafPIT, bsmtrain_LRG));

            //Розробка програмного забезпечення - молодші спеціалісти
            Map<EducationForm, Integer> pPZJSpecLicQuantity=new HashMap();
            pPZJSpecLicQuantity.put(corr_EF, 30);
            dao.persistOrGet(LicenseRecord.class, new LicenseRecord(licenseCPU, tdosRPZ_JS, jSpec_EQL, pPZJSpecLicQuantity, lrCal20100701, kafPIT, jstrain_LRG));
            
            //Програмна інженерія- бакалаври, денна
            Map<EducationForm, Integer> pIBachLicQuantity=new HashMap();
            pIBachLicQuantity.put(stat_EF, 60);
            pIBachLicQuantity.put(corr_EF, 60);
            dao.persistOrGet(LicenseRecord.class, new LicenseRecord(licenseCPU, tdosPI_B, bach_EQL, pIBachLicQuantity, lrCal20100701, kafPIT, jstrain_LRG));

            //Педагогіка вищої школи- магістри
            Map<EducationForm, Integer> pVSHMasterLicQuantity=new HashMap();
            pVSHMasterLicQuantity.put(stat_EF, 30);
            pVSHMasterLicQuantity.put(corr_EF, 30);
            dao.persistOrGet(LicenseRecord.class, new LicenseRecord(licenseCPU, tdosPVSH_SM, master_EQL, pVSHMasterLicQuantity, lrCal20090701, kafEICPHS, bsmtrain_LRG));
            
            //Програмна інженерія- бакалаври, денна
            Map<EducationForm, Integer> sABachLicQuantity=new HashMap();
            sABachLicQuantity.put(stat_EF, 30);
            sABachLicQuantity.put(corr_EF, 30);
            dao.persistOrGet(LicenseRecord.class, new LicenseRecord(licenseCPU, tdosSA_B, bach_EQL, sABachLicQuantity, lrCal20090701, kafSAVM, btrain_LRG));
        }
    }
}
