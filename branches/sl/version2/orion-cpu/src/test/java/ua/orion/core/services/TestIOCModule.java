package ua.orion.core.services;

import org.apache.tapestry5.ioc.Configuration;

public class TestIOCModule {

    public static void contributeApplicationMessagesSource(Configuration<String> conf) {
        conf.add("test.Messages1");
        conf.add("test.Messages2");
    }
    
    public static void contributeModelLibraryService(Configuration<String> conf) {
        conf.add("test.foo");
        conf.add("test.bar");
    }
}
