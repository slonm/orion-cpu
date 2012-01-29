package ua.orion.cpu.core.eduprocplanning.stringvalues;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import ua.orion.core.services.ApplicationMessagesSource;
import ua.orion.core.services.StringValueProvider;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;

/**
 *
 * @author slobodyanuk
 */
public class EduPlanStringValueProvider implements StringValueProvider<EduPlan> {

    @Inject
    private StringValueProvider svp;
    @Inject
    private ApplicationMessagesSource messagesSource;

    @Override
    public String getStringValue(EduPlan e) {
        if (e == null) {
            return null;
        }
        Messages mes= messagesSource.getMessages();
        StringBuilder sb = new StringBuilder();
        sb.append(svp.getStringValue(e.getLicenseRecord())).append(". ");
        sb.append(mes.get("property.EduPlan.IntroducingDate")).append(": ");
        sb.append(svp.getStringValue(e.getIntroducingDate()));
//        sb.append(" (").append(svp.getStringValue(e.getEduPlanState())).append(")");
        return sb.toString();
    }
}
