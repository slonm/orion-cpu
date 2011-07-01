package ua.orion.cpu.web.eduprocplanning.pages;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.naming.event.EventContext;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.AjaxFormLoop;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.PropertyOutputContext;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDisciplineCycle;

/**
 * Блоки для beaneditor
 * @author sl
 */
@SuppressWarnings("unused")
public class PropertyBlocks {

    @Inject
    private Messages messages;
    //@Environmental определяет поле, которое в runtime заменяется read-only значением,
    //полученным от службы Environment (Обеспечивает доступ к объектам окружения,
    //создаваемыми используемыми в классе компонентами - форма позднего связывания.)
    //Свойство, представляющее контекст, получаемый при выводе грида на экран
    @Environmental
    @Property(write = false)
    private PropertyOutputContext displayContext;
    //Свойство, представляющее контекст, получаемый при выводе бинэдитора на экран
    @Environmental
    @Property(write = false)
    private PropertyEditContext editContext;
    //Компонент Loop, выводящий в грид все элементы SortedSet<EduPlanDisciplineCycle> 
    //eduPlanDisciplineCycles
    //В параметрах компонента указаны: в source - метод EduPlanDisciplineCyclesLoop(), возвращающий
    //набор циклов дисциплины; в value - поле текущего класса,
    //из которого извлекаются значения для отображением в гриде в компоненте Loop
    @Component(parameters = {"source=EduPlanDisciplineCyclesLoop", "value=eduPlanDisciplineCycle"})
    private Loop eduPlanDisciplineCyclesLoop;
    //Свойство, необходимое для установления текущего значения при выводе
    //элементов набора в гриде в компоненте Loop
    private EduPlanDisciplineCycle eduPlanDisciplineCycle;
//    //Используемый методами этого класса набор
    private SortedSet<EduPlanDisciplineCycle> tmpSet = null;
    //Компонент AjaxFormLoop, выводящий в бинэдитор все элементы SortedSet<EduPlanDisciplineCycle> 
    //eduPlanDisciplineCycles
    //В параметрах компонента указаны: в source - метод getEduPlanDisciplineCycles(), возвращающий
    //набор циклов дисциплины; в value - поле текущего класса,
    //из которого извлекаются значения для отображением в гриде в компоненте AjaxFormLoop
    @Component(parameters = {"source=EduPlanDisciplineCycles", "value=eduPlanDisciplineCycle"})
    private AjaxFormLoop eduPlanDisciplineCycles;

    /**
     * Метод, возвращающий циклы дисциплин учебного плана для вывода в грид
     */
    public Set getEduPlanDisciplineCyclesLoop() {
        tmpSet=(SortedSet<EduPlanDisciplineCycle>) displayContext.getPropertyValue();
        return tmpSet;
    }
    
    public void setEduPlanDisciplineCyclesLoop(EduPlanDisciplineCycle eduPlanDisciplineCycle){
        SortedSet<EduPlanDisciplineCycle> tempSet = new TreeSet<EduPlanDisciplineCycle>();
        tempSet.add(eduPlanDisciplineCycle);
    }

    /**
     * Метод, возвращающий циклы дисциплин учебного плана для вывода в AjaxFormLoop
     */
    public Set getEduPlanDisciplineCycles() {
        return (SortedSet<EduPlanDisciplineCycle>) editContext.getPropertyValue();
    }

    /**
     * Геттер поля eduPlanDisciplineCycle данного класса
     */
    public EduPlanDisciplineCycle getEduPlanDisciplineCycle() {
        return eduPlanDisciplineCycle;
    }

    /**
     * Сеттер поля eduPlanDisciplineCycle данного класса
     */
    public void setEduPlanDisciplineCycle(EduPlanDisciplineCycle eduPlanDisciplineCycle) {
        this.eduPlanDisciplineCycle = eduPlanDisciplineCycle;
    }
    
    public Double getCycleTotalCredits(){
        return eduPlanDisciplineCycle.getePPCycleTotalCredits();
    }
    /**
     * Запрещает явное открытие этой псевдо-страницы.
     * @param ec Page activation context
     * @return the empty string
     * @author sl
     */
    public String onActivate(EventContext ec) {
        return "";
    }
}
