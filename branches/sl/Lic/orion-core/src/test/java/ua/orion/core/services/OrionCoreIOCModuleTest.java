package ua.orion.core.services;

import java.util.*;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.test.IOCTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.DAO;
import test.foo.entities.Person;
import ua.orion.core.EntityOrientedBeanFactory;

public class OrionCoreIOCModuleTest extends IOCTestCase{

    @Test
    public void services() {
//        Registry registry=this.buildRegistry(OrionCoreIOCModule.class, TestIOCModule.class, Ejb3HibernateModule.class, JPACoreModule.class);
//        
//        ApplicationMessagesSource ams = registry.getService(ApplicationMessagesSource.class);
//        Messages m= ams.getMessages(Locale.ENGLISH);
//        Assert.assertTrue(m.contains("transliterated.name.for.Apple"));
//        
//        ModelLibraryService mls = registry.getService(ModelLibraryService.class);
//        
//        EntityOrientedBeanFactory factory=new EntityOrientedBeanFactory(mls,registry, "dao", null, "DAO");
//        
//        Assert.assertNotNull(factory.create(DAO.class, Person.class));
//        registry.shutdown();
    }
}
