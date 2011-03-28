package orion.cpu.views.tapestry.services;

import org.apache.tapestry5.ioc.Invocation;


import org.apache.tapestry5.services.ApplicationStateManager;

import br.com.arsmachina.authentication.entity.User;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.services.LocalizationSetter;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * 
 */
public class LocalizationSetterAdvice implements MethodAdvice {

    private final ApplicationStateManager applicationStateManager;
    private final LocalizationSetter localizationSetter;

    /**
     * Single constructor of this class.
     *
     * @param applicationStateManager an {@link ApplicationStateManager}. It cannot be null.
     */
    public LocalizationSetterAdvice(ApplicationStateManager applicationStateManager,
            LocalizationSetter localizationSetter) {

        this.applicationStateManager = Defense.notNull(applicationStateManager, "applicationStateManager");
        this.localizationSetter = Defense.notNull(localizationSetter, "localizationSetter");
    }

    @Override
    public void advise(Invocation invocation) {
        invocation.proceed();
        User user = applicationStateManager.getIfExists(User.class);

        if (user != null && !User.GUEST_USER.getLogin().equalsIgnoreCase(user.getLogin())) {
            String locale = user.getLocale();
            String call=(String)invocation.getParameter(0);
            if (locale != null && locale.length() > 0 && ! locale.equalsIgnoreCase(call)) {
                if (invocation.getMethodName().equals("setLocaleFromLocaleName")) {
                    localizationSetter.setLocaleFromLocaleName(user.getLocale());
                } else if (invocation.getMethodName().equals("setNonPeristentLocaleFromLocaleName")) {
                    localizationSetter.setNonPeristentLocaleFromLocaleName(user.getLocale());
                }
            }
        }
    }
}
