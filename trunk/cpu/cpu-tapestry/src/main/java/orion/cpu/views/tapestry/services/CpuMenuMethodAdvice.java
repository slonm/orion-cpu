package orion.cpu.views.tapestry.services;

import br.com.arsmachina.authentication.entity.Permission;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BeanModelSource;
import orion.cpu.security.OperationTypes;
import orion.cpu.security.services.ExtendedAuthorizer;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;
import orion.tapestry.menu.services.CpuMenu;
import orion.tapestry.services.FieldLabelSource;

/**
 * Консультант {@link BeanModelSource} для поддержки сервиса
 * {@link FieldLabelSource}
 * @author sl
 */
public class CpuMenuMethodAdvice implements MethodAdvice {

    private final ExtendedAuthorizer authorizer;
    private final TypeCoercer coercer;
    private final Class<?> navigatorClass;
    private final CpuMenu cpuMenu;

    public CpuMenuMethodAdvice(ExtendedAuthorizer authorizer, TypeCoercer coercer,
            CpuMenu cpuMenu,
            @Inject @Value("${cpumenu.navigatorpage}") Class<?> navigatorClass) {
        this.coercer = coercer;
        this.authorizer = authorizer;
        this.navigatorClass = navigatorClass;
        this.cpuMenu = cpuMenu;
    }

    //TODO RLS
    //Если невозможно получить класс с которым работает страница, то разрешаем
    //размещение пункта
    private boolean isPermitted(MenuData data) {
        Boolean ret = false;
        Class<?> clasz = coercer.coerce(data.getPageLink(), Class.class);
        if (clasz == null) {
            ret = true;
        } else {
            ret = authorizer.can(new Permission(clasz, OperationTypes.MENU_OP));
        }
        if (!ret) {
            return false;
        }
        ret = !navigatorClass.equals(data.getPageLink().getPageClass());
        Iterator<MenuItem> iter = data.getItems().iterator();
        while (iter.hasNext()) {
            MenuItem item = iter.next();
            clasz = coercer.coerce(item.getItemLink(), Class.class);
            if (clasz == null) {
                if (cpuMenu.getOneMenu(item.getUid(), data.getPageLink().getContext(),
                        data.getPageLink().getParameters(), data.getPageLink().getAnchor()) == null) {
                    iter.remove();
                } else {
                    ret = true;
                }
            } else {
                if (!authorizer.can(new Permission(clasz, OperationTypes.MENU_OP))) {
                    iter.remove();
                } else {
                    ret = true;
                }
            }
        }
        return ret;
    }

    @Override
    public void advise(Invocation invocation) {
        invocation.proceed();
        if (invocation.getMethodName().equals("getMenu")) {
            Iterator<MenuData> iter = ((ArrayList<MenuData>) invocation.getResult()).iterator();
            while (iter.hasNext()) {
                if (!isPermitted(iter.next())) {
                    iter.remove();
                }
            }
        }
        if (invocation.getMethodName().equals("getOneMenu")) {
            MenuData data = (MenuData) invocation.getResult();
            if (!isPermitted(data)) {
                invocation.overrideResult(null);
            }
        }
    }
}
