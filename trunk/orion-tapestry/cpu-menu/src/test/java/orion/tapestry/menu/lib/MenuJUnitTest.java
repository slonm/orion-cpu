package orion.tapestry.menu.lib;

import orion.tapestry.menu.lib.LinkCreator;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;
import orion.tapestry.menu.lib.MenuItemSource;
import orion.tapestry.menu.lib.PageLinkCreator;
import orion.tapestry.menu.services.CpuMenuImpl;
import orion.tapestry.menu.services.CpuMenu;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.model.ComponentModel;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.runtime.PageLifecycleListener;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import orion.tapestry.menu.pages.Navigator;
import orion.tapestry.menu.services.DefaultLinkCreatorFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class MenuJUnitTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MenuJUnitTest.class);
    }

    public MenuJUnitTest() {
    }
    MenuItemSource mi1, mi2;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mi1 = new MenuItemSource("Start>fin1>moe1", null);
        mi2 = new MenuItemSource("Start>fin2>moe2", null);
    }

    @After
    public void tearDown() {
    }

    // Add test methods here.
    // The methods must be annotated with annotation @Test
    //
    @Test
    public void compare() {
        Assert.assertEquals(-1, mi1.compareTo(mi2));
        Assert.assertEquals(1, mi2.compareTo(mi1));
    }

    @Test
    public void getPathSplitted() {
        String[] a = mi1.getPathSplitted();
        Assert.assertEquals("Start", a[0]);
        Assert.assertEquals("fin1", a[1]);
        Assert.assertEquals("moe1", a[2]);
        Assert.assertEquals("moe1", mi1.getLabel());
    }

    @Test
    public void getPath() {
        String a = mi1.getPath();
        Assert.assertEquals("Start>fin1>moe1", a);
    }

    @Test
    public void setPathSplitted() {
        String[] s = {"Start", "fin1", "123moe"};
        mi1.setPathSplitted(s);
        String[] a = mi1.getPathSplitted();
        Assert.assertEquals("Start", a[0]);
        Assert.assertEquals("fin1", a[1]);
        Assert.assertEquals("123moe", a[2]);
        Assert.assertEquals("moe", mi1.getLabel());
    }

    @Test
    public void setPath() {
        String s = "Start>fin1>233moe5";
        mi1.setPath(s);

        String[] a = mi1.getPathSplitted();
        Assert.assertEquals("Start", a[0]);
        Assert.assertEquals("fin1", a[1]);
        Assert.assertEquals("233moe5", a[2]);
        Assert.assertEquals("moe5", mi1.getLabel());
    }
