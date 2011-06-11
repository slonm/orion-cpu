package orion.tapestry.menu.components;

import java.util.List;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.services.CpuMenu;

/**
 * @author Gennadiy Dobrovolsky
 * Компонета для рисования строки меню
 */
@SuppressWarnings("unused")
public class MenuBar {

    /**
     * Параметр может содержать List<MenuData> или просто строку
     * пути в меню. Во втором случае List<MenuData> генерируется автоматически
     * с пустым activation context
     */
    @Parameter(name = "menudata", required = false, autoconnect = true)
    private Object menudata;

    private List<MenuData> realMenudata;

    @Inject
    private CpuMenu cpuMenu;

    void beginRender(MarkupWriter w){
        if(menudata instanceof String){
            realMenudata=cpuMenu.getMenu(menudata.toString(), null, null, null);
        }else{
            realMenudata=(List<MenuData>)menudata;
        }
    }

    public List<MenuData> getMenuList() {
        return realMenudata;
    }

    @Property
    private MenuData _Menu;
}
