package ua.orion.core.services;

import java.util.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.bar.controllers.HibernatePersonController;
import test.foo.dao.PersonDAO;
import test.foo.entities.Person;
import test.foo.services.FooService;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.core.annotations.AfterLibrary;
import ua.orion.core.annotations.BeforeLibrary;

public class ModelLibraryServiceImplTest {

    class Lib1Service {
    };

    @AfterLibrary("Lib1")
    class Lib2Service {
    };

    @AfterLibrary({"Lib1", "Lib2"})
    class Lib3Service {
    };

    @BeforeLibrary("Lib2")
    class Lib4Service {
    };
    final ModelLibraryService mls = new ModelLibraryServiceImpl(Arrays.asList(new ModelLibraryInfo("Foo", "test.foo"),
            new ModelLibraryInfo("Bar", "test.bar")));

    @Test
    public void resolveEntityOrientedBeanClasses() {
        Assert.assertEquals(mls.resolveEntityOrientedBeanClasses("Person", "dao", null, "DAO").toArray()[0], PersonDAO.class);
        Assert.assertTrue(mls.resolveEntityOrientedBeanClasses("Person", "dao", null, "DAOdsfbb").isEmpty());
        Assert.assertEquals(mls.resolveEntityOrientedBeanClasses(Person.class, "controllers", "Hibernate", "Controller").toArray()[0], HibernatePersonController.class);
    }

    @Test
    public void resolveLibraryOrientedBeanClasses() {
        Assert.assertEquals(mls.resolveLibraryOrientedBeanClasses("services", null, "Service").toArray()[0], FooService.class);
        Assert.assertTrue(mls.resolveLibraryOrientedBeanClasses("services", null, "Ololo").isEmpty());
    }

    @Test
    public void classServiceOrderer() {
        Map<Class<?>, String> map = new HashMap();
        map.put(Lib3Service.class, "Lib3");
        map.put(Lib4Service.class, "Lib4");
        map.put(Lib2Service.class, "Lib2");
        map.put(Lib1Service.class, "Lib1");
        List<Class<?>> ordered = new ArrayList();
        ordered.addAll(map.keySet());
        ModelLibraryServiceImpl.sort(ordered, map);
        List<Class<?>> etalon = new ArrayList();
        etalon.add(Lib1Service.class);
        etalon.add(Lib4Service.class);
        etalon.add(Lib2Service.class);
        etalon.add(Lib3Service.class);
        Assert.assertEquals(etalon, ordered);
    }
}
