package ua.orion.cpu.core.eduprocplanning.services;

import java.util.*;
import ua.orion.cpu.core.licensing.entities.*;

/**
 *
 * @author sl
 */
public interface EduProcPlanningService {

    /**
     * Поиск лицензии с указанными серией, номером и датой получения
     * @param serial
     * @param number
     * @param issue
     * @return экземпляр лицензии
     */
    License findLicense(String serial, String number, Calendar issue);
    
    LicenseRecord findLicenseRecordByExample(String serial, String number, 
    Calendar issue, EducationalQualificationLevel educationalQualificationLevel, 
    TrainingDirectionOrSpeciality trainingDirectionOrSpeciality, 
    String licenseRecordGroupName, String orgUnitName, Calendar termination);

}
