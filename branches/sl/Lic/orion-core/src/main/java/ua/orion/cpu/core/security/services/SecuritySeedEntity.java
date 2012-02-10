package ua.orion.cpu.core.security.services;

import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.security.OrionSecuritySymbols;

/**
 *
 * @author sl
 */
//@AfterLibrary(OrgUnitsSymbols.ORG_UNITS)
public class SecuritySeedEntity {
    public SecuritySeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(OrionSecuritySymbols.SECURITY_LIB));
        if (testData) {
        }
    }
}
