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
package org.tynamo.jpa.internal;

import java.util.List;
import org.tynamo.jpa.JPAEntityManagerSource;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.ejb.Ejb3Configuration;
import org.tynamo.jpa.Ejb3HibernateConfigurer;
import org.tynamo.jpa.Ejb3HibernateSymbols;

public class Ejb3HibernateEntityManagerSourceImpl implements JPAEntityManagerSource, RegistryShutdownListener {

    private final EntityManagerFactory entityManagerFactory;

    public Ejb3HibernateEntityManagerSourceImpl(Logger logger, 
            @Symbol(Ejb3HibernateSymbols.HIBERNATE_CONFIG_LOCATION) String hibernateCfgLocation,
            @Symbol(Ejb3HibernateSymbols.DEFAULT_CONFIGURATION) boolean defaultConfiguration,
            List<Ejb3HibernateConfigurer> hibernateConfigurers) {
        long startTime = System.currentTimeMillis();

        Ejb3Configuration configuration = new Ejb3Configuration();
        for (Ejb3HibernateConfigurer configurer : hibernateConfigurers) {
            configurer.configure(configuration);
        }

        if (defaultConfiguration) {
            configuration.configure(hibernateCfgLocation);
        }

        long configurationComplete = System.currentTimeMillis();

        entityManagerFactory = configuration.buildEntityManagerFactory();

        long factoryCreated = System.currentTimeMillis();

        logger.info(JPACoreMessages.startupTiming(configurationComplete - startTime, factoryCreated - startTime));
    }

    public EntityManager create() {
        return entityManagerFactory.createEntityManager();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void registryDidShutdown() {
        entityManagerFactory.close();
    }
}
