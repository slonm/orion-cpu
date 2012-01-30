package ua.orion.cpu.core.eduprocplanning.services;

import java.util.*;
import ua.orion.cpu.core.eduprocplanning.entities.*;
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

    List<EduPlanCycle> findEduPlanCyclesByEduPlan(EduPlan plan);

    List<EduPlanDiscipline> findDisciplinesByEduPlanCycleAndEduPlan(EduPlanCycle cycle, EduPlan plan);
}
