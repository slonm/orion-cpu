package ua.orion.web.security.services;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BeanModelSource;
import ua.orion.tapestry.menu.lib.MenuData;
import ua.orion.tapestry.menu.lib.MenuItem;
import ua.orion.tapestry.menu.services.OrionMenuService;
import ua.orion.web.OrionWebSymbols;

/**
 * Консультант {@link BeanModelSource} для удаления неразрешенных пунктов
 * @author sl
 */
public class OrionMenuServiceMethodAdvice implements MethodAdvice {

    private final TypeCoercer coercer;
    private final String navigatorPage;
    private final OrionMenuService cpuMenu;
    public OrionMenuServiceMethodAdvice(TypeCoercer coercer,
            OrionMenuService cpuMenu,
            @Inject @Symbol(OrionWebSymbols.MENU_NAVIGATOR) String navigatorPage) {
        this.coercer = coercer;
        this.navigatorPage = navigatorPage;
        this.cpuMenu = cpuMenu;
    }

//    @PostInjection
//    public void registerAsListener(LinkCreationHub hub,
//            @Autobuild MenupathLinkCreationListener menupathLinkCreationListener) {
//        hub.addListener(menupathLinkCreationListener);
//    }

    /**
     * 1.Можно видеть только ветки тех подсистем, на которые есть роли
     * 2.Если невозможно получить класс с которым работает страница, то разрешаем
     * размещение пункта
     * TODO RLS
     */
    private boolean isPermitted(MenuData data) {
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
        ret = !navigatorPage.equalsIgnoreCase(data.getPageLink().getPage());
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
     * вычисляет по результирующим правам доступа для всех ролей пользователя
     *
     * @param clasz
     * @return
     */
    private boolean can(Class<?> clasz) {
        return SecurityUtils.getSubject().isPermitted(clasz.getSimpleName()+":menu");
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

