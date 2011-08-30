package ua.orion.cpu.web.eduprocplanning.pages;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.naming.event.EventContext;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.AjaxFormLoop;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.PropertyOutputContext;
import org.tynamo.jpa.annotations.CommitAfter;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.eduprocplanning.entities.EPPCycle;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDisciplineCycle;
import ua.orion.web.services.TapestryDataSource;

/**
 * Блоки для beaneditor
 * @author sl
 */
@SuppressWarnings("unused")
public class PropertyBlocks {

    @Inject
    private Messages messages;
    @Inject
    private EntityService es;
    @Inject
    private TapestryDataSource dataSource;
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
    @Component(parameters = {"source=EduPlanDisciplineCyclesD", "value=eduPlanDisciplineCycle"})
    private Loop eduPlanDisciplineCyclesLoop;
    //Свойство, необходимое для установления текущего значения при выводе
    //элементов набора в гриде в компоненте Loop
    @PageActivationContext
    @Property
    private EduPlan eduPlan;
//    @Property
    private EduPlanDisciplineCycle eduPlanDisciplineCycle;
//    //Используемый методами этого класса набор
    private Set<EduPlanDisciplineCycle> tmpSet = null;
    //Компонент AjaxFormLoop, выводящий в бинэдитор все элементы Set<EduPlanDisciplineCycle> 
    //eduPlanDisciplineCycles
    //В параметрах компонента указаны: в source - метод getEduPlanDisciplineCycles(), возвращающий
    //набор циклов дисциплины; в value - поле текущего класса,
    //из которого извлекаются значения для отображением в гриде в компоненте AjaxFormLoop
    @Component(parameters = {"source=EduPlanDisciplineCyclesE", "value=eduPlanDisciplineCycle"})
    private AjaxFormLoop eduPlanDisciplineCycles;
    @Component(parameters = {
	"model=prop:ePPCycleNameSelectModel"
    })
    private Select ePPCycleNameSelect;

    /**
     * Метод, возвращающий циклы дисциплин учебного плана для вывода в грид
     */
    public Set getEduPlanDisciplineCyclesD() {
	tmpSet = (Set<EduPlanDisciplineCycle>) displayContext.getPropertyValue();
	return tmpSet;
    }

    /**
     * Метод, возвращающий циклы дисциплин учебного плана для вывода в AjaxFormLoop
     */
    public Set getEduPlanDisciplineCyclesE() {
	return (Set<EduPlanDisciplineCycle>) editContext.getPropertyValue();
    }

    /*
     * Добавил сеттер, но почему то в него не заходит
     */
    public void seteduPlanDisciplineCycles(Set<EduPlanDisciplineCycle> eduPlanDisciplineCycles) {
	tmpSet = (Set<EduPlanDisciplineCycle>) editContext.getPropertyValue();
	tmpSet = eduPlanDisciplineCycles;
    }

    /**
     * Геттер поля eduPlanDisciplineCycle данного класса
     * @return 
     */
    public EduPlanDisciplineCycle getEduPlanDisciplineCycle() {
	return eduPlanDisciplineCycle;
    }

    public void setEduPlanDisciplineCycle(EduPlanDisciplineCycle eduPlanDisciplineCycle) {
	this.eduPlanDisciplineCycle = eduPlanDisciplineCycle;
    }

    public Double getCycleTotalCredits() {
	return getEduPlanDisciplineCycle().getePPCycleTotalCredits();
    }

    public void setCycleTotalCredits(Double cycleTotalCredits) {
	this.eduPlanDisciplineCycle.setePPCycleTotalCredits(cycleTotalCredits);
    }

    /**
     * Обработчик события добавления строки в AjaxFormLoop
     * @return объект класса eduPlanDisciplineCycle, добавляемый в AjaxFormLoop
     */
    @CommitAfter
    Object onAddRow() {
	EduPlanDisciplineCycle ePDCycle = new EduPlanDisciplineCycle();
	eduPlan.getEduPlanDisciplineCycles().add(ePDCycle);
	//т.к. ассоциаиция однонаправленная - нужно раскомментрировать для двунаправленной ассоциации
//    eduPlanDisciplineCycle.setEduPlan(eduPlan);
	return ePDCycle;
    }

    /**
     * Обработчик события удаления строки в AjaxFormLoop
     * @return объект класса eduPlanDisciplineCycle, удаляемый из AjaxFormLoop
     */
    @CommitAfter
    void onRemoveRow(EduPlanDisciplineCycle eduPlanDisciplineCycle) {
	es.remove(eduPlanDisciplineCycle);
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

    /**
     * Метод получения цикла дисциплин. 
     * Выполняется для каждого цикла дисциплин в учебном плане. 
     * Используется для того, чтобы определить текущий справочник в цикле. 
     * @return Цикл дисциплин, поступающих из освтньо-професійних програм
     */
    public EPPCycle getEPPCycleName() {
	return eduPlanDisciplineCycle.getEPPCycle();
    }

    /**
     * Сохранение цикла дисциплин для определенного учебного плана.
     * @param cycle - выбранный цикл.
     */
    public void setEPPCycleName(EPPCycle cycle) {
	eduPlanDisciplineCycle.setEPPCycle(cycle);
    }

    public SelectModel getEPPCycleNameSelectModel() {
	SelectModel selectModel = dataSource.getSelectModel(EduPlanDisciplineCycle.class, "ePPCycle");
	List<OptionModel> models = selectModel.getOptions();
	Collections.sort(models, new Comparator<OptionModel>() {

	    @Override
	    public int compare(OptionModel o1, OptionModel o2) {
		return o1.getValue().toString().compareTo(o2.getValue().toString());
	    }
	});
	SelectModel a = new SelectModelImpl(selectModel.getOptionGroups(), models);
	return a;
    }
}
