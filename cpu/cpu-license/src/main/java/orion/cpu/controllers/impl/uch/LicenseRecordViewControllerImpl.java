package orion.cpu.controllers.impl.uch;

import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.service.DAOSource;
import orion.cpu.controllers.impl.BaseController;
import orion.cpu.entities.uch.LicenseRecord;
import orion.cpu.entities.uch.LicenseRecordView;
import orion.cpu.services.DefaultControllerListeners;

/**
 * Контроллер для LicenseRecordView
 * @author sl
 */
public class LicenseRecordViewControllerImpl extends BaseController<LicenseRecordView, Integer> {

    private final DAO<LicenseRecord, Integer> lrDAO;

    /**
     * 
     * @param dAOSource
     * @param defaultControllerListeners 
     */
    public LicenseRecordViewControllerImpl(DAOSource dAOSource, DefaultControllerListeners defaultControllerListeners) {
        super(LicenseRecordView.class, dAOSource, defaultControllerListeners);
        lrDAO = dAOSource.get(LicenseRecord.class);
        if (lrDAO == null) {
            throw new RuntimeException("DAO for LicenseRecord not found");
        }

    }

    /**
     * Метод разрешает неоднозначность, когда дневная форма обучения персистентна, а
     * заочная нет, или наоборот.
     */
    @Override
    public LicenseRecordView update(LicenseRecordView object) {
        if (!isPersistent(object)) {
            if (!lrDAO.isPersistent(object.getStationaryLicenseRecord())) {
                lrDAO.save(object.getStationaryLicenseRecord());
            } else if (!lrDAO.isPersistent(object.getCorrespondenseLicenseRecord())) {
                lrDAO.save(object.getCorrespondenseLicenseRecord());
            }
        }
        return super.update(object);
    }
}
