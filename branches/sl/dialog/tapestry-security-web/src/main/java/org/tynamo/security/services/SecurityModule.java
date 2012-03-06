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
package org.tynamo.security.services;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Properties;
import org.apache.shiro.ShiroException;
import org.apache.shiro.mgt.AuthorizingSecurityManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.util.ClassUtils;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.services.*;
import org.slf4j.Logger;
import org.tynamo.security.SecurityComponentRequestFilter;
import org.tynamo.security.SecuritySymbols;
import org.tynamo.security.ShiroAnnotationWorker;
import org.tynamo.security.ShiroExceptionHandler;
import org.tynamo.security.core.ConjunctionModularRealmAuthorizer;
import org.tynamo.security.core.SecurityCoreSymbols;
import org.tynamo.security.core.services.RealmSource;
import org.tynamo.security.filter.SecurityRequestFilter;
import org.tynamo.security.services.impl.ClassInterceptorsCacheImpl;
import org.tynamo.security.services.impl.PageServiceImpl;
import org.tynamo.security.services.impl.SecurityServiceImpl;
import org.tynamo.shiro.extension.authz.aop.AopHelper;
import org.tynamo.shiro.extension.authz.aop.DefaultSecurityInterceptor;

/**
 * The main entry point for Security integration.
 *
 */
public class SecurityModule {
    
    private static final String EXCEPTION_HANDLE_METHOD_NAME = "handleRequestException";
    private static final String PATH_PREFIX = "security";
    private static String version = "unversioned";
    
    static {
        Properties moduleProperties = new Properties();
        try {
            moduleProperties.load(SecurityModule.class.getResourceAsStream("module.properties"));
            version = moduleProperties.getProperty("module.version");
        } catch (IOException e) {
            // ignore
        }
    }
    
    public static void bind(final ServiceBinder binder) {
        
        binder.bind(ClassInterceptorsCache.class, ClassInterceptorsCacheImpl.class);
        binder.bind(SecurityService.class, SecurityServiceImpl.class);
        binder.bind(ComponentRequestFilter.class, SecurityComponentRequestFilter.class);
        binder.bind(ShiroExceptionHandler.class);
        binder.bind(PageService.class, PageServiceImpl.class);
    }
    
    public static SecurityRequestFilter buildSecurityRequestFilter(RealmSource realmSource,
            @Autobuild SecurityRequestFilter filter,
            @Symbol(SecurityCoreSymbols.CONJUCTION_AUTHORITY) final boolean isConjuction) {
        if (isConjuction && filter.getSecurityManager() instanceof AuthorizingSecurityManager) {
            ((AuthorizingSecurityManager) filter.getSecurityManager()).setAuthorizer(new ConjunctionModularRealmAuthorizer());
        }
        if (filter.getSecurityManager() instanceof RealmSecurityManager) {
            ((RealmSecurityManager) filter.getSecurityManager()).setRealms(realmSource.getRealms());
        }
        return filter;
    }
    
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(SecuritySymbols.LOGIN_URL, "/" + PATH_PREFIX + "/login");
        configuration.add(SecuritySymbols.SUCCESS_URL, "/index");
        configuration.add(SecuritySymbols.UNAUTHORIZED_URL, "/" + PATH_PREFIX + "/unauthorized");
        configuration.add(SecuritySymbols.DEFAULTSIGNINPAGE, "/defaultSignInPage");
    }

    /**
     * Create ClassInterceptorsCache through annotations on the class page,
     * which then will use SecurityFilter.
     * <p/>
     */
    public void contributeApplicationInitializer(OrderedConfiguration<ApplicationInitializerFilter> configuration,
            final ComponentClassResolver componentClassResolver,
            final ClassInterceptorsCache classInterceptorsCache) {
        
        configuration.add("SecurityApplicationInitializerFilter", new ApplicationInitializerFilter() {
            
            @Override
            public void initializeApplication(Context context, ApplicationInitializer initializer) {
                
                initializer.initializeApplication(context);
                
                for (String name : componentClassResolver.getPageNames()) {
                    String className = componentClassResolver.resolvePageNameToClassName(name);
                    Class<?> clazz = ClassUtils.forName(className);
                    
                    while (clazz != null) {
                        for (Class<? extends Annotation> annotationClass : AopHelper.getAutorizationAnnotationClasses()) {
                            Annotation classAnnotation = clazz.getAnnotation(annotationClass);
                            if (classAnnotation != null) {
                                //Add in the cache which then will be used in RequestFilter
                                classInterceptorsCache.add(className, new DefaultSecurityInterceptor(classAnnotation));
                            }
                        }
                        clazz = clazz.getSuperclass();
                    }
                }
            }
        });
    }
    
    public static void contributeComponentRequestHandler(OrderedConfiguration<ComponentRequestFilter> configuration,
            @Local ComponentRequestFilter filter) {
        configuration.add("SecurityFilter", filter, "before:*");
    }
    
    public static void contributeComponentClassTransformWorker(OrderedConfiguration<ComponentClassTransformWorker> configuration) {
        configuration.addInstance(ShiroAnnotationWorker.class.getSimpleName(), ShiroAnnotationWorker.class);
    }
    
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping(PATH_PREFIX, "org.tynamo.security"));
    }
    
    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
        configuration.add(PATH_PREFIX + "-" + version, "org/tynamo/security");
    }

    /**
     * Advise current RequestExceptionHandler for we can catch ShiroException
     * exceptions and handle this.
     *
     * @see org.tynamo.security.ShiroExceptionHandler
     */
    @Match("RequestExceptionHandler")
    public static void adviseShiroRequestExceptionHandler(MethodAdviceReceiver receiver,
            final Logger logger,
            final RequestGlobals requestGlobals,
            final Response response,
            final SecurityService securityService,
            final ShiroExceptionHandler handler) {
        
        Method handleMethod;
        
        try {
            Class<?> serviceInterface = receiver.getInterface();
            handleMethod = serviceInterface.getMethod(EXCEPTION_HANDLE_METHOD_NAME, Throwable.class);
        } catch (Exception e) {
            throw new RuntimeException("Can't find method  "
                    + "RequestExceptionHandler." + EXCEPTION_HANDLE_METHOD_NAME + ". Changed API?", e);
        }
        
        MethodAdvice advice = new MethodAdvice() {
            
            @Override
            public void advise(Invocation invocation) {
                Throwable exception = (Throwable) invocation.getParameter(0);
                
                ShiroException shiroException = null;
                
                while (exception != null) {
                    if (exception instanceof ShiroException) {
                        shiroException = (ShiroException) exception;
                        break;
                    } else {
                        exception = exception.getCause();
                    }
                }
                
                if (shiroException != null) {
                    
                    try {
                        handler.handle(shiroException);
                    } catch (Exception e) {
                        logger.error("Error handling SecurityException", e);
                        invocation.proceed();
                    }
                    
                } else {
                    invocation.proceed();
                }
            }
        };
        receiver.adviseMethod(handleMethod, advice);
    }
    
    public static void contributeHttpServletRequestHandler(OrderedConfiguration<HttpServletRequestFilter> configuration,
            SecurityRequestFilter securityRequestFilter) {
        configuration.add("SecurityRequestFilter", securityRequestFilter, "after:StoreIntoGlobals");
    }
}
