package ua.orion.cpu.core.eduprocplanning.services;

import java.util.Calendar;
import ua.orion.cpu.core.licensing.entities.License;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ua.orion.core.services.EntityService;
import ua.orion.core.utils.DateTimeUtils;
import ua.orion.cpu.core.licensing.entities.EducationalQualificationLevel;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.core.licensing.entities.LicenseRecordGroup;
import ua.orion.cpu.core.licensing.entities.TrainingDirectionOrSpeciality;
import ua.orion.cpu.core.orgunits.entities.OrgUnit;

/**
 *
 * @author sl
 */
public class EduProcPlanningServiceImpl implements EduProcPlanningService {

    private final EntityService entityService;

    public EduProcPlanningServiceImpl(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public License findLicense(String serial, String number, Calendar issue) {
        CriteriaBuilder cb = entityService.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<License> query = cb.createQuery(License.class);
        Root<License> root = query.from(License.class);
        query.where(cb.and(
                cb.equal(root.get("serial"), serial),
                cb.equal(root.get("number"), number),
                cb.equal(root.get("issue"), issue)));
        return entityService.getEntityManager().createQuery(query).getSingleResult();
    }
    //TODO Переписать с использованием языка JPA QueryLanguage 
    @Override
    public LicenseRecord findLicenseRecordByExample(String serial, String number,
            Calendar issue, EducationalQualificationLevel educationalQualificationLevel,
            TrainingDirectionOrSpeciality trainingDirectionOrSpeciality,
            String licenseRecordGroupName, String orgUnitName, Calendar termination) {        
        LicenseRecord lrSample = new LicenseRecord();
        lrSample.setLicense(findLicense(serial, number, issue));
        lrSample.setEducationalQualificationLevel(educationalQualificationLevel);
        lrSample.setTrainingDirectionOrSpeciality(trainingDirectionOrSpeciality);
        lrSample.setLicenseRecordGroup(entityService.findByName(LicenseRecordGroup.class, licenseRecordGroupName));
        lrSample.setOrgUnit(entityService.findByName(OrgUnit.class, orgUnitName));
        lrSample.setTermination(termination);

        CriteriaBuilder cb = entityService.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<LicenseRecord> query = cb.createQuery(LicenseRecord.class);
        Root<LicenseRecord> root = query.from(LicenseRecord.class);
        query.where(cb.equal(root, lrSample));
        return entityService.getEntityManager().createQuery(query).getSingleResult();
    }
}
