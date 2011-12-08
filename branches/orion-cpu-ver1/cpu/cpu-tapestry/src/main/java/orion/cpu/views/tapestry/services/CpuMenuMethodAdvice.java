package orion.cpu.views.tapestry.services;

import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authentication.entity.Role;
import br.com.arsmachina.authentication.entity.User;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.PostInjection;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.LinkCreationHub;
import orion.cpu.security.OperationTypes;
import orion.cpu.security.services.ExtendedAuthorizer;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;
import orion.tapestry.menu.services.CpuMenu;
import static orion.cpu.views.tapestry.utils.CpuTapestryUtils.subSystemNameByMenupath;

/**
 * Консультант {@link BeanModelSource} для удаления неразрешенных пунктов
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

    @PostInjection
    public void registerAsListener(LinkCreationHub hub,
            @Autobuild MenupathLinkCreationListener menupathLinkCreationListener) {
        hub.addListener(menupathLinkCreationListener);
    }

    /**
     * 1.Можно видеть только ветки тех подсистем, на которые есть роли
     * 2.Если невозможно получить класс с которым работает страница, то разрешаем
     * размещение пункта
     * TODO RLS
     */
    private boolean isPermitted(MenuData data) {
        if (!can(data)) {
            return false;
        }
        Boolean ret = false;
        Class<?> clasz = coercer.coerce(data.getPageLink(), Class.class);
        if (clasz == null) {
            ret = true;
        } else {
            ret = can(clasz);
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
                        null, null) == null) {
                    iter.remove();
                } else {
                    ret = true;
                }
            } else {
                if (!can(clasz)) {
                    iter.remove();
                } else {
                    ret = true;
                }
            }
        }
        return ret;
    }

    /**
     * вычисляет по всем ролям пользователя доступ к ветке
     *
     * @param clasz
     * @return
     */
    private boolean can(MenuData data) {
        String subSystemName = subSystemNameByMenupath(data.getTitle());
        if(subSystemName==null)return true;
        User user = authorizer.getUser();
        for (Role r : user.getRoles()) {
            authorizer.storeUserAndRole(user, r);
            if (r.getSubSystem().getName().equalsIgnoreCase(subSystemName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * вычисляет по результирующим правам доступа для всех ролей пользователя
     *
     * @param clasz
     * @return
     */
    private boolean can(Class<?> clasz) {
        User user = authorizer.getUser();
        authorizer.pushUserAndRole();
        boolean ret = false;
        try {
            for (Role r : user.getRoles()) {
                authorizer.storeUserAndRole(user, r);
                if (authorizer.can(new Permission(clasz, OperationTypes.MENU_OP))) {
                    ret = true;
                    throw new RuntimeException();
                }
            }
        } catch (Throwable t) {
        } finally {
            authorizer.popUserAndRole();
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
