// Copyright 2007, 2008 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.tynamo.jpa.internal.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.tapestry5.ioc.test.IOCTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.jpa.JPAEntityManagerSource;
import org.tynamo.jpa.example.app0.entities.User;
import org.tynamo.jpa.internal.Ejb3HibernateEntityManagerSourceImpl;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import org.apache.tapestry5.ioc.internal.services.ClassNameLocatorImpl;
import org.apache.tapestry5.ioc.internal.services.ClasspathURLConverterImpl;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.tynamo.jpa.Ejb3HibernateConfigurer;
import org.tynamo.jpa.JPAEntityPackageManager;
import org.tynamo.jpa.internal.DatabaseSchemaObjectCreator;
import org.tynamo.jpa.internal.PackageNameEjb3HibernateConfigurer;

public class Ejb3HibernateEntityManagerSourceImplTest extends IOCTestCase {

    private final Logger log = LoggerFactory.getLogger("tapestry.jpa.JPAEntityManagerSourceTest");

    @Test
    public void startup_without_packages() {

        final Collection<String> packageNames = Arrays.asList("org.tynamo.jpa.example.app0.entities");
        JPAEntityPackageManager packageManager = new JPAEntityPackageManager() {

            public Collection<String> getPackageNames() {
                return packageNames;
            }
        };
        ClassNameLocator classNameLocator=new ClassNameLocatorImpl(new ClasspathURLConverterImpl());
        List<Ejb3HibernateConfigurer> filters = Arrays.asList(
                new PackageNameEjb3HibernateConfigurer(packageManager, classNameLocator),
                new DatabaseSchemaObjectCreator(log,packageManager, classNameLocator, "CREATE SCHEMA %s AUTHORIZATION SA"));
//        replay();

        JPAEntityManagerSource source = new Ejb3HibernateEntityManagerSourceImpl(log, "hibernate.cfg.xml", true, filters);

        EntityManager entityManager = source.create();
        Assert.assertNotNull(entityManager);

        // make sure it found the entity in the package
        EntityType<User> etype = entityManager.getEntityManagerFactory().getMetamodel().entity(User.class);
        Assert.assertEquals(etype.getName(), "org.tynamo.jpa.example.app0.entities.User");
        User user=new User();
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setEmail("test@test.test");
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
//        verify();
    }
}
