package orion.cpu.services;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.*;
import br.com.arsmachina.controller.Controller;
import java.util.*;
import orion.cpu.entities.ref.*;
import orion.cpu.entities.uch.*;
import orion.cpu.security.OperationTypes;
import orion.cpu.services.impl.InitializeDatabaseSupport;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Начальная инициализация данных.
 * @author sl
 */
public class LicenseInitializeDatabase extends OperationTypes implements Runnable {

    private final InitializeDatabaseSupport iDBSpt;
    private final Controller<EducationForm, Integer> eduFormController;

    public LicenseInitializeDatabase(InitializeDatabaseSupport initializeDatabaseSupport) {
        this.iDBSpt = Defense.notNull(initializeDatabaseSupport, "initializeDatabaseSupport");
        eduFormController = iDBSpt.getControllerSource().get(EducationForm.class);
    }

    @Override
    public void run() {
        //---------Константы---------
        //---------Формы обучения---------
        EducationForm stat_EF = iDBSpt.getStoredConstantsSource().get(EducationForm.class, EducationForm.STATIONARY_KEY);
        if (stat_EF == null) {
            stat_EF = new EducationForm("Денна");
            stat_EF.setShortName("Д");
            iDBSpt.getStoredConstantsSource().put(EducationForm.class, EducationForm.STATIONARY_KEY, stat_EF);
        }
        EducationForm corr_EF = iDBSpt.getStoredConstantsSource().get(EducationForm.class, EducationForm.CORRESPONDENCE_KEY);
        if (corr_EF == null) {
            corr_EF = new EducationForm("Заочна");
            corr_EF.setShortName("З");
            iDBSpt.getStoredConstantsSource().put(EducationForm.class, EducationForm.CORRESPONDENCE_KEY, corr_EF);
        }

        //---------Квалификационные уровни---------
        EducationalQualificationLevel jSpec_EQL = iDBSpt.getStoredConstantsSource().get(EducationalQualificationLevel.class, EducationalQualificationLevel.JUNIOR_SPECIALIST_KEY);
        if (jSpec_EQL == null) {
            jSpec_EQL = new EducationalQualificationLevel("Молодший спеціаліст", "5");
            jSpec_EQL.setShortName("Мл");
            iDBSpt.getStoredConstantsSource().put(EducationalQualificationLevel.class, EducationalQualificationLevel.JUNIOR_SPECIALIST_KEY, jSpec_EQL);
        }
        EducationalQualificationLevel bach_EQL = iDBSpt.getStoredConstantsSource().get(EducationalQualificationLevel.class, EducationalQualificationLevel.BACHELOR_KEY);
        if (bach_EQL == null) {
            bach_EQL = new EducationalQualificationLevel("Бакалавр", "6");
            bach_EQL.setShortName("Б");
            iDBSpt.getStoredConstantsSource().put(EducationalQualificationLevel.class, EducationalQualificationLevel.BACHELOR_KEY, bach_EQL);
        }
        EducationalQualificationLevel spec_EQL = iDBSpt.getStoredConstantsSource().get(EducationalQualificationLevel.class, EducationalQualificationLevel.SPECIALIST_KEY);
        if (spec_EQL == null) {
            spec_EQL = new EducationalQualificationLevel("Спеціаліст", "7");
            spec_EQL.setShortName("С");
            iDBSpt.getStoredConstantsSource().put(EducationalQualificationLevel.class, EducationalQualificationLevel.SPECIALIST_KEY, spec_EQL);
        }
        EducationalQualificationLevel master_EQL = iDBSpt.getStoredConstantsSource().get(EducationalQualificationLevel.class, EducationalQualificationLevel.MASTER_KEY);
        if (master_EQL == null) {
            master_EQL = new EducationalQualificationLevel("Магістр", "8");
            master_EQL.setShortName("М");
            iDBSpt.getStoredConstantsSource().put(EducationalQualificationLevel.class, EducationalQualificationLevel.MASTER_KEY, master_EQL);
        }

        //---------Права----------
        Map<String, Permission> EFPermissions = iDBSpt.getPermissionsMap(EducationForm.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> EQLPermissions = iDBSpt.getPermissionsMap(EducationalQualificationLevel.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> KAOTPermissions = iDBSpt.getPermissionsMap(KnowledgeAreaOrTrainingDirection.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> TDOSPermissions = iDBSpt.getPermissionsMap(TrainingDirectionOrSpeciality.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);

        Map<String, Permission> LPermissions = iDBSpt.getPermissionsMap(License.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> LRPermissions = iDBSpt.getPermissionsMap(LicenseRecord.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        Map<String, Permission> LRVPermissions = iDBSpt.getPermissionsMap(LicenseRecordView.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
        if (iDBSpt.isFillTestData()) {
            //---------Группы прав----------
            //Права просмотра записей о лицензиях
            PermissionGroup pgReadLicenseRecords = iDBSpt.saveOrUpdatePermissionGroup("Подсистема лицензий. Просмотр лицензий",
                    LPermissions.get(READ_OP), LRPermissions.get(READ_OP), LRVPermissions.get(READ_OP));

            //Права изменения записей о лицензиях
            PermissionGroup pgManageLicenseRecords = iDBSpt.saveOrUpdatePermissionGroup("Подсистема лицензий. Управление лицензиями",
                    LPermissions.get(STORE_OP), LPermissions.get(UPDATE_OP), LPermissions.get(REMOVE_OP),
                    LRPermissions.get(STORE_OP), LRPermissions.get(UPDATE_OP), LRPermissions.get(REMOVE_OP),
                    LRVPermissions.get(STORE_OP), LRVPermissions.get(UPDATE_OP), LRVPermissions.get(REMOVE_OP));

            //Права изменения справочников для системы лицензий
            PermissionGroup pgManageLicenseRecordsReference = iDBSpt.saveOrUpdatePermissionGroup("Подсистема лицензий. Управление справочниками",
                    EFPermissions.get(STORE_OP), EFPermissions.get(UPDATE_OP), EFPermissions.get(REMOVE_OP),
                    EQLPermissions.get(STORE_OP), EQLPermissions.get(UPDATE_OP), EQLPermissions.get(REMOVE_OP),
                    KAOTPermissions.get(STORE_OP), KAOTPermissions.get(UPDATE_OP), KAOTPermissions.get(REMOVE_OP),
                    TDOSPermissions.get(STORE_OP), TDOSPermissions.get(UPDATE_OP), TDOSPermissions.get(REMOVE_OP));

            //Права добавления справочников для системы лицензий
            PermissionGroup pgStoreLicenseRecordsReference = iDBSpt.saveOrUpdatePermissionGroup("Подсистема лицензий. Пополнение справочников",
                    EFPermissions.get(STORE_OP), EQLPermissions.get(STORE_OP),
                    KAOTPermissions.get(STORE_OP), TDOSPermissions.get(STORE_OP));

            //Права просмотра справочников для системы лицензий
            PermissionGroup pgReadLicenseRecordsReference = iDBSpt.saveOrUpdatePermissionGroup("Подсистема лицензий. Просмотр справочников",
                    EFPermissions.get(READ_OP), EQLPermissions.get(READ_OP),
                    KAOTPermissions.get(READ_OP), TDOSPermissions.get(READ_OP));

            //---------Роли----------
            Role roleLG = iDBSpt.saveOrUpdateRole("LicenseGuest",
                    "Просмотр записей о лицензиях", pgReadLicenseRecords);

            Role roleLO = iDBSpt.saveOrUpdateRole("LicenseOperator",
                    "Оператор управления записями о лицензиях",
                    pgReadLicenseRecords, pgManageLicenseRecords, pgStoreLicenseRecordsReference);

            Role roleLM = iDBSpt.saveOrUpdateRole("LicenseManager",
                    "Менеджер управления записями о лицензиях",
                    pgReadLicenseRecords, pgManageLicenseRecords, pgManageLicenseRecordsReference,
                    pgReadLicenseRecordsReference);

            //---------Пользователи----------
            UserController uCnt = iDBSpt.getUserController();
            User user = uCnt.findByLogin("sl");
            user.add(roleLO);
            for(PermissionGroup pg: roleLO.getPermissionGroups()){
                user.add(pg);
            }
            uCnt.saveOrUpdate(user);

            user = uCnt.findByLogin("TII");
            user.add(roleLM);
            for(PermissionGroup pg: roleLM.getPermissionGroups()){
                user.add(pg);
            }
            uCnt.saveOrUpdate(user);
        }
    }
}