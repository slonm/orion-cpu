/*
 * Copyright 2010 sl.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tynamo.jpa;

import java.util.Collection;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.ejb.Ejb3Configuration;
import org.tynamo.jpa.internal.DatabaseSchemaObjectCreator;
import org.tynamo.jpa.internal.Ejb3HibernateEntityManagerSourceImpl;
import org.tynamo.jpa.internal.PackageNameEjb3HibernateConfigurer;

/**
 *
 * @author sl
 */
public class Ejb3HibernateModule {

    public static JPAEntityManagerSource buildEjb3HibernateEntityManagerSource(
            RegistryShutdownHub hub, @Autobuild Ejb3HibernateEntityManagerSourceImpl source) {
        hub.addRegistryShutdownListener(source);
        return source;
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(Ejb3HibernateSymbols.DEFAULT_CONFIGURATION, "true");
        configuration.add(Ejb3HibernateSymbols.HIBERNATE_CONFIG_LOCATION, "hibernate.cfg.xml");
        configuration.add(Ejb3HibernateSymbols.CREATE_SCHEMA_STATEMENT, "");
        configuration.add(Ejb3HibernateSymbols.IMPROVED_NAMING_STRATEGY, "true");
    }

    public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration,
            @Local JPAEntityManagerSource override) {
        configuration.add(JPAEntityManagerSource.class, override);
    }

    /**
     * Adds the configurers: <dl> <dt>PackageName
     * <dd> loads entities by package name</dl>
     */
    public static void contributeEjb3HibernateEntityManagerSource(
            OrderedConfiguration<Ejb3HibernateConfigurer> config,
            @Symbol(Ejb3HibernateSymbols.CREATE_SCHEMA_STATEMENT) String createSchemaStatement,
            @Symbol(Ejb3HibernateSymbols.IMPROVED_NAMING_STRATEGY) boolean improvedNamingStrategy) {
        if(improvedNamingStrategy){
            config.add("ImprovedNamingStrategy", new Ejb3HibernateConfigurer() {

                public void configure(Ejb3Configuration configuration) {
                    configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
                }
            });
        }        
        if (!createSchemaStatement.isEmpty()) {
            config.addInstance("SchemaCreator", DatabaseSchemaObjectCreator.class);
        }
        config.addInstance("PackageName", PackageNameEjb3HibernateConfigurer.class);
    }

    public static JPAEntityPackageManager buildJPAEntityPackageManager(
            final Collection<String> packageNames) {
        return new JPAEntityPackageManager() {

            public Collection<String> getPackageNames() {
                return packageNames;
            }
        };
    }
}
