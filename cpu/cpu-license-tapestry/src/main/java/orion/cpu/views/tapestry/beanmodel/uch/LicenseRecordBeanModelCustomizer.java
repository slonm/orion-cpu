package orion.cpu.views.tapestry.beanmodel.uch;

import br.com.arsmachina.tapestrycrud.beanmodel.AbstractBeanModelCustomizer;
import org.apache.tapestry5.beaneditor.BeanModel;
import orion.cpu.entities.uch.LicenseRecord;

/**
 * Кастомизатор бинмодели для лицензионных записей
 * @author kgp
 */
public class LicenseRecordBeanModelCustomizer extends AbstractBeanModelCustomizer<LicenseRecord> {

    /**
     * Кастомизатор интерфейса грида для лицензионных записей:
     * исключаются поля: номер лицензии и образовательно-квалификационный уровень
     * @return бин-модель для сущности лицензионная запись
     * @see BeanModelCustomizer#customizeDisplayModel(BeanModel)
     */
    @Override
    public BeanModel<LicenseRecord> customizeDisplayModel(BeanModel<LicenseRecord> model) {
        model.exclude("license","educationalQualificationLevel");
        return model;
    }

    /**
     * Кастомизатор интерфейса бинэдитора для лицензионных записей:
     * исключаются вычислимые с помощью аннотации @Formula поля 
     * @return бин-модель для сущности лицензионная запись
     * @see BeanModelCustomizer#customizeEditModel(BeanModel)
     */
    @Override
    public BeanModel<LicenseRecord> customizeEditModel(BeanModel<LicenseRecord> model) {
        model.exclude("knowledgeAreaOrTrainingDirectionCode", "knowledgeAreaOrTrainingDirectionName", "code");
        return model;
    }
}
