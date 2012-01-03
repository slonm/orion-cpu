package ua.orion.core.services;

import java.util.Arrays;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.test.IOCTestCase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.licensing.service.LicensingIOCModule;
import test.licensing.service.LicensingSeedEntity;
import ua.orion.core.ModelLibraryInfo;

/**
 *
 * @author sl
 */
public class SeedEntityTest extends IOCTestCase{
    
    
    @BeforeClass
    public void setUp() throws Exception {
    }
    
    @Test
    public void testLicensingSeedEntity() {
//    Registry registry=this.buildRegistry(OrionCoreIOCModule.class, LicensingIOCModule.class);
//    registry.performRegistryStartup();
//    JPATransactionManager tm=registry.getService(JPATransactionManager.class);
//    new LicensingSeedEntity(registry.getService(EntityService.class));
//    tm.commit();
//    new LicensingSeedEntity(registry.getService(EntityService.class));
//    tm.commit();
//    registry.shutdown();
    }

}