//TODO переделать тест
//    @Test
//    public void getMenu() {
//        // --------------------- create configuration - begin ------------------
//        TreeMap<String, LinkCreator> config = new TreeMap<String, LinkCreator>();
//        String[] path = {
//            "Start>fin2>esche1",
//            "Start>fin2>esche2",
//            "Start>fin2>esche3",
//            "Start>fin1>moe1",
//            "Start>fin1>moe2",
//            "Start>fin1>moe3",
//            "Start>fin1>moe1>e-moe1",
//            "Start>fin1>moe1>e-moe2"
//        };
//        for (String s : path) {
//            config.put(s, new PageLinkCreator(this.getClass()));
//        }
//        // --------------------- create configuration - end --------------------
//
//
//        MenuData md;
//        Object[] items;
//        ArrayList<MenuData> m1;
//        ComponentResources linksource=new cr();
//
//        CpuMenu mnu = new CpuMenuImpl(config);
//
//        m1 = mnu.getMenu("Start",linksource);
//        md = m1.get(0);
//        Assert.assertEquals("Start", md.getTitle());
//
//        items = md.getItems().toArray();
//        Assert.assertEquals("Start>fin1", ((MenuItem) items[0]).getPath());
//        Assert.assertEquals("Start>fin2", ((MenuItem) items[1]).getPath());
//
//        m1 = mnu.getMenu("Start>fin1",linksource);
//        md = m1.get(0);
//        Assert.assertEquals("Start", md.getTitle());
//        items = md.getItems().toArray();
//        Assert.assertEquals("Start>fin1", ((MenuItem) items[0]).getPath());
//        Assert.assertEquals("Start>fin2", ((MenuItem) items[1]).getPath());
//
//        md = m1.get(1);
//        Assert.assertEquals("fin1", md.getTitle());
//        items = md.getItems().toArray();
//        Assert.assertEquals("Start>fin1>moe1", ((MenuItem) items[0]).getPath());
//        Assert.assertEquals("Start>fin1>moe2", ((MenuItem) items[1]).getPath());
//        Assert.assertEquals("Start>fin1>moe3", ((MenuItem) items[2]).getPath());
//
//
//        m1 = mnu.getMenu("Start>fin1>moe1",linksource);
//        md = m1.get(0);
//        Assert.assertEquals("Start", md.getTitle());
//
//        items = md.getItems().toArray();
//        Assert.assertEquals("Start>fin1", ((MenuItem) items[0]).getPath());
//        Assert.assertEquals("Start>fin2", ((MenuItem) items[1]).getPath());
//
//        md = m1.get(1);
//        Assert.assertEquals("fin1", md.getTitle());
//
//        items = md.getItems().toArray();
//        Assert.assertEquals("Start>fin1>moe1", ((MenuItem) items[0]).getPath());
//        Assert.assertEquals("Start>fin1>moe2", ((MenuItem) items[1]).getPath());
//        Assert.assertEquals("Start>fin1>moe3", ((MenuItem) items[2]).getPath());
//
//        md = m1.get(2);
//        Assert.assertEquals("moe1", md.getTitle());
//
//        items = md.getItems().toArray();
//        Assert.assertEquals("Start>fin1>moe1>e-moe1", ((MenuItem) items[0]).getPath());
//        Assert.assertEquals("Start>fin1>moe1>e-moe2", ((MenuItem) items[1]).getPath());
//
//
//        md = mnu.getOneMenu("Start>fin2", linksource, items);
//        Assert.assertEquals("fin2", md.getTitle());
//
//        items = md.getItems().toArray();
//        Assert.assertEquals("Start>fin2>esche1", ((MenuItem) items[0]).getPath());
//        Assert.assertEquals("Start>fin2>esche2", ((MenuItem) items[1]).getPath());
//        Assert.assertEquals("Start>fin2>esche3", ((MenuItem) items[2]).getPath());
//    }
}

class EmptyLink implements Link {

    private String URI;

    EmptyLink(String s) {
        URI = s;
    }

    @Override
    public String toURI() {
        return this.URI;
    }

    @Override
    public List<String> getParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getParameterValue(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParameter(String parameterName, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toRedirectURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAnchor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAnchor(String anchor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toAbsoluteURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

class cr implements ComponentResources{

    @Override
    public Resource getBaseResource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ComponentModel getComponentModel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Component getComponent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Component getContainer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ComponentResources getContainerResources() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Messages getContainerMessages() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Component getPage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Component getEmbeddedComponent(String embeddedId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBound(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Annotation> T getParameterAnnotation(String parameterName, Class<T> annotationType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void renderInformalParameters(MarkupWriter writer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Messages getMessages() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class getBoundType(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AnnotationProvider getAnnotationProvider(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Block getBlockParameter(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRenderVariable(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void storeRenderVariable(String name, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPageLifecycleListener(PageLifecycleListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void discardPersistentFieldChanges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getElementName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getInformalParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T getInformalParameter(String name, Class<T> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getNestedId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCompleteId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean triggerEvent(String eventType, Object[] contextValues, ComponentEventCallback callback) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean triggerContextEvent(String eventType, EventContext context, ComponentEventCallback callback) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRendering() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Logger getLogger() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getElementName(String defaultElementName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Block getBlock(String blockId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Block findBlock(String blockId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPageName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasBody() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Block getBody() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link createEventLink(String eventType, Object... context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link createActionLink(String eventType, boolean forForm, Object... context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link createFormEventLink(String eventType, Object... context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link createPageLink(String pageName, boolean override, Object... context) {
        return new EmptyLink(pageName);
    }

    @Override
    public Link createPageLink(Class pageClass, boolean override, Object... context) {
        return new EmptyLink(pageClass.getName());
    }

    @Override
    public Location getLocation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}