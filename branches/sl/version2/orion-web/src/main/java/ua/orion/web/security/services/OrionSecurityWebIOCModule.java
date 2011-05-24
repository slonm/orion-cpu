package ua.orion.web.security.services;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.linktransform.PageRenderLinkTransformer;
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

    @Startup
    public static void setShiroCacheManager(DefaultWebSecurityManager securityManager,
            @Symbol(OrionSecuritySymbols.EHCACHE_CONFIG) String config) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile(config);
        securityManager.setCacheManager(new EhCacheManager());
    }

    /**
     * Скрещиваем меню с системой безопасности
     * @param receiver приемник событий OrionMenuService
     */
    @Match("OrionMenuService")
    public static void adviseOrionMenuServiceForSecurity(MethodAdviceReceiver receiver,
            @Autobuild OrionMenuServiceMethodAdvice advice) {
        receiver.adviseAllMethods(advice);
    }
}
