package ua.orion.web.security.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.linktransform.PageRenderLinkTransformer;

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
}
