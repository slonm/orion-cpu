/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.tynamo.security.core.services;

import org.tynamo.security.core.SecurityCoreSymbols;
import org.apache.shiro.util.ThreadContext;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Order;
import org.tynamo.shiro.extension.authz.aop.AopHelper;
import org.tynamo.shiro.extension.authz.aop.SecurityInterceptor;

import java.lang.reflect.Method;
import java.util.List;
import org.tynamo.security.core.services.impl.RealmSourceImpl;

/**
 * The main entry point for Security integration.
 *
 */
public class SecurityCoreModule
{

	public static void bind(final ServiceBinder binder)
	{
		binder.bind(RealmSource.class, RealmSourceImpl.class);
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
	{
		configuration.add(SecurityCoreSymbols.CONFIG_PATH, "classpath:shiro.ini");
		configuration.add(SecurityCoreSymbols.SHOULD_LOAD_INI_FROM_CONFIG_PATH, "false");
		configuration.add(SecurityCoreSymbols.ENABLED, "true");
	}

	/**
	 * Secure all service methods that are marked with authorization annotations.
	 * <p/>
	 * <b>Restriction:</b> Only service interfaces can be annotated.
	 */
	@Match("*")
	@Order("before:*")
	public static void adviseSecurityAssert(MethodAdviceReceiver receiver)
	{

		Class<?> serviceInterface = receiver.getInterface();

		for (Method method : serviceInterface.getMethods())
		{

			List<SecurityInterceptor> interceptors =
					AopHelper.createSecurityInterceptorsSeeingInterfaces(method, serviceInterface);

			for (final SecurityInterceptor interceptor : interceptors)
			{
				MethodAdvice advice = new MethodAdvice()
				{
					@Override
					public void advise(Invocation invocation)
					{
						// Only (try to) intercept if subject is bound.
						// This is useful in case background or initializing operations
						// call service operations that are secure
						if (ThreadContext.getSubject() != null) interceptor.intercept();
						invocation.proceed();

					}
				};
				receiver.adviseMethod(method, advice);
			}

		}
	}
}
