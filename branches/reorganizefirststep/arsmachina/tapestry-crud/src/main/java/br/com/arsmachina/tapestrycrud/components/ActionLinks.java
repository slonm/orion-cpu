// Copyright 2008 Thiago H. de Paula Figueiredo
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
package br.com.arsmachina.tapestrycrud.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;

import br.com.arsmachina.tapestrycrud.Constants;
import org.apache.tapestry5.annotations.Import;

/**
 * Component that renders the action links in a listing page. It is meant to be used in a
 * {@link Grid} column. <a href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/ListProject.tml?view=markup"
 * >Ars Machina Project Example</a>. The default icons used are taken from the <a
 * href="http://www.famfamfam.com/lab/icons/silk/">Silk</a> icon set (Creative Commons Attribution
 * 2.5 License).
 * 
 * @author sl
 */
@Import(stylesheet=Constants.TAPESTRY_CRUD_CSS_ASSET)
public class ActionLinks {

    /**
     * Show the edit link?
     */
    @Parameter(value="true", allowNull=false, defaultPrefix=BindingConstants.LITERAL)
    @Property
    private Boolean edit;
    /**
     * Show the remove link?
     */
    @Parameter(value="true", allowNull=false, defaultPrefix=BindingConstants.LITERAL)
    @Property
    private Boolean remove;
    /**
     * Show the view link?
     */
    @Parameter(value="true", allowNull=false, defaultPrefix=BindingConstants.LITERAL)
    @Property
    private Boolean view;
}
