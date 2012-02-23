package ua.orion.cpu.core.licensing.services;

import java.util.*;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.cpu.core.orgunits.entities.*;

/**
 *
 * @author sl
 */
public interface LicensingService {

    List<LicenseRecord> findLicenseRecordsByOrgUnit(OrgUnit orgUnit);

    List<LicenseRecord> findLicenseRecordsByTerminationDate(Date terminationDate);

    List<LicenseRecord> findLicenseRecordsByEducationalQualificationLevel(String codeLevel);

    List<LicenseRecord> findLicenseRecordsByTrainingDirection(String codeDirection);

    boolean existsForcedLicense();
}
