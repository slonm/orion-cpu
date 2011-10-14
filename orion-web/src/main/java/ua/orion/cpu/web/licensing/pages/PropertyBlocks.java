package ua.orion.cpu.web.licensing.pages;

import java.util.Set;
import java.util.SortedMap;
import javax.naming.event.EventContext;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.PropertyOutputContext;
import ua.orion.core.annotations.PersistentSingleton;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.EducationForm;
import ua.orion.web.BooleanSelectModel;

/**
 * Блоки для beaneditor
 * @author sl
 */
@SuppressWarnings("unused")
public class PropertyBlocks {

    @Inject
    @PersistentSingleton(EducationForm.STATIONARY_UKEY)
    private EducationForm stationary;
    @Inject
    @PersistentSingleton(EducationForm.CORRESPONDENCE_UKEY)
    private EducationForm correspondence;
    private EntityService entityService;
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
    //Компонент BooleanSelectField, определяющий вывод слов "Направление" или "Специальность"
    @Component(parameters = {
        "value=editContext.propertyValue",
        "label=prop:editContext.label",
        "model=prop:TrainingDirectionOrSpecialitySelectModel",
        "clientId=prop:editContext.propertyId",
        "validate=prop:TrainingDirectionOrSpecialityValidator"
    })
    private Select trainingDirectionOrSpeciality;
    //Компонент BooleanSelectField, определяющий вывод слов "Область знаний" или "Направление"
    @Component(parameters = {
        "value=editContext.propertyValue",
        "label=prop:editContext.label",
        "model=prop:KnowledgeAreaOrTrainingDirectionSelectModel",
        "clientId=prop:editContext.propertyId",
        "validate=prop:KnowledgeAreaOrTrainingDirectionValidator"
    })
    private Select knowledgeAreaOrTrainingDirection;
    //Компонент Loop, выводящий в грид все элементы мэпа "форма обучения -
    //лицензированный объем" (ключ - форма обучения)
    //В параметрах компонента указаны: в source - метод getEduFormsD(), возвращающий
    //набор ключей, хранящихся в мэп; в value - поле текущего класса,
    //из которого извлекаются значения для отображением в гриде в компоненте Loop
    @Component(parameters = {"source=EduFormsD", "value=eduForm"})
    private Loop eduFormLicenseQuantity;
    //Свойство, необходимое для установления текущего значения при выводе
    //элементов мэпа в гриде в компоненте Loop
    @Property(read = false)
    private EducationForm eduForm;
    //Используемый методами этого класса мэп
    private SortedMap<EducationForm, Integer> mp = null;
    //Компонент Loop, выводящий в бинэдитор все элементы мэпа "форма обучения -
    //лицензированный объем" (ключ - форма обучения)
    //В параметрах компонента указаны: в source - метод getEduForms(), возвращающий
    //набор ключей, хранящихся в мэп; в value - поле текущего класса,
    //из которого извлекаются значения для отображением в гриде в компоненте Loop
    @Component(parameters = {"source=EduFormsE",
        "value=EduForm"})
    private Loop eduFormLicenseQuantityAfl;

    public FieldValidator<?> getKnowledgeAreaOrTrainingDirectionValidator() {
        return editContext.getValidator(knowledgeAreaOrTrainingDirection);
    }

    public FieldValidator<?> getTrainingDirectionOrSpecialityValidator() {
        return editContext.getValidator(trainingDirectionOrSpeciality);
    }

    public SelectModel getKnowledgeAreaOrTrainingDirectionSelectModel() {
        return new BooleanSelectModel(messages.get("KnowledgeAreaOrTrainingDirection-true"), messages.get("KnowledgeAreaOrTrainingDirection-false"));
    }

    public SelectModel getTrainingDirectionOrSpecialitySelectModel() {
        return new BooleanSelectModel(messages.get("TrainingDirectionOrSpeciality-true"), messages.get("TrainingDirectionOrSpeciality-false"));
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

    /**
     * Метод для установки значения Map в контекст.
     * Используется при сохранении формы.
     * @param value (Целочисленный тип, который отвечает за лиценз. объем студентов для данной формы обучения)
     */
    public void setEduFormLicenseQuantities(Integer value) {
        SortedMap<EducationForm, Integer> tempMap = (SortedMap<EducationForm, Integer>) editContext.getPropertyValue();
        tempMap.put(getEduForm(), value);
    }

    /**
     * Метод возвращает текущую форму обучения для компонента Loop.
     * Используется для получения формы обучения с последующим ее сохранением.
     * @return EducationForm (форма обучения)
     */
    public EducationForm getEduForm() {
        return eduForm;
    }

    /**
     * Метод для получения ключей мэпа "форма обучения - лицензированный объем"
     * (используется параметром source компонентов Loop для отображения в Grid)
     * @return множество ключей мэпа Map<EducationForm, Integer> licenseQuantityByEducationForm
     */
    public Set getEduFormsD() {
        mp = (SortedMap<EducationForm, Integer>) displayContext.getPropertyValue();
        return mp.keySet();
    }
    
    /**
     * Метод для получения ключей мэпа "форма обучения - лицензированный объем"
     * (используется параметром source компонентов Loop для редактирования в BeanEditor)
     * @return множество ключей мэпа Map<EducationForm, Integer> licenseQuantityByEducationForm
     */
    public Set getEduFormsE() {
        mp = (SortedMap<EducationForm, Integer>) editContext.getPropertyValue();
        if (mp.isEmpty()) {
                    mp.put(stationary, 0);
                    mp.put(correspondence, 0);
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
