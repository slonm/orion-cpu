package orion.cpu.views.tapestry.services;

import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.PostInjection;
import org.apache.tapestry5.ioc.annotations.Value;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.views.tapestry.pages.ListView;
import orion.tapestry.menu.lib.DefaultMenuLink;
import orion.tapestry.menu.lib.PageMenuLink;

/**
 *
 * @author sl
 */
public class MenuLinkBuilder {

    private final TapestryCrudModuleService tcms;

    public MenuLinkBuilder(TapestryCrudModuleService tcms) {
        this.tcms = tcms;
    }

    //инициализация DefaultMenuLink.navigatorPageClass
    @PostInjection
    public void postInjection(@Inject @Value("${cpumenu.navigatorpage}") Class<?> clasz){
        if(clasz!=null){
            DefaultMenuLink.setNavigatorPageClass(clasz);
        }
    }

    /**
     * Создает ссылку меню на навигатор
     * К ссылке добавляется параметр menupath с путем в меню и
     * параметр sys с прикладной подсистемой (для дальнейшего вычисления роли)
     */
    public DefaultMenuLink buildDefaultMenuLink(String menupath) {
        DefaultMenuLink lnk = new DefaultMenuLink(menupath);
        addParameters(lnk, menupath);
        return lnk;
    }

    /**
     * Создает ссылку меню
     * К ссылке добавляется параметр menupath с путем в меню и
     * параметр sys с прикладной подсистемой (для дальнейшего вычисления роли)
     */
    public PageMenuLink buildPageMenuLink(Class<?> pageClass, String menupath) {
        PageMenuLink lnk = new PageMenuLink(pageClass);
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
    public PageMenuLink buildListPageMenuLink(Class<?> entity, String menupath) {
        PageMenuLink lnk;
        Class<?> clasz = tcms.getListPageClass(entity);
        if (ListView.class.equals(clasz)) {
            lnk = new PageMenuLink(clasz, BaseEntity.getFullClassName(entity));
        } else {
            lnk = new PageMenuLink(clasz);
        }
        addParameters(lnk, menupath);
        return lnk;
    }

    private void addParameters(PageMenuLink lnk, String menupath) {
        lnk.setParameterPersistent("menupath", menupath);
    }
}
