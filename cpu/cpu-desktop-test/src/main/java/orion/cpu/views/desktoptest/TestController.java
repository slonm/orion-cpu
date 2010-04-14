package orion.cpu.views.desktoptest;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.service.ControllerSource;
import java.util.*;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.slf4j.*;
import orion.cpu.entities.ref.EducationForm;

public class TestController {
    static final private Logger LOG=LoggerFactory.getLogger(TestController.class);

    private ObjectLocator locator;

    public TestController(ObjectLocator locator) {
        this.locator = locator;
        run();
    }

    void run(){
        ControllerSource controllerSource = locator.getService(ControllerSource.class);
        Controller<EducationForm, Integer> controller = controllerSource.get(EducationForm.class);
        List<EducationForm> eList = controller.findAll();
        for(EducationForm e: eList){
            LOG.error("Record: " + e);
        }
    }

}