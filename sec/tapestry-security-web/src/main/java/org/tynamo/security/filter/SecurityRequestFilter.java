package org.tynamo.security.filter;

import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.IniShiroFilter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.slf4j.Logger;
import org.tynamo.security.FilterChainDefinition;
import org.tynamo.security.SecuritySymbols;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.tynamo.security.core.SecurityCoreSymbols;

@ServiceId("SecurityRequestFilter")
public class SecurityRequestFilter extends IniShiroFilter implements HttpServletRequestFilter
{

	@SuppressWarnings("unused")
	private Logger logger;

	private String loginUrl;
	private String unauthorizedUrl;
	private String successUrl;
        final boolean dummyLogin;

	public SecurityRequestFilter(List<FilterChainDefinition> filterChainDefinitions,
	                             Logger logger,
	                             @Inject @Symbol(SecuritySymbols.SUCCESS_URL) String successUrl,
	                             @Inject @Symbol(SecuritySymbols.LOGIN_URL) String loginUrl,
	                             @Inject @Symbol(SecuritySymbols.UNAUTHORIZED_URL) String unauthorizedUrl,
	                             @Inject @Symbol(SecurityCoreSymbols.CONFIG_PATH) String configPath,
	                             @Inject @Symbol(SecurityCoreSymbols.SHOULD_LOAD_INI_FROM_CONFIG_PATH) boolean shouldLoadIniFromPath,
	                             @Inject @Symbol(SecurityCoreSymbols.ENABLED) final boolean enabled,
	                             ApplicationGlobals globals) throws Exception
	{
		final ServletContext servletContext = globals.getServletContext();

		this.logger = logger;
		this.loginUrl = loginUrl;
		this.unauthorizedUrl = unauthorizedUrl;
		this.successUrl = successUrl;
                this.dummyLogin=!enabled;

		if (shouldLoadIniFromPath)
		{
			setConfigPath(configPath);
			init(new FilterConfig()
			{

				@Override
				public String getFilterName()
				{
					return "ShiroFilter";
				}

				@Override
				public ServletContext getServletContext()
				{
					return servletContext;
				}

				@Override
				public String getInitParameter(String name)
				{
					return null;  //To change body of implemented methods use File | Settings | File Templates.
				}

				@SuppressWarnings("rawtypes")
				@Override
				public Enumeration getInitParameterNames()
				{
					return new Enumeration()
					{

						@Override
						public boolean hasMoreElements()
						{
							return false;
						}

						@Override
						public Object nextElement()
						{
							return null;  //To change body of implemented methods use File | Settings | File Templates.
						}
					};
				}
			}
			);
		}

		PathMatchingFilterChainResolver chainResolver = (PathMatchingFilterChainResolver) getFilterChainResolver();
		if (chainResolver == null)
		{
			FilterChainManager manager = new DefaultFilterChainManager();
			//Expose the constructed FilterChainManager by first wrapping it in a
			// FilterChainResolver implementation. The ShiroFilter implementations
			// do not know about FilterChainManagers - only resolvers:
			chainResolver = new PathMatchingFilterChainResolver();
			chainResolver.setFilterChainManager(manager);
			setFilterChainResolver(chainResolver);
		}

		Map<String, Filter> defaultFilters = chainResolver.getFilterChainManager().getFilters();
		//apply global settings if necessary:
		for (Filter filter : defaultFilters.values())
		{
			applyGlobalPropertiesIfNecessary(filter);
		}

/*
        //Apply the acquired and/or configured filters:
        Map<String, Filter> filters = getFilters();
        if (!CollectionUtils.isEmpty(filters)) {
            for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                applyGlobalPropertiesIfNecessary(filter);
                if (filter instanceof Nameable) {
                    ((Nameable) filter).setName(name);
                }
                //'init' argument is false, since Spring-configured filters should be initialized
                //in Spring (i.e. 'init-method=blah') or implement InitializingBean:
                manager.addFilter(name, filter, false);
            }
        }
*/

		//build up the chains:
		for (FilterChainDefinition filterChainDefinition : filterChainDefinitions)
		{
			logger.debug("adding filterChainDefinition: " + filterChainDefinition);
			chainResolver.getFilterChainManager().createChain(filterChainDefinition.getAntUrlPathExpression(), filterChainDefinition.getChainDefinition());
		}

	}

	@Override
	public boolean service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
	                       final HttpServletRequestHandler handler) throws IOException
	{
		// Assume request handled if filter chain is NOT executed
		final boolean[] res = new boolean[]{true};
		try
		{
			doFilter(httpServletRequest, httpServletResponse, new FilterChain()
			{
                                @Override
				public void doFilter(final ServletRequest request,
				                     final ServletResponse response) throws IOException, ServletException
				{
					res[0] = handler.service((HttpServletRequest) request, (HttpServletResponse) response);
				}
			});
                        if(dummyLogin)
                            SecurityUtils.getSubject().login(new UsernamePasswordToken("Dummy", ""));
		} catch (ServletException e)
		{
			IOException ex = new IOException(e.getMessage());
			ex.initCause(e);
			throw ex;
		}
		return res[0];
	}

	private void applyLoginUrlIfNecessary(Filter filter)
	{
		if (StringUtils.hasText(loginUrl) && (filter instanceof AccessControlFilter))
		{
			AccessControlFilter acFilter = (AccessControlFilter) filter;
			//only apply the login url if they haven't explicitly configured one already:
			String existingLoginUrl = acFilter.getLoginUrl();
			if (AccessControlFilter.DEFAULT_LOGIN_URL.equals(existingLoginUrl))
			{
				acFilter.setLoginUrl(loginUrl);
			}
		}
	}

	private void applySuccessUrlIfNecessary(Filter filter)
	{
		if (StringUtils.hasText(successUrl) && (filter instanceof AuthenticationFilter))
		{
			AuthenticationFilter authcFilter = (AuthenticationFilter) filter;
			//only apply the successUrl if they haven't explicitly configured one already:
			String existingSuccessUrl = authcFilter.getSuccessUrl();
			if (AuthenticationFilter.DEFAULT_SUCCESS_URL.equals(existingSuccessUrl))
			{
				authcFilter.setSuccessUrl(successUrl);
			}
		}
	}

	private void applyUnauthorizedUrlIfNecessary(Filter filter)
	{
		if (StringUtils.hasText(unauthorizedUrl) && (filter instanceof AuthorizationFilter))
		{
			AuthorizationFilter authzFilter = (AuthorizationFilter) filter;
			//only apply the unauthorizedUrl if they haven't explicitly configured one already:
			String existingUnauthorizedUrl = authzFilter.getUnauthorizedUrl();
			if (existingUnauthorizedUrl == null)
			{
				authzFilter.setUnauthorizedUrl(unauthorizedUrl);
			}
		}
	}

	private void applyGlobalPropertiesIfNecessary(Filter filter)
	{
		applyLoginUrlIfNecessary(filter);
		applySuccessUrlIfNecessary(filter);
		applyUnauthorizedUrlIfNecessary(filter);
	}
}
