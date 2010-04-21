package orion.cpu.views.desktoptest;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.service.ControllerSource;
import java.util.List;
import org.apache.tapestry5.ioc.Registry;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import orion.cpu.entities.pub.*;
import static org.testng.Assert.assertEquals;
import orion.cpu.views.desktoptest.Main;

/**
 *
 * @author sl
 */
public class DAOFindByExampleTest {

    Registry registry;

    @BeforeClass
    public void setUp() {
        registry = Main.startup();
    }

    @AfterClass
    public void cleanUp() {
        Main.shutdown(registry);
    }

    //@BeforeTest
    public void cleanDB(){
        ControllerSource cs = registry.getService(ControllerSource.class);
        Controller<A, Integer> cA = cs.get(A.class);
        Controller<B, Integer> cB = cs.get(B.class);
        
        for(A a: cA.findAll()){
            cA.delete(a);
        }
        
        for(B b: cB.findAll()){
            cB.delete(b);
        }
    }

    @Test
    public void joinedStrategy() {
        cleanDB();
        ControllerSource cs = registry.getService(ControllerSource.class);
        Controller<A, Integer> cA = cs.get(A.class);
        Controller<B, Integer> cB = cs.get(B.class);

        A aX = new A("XX");
        cA.save(aX);
        A aY = new A("YY");
        cA.save(aY);

        B bX = new B("XX");
        cB.save(bX);
        B bY = new B("YY");
        cB.save(bY);

        List<A> la = cA.findByExample(aX);
        assertEquals(la.size(), 2);

        List<B> lb = cB.findByExample(bX);
        assertEquals(lb.size(), 1);
    }

    @Test
    public void associations() {
        cleanDB();
        ControllerSource cs = registry.getService(ControllerSource.class);
        Controller<B, Integer> cB = cs.get(B.class);

        B bX = new B("XX");
        cB.save(bX);

        B bY = new B("YY").alias(bX);
        cB.save(bY);

        B bZ = new B("ZZ").alias(bX);
        cB.save(bZ);

        B bQ = new B("QQ").alias(bY);
        cB.save(bQ);

        B example = new B().alias(bX);

        List<B> lb = cB.findByExample(example);
        assertEquals(lb.size(), 2);

        example.alias(bY);

        lb = cB.findByExample(example);
        assertEquals(lb.size(), 1);
    }

}
