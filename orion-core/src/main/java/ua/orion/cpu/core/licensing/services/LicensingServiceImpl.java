package ua.orion.cpu.core.licensing.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.cpu.core.orgunits.entities.OrgUnit;

/**
 *
 * @author sl
 */
public class LicensingServiceImpl implements LicensingService {

    private final EntityService es;
    private final EntityManager em;
    private final CriteriaBuilder cb;

    public LicensingServiceImpl(EntityService entityService) {
        this.es = entityService;
        em = entityService.getEntityManager();
        cb = em.getCriteriaBuilder();
    }

    @Override
    public List<LicenseRecord> findLicenseRecordsByOrgUnit(OrgUnit orgUnit) {
        CriteriaQuery<LicenseRecord> query = cb.createQuery(LicenseRecord.class);
        Root<LicenseRecord> root = query.from(LicenseRecord.class);
        query.where(cb.equal(root.get("orgUnit"), orgUnit));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<LicenseRecord> findLicenseRecordsByTerminationDate(Date terminationDate) {
        CriteriaQuery<LicenseRecord> query = cb.createQuery(LicenseRecord.class);
        Root<LicenseRecord> root = query.from(LicenseRecord.class);
        query.where(cb.equal(root.get("terminationDate"), terminationDate));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<LicenseRecord> findLicenseRecordsByEducationalQualificationLevel(String codeLevel) {
        CriteriaQuery<LicenseRecord> query = cb.createQuery(LicenseRecord.class);
        Root<LicenseRecord> root = query.from(LicenseRecord.class);
        query.where(cb.equal(root.get("educationalQualificationLevel.code"), codeLevel));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<LicenseRecord> findLicenseRecordsByTrainingDirection(String codeDirection) {
        CriteriaQuery<LicenseRecord> query = cb.createQuery(LicenseRecord.class);
        Root<LicenseRecord> root = query.from(LicenseRecord.class);
        query.where(cb.equal(root.get("trainingDirectionOrSpeciality.code"), codeDirection));
        return em.createQuery(query).getResultList();
    }

    @Override
    public boolean existsNewStateLicense() {
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<License> f = query.from(License.class);
        query.select(cb.count(f));
        query.where(cb.equal(f.get("licenseState"), LicenseState.NEW));
        return em.createQuery(query).getSingleResult().intValue() != 0;
    }

    @Override
    public License findLicense(String serial, String number, Calendar issue) {
        CriteriaQuery<License> query = cb.createQuery(License.class);
        Root<License> root = query.from(License.class);
        query.where(cb.and(
                cb.equal(root.get("serial"), serial),
                cb.equal(root.get("number"), number),
                cb.equal(root.get("issue"), issue)));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public LicenseRecord findLicenseRecordByTrainingDirection(String serial, String number,
            Calendar issue, String eql,
            String td, Calendar termination) {
        String source = "select lr from LicenseRecord lr"
                + " join lr.license l join lr.educationalQualificationLevel eql"
                + " join lr.trainingDirection td"
                + " where l.serial=:serial and l.number=:number and l.issue=:issue and eql.UKey=:eql"
                + " and td.name=:td and lr.termination=:termination";
        TypedQuery<LicenseRecord> query = em.createQuery(source, LicenseRecord.class);
        query.setParameter("serial", serial);
        query.setParameter("number", number);
        query.setParameter("issue", issue);
        query.setParameter("eql", eql);
        query.setParameter("td", td);
        query.setParameter("termination", termination);
        List<LicenseRecord> l = query.setMaxResults(1).getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public LicenseRecord findLicenseRecordBySpeciality(String serial, String number,
            Calendar issue, String eql,
            String speciality, Calendar termination) {
        String source = "select lr from LicenseRecord lr"
                + " join lr.license l join lr.educationalQualificationLevel eql"
                + " join lr.speciality sp"
                + " where l.serial=:serial and l.number=:number and l.issue=:issue and eql.UKey=:eql"
                + " and sp.name=:speciality and lr.termination=:termination";
        TypedQuery<LicenseRecord> query = em.createQuery(source, LicenseRecord.class);
        query.setParameter("serial", serial);
        query.setParameter("number", number);
        query.setParameter("issue", issue);
        query.setParameter("eql", eql);
        query.setParameter("speciality", speciality);
        query.setParameter("termination", termination);
        List<LicenseRecord> l = query.setMaxResults(1).getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public void forceAndMergeLicense(License license) {
        if (LicenseState.NEW != license.getLicenseState()) {
            throw new RuntimeException("License not in NEW state");
        }
        License forced = findForcedLicense();
        if (forced != null) {
            forced.setLicenseState(LicenseState.OBSOLETE);
            em.merge(forced);
        }
        license.setLicenseState(LicenseState.FORCED);
        em.merge(license);
    }

    @Override
    public void cloneLicenseRecordsFromForced(License license) {
        License forced = findForcedLicense();
        if (forced != null) {
            for (LicenseRecord lr : forced.getLicenseRecords()) {
                LicenseRecord newLr = lr.clone();
                newLr.setLicense(license);
                license.getLicenseRecords().add(newLr);
                em.persist(newLr);
            }
        }
    }

    @Override
    public License findForcedLicense() {
        CriteriaQuery<License> query = cb.createQuery(License.class);
        Root<License> f = query.from(License.class);
        query.where(cb.equal(f.get("licenseState"), LicenseState.FORCED));
        try {
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Speciality> findSpecialityByKnowledgeArea(KnowledgeArea knowledgeArea) {
        String source = "select sp from Speciality sp"
                + " join sp.trainingDirection td"
                + " where td.knowledgeArea=:knowledgeArea";
        TypedQuery<Speciality> query = es.getEntityManager().createQuery(source, Speciality.class);
        query.setParameter("knowledgeArea", knowledgeArea);
        return query.getResultList();
    }

    @Override
    public List<TrainingDirection> findTrainingDirectionByKnowledgeArea(KnowledgeArea knowledgeArea) {
        CriteriaQuery<TrainingDirection> query = cb.createQuery(TrainingDirection.class);
        Root<?> root = query.from(TrainingDirection.class);
        query.where(cb.equal(root.get("knowledgeArea"), knowledgeArea));
        return es.getEntityManager().createQuery(query).getResultList();
    }
}
