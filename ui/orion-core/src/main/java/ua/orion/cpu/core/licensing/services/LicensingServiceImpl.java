package ua.orion.cpu.core.licensing.services;

import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.core.orgunits.entities.OrgUnit;

/**
 *
 * @author sl
 */
public class LicensingServiceImpl implements LicensingService{
    private final EntityService entityService;

    public LicensingServiceImpl(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public List<LicenseRecord> findLicenseRecordsByOrgUnit(OrgUnit orgUnit) {
            CriteriaBuilder cb = entityService.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<LicenseRecord> query = cb.createQuery(LicenseRecord.class);
            Root<LicenseRecord> root = query.from(LicenseRecord.class);
            query.where(cb.equal(root.get("orgUnit"), orgUnit));
            return entityService.getEntityManager().createQuery(query).getResultList();
   }

    @Override
    public List<LicenseRecord> findLicenseRecordsByTerminationDate(Date terminationDate) {
            CriteriaBuilder cb = entityService.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<LicenseRecord> query = cb.createQuery(LicenseRecord.class);
            Root<LicenseRecord> root = query.from(LicenseRecord.class);
            query.where(cb.equal(root.get("terminationDate"), terminationDate));
            return entityService.getEntityManager().createQuery(query).getResultList();
    }

    @Override
    public List<LicenseRecord> findLicenseRecordsByEducationalQualificationLevel(String codeLevel) {
            CriteriaBuilder cb = entityService.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<LicenseRecord> query = cb.createQuery(LicenseRecord.class);
            Root<LicenseRecord> root = query.from(LicenseRecord.class);
            query.where(cb.equal(root.get("educationalQualificationLevel.code"), codeLevel));
            return entityService.getEntityManager().createQuery(query).getResultList();
    }

    @Override
    public List<LicenseRecord> findLicenseRecordsByTrainingDirection(String codeDirection) {
            CriteriaBuilder cb = entityService.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<LicenseRecord> query = cb.createQuery(LicenseRecord.class);
            Root<LicenseRecord> root = query.from(LicenseRecord.class);
            query.where(cb.equal(root.get("trainingDirectionOrSpeciality.code"), codeDirection));
            return entityService.getEntityManager().createQuery(query).getResultList();
    }
    
}
