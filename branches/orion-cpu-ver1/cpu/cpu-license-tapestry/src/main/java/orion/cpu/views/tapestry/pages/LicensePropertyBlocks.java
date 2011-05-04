package orion.cpu.views.tapestry.pages;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import javax.naming.event.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.AjaxFormLoop;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.PropertyOutputContext;
import orion.cpu.entities.ref.EducationForm;
import orion.tapestry.components.BooleanSelectField;

/**
 * Блоки для beaneditor для вывода слов "направление" или "специальность"
 * и "Область знаний" или "направление"
 * а также для отображения мэпа "форма обучения - лицензированный объем"
 * @author sl, kgp
 */
@SuppressWarnings("unused")
public class LicensePropertyBlocks {
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
    //Компонент BooleanSelectField, определяющий вывод слов "Направление" или "Специальность"
    @Component(parameters = {"value=editContext.propertyValue",
        "label=prop:editContext.label",
        "trueLabel=message:TrainingDirectionOrSpeciality-true",
        "falseLabel=message:TrainingDirectionOrSpeciality-false",
        "validate=prop:trainingDirectionOrSpecialityValidator",
        "clientId=prop:editContext.propertyId"})
    private BooleanSelectField trainingDirectionOrSpeciality;
    //Компонент BooleanSelectField, определяющий вывод слов "Область знаний" или "Направление"
    @Component(parameters = {"value=editContext.propertyValue",
        "label=prop:editContext.label",
        "trueLabel=message:knowledgeAreaOrTrainingDirection-true",
        "falseLabel=message:knowledgeAreaOrTrainingDirection-false",
        "validate=prop:knowledgeAreaOrTrainingDirectionValidator",
        "clientId=prop:editContext.propertyId"})
    private BooleanSelectField knowledgeAreaOrTrainingDirection;
    //Компонент Loop, выводящий в грид все элементы мэпа "форма обучения -
    //лицензированный объем" (ключ - форма обучения)
    //В параметрах компонента указаны: в source - метод getEduForms(), возвращающий
    //набор ключей, хранящихся в мэп; в value - поле текущего класса,
    //из которого извлекаются значения для отображением в гриде в компоненте Loop
    @Component(parameters = {"source=EduForms", "value=eduForm"})
    private Loop eduFormLicenseQuantity;
    //Свойство, необходимое для установления текущего значения при выводе
    //элементов мэпа в гриде в компоненте Loop
    @Property
    private EducationForm eduForm;

     @Component(parameters = {"source=licenseQuantityByEducationForm",
     "value=eduForm"})
     private AjaxFormLoop eduFormLicenseQuantityAfl;

    public FieldValidator<?> getKnowledgeAreaOrTrainingDirectionValidator() {
        return editContext.getValidator(knowledgeAreaOrTrainingDirection);
    }

    public FieldValidator<?> getTrainingDirectionOrSpecialityValidator() {
        return editContext.getValidator(trainingDirectionOrSpeciality);
    }

    /**
     * Метод, возвращающий мэп с формами обучения и количеством лицензий
     * (используется в компоненте Loop в шаблоне)
     * @return мэп с формами обучения и количеством лицензий     *
     */
    public Integer getEduFormLicenseQuantities() {
        SortedMap<EducationForm, Integer> mp = (SortedMap<EducationForm, Integer>) displayContext.getPropertyValue();
        return mp.get(eduForm);
    }

    //TODO Создать компаратор и отсортировать по значению поля EducationForm
    //изучить параметры отображения Мар Hibernate-ом

    /**
     * Метод для получения ключей мэпа "форма обучения - лицензированный объем"
     * (используется параметром source компонент Loop)
     * @return множество ключей мэпа Map<EducationForm, Integer> licenseQuantityByEducationForm
     */
        public Set getEduForms() {
        SortedMap<EducationForm, Integer> mp = (SortedMap<EducationForm, Integer>) displayContext.getPropertyValue();
        return mp.keySet();
    }
    //TODO Добавить источник данных для LOOP, обработчики событий удаления, добавления
    

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
