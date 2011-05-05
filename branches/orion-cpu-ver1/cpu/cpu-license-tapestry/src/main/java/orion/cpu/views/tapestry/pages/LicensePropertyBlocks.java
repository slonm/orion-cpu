package orion.cpu.views.tapestry.pages;

import java.util.Set;
import java.util.SortedMap;
import javax.naming.event.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
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
    //Свойство, представляющее контекст, получаемый при выводе грида на экран
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
    @Property(read=false)
    private EducationForm eduForm;
    //Используемый методами этого класса мэп
    private SortedMap<EducationForm, Integer> mp = null;
    //Компонент Loop, выводящий в бинэдитор все элементы мэпа "форма обучения -
    //лицензированный объем" (ключ - форма обучения)
    //В параметрах компонента указаны: в source - метод getEduForms(), возвращающий
    //набор ключей, хранящихся в мэп; в value - поле текущего класса,
    //из которого извлекаются значения для отображением в гриде в компоненте Loop
    @Component(parameters = {"source=EduForms",
        "value=EduForm"})
    private Loop eduFormLicenseQuantityAfl;

    public FieldValidator<?> getKnowledgeAreaOrTrainingDirectionValidator() {
        return editContext.getValidator(knowledgeAreaOrTrainingDirection);
    }

    public FieldValidator<?> getTrainingDirectionOrSpecialityValidator() {
        return editContext.getValidator(trainingDirectionOrSpeciality);
    }

    /**
     * Метод, возвращающий количество лицензий для записи с ключом eduForm
     * (используется в компонентах Loop в шаблоне)
     * @return количество лицензий для записи с ключом eduForm
     */
    public Integer getEduFormLicenseQuantities() {
        try {
            mp = (SortedMap<EducationForm, Integer>) displayContext.getPropertyValue();
        } catch (Exception ex) {
            System.out.println("EXCEPTION STACK" + ex.getStackTrace());
            mp = (SortedMap<EducationForm, Integer>) editContext.getPropertyValue();
        }
        return mp.get(eduForm);
    }

    public void setEduFormLicenseQuantities(Integer value) {
        mp.put(getEduForm(), value);
        editContext.setPropertyValue(mp);
    }

    public EducationForm getEduForm(){
        System.out.println("--- "+eduForm.getName()+" ---");
        return eduForm;
    }

    /**
     * Метод для получения ключей мэпа "форма обучения - лицензированный объем"
     * (используется параметром source компонентов Loop)
     * @return множество ключей мэпа Map<EducationForm, Integer> licenseQuantityByEducationForm
     */
    public Set getEduForms() {
        try {
            mp = (SortedMap<EducationForm, Integer>) displayContext.getPropertyValue();
        } catch (Exception ex) {
            mp = (SortedMap<EducationForm, Integer>) editContext.getPropertyValue();
        }
        return mp.keySet();
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
