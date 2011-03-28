package orion.tapestry.menu.lib;

import orion.tapestry.menu.lib.IMenuLink;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;
import orion.tapestry.menu.lib.MenuItemSource;
import orion.tapestry.menu.lib.PageMenuLink;
import orion.tapestry.menu.services.CpuMenuImpl;
import orion.tapestry.menu.services.CpuMenu;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
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
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class MenuJUnitTest {

    TreeMap<String, IMenuLink> config;

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MenuJUnitTest.class);
    }

    public MenuJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        // --------------------- create configuration - begin ------------------
        config = new TreeMap<String, IMenuLink>();
        String[] path = {
            "Start>fin2>esche1",
            "Start>fin2>esche2",
            "Start>fin2>esche3",
            "Start>fin1>30moe1",
            "Start>fin1>20moe2",
            "Start>fin1>10moe3",
            "Start>fin1>moe1>e-moe1",
            "Start>fin1>moe1>e-moe2"
        };
        for (String s : path) {
            config.put(s, new PageMenuLink(this.getClass()));
        }
        // --------------------- create configuration - end --------------------

    }

    @After
    public void tearDown() {
    }

    // Add test methods here.
    // The methods must be annotated with annotation @Test
    //
    @Test
    public void testMenuItemPosition_1() {
        MenuItemPosition mip = new MenuItemPosition("Start>ok>100staff");
        Assert.assertEquals(3, mip.positionSplitted.length);

        // testing label and weight
        Assert.assertEquals("Start", mip.positionSplitted[0].label);
        Assert.assertEquals(0, mip.positionSplitted[0].weight);

        Assert.assertEquals("ok", mip.positionSplitted[1].label);
        Assert.assertEquals(0, mip.positionSplitted[1].weight);

        Assert.assertEquals("staff", mip.positionSplitted[2].label);
        Assert.assertEquals(100, mip.positionSplitted[2].weight);

        // testing uid
        Assert.assertEquals("Start>ok>staff", mip.uid);

        // testing position
        Assert.assertEquals("Start>ok>100staff", mip.position);


        // testing updatePositionWeight()
        mip.updatePositionWeight(1, 23);
        Assert.assertEquals(23, mip.positionSplitted[1].weight);
        Assert.assertEquals("Start>23ok>100staff", mip.position);


        // testing getLastWeight()
        Assert.assertEquals(100, mip.getLastWeight());


        // testing getLastLabel()
        Assert.assertEquals("staff", mip.getLastLabel());

        // testing isChildOf()
        MenuItemPosition mip2 = new MenuItemPosition("Start>ok");
        Assert.assertTrue(mip.isChildOf(mip2));
        Assert.assertFalse(mip2.isChildOf(mip));

        MenuItemPosition mip3 = new MenuItemPosition("Start>abo");
        Assert.assertFalse(mip.isChildOf(mip3));
        Assert.assertFalse(mip2.isChildOf(mip3));
        Assert.assertFalse(mip3.isChildOf(mip));
        Assert.assertFalse(mip3.isChildOf(mip2));


        // testing compareTo()
        mip2.updatePositionWeight(1, 23);
        Assert.assertTrue(mip.compareTo(mip2) > 0);

        // testing getParents()
        ArrayList<MenuItemPosition> pa = mip.getParents();
        Assert.assertEquals(pa.get(0).uid, "Start");
        Assert.assertEquals(pa.get(1).uid, "Start>ok");
        Assert.assertEquals(pa.size(), 2);


        // testing equals();
        Assert.assertTrue(mip2.equals(new String("Start>ok")));
        Assert.assertTrue(mip2.equals(new MenuItemPosition("Start>ok")));
    }

    @Test
    public void testMenuItemSource_1() {

        MenuItemSource mi1 = new MenuItemSource("Start>fin1>30moe1", null);
        Assert.assertEquals(mi1.position.uid, "Start>fin1>moe1");

        MenuItemSource mi2 = new MenuItemSource("Start>fin1>30moe2", null);
        Assert.assertEquals(mi2.position.uid, "Start>fin1>moe2");

        Assert.assertFalse(mi1.compareTo(mi2) > 0);
        Assert.assertTrue(mi2.compareTo(mi1) > 0);
    }

    @Test
    public void testCpuMenuImpl_1() {

        CpuMenuImpl menu = new CpuMenuImpl(this.config);

        //SortedMap<MenuItemPosition, MenuItemSource> fullMenu;
        // test if items exists
        MenuItemPosition mp = new MenuItemPosition("Start>fin1>moe1");
        Assert.assertTrue(menu.containsKey(mp));

    }

    @Test
    public void testCpuMenuImpl_2() {
        CpuMenuImpl mnu = new CpuMenuImpl(this.config);
        MenuData md;
        Object[] items;
        ArrayList<MenuData> m1;
//        ComponentResources linksource=new cr();
//
        m1 = mnu.getMenu("Start", null, null, null);
        md = m1.get(0);
        Assert.assertEquals("Start", md.getTitle());

        items = md.getItems().toArray();
        Assert.assertEquals("Start>fin1", ((MenuItem) items[0]).position.uid);
        Assert.assertEquals("Start>fin2", ((MenuItem) items[1]).position.uid);


        m1 = mnu.getMenu("Start>fin1", null, null, null);
        md = m1.get(0);
        Assert.assertEquals("Start", md.getTitle());
        items = md.getItems().toArray();
        Assert.assertEquals("Start>fin1", ((MenuItem) items[0]).position.uid);
        Assert.assertEquals("Start>fin2", ((MenuItem) items[1]).position.uid);

        md = m1.get(1);
        Assert.assertEquals("Start>fin1", md.getTitle());
        items = md.getItems().toArray();
        Assert.assertEquals("Start>fin1>moe3", ((MenuItem) items[0]).position.uid);
        Assert.assertEquals("Start>fin1>moe2", ((MenuItem) items[1]).position.uid);
        Assert.assertEquals("Start>fin1>moe1", ((MenuItem) items[2]).position.uid);

        m1 = mnu.getMenu("Start>fin1>moe1", null, null, null);
        md = m1.get(0);
        Assert.assertEquals("Start", md.getTitle());

        items = md.getItems().toArray();
        Assert.assertEquals("Start>fin1", ((MenuItem) items[0]).position.uid);
        Assert.assertEquals("Start>fin2", ((MenuItem) items[1]).position.uid);



        md = m1.get(1);
        Assert.assertEquals("Start>fin1", md.getTitle());

        items = md.getItems().toArray();
        Assert.assertEquals("Start>fin1>moe3", ((MenuItem) items[0]).position.uid);
        Assert.assertEquals("Start>fin1>moe2", ((MenuItem) items[1]).position.uid);
        Assert.assertEquals("Start>fin1>moe1", ((MenuItem) items[2]).position.uid);

        md = m1.get(2);
        Assert.assertEquals("Start>fin1>moe1", md.getTitle());

        items = md.getItems().toArray();
        Assert.assertEquals("Start>fin1>moe1>e-moe1", ((MenuItem) items[0]).position.uid);
        Assert.assertEquals("Start>fin1>moe1>e-moe2", ((MenuItem) items[1]).position.uid);


        md = mnu.getOneMenu("Start>fin2", null, null, null);
        Assert.assertEquals("Start>fin2", md.getTitle());

        items = md.getItems().toArray();
        Assert.assertEquals("Start>fin2>esche1", ((MenuItem) items[0]).position.uid);
        Assert.assertEquals("Start>fin2>esche2", ((MenuItem) items[1]).position.uid);
        Assert.assertEquals("Start>fin2>esche3", ((MenuItem) items[2]).position.uid);
    }

    @Test
    public void testCpuMenuImpl_3() {
        CpuMenuImpl mnu = new CpuMenuImpl(this.config);
        MenuData md;
        Object[] items;
        ArrayList<MenuData> m1;

        m1 = mnu.getMenu("Start>fin1>moe4", null, null, null);

        Assert.assertEquals(2, m1.size());

        md = m1.get(0);
        Assert.assertEquals("Start", md.getTitle());

        md = m1.get(1);
        Assert.assertEquals("Start>fin1", md.getTitle());
    }

    public static void main(String[] a) {
        MenuJUnitTest t = new MenuJUnitTest();
        t.setUp();
        t.testCpuMenuImpl_3();
    }
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

    @Override
    public Link addParameterValue(String parameterName, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeParameter(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getBasePath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link copyWithBasePath(String basePath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toAbsoluteURI(boolean secure) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

class cr implements ComponentResources {

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

    @Override
    public void removePageLifecycleListener(PageLifecycleListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isMixin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
