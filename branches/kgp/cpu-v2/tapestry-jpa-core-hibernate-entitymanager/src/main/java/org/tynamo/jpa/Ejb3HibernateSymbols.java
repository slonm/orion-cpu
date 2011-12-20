// Copyright 2010 The Apache Software Foundation
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

package org.tynamo.jpa;

/**
 * Configuration symbols, for use with contributions to {@link org.apache.tapestry5.ioc.services.ApplicationDefaults}.
 *
 */
public class Ejb3HibernateSymbols
{
    /**
     * If true, then the {@link org.tynamo.jpa.internal.Ejb3HibernateEntityManagerSourceImpl} will invoke {@link
     * org.hibernate.ejb.Ejb3Configuration#configure(hibernateConfig)}, to read the application's 
     * <code>hibernate.cfg.xml</code>. This
     * should be set to false for applications that configure exclusively in code.
     */
    public static final String DEFAULT_CONFIGURATION = "tapestry.ejb3hibernate.default-configuration";
    
    public static final String HIBERNATE_CONFIG_LOCATION = "tapestry.ejb3hibernate.config-location";
    
    public static final String CREATE_SCHEMA_STATEMENT = "tapestry.ejb3hibernate.create-schema-statement";
    
    public static final String IMPROVED_NAMING_STRATEGY = "tapestry.ejb3hibernate.improved-naming-strategy";
}
