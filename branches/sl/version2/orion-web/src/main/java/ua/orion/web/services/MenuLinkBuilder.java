package ua.orion.web.services;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.PostInjection;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.tapestry.menu.lib.DefaultMenuLink;
import ua.orion.tapestry.menu.lib.PageMenuLink;
import ua.orion.web.services.TapestryDataSource;

/**
 *
 * @author sl
 */
public class MenuLinkBuilder {

    private final TapestryDataSource tcds;

    public MenuLinkBuilder(TapestryDataSource tcds) {
        this.tcds = tcds;
    }

    //инициализация DefaultMenuLink.navigatorPageClass
    @PostInjection
    public void postInjection(@Inject @Symbol("cpumenu.navigatorpage") String page) {
        DefaultMenuLink.setNavigatorPage(page);
    }

    /**
     * Создает ссылку меню
     * К ссылке добавляется параметр menupath с путем в меню и
     * параметр sys с прикладной подсистемой (для дальнейшего вычисления роли)
     */
    public PageMenuLink buildPageMenuLink(String page, String menupath) {
        PageMenuLink lnk = new PageMenuLink(page);
        addParameters(lnk, menupath);
        return lnk;
    }

    /**
     * Создает ссылку меню на страницу списка сущностей по информации
     * TapestryCrudModuleService.getListPageClass
     * Если класс страницы orion.cpu.views.tapestry.pages.ListView, то добавляет в
     * activation context зашифрованное имя класса сущности
     * К ссылке добавляется параметр menupath с путем в меню и
     * параметр sys с прикладной подсистемой (для дальнейшего вычисления роли)
     */
    public PageMenuLink buildCrudPageMenuLink(Class<?> entity, String menupath) {
        PageMenuLink lnk;
        String page = tcds.getCrudPage(entity);
        if ("ori/crud".equalsIgnoreCase(page)) {
            lnk = new PageMenuLink(page, entity.getSimpleName());
        } else {
            lnk = new PageMenuLink(page);
        }
        addParameters(lnk, menupath);
        return lnk;
    }

    private void addParameters(PageMenuLink lnk, String menupath) {
        lnk.setParameterPersistent("menupath", menupath);
    }
}
