package ua.orion.core.services;

import org.apache.tapestry5.ioc.Configuration;
import ua.orion.core.ModelLibraryInfo;

public class TestIOCModule {

    public static void contributeApplicationMessagesSource(Configuration<String> conf) {
        conf.add("test.Messages1");
        conf.add("test.Messages2");
    }
    
    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo("Foo", "test.foo"));
        conf.add(new ModelLibraryInfo("Foo", "test.bar"));
    }
}
