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

package br.com.arsmachina.authentication.springsecurity.components;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.Request;

/**
 * Component that provides a Spring Security-aware login component. It is generated as a
 * <code>&lt;form class="tssga-login"&gt;...&lt;form&gt;</code> tag.
 * The login submit button takes its label from the <code>button.login</code>
 * internationalization message.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class Login {

	@Inject
	@Value("${spring-security.check.url}")
	private String checkUrl;

	@Inject
	private Request request;

	private boolean failed = false;

	public boolean isFailed() {
		return failed;
	}

	/**
	 * Returns the Spring Security check URL.
	 * 
	 * @return a {@link String}.
	 */
	public String getLoginCheckUrl() {
		return request.getContextPath() + checkUrl;
	}

}
