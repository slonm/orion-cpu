package orion.cpu.views.tapestry.services;

import br.com.arsmachina.authentication.entity.Permission;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import orion.cpu.security.OperationTypes;
import orion.cpu.security.services.ExtendedAuthorizer;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;
import orion.tapestry.services.FieldLabelSource;

/**
 * Консультант {@link BeanModelSource} для поддержки сервиса
 * {@link FieldLabelSource}
 * @author sl
 */
public class CpuMenuMethodAdvice implements MethodAdvice {

    private final ExtendedAuthorizer authorizer;
    private final TypeCoercer coercer;

    public CpuMenuMethodAdvice(ExtendedAuthorizer authorizer, TypeCoercer coercer) {
        this.coercer = coercer;
        this.authorizer = authorizer;
    }

    //TODO RLS
    //Если невозможно получить класс с которым работает страница, то разрешаем
    //размещение пункта
    private boolean isPermitted(MenuData data) {
        Boolean ret = false;
        Class<?> clasz = coercer.coerce(data.getPageLink(), Class.class);
        if (clasz == null) {
            ret = true;
        }
        if (!ret) {
            ret = authorizer.can(new Permission(clasz, OperationTypes.MENU_OP));
        }
        if (!ret) {
            return false;
        }
        Iterator<MenuItem> iter = data.getItems().iterator();
        while (iter.hasNext()) {
            clasz = coercer.coerce(iter.next().getItemLink(), Class.class);
            if (clasz == null) {
                continue;
            }
            if (!authorizer.can(new Permission(clasz, OperationTypes.MENU_OP))) {
                iter.remove();
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
