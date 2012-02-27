package ua.orion.cpu.core.licensing.services;

import java.util.*;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.cpu.core.orgunits.entities.*;

/**
 *
 * @author sl
 */
public interface LicensingService {

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
     * @param trainingDirection name of trainingDirection
     * 
     */
    LicenseRecord findLicenseRecordByTrainingDirection(String serial, String number, 
    Calendar issue, String educationalQualificationLevel, 
    String trainingDirection, Calendar termination);

    /*
     * @param educationalQualificationLevel UKey of educationalQualificationLevel
     * @param speciality name of speciality
     * 
     */
    LicenseRecord findLicenseRecordBySpeciality(String serial, String number, 
    Calendar issue, String educationalQualificationLevel, 
    String speciality, Calendar termination);

    List<LicenseRecord> findLicenseRecordsByOrgUnit(OrgUnit orgUnit);

    List<LicenseRecord> findLicenseRecordsByTerminationDate(Date terminationDate);

    List<LicenseRecord> findLicenseRecordsByEducationalQualificationLevel(String codeLevel);

    List<LicenseRecord> findLicenseRecordsByTrainingDirection(String codeDirection);

    boolean existsForcedLicense();
}