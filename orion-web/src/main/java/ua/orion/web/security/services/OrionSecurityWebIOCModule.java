package ua.orion.web.security.services;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.linktransform.ComponentEventLinkTransformer;
import org.apache.tapestry5.services.linktransform.PageRenderLinkTransformer;
import org.tynamo.security.filter.SecurityRequestFilter;
import ua.orion.cpu.core.security.OrionSecuritySymbols;

/**
 * The main entry point for OrionSecurity integration.
 *
 */
public class OrionSecurityWebIOCModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(ThreadLocalRoleRequestFilter.class);
    }

    public static void contributeHttpServletRequestHandler(
            OrderedConfiguration<HttpServletRequestFilter> configuration,
            ThreadLocalRoleRequestFilter filter) {
        configuration.add("RoleFilter", filter, "after:SecurityFilter");
    }

    public static void contributePageRenderLinkTransformer(
            OrderedConfiguration<PageRenderLinkTransformer> configuration,
            ThreadLocalRoleRequestFilter filter) {
        configuration.add("RoleHolder", filter, "before:*");
    }

    public static void contributeComponentEventLinkTransformer(
            OrderedConfiguration<ComponentEventLinkTransformer> configuration,
            ThreadLocalRoleRequestFilter filter) {
        configuration.add("RoleHolder", filter, "before:*");
    }

    @Startup
    public static void setShiroCacheManager(SecurityRequestFilter filter,
            @Symbol(OrionSecuritySymbols.EHCACHE_CONFIG) String config) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile(config);
        if (filter.getSecurityManager() instanceof CachingSecurityManager) {
            ((CachingSecurityManager) filter.getSecurityManager()).setCacheManager(new EhCacheManager());
        }
    }
}
