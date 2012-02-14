// Copyright 2008, 2010 The Apache Software Foundation
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

package ua.orion.web.mixins;

import javax.inject.Inject;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.HeartbeatDeferred;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Подмешиватель применяется к компоненту {@link org.apache.tapestry5.corelib.components.Select}
 * для привязки его к конкретному свойству сущности
 */
public class EntitySelect
{
    @InjectContainer
    private Select container;

    @Parameter(required = true, allowNull = false)
    private Object entity;
    
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String property;

    @Inject
    private ComponentResources resources;
    
    void setupRender()
    {
    }
}
