package orion.cpu.dao.impl.uch;

import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.dao.SortCriterion;
import br.com.arsmachina.module.service.DAOSource;
import java.util.*;
import orion.cpu.entities.ref.EducationForm;
import orion.cpu.entities.uch.LicenseRecord;
import orion.cpu.entities.uch.LicenseRecordView;
import orion.cpu.services.StoredConstantsSource;
import ua.mihailslobodyanuk.utils.Defense;
import ua.mihailslobodyanuk.utils.reflect.generics.PropertyValue;

/**
 * DAO implementation for LicenseRecordView
 * Везде за id LicenseRecordView принимается id дневной формы обучения
 * @author sl
 */
public class LicenseRecordViewDAOImpl implements DAO<LicenseRecordView, Integer> {

    private final DAOSource dAOSource;
    private final StoredConstantsSource storedConstantsSource;
    private DAO<LicenseRecord, Integer> lrDAO = null;
    private EducationForm CORRESPONDENCE_EF = null;
    private EducationForm STATIONARY_EF = null;

    public LicenseRecordViewDAOImpl(DAOSource dAOSource, StoredConstantsSource storedConstantsSource) {
        this.dAOSource = Defense.notNull(dAOSource, "dAOSource");
        this.storedConstantsSource = Defense.notNull(storedConstantsSource, "storedConstantsSource");
    }

    private DAO<LicenseRecord, Integer> getLrDAO() {
        if (lrDAO == null) {
            lrDAO = dAOSource.get(LicenseRecord.class);
            if (lrDAO == null) {
                throw new RuntimeException("DAO for LicenseRecord not found");
            }
        }
        return lrDAO;
    }

    private EducationForm getCORRESPONDENCE_EF() {
        if (CORRESPONDENCE_EF == null) {
            CORRESPONDENCE_EF = storedConstantsSource.get(EducationForm.class, EducationForm.CORRESPONDENCE_KEY);
            if (CORRESPONDENCE_EF == null) {
                throw new RuntimeException("Stored constant for type EducationForm with key '" + EducationForm.CORRESPONDENCE_KEY + "' not found");
            }
        }
        return CORRESPONDENCE_EF;
    }

    private EducationForm getSTATIONARY_EF() {
        if (STATIONARY_EF == null) {
            STATIONARY_EF = storedConstantsSource.get(EducationForm.class, EducationForm.STATIONARY_KEY);
            if (STATIONARY_EF == null) {
                throw new RuntimeException("Stored constant for type EducationForm with key '" + EducationForm.STATIONARY_KEY + "' not found");
            }
        }
        return STATIONARY_EF;
    }

    @Override
    public int countAll() {
        return findAll().size();
    }

    private LicenseRecordView findByStationary(LicenseRecord stat) {
        if (stat == null) {
            return null;
        }
        LicenseRecord example = stat.clone();
        example.setEducationForm(getCORRESPONDENCE_EF());
        example.setStudentLicenseQuantity(null);
        List<LicenseRecord> corrs = getLrDAO().findByExample(example);
        LicenseRecord corr;
        if (corrs.isEmpty()) {
            example.setStudentLicenseQuantity(0);
            corr = example;
        } else {
            corr = corrs.get(0);
        }
        return new LicenseRecordView(stat, corr);
    }

    /**
     * Использует Id стационарной формы обучения
     * @param id
     * @return
     * @author sl
     */
    @Override
    public LicenseRecordView findById(Integer id) {
        Defense.notNull(id, "id");
        return findByStationary(getLrDAO().findById(id));
    }

    @Override
    public List<LicenseRecordView> findAll() {
        LicenseRecord example = new LicenseRecord();
        example.setEducationForm(getSTATIONARY_EF());
        List<LicenseRecordView> licenseRecordViewList = new LinkedList<LicenseRecordView>();
        List<LicenseRecord> licenseRecordList = getLrDAO().findByExample(example);
        for (LicenseRecord licenseRecord : licenseRecordList) {
            licenseRecordViewList.add(findByStationary(licenseRecord));
        }
        Collections.sort(licenseRecordViewList);
        return licenseRecordViewList;
    }

    @Override
    public List<LicenseRecordView> findByIds(Integer... ids) {
        Defense.notNull(ids, "ids");
        List<LicenseRecordView> lst = new LinkedList<LicenseRecordView>();
        for (Integer id : ids) {
            lst.add(findById(id));
        }
        Collections.sort(lst);
        return lst;
    }

