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
package br.com.arsmachina.authentication.springsecurity.ioc;

import br.com.arsmachina.authentication.controller.PasswordEncrypter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.AliasContribution;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.providers.encoding.PlaintextPasswordEncoder;
import org.springframework.security.userdetails.UserDetailsService;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.springsecurity.SpringSecurityMD5PasswordEncrypter;
import br.com.arsmachina.authentication.springsecurity.UserDetailsServiceImpl;
import br.com.arsmachina.authentication.springsecurity.internal.UserASORequestFilter;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import orion.cpu.security.AuthorizerUserAndRoleRequestFilter;
import orion.cpu.security.RoleSSORequestFilter;

/**
 * Tapestry-IoC module for generic-authentication-spring-security. It binds
 * {@link UserDetailsService} to {@link UserDetailsServiceImpl}.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestrySpringSecurityGenericAuthenticationModule {

    /**
     * Name of the symbol that defines if we allow multiple simultaneous logins by the same user.
     */
    public static final String ALLOW_SIMULTANEOUS_LOGINS_SYMBOL = "generic-authentication-spring-security.allow-simultaneuos-logins";
    /**
     * Name of the symbol that defines if the {@link Md5PasswordEncoder} will be used
     * by Spring Security as the {@link PasswordEncoder} intead of {@link PlaintextPasswordEncoder}.
     * The default value is <code>true</code>.
     */
    public static final String USE_MD5_PASSWROD_ENCODER_SYMBOL = "generic-authentication-spring-security.use-md5-encoder";
    private static final String COMPONENT_PREFIX = "tssga";

    /**
     * Creates the {@link UserDetailsService} instantiating {@link UserDetailsServiceImpl}.
     *
     * @param controller an {@link UserController}.
     * @param allowSimultaneousLogins a <code>boolean</code>.
     */
    public static UserDetailsService buildUserDetailsService(UserController controller,
            @Inject @Symbol(ALLOW_SIMULTANEOUS_LOGINS_SYMBOL) boolean allowSimultaneousLogins) {
        return new UserDetailsServiceImpl(controller, allowSimultaneousLogins);
    }

    /**
     * Contributes the {@link UserASORequestFilter} to the {@link RequestHandler} service.
     *
     * @param configuration
     * @param filter an {@link UserASORequestFilter}.
     */
    public static void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration) {

        configuration.addInstance("useraso", UserASORequestFilter.class);
        configuration.addInstance("rolesso", RoleSSORequestFilter.class, "after:useraso");

    }

    public static void contributeHttpServletRequestHandler(OrderedConfiguration<HttpServletRequestFilter> configuration) {
        configuration.addInstance("authorizerUserAndRole", AuthorizerUserAndRoleRequestFilter.class,"after:springSecurity*");

    }

    public static void contributeProviderManager(
            OrderedConfiguration<AuthenticationProvider> configuration,
            @InjectService("DaoAuthenticationProvider") AuthenticationProvider daoAuthenticationProvider) {

        configuration.add("daoAuthenticationProvider", daoAuthenticationProvider);

    }

    /**
     * Defines the default value for {@link #ALLOW_SIMULTANEOUS_LOGINS_SYMBOL}
     * (<code>false</code>) and {@link #USE_MD5_PASSWROD_ENCODER_SYMBOL} (<code>true</code>).
     *
     * @param configuration
     */
    public static void contributeFactoryDefaults(
            MappedConfiguration<String, String> configuration) {

        configuration.add(ALLOW_SIMULTANEOUS_LOGINS_SYMBOL, "true");
        configuration.add(USE_MD5_PASSWROD_ENCODER_SYMBOL, "true");

    }

    /**
     * Defines the default value for <code>spring-security.password.salt</code>
     * from the <code>salt.properties</code> file in the root of the classpath, if found.
     *
     * @param configuration
     */
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration) {

        Properties properties = new Properties();

        InputStream inputStream = TapestrySpringSecurityGenericAuthenticationModule.class.getResourceAsStream("/salt.properties");

        if (inputStream == null) {
            throw new RuntimeException("salt.properties was not found in the classpath");
        }

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            final String message = "Exception trying to load salt.properties from the classpath";
            throw new RuntimeException(message, e);
        }

        String salt = properties.getProperty("salt");

        if (salt != null) {
            configuration.add("spring-security.password.salt", salt);
        } else {
            throw new RuntimeException("salt.properties must have a salt property");
        }

    }

    /**
     * Changes the {@link PasswordEncoder} used by Spring Security from
     * {@link PlaintextPasswordEncoder} to {@link Md5PasswordEncoder}.
     *
     * @param configuration a {@link Configuration}.
     */
    public static void contributeAlias(
            Configuration<AliasContribution<PasswordEncoder>> configuration,
            @Inject @Symbol(USE_MD5_PASSWROD_ENCODER_SYMBOL) boolean useMd5) {

        if (useMd5) {

            final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
            configuration.add(AliasContribution.create(PasswordEncoder.class, passwordEncoder));

        }

    }

    /**
     * Contributes this package components under the <code>tssga</code> prefix.
     *
     * @param configuration a {@link Configuration}.
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {

        configuration.add(new LibraryMapping(COMPONENT_PREFIX,
                "br.com.arsmachina.authentication.springsecurity"));

    }

    /**
     * Создает сервис PasswordEncrypter.
     * @param salt
     * @return сервис
     */
    public static PasswordEncrypter build(@Inject @Symbol("spring-security.password.salt")
            final String salt) {
        return new SpringSecurityMD5PasswordEncrypter(salt);
    }
}
