package ua.orion.cpu.web.licensing.pages;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.web.AdditionalConstraintsApplier;
import ua.orion.web.components.Crud;
import ua.orion.web.services.RequestInfo;
import ua.orion.web.services.TapestryDataSource;

/**
 * Страница, которая предоставляет CRUD для
 * ua.orion.cpu.core.licensing.entities.License
 *
 * @author slobodyanuk
 */
@SuppressWarnings("unused")
public class License {
    //---Services---

    @Inject
    private RequestInfo info;
    @Inject
    private Logger LOG;
    @Inject
    @Property(write = false)
    private EntityService es;
    @Inject
    private TapestryDataSource dataSource;
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    //---Locals---
    @Component
    @Property(write = false)
    private Crud crud;
    @Property
    @Persist
    private ua.orion.cpu.core.licensing.entities.License license;
    @Component
    private Zone specialityZone;
    @Component
    private Zone trainingDirectionZone;
    @Property(write = false)
    private TrainingDirection knowledgeAreaContainer;

    public LicenseRecord getLicenseRecord() {
        return (LicenseRecord) crud.getObject();
    }

    public GridDataSource getSource() {
        return dataSource.getGridDataSource(LicenseRecord.class, new AdditionalConstraintsApplier<LicenseRecord>() {

            @Override
            public void applyAdditionalConstraints(CriteriaQuery<LicenseRecord> criteria,
                    Root<LicenseRecord> root, CriteriaBuilder builder) {
                criteria.where(builder.equal(root.get("license"), license));
            }
        });
    }

    Object onActivate(EventContext context) {
        if (!info.isComponentEventRequest()) {
            try {
                if (context.getCount() != 1) {
                    throw new RuntimeException();
                }
                license = es.find(ua.orion.cpu.core.licensing.entities.License.class, context.get(String.class, 0));
                license.getId(); //throw exception if object is null
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
            SecurityUtils.getSubject().checkPermission("License:read:" + license.getId());
        }
        knowledgeAreaContainer = new TrainingDirection();
        try {
            knowledgeAreaContainer.setKnowledgeArea(
                    getLicenseRecord().getTrainingDirection().getKnowledgeArea());
            LOG.debug("KnowledgeArea is set to {}", knowledgeAreaContainer.getKnowledgeArea());
        } catch (NullPointerException ex) {
            LOG.debug("KnowledgeArea is not set");
        }
        return null;
    }

    Object onSuccessFromEditForm() {
        return crud.onSuccessFromEditForm();
    }

    Object onSuccessFromAddForm() {
        return crud.onSuccessFromEditForm();
    }

    void onBeforeAddPopupFromCrud(LicenseRecord lr) {
        lr.setLicense(license);
        knowledgeAreaContainer.setKnowledgeArea(null);
    }

    public BeanModel getBeanModel() {
        BeanModel bm = dataSource.getBeanModelForEdit(LicenseRecord.class);
        String ka = "knowledgeArea";
        bm.addEmpty(ka);
        List<String> props = new LinkedList<>(bm.getPropertyNames());
        props.remove(ka);
        props.add(2, ka);
        bm.reorder(props.toArray(new String[0]));
        return bm;
    }

    public void onValueChangedFromLevelSelect(EducationalQualificationLevel level) {
        getLicenseRecord().setEducationalQualificationLevel(level);
        getLicenseRecord().preSave();
        ajaxResponseRenderer.addRender("specialityZone", specialityZone);
        ajaxResponseRenderer.addRender("trainingDirectionZone", trainingDirectionZone);
    }

    public void onValueChangedFromSpecialitySelect(Speciality spec) {
        LOG.debug("Speciality {} is selected", spec);
        getLicenseRecord().setSpeciality(spec);
        getLicenseRecord().preSave();
        ajaxResponseRenderer.addRender("trainingDirectionZone", trainingDirectionZone);
    }

    public void onValueChangedFromTrainingDirectionSelect(TrainingDirection td) {
        LOG.debug("TrainingDirection {} is selected", td);
        getLicenseRecord().setTrainingDirection(td);
        getLicenseRecord().preSave();
        ajaxResponseRenderer.addRender("specialityZone", specialityZone);
    }

    public void onValueChangedFromKnowledgeAreaSelect(KnowledgeArea ka) {
        LOG.debug("KnowledgeArea {} is selected", ka);
        knowledgeAreaContainer.setKnowledgeArea(ka);
        ajaxResponseRenderer.addRender("specialityZone", specialityZone);
        ajaxResponseRenderer.addRender("trainingDirectionZone", trainingDirectionZone);
    }

    public boolean getIsEditSpeciality() {
        try {
            return knowledgeAreaContainer.getKnowledgeArea() != null
                    && !EducationalQualificationLevel.BACHELOR_UKEY.equals(
                    getLicenseRecord().getEducationalQualificationLevel().getUKey());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean getIsEditTrainingDirection() {
        try {
            return knowledgeAreaContainer.getKnowledgeArea() != null
                    && EducationalQualificationLevel.BACHELOR_UKEY.equals(
                    getLicenseRecord().getEducationalQualificationLevel().getUKey());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public Object getTrainingDirectionModel() {
        CriteriaBuilder cb = es.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TrainingDirection> query = cb.createQuery(TrainingDirection.class);
        Root<?> root = query.from(TrainingDirection.class);
        query.where(cb.equal(root.get("knowledgeArea"), knowledgeAreaContainer.getKnowledgeArea()));
        return es.getEntityManager().createQuery(query).getResultList();
    }

    public Object getSpecialityModel() {
        String source = "select sp from Speciality sp"
                + " join sp.trainingDirection td"
                + " where td.knowledgeArea=:knowledgeArea";
        TypedQuery<Speciality> query = es.getEntityManager().createQuery(source, Speciality.class);
        query.setParameter("knowledgeArea", knowledgeAreaContainer.getKnowledgeArea());
        return query.getResultList();
    }
}
