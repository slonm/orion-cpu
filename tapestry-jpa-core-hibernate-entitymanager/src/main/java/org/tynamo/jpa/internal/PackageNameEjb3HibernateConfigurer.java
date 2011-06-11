// Copyright 2007, 2008, 2010 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.tynamo.jpa.internal;

import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.hibernate.ejb.Ejb3Configuration;
import org.tynamo.jpa.Ejb3HibernateConfigurer;
import org.tynamo.jpa.JPAEntityPackageManager;

/**
 * Adds entity classes from a given set of packages to the configuration.
 */
public final class PackageNameEjb3HibernateConfigurer implements Ejb3HibernateConfigurer
{
    private final JPAEntityPackageManager packageManager;

    private final ClassNameLocator classNameLocator;

    public PackageNameEjb3HibernateConfigurer(JPAEntityPackageManager packageManager,
            ClassNameLocator classNameLocator)
    {
        this.packageManager = packageManager;
        this.classNameLocator = classNameLocator;
    }

    public void configure(Ejb3Configuration configuration)
    {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        for (String packageName : packageManager.getPackageNames())
        {
            configuration.addPackage(packageName);

            for (String className : classNameLocator.locateClassNames(packageName))
            {
                try
                {
                    Class entityClass = contextClassLoader.loadClass(className);

                    configuration.addAnnotatedClass(entityClass);
                }
                catch (ClassNotFoundException ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
