package ua.orion.cpu.core.eduprocplanning.services;

import java.util.*;
import javax.persistence.TypedQuery;
import ua.orion.cpu.core.licensing.entities.License;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.eduprocplanning.entities.*;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;

/**
 *
 * @author sl
 */
public class EduProcPlanningServiceImpl implements EduProcPlanningService {

    private final EntityService es;

    public EduProcPlanningServiceImpl(EntityService entityService) {
        this.es = entityService;
    }

    @Override
    public License findLicense(String serial, String number, Calendar issue) {
        CriteriaBuilder cb = es.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<License> query = cb.createQuery(License.class);
        Root<License> root = query.from(License.class);
        query.where(cb.and(
                cb.equal(root.get("serial"), serial),
                cb.equal(root.get("number"), number),
                cb.equal(root.get("issue"), issue)));
        return es.getEntityManager().createQuery(query).getSingleResult();
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
        TypedQuery<LicenseRecord> query = es.getEntityManager().createQuery(source, LicenseRecord.class);
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
        TypedQuery<LicenseRecord> query = es.getEntityManager().createQuery(source, LicenseRecord.class);
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
    public List<EduPlanCycle> findEduPlanCyclesByEduPlan(EduPlan plan) {
        String source = "select c from EduPlanCycle c"
                + " where c.eduPlan=:plan order by c.number";
        TypedQuery<EduPlanCycle> query = es.getEntityManager().createQuery(source, EduPlanCycle.class);
        query.setParameter("plan", plan);
        return query.getResultList();
    }
    
    @Override
    public List<EduPlanDiscipline> findDisciplinesByEduPlanCycleAndEduPlan(EduPlanCycle cycle, EduPlan plan) {
        String source = "select d from EduPlanDiscipline d join d.eduPlan"
                + " where d.eduPlan=:plan and :cycle MEMBER OF d.eduPlanDisciplineTags"
                + " order by d.number";
        TypedQuery<EduPlanDiscipline> query = es.getEntityManager().createQuery(source, EduPlanDiscipline.class);
        query.setParameter("plan", plan);
        query.setParameter("cycle", cycle);
        return query.getResultList();
    }
}
