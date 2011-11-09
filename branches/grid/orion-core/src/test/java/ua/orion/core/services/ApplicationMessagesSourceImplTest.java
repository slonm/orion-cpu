package ua.orion.core.services;

import java.util.*;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.ioc.test.IOCTestCase;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

public class ApplicationMessagesSourceImplTest extends IOCTestCase {

    ThreadLocale threadLocale;
    @BeforeMethod
    public void prepare() {
        threadLocale = this.newMock(ThreadLocale.class);
        expect(threadLocale.getLocale()).andReturn(new Locale("en")).anyTimes();
    }
    
    @Test
    public void complex_messages_resources() {
        List<String> resources = new ArrayList<String>();
        resources.add("test.Messages1");
        resources.add("test.Messages2");
        ApplicationMessagesSource ams = new ApplicationMessagesSourceImpl(resources, threadLocale);
        Locale.setDefault(Locale.ENGLISH);
        Messages m = ams.getMessages(new Locale("en"));
        
        Assert.assertFalse(m.contains("transliterated"));
        Assert.assertTrue(m.contains("transliterated.name.for.Apple"));
        
        Assert.assertEquals(m.get("transliterated.name.for.Apple"), "Apple");
        Assert.assertEquals(m.get("transliterated.name.for.Query"), "Query");
        Assert.assertEquals(m.get("transliterated.name.for.Tree"), "Tree");
        
        m = ams.getMessages(new Locale("ru"));
        
        Assert.assertEquals(m.get("transliterated.name.for.Apple"), "Yabloko");
        Assert.assertEquals(m.get("transliterated.name.for.Query"), "Zapros");
        Assert.assertEquals(m.get("transliterated.name.for.Tree"), "Derevo");
        
        m = ams.getMessages(new Locale("uk", "UA"));
        
        Assert.assertEquals(m.get("transliterated.name.for.Apple"), "Yabl");
        Assert.assertEquals(m.get("transliterated.name.for.Query"), "Zapit");
        Assert.assertEquals(m.get("transliterated.name.for.Tree"), "Tree");
        
    }
}