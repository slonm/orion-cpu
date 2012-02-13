package ua.orion.cpu.web.licensing.pages;

import javax.persistence.criteria.*;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.EducationalQualificationLevel;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.core.licensing.entities.Speciality;
import ua.orion.cpu.core.licensing.entities.TrainingDirection;
import ua.orion.web.AdditionalConstraintsApplier;
import ua.orion.web.components.Crud;
import ua.orion.web.services.RequestInfo;
import ua.orion.web.services.TapestryDataSource;

/**
 * Страница, которая предоставляет CRUD для простых сущностей
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
    private EntityService entityService;
    @Inject
    private TapestryDataSource dataSource;
    @Inject
    @Property(write = false)
    private TapestryDataSource tapestryComponentDataSource;
    @Environmental
    @Property(write = false)
    private PropertyEditContext editContext;
    @Inject
    @Property(write = false)
    private ValueEncoderSource valueEncoderSource;
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    //---Locals---
    @Component
    @Property(write = false)
    private Crud crud;
    @Property
    @Persist
    private ua.orion.cpu.core.licensing.entities.License license;

    /**
     * Задано явно для возможности вызова из других классов
     */
    public Class<?> getLicenseRecordClass() {
        return LicenseRecord.class;
    }

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
                license = entityService.find(ua.orion.cpu.core.licensing.entities.License.class, context.get(String.class, 0));
                license.getId(); //throw exception if object is null
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
            SecurityUtils.getSubject().checkPermission("License:read:" + license.getId());
        }
        return null;
    }

    Object onSuccessFromEditForm() {
        return crud.onSuccessFromEditForm();
    }

    public BeanModel getBeanModel() {
        return dataSource.getBeanModelForEdit(LicenseRecord.class);
    }
    @Component
    @Property(write = false)
    private Select levelSelect;
    @Component
    @Property(write = false)
    private Select specialitySelect;
    @Component
    @Property(write = false)
    private Select trainingDirectionSelect;
    @Component
    private Zone specialityZone;
    @Component
    private Zone trainingDirectionZone;

    public void onValueChangedFromLevelSelect(EducationalQualificationLevel level) {
        getLicenseRecord().setEducationalQualificationLevel(level);
        //getLicenseRecord().preSave();
        ajaxResponseRenderer.addRender("specialityZone", specialityZone);
        ajaxResponseRenderer.addRender("trainingDirectionZone", trainingDirectionZone);
    }

    public void onValueChangedFromSpecialitySelect(Speciality spec) {
        getLicenseRecord().setSpeciality(spec);
        getLicenseRecord().preSave();
        ajaxResponseRenderer.addRender("trainingDirectionZone", trainingDirectionZone);
    }

    public void onValueChangedTrainingDirectionSelect(TrainingDirection td) {
        getLicenseRecord().setTrainingDirection(td);
        getLicenseRecord().preSave();
        ajaxResponseRenderer.addRender("specialityZone", specialityZone);
    }

    public boolean getIsShowSpeciality() {
        return !EducationalQualificationLevel.BACHELOR_UKEY.equals(getLicenseRecord().getEducationalQualificationLevel().getUKey());
    }

    public ValueEncoder getLevelEncoder() {
        return valueEncoderSource.getValueEncoder(entityService.getMetaEntity("EducationalQualificationLevel").getEntityClass());
    }
}
