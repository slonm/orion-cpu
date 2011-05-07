package ua.orion.cpu.licensing.services;

import java.util.*;
import ua.orion.cpu.licensing.entities.*;
import ua.orion.cpu.orgunits.entities.*;

/**
 *
 * @author sl
 */
public interface LicensingService {

    List<LicenseRecord> findLicenseRecordsByOrgUnit(OrgUnit orgUnit);

    List<LicenseRecord> findLicenseRecordsByTerminationDate(Date terminationDate);

    List<LicenseRecord> findLicenseRecordsByEducationalQualificationLevel(String codeLevel);

    List<LicenseRecord> findLicenseRecordsByTrainingDirection(String codeDirection);
}