    @Override
    public List<LicenseRecordView> findByExample(LicenseRecordView example) {
        List<LicenseRecordView> lst = new LinkedList<LicenseRecordView>();
        for (LicenseRecordView o1 : findAll()) {
            if (EqualForExample(o1.getCode(), example.getCode()) &&
                    EqualForExample(o1.getCorrespondenseStudentLicenseQuantity(), example.getCorrespondenseStudentLicenseQuantity()) &&
                    EqualForExample(o1.getEducationalQualificationLevel(), example.getEducationalQualificationLevel()) &&
                    EqualForExample(o1.getKnowledgeAreaOrTrainingDirection(), example.getKnowledgeAreaOrTrainingDirection()) &&
                    EqualForExample(o1.getLicense(), example.getLicense()) &&
                    EqualForExample(o1.getLicenseIssueDate(), example.getLicenseIssueDate()) &&
                    EqualForExample(o1.getLicenseSerialNumber(), example.getLicenseSerialNumber()) &&
                    EqualForExample(o1.getStationaryStudentLicenseQuantity(), example.getStationaryStudentLicenseQuantity()) &&
                    EqualForExample(o1.getTerminationDate(), example.getTerminationDate()) &&
                    EqualForExample(o1.getTrainingDirectionOrSpeciality(), example.getTrainingDirectionOrSpeciality()) &&
                    EqualForExample(o1.getOrgUnit(), example.getOrgUnit())) {
                lst.add(o1);

            }
        }
        Collections.sort(lst);
        return lst;
    }

    private <X> boolean EqualForExample(X o1, X o2) {
        return o1 == null || o2 == null || o1.equals(o2);
    }

    @Override
    public List<LicenseRecordView> findAll(int firstResult, int maxResults, final SortCriterion... sortConstraints) {
        Defense.notNull(firstResult, "firstResult");
        Defense.notNull(maxResults, "maxResults");
        List<LicenseRecordView> sortedListAll = new LinkedList<LicenseRecordView>();
        sortedListAll.addAll(findAll());
        if (sortConstraints != null) {
            Collections.sort(sortedListAll, new Comparator<LicenseRecordView>() {

                @Override
                public int compare(LicenseRecordView o1, LicenseRecordView o2) {
                    for (SortCriterion sortConstraint : sortConstraints) {
                        if (sortConstraint == null) {
                            continue;
                        }
                        //А что если property не Object?
                        Object o1PropValue = PropertyValue.get(o1, sortConstraint.getProperty());
                        Object o2PropValue = PropertyValue.get(o2, sortConstraint.getProperty());
                        if (o1PropValue instanceof Comparable) {
                            int ret = ((Comparable) o1PropValue).compareTo(o2PropValue);
                            if (ret != 0) {
                                if (sortConstraint.isAscending()) {
                                    return ret;
                                } else {
                                    return -ret;
                                }
                            }
                        }
                    }
                    return 0;
                }
            });
        }
        return sortedListAll.subList(firstResult, Math.min(maxResults, sortedListAll.size()));
    }

    @Override
    public LicenseRecordView reattach(LicenseRecordView object) {
        getLrDAO().reattach(object.getStationaryLicenseRecord());
        getLrDAO().reattach(object.getCorrespondenseLicenseRecord());
        return object;
    }

    @Override
    public void delete(LicenseRecordView object) {
        getLrDAO().delete(object.getStationaryLicenseRecord());
        getLrDAO().delete(object.getCorrespondenseLicenseRecord());
    }

    @Override
    public void delete(Integer id) {
        LicenseRecordView object = findByStationary(getLrDAO().findById(id));
        delete(object);
    }

    @Override
    public void save(LicenseRecordView object) {
        getLrDAO().save(object.getStationaryLicenseRecord());
        getLrDAO().save(object.getCorrespondenseLicenseRecord());
    }

    @Override
    public LicenseRecordView update(LicenseRecordView object) {
        object.setStationaryLicenseRecord(getLrDAO().update(object.getStationaryLicenseRecord()));
        object.setCorrespondenseLicenseRecord(getLrDAO().update(object.getCorrespondenseLicenseRecord()));
        return object;
    }

    @Override
    public void evict(LicenseRecordView object) {
        getLrDAO().evict(object.getStationaryLicenseRecord());
        getLrDAO().evict(object.getCorrespondenseLicenseRecord());
    }

    @Override
    public boolean isPersistent(LicenseRecordView object) {
        return getLrDAO().isPersistent(object.getStationaryLicenseRecord()) &&
                getLrDAO().isPersistent(object.getCorrespondenseLicenseRecord());
    }

    @Override
    public void refresh(LicenseRecordView object) {
        getLrDAO().refresh(object.getStationaryLicenseRecord());
        getLrDAO().refresh(object.getCorrespondenseLicenseRecord());
    }

    @Override
    public SortCriterion[] getDefaultSortCriteria() {
        return new SortCriterion[0];
    }
}
