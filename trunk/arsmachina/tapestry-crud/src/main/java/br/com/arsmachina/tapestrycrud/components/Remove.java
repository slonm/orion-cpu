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

import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.mixins.DiscardBody;

/**
 * A component that doesn't render its tag nor its body. It is used for 
 * previewability purposes and works a lot like Tapestry 4's <code>$remove$</code>.
 * One example can be found
 * <a href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/Index.tml?view=markup"
 * 		>in the Ars Machina Project Example Application</a>.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class Remove {

	@Mixin
	@SuppressWarnings("unused")
	private DiscardBody discardBody;
	
	@SetupRender
	public boolean nothing() {
		return true;
	}

}
