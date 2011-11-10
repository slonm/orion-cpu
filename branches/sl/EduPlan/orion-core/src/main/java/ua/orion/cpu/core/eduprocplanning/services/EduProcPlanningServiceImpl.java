package ua.orion.cpu.core.eduprocplanning.services;

import java.util.*;
import javax.persistence.TypedQuery;
import ua.orion.cpu.core.licensing.entities.License;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ua.orion.core.services.EntityService;
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
    public LicenseRecord findLicenseRecordByExample(String serial, String number,
            Calendar issue, String eql,
            String tdos, Calendar termination) {
        String source = "select lr from LicenseRecord lr"
                + " join lr.license l join lr.educationalQualificationLevel eql"
                + " join lr.trainingDirectionOrSpeciality tdos"
                + " where l.serial=:serial and l.number=:number and l.issue=:issue and eql.UKey=:eql"
                + " and tdos.name=:tdos and lr.termination=:termination";
        TypedQuery<LicenseRecord> query = es.getEntityManager().createQuery(source, LicenseRecord.class);
        query.setParameter("serial", serial);
        query.setParameter("number", number);
        query.setParameter("issue", issue);
        query.setParameter("eql", eql);
        query.setParameter("tdos", tdos);
        query.setParameter("termination", termination);
        List<LicenseRecord> l = query.setMaxResults(1).getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }
}
