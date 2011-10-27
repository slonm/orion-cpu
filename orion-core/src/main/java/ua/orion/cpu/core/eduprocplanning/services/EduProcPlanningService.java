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
    
    /*
     * @param educationalQualificationLevel UKey of educationalQualificationLevel
     * @param trainingDirectionOrSpeciality name of trainingDirectionOrSpeciality
     * 
     */
    LicenseRecord findLicenseRecordByExample(String serial, String number, 
    Calendar issue, String educationalQualificationLevel, 
    String trainingDirectionOrSpeciality, Calendar termination);

}
