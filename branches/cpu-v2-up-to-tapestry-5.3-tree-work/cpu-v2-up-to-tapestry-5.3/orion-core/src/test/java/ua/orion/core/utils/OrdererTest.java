/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.utils;
import java.util.*;
import ua.orion.core.annotations.OrderLibrary;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author slobodyanuk
 */
public class OrdererTest {
    
    class Lib1Service {
    };

    @OrderLibrary("after:Lib1")
    class Lib2Service {
    };

    @OrderLibrary({"after:Lib1,Lib2"})
    class Lib3Service {
    };

    @OrderLibrary("before:Lib2")
    class Lib4Service {
    };
    
    @Test
    public void classServiceOrderer() {
        Orderer<Class<?>> orderer=new Orderer();
        orderer.add("Lib3", Lib3Service.class, "after:Lib1,Lib2");
        orderer.add("Lib4", Lib4Service.class, "before:Lib2");
        orderer.add("Lib2", Lib2Service.class, "after:Lib1");
        orderer.add("Lib1", Lib1Service.class);
        List<Class<?>> ordered = new ArrayList();
        ordered.addAll(orderer.getOrdered());
        List<Class<?>> etalon = new ArrayList();
        etalon.add(Lib1Service.class);
        etalon.add(Lib4Service.class);
        etalon.add(Lib2Service.class);
        etalon.add(Lib3Service.class);
        Assert.assertEquals(etalon, ordered);
    }
}
