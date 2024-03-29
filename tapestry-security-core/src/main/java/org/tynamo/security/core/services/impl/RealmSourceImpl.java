/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements.  See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership.  The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License.  You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.  See the License for the specific language governing permissions and limitations
 * under the License.
 */
package org.tynamo.security.core.services.impl;

import java.util.Collection;
import java.util.Collections;
import org.apache.shiro.realm.Realm;
import org.tynamo.security.core.services.RealmSource;


/**
 * DOCUMENT ME!
 *
 * @see SecurityService
 */
public class RealmSourceImpl implements RealmSource
{

	private final Collection<Realm> realms;

        //~ Methods ------------------------------------------------------------------------------------

        public RealmSourceImpl(Collection<Realm> realms) {
            this.realms = Collections.unmodifiableCollection(realms);
        }
        
        @Override
        public Collection<Realm> getRealms() {
            return realms;
        }

} 
