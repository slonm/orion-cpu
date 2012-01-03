package ua.orion.cpu.web.eduprocplanning.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.annotations.Property;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDiscipline;

/**
 *
 * @author sl
 */
@SuppressWarnings("unused")
public class EduPlan {

    //---Locals---
    @Property
    private ua.orion.cpu.core.eduprocplanning.entities.EduPlan object;

    public Class<?> getDisciplineClass(){
        return EduPlanDiscipline.class;
    }
    public void onActivate(ua.orion.cpu.core.eduprocplanning.entities.EduPlan object) {
        this.object = object;
        SecurityUtils.getSubject().checkPermission("EduPlan:read:" + object.getId());
    }
    
    public Object onActivate() {
        return "";
    }
}
