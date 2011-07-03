package ua.orion.cpu.core.security.services;

import ua.orion.cpu.core.orgunits.services.*;
import ua.orion.cpu.core.orgunits.OrgUnitsSymbols;
import ua.orion.cpu.core.orgunits.entities.Chair;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.security.entities.Acl;
import ua.orion.cpu.core.security.entities.SubjectType;

/**
 *
 * @author sl
 */
//@AfterLibrary(OrgUnitsSymbols.ORG_UNITS)
public class SecuritySeedEntity {
    public SecuritySeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(OrgUnitsSymbols.ORG_UNITS_LIB));
        if (testData) {
        }
    }
}
