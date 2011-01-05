package ua.orion.core.services;

import ua.orion.core.services.ModelLibraryService;
import ua.orion.core.services.ModelLibraryServiceImpl;
import java.util.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.bar.controllers.HibernatePersonController;
import test.foo.dao.PersonDAO;
import test.foo.entities.Person;
import ua.orion.core.utils.ModelLibraryInfo;

public class ModelLibraryServiceImplTest {

    final ModelLibraryService mls = new ModelLibraryServiceImpl(Arrays.asList(new ModelLibraryInfo("Foo", "test.foo"),
            new ModelLibraryInfo("Bar", "test.bar")));

    @Test
    public void model_library() {
        Assert.assertEquals(mls.resolveEntityOrientedBeanClasses("Person", "dao", null, "DAO").toArray()[0], PersonDAO.class);
        Assert.assertTrue(mls.resolveEntityOrientedBeanClasses("Person", "dao", null, "DAOdsfbb").isEmpty());
        Assert.assertEquals(mls.resolveEntityOrientedBeanClasses(Person.class, "controllers", "Hibernate", "Controller").toArray()[0], HibernatePersonController.class);
    }
}
