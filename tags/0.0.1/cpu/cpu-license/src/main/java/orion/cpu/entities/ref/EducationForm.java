package orion.cpu.entities.ref;

import javax.persistence.*;
import orion.cpu.baseentities.ReferenceEntity;

/**
 * Справочник форм обучения
 * @author kgp
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}),
    @UniqueConstraint(columnNames = {"shortName"})
})
public class EducationForm extends ReferenceEntity<EducationForm> {

    private static final long serialVersionUID = 1L;
    /**
     * Дневная форма обучения
     */
    public static final String STATIONARY_KEY = "STATIONARY";
    /**
     * Заочная форма обучения
     */
    public static final String CORRESPONDENCE_KEY = "CORRESPONDENCE";

    public EducationForm() {
    }

    public EducationForm(String name) {
        setName(name);
    }
}
