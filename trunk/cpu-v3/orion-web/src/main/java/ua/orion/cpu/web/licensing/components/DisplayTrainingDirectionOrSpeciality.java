/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.web.licensing.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import ua.orion.cpu.core.licensing.entities.EducationalQualificationLevel;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.web.licensing.TrainingDirectionOrSpeciality;

/**
 * Отображает направление или специальность (в зависимости от параметра Mode)
 * Если есть варианты подготовки, то отображает их в спойлере.
 * Если есть специальность, то варианты подготовки относятся к ней,
 * иначе к направлению
 * @author slobodyanuk
 */
public class DisplayTrainingDirectionOrSpeciality {

    @Parameter(required = true, allowNull = false)
    @Property
    private LicenseRecord licenseRecord;
    @Parameter(required = true, allowNull = false, defaultPrefix = "literal")
    private TrainingDirectionOrSpeciality mode;
    @Property
    private String trainingVariant;

    public boolean getIsTrainingDirection() {
        return mode == TrainingDirectionOrSpeciality.TRAINING_DIRECTION;
    }

    public Object getTrainingVariants() {
        return licenseRecord.getTrainingVariants().split("\n");
    }

    /**
     * Показываем варианты если это режим отображения направлений для 
     * бакалавров или отображение специальностей для остальных
     */
    public boolean getShowVariants() {
        return licenseRecord.getTrainingVariants() != null
                && ((mode == TrainingDirectionOrSpeciality.TRAINING_DIRECTION
                && EducationalQualificationLevel.BACHELOR_UKEY.equals(licenseRecord.getEducationalQualificationLevel().getUKey()))
                || (mode == TrainingDirectionOrSpeciality.SPECIALITY
                && !EducationalQualificationLevel.BACHELOR_UKEY.equals(licenseRecord.getEducationalQualificationLevel().getUKey())));
    }
}
