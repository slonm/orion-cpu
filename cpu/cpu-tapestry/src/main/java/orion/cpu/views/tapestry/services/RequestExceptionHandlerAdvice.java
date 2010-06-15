/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.cpu.views.tapestry.services;

import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.authorization.AuthorizationException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import orion.cpu.views.tapestry.pages.ErrorReport;
import orion.cpu.views.tapestry.pages.Login;

/**
 *
 * @author sl
 */
public class RequestExceptionHandlerAdvice implements MethodAdvice {

    private final ComponentSource componentSource;
    private final PageRenderLinkSource linkSource;
    private final Logger logger;
    private final Response response;
    private final ApplicationStateManager applicationStateManager;
    private final ComponentClassResolver resolver;

    public RequestExceptionHandlerAdvice(PageRenderLinkSource linkSource, Logger logger,
            Response response, ApplicationStateManager applicationStateManager,
            ComponentClassResolver resolver, ComponentSource componentSource) {
        this.componentSource = componentSource;
        this.linkSource = linkSource;
        this.logger = logger;
        this.response = response;
        this.applicationStateManager = applicationStateManager;
        this.resolver = resolver;
    }

    @Override
    public void advise(Invocation invocation) {
        if (invocation.getMethodName().equals("handleRequestException")) {
            Throwable cause = (Throwable) invocation.getParameter(0);
            int i = 0;
            while (true) {
                if (cause == null || cause instanceof AuthorizationException || i > 1000) {
                    break;
                }
                i++;
                cause = cause.getCause();
            }
            if (cause != null) {
                AuthorizationException exception = (AuthorizationException) cause;
                logger.error(exception.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("X-Tapestry-ErrorMessage", InternalUtils.toMessage(exception));
                User user = applicationStateManager.getIfExists(User.class);
                Link link = null;
                if (user == null) {
                    link = linkSource.createPageRenderLink(Login.class);
                    ILogin login = (ILogin) componentSource.getPage(resolver.resolvePageClassNameToPageName(Login.class.getName()));
                    login.popCurrentURL();
                } else {
                    link = linkSource.createPageRenderLink(ErrorReport.class);
                    ExceptionReporter rootComponent = (ExceptionReporter) componentSource.getPage(resolver.resolvePageClassNameToPageName(ErrorReport.class.getName()));
                    rootComponent.reportException(exception);
                }
                try {
                    response.sendRedirect(link);
                } catch (IOException ex) {
                    logger.error("Error by rendering {}: {}", link.toAbsoluteURI(), ex.getMessage());
                }
            } else {
                invocation.proceed();
            }
        }
    }
}
