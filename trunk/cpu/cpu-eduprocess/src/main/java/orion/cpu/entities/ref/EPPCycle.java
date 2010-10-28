package orion.cpu.entities.ref;

import javax.persistence.*;
import orion.cpu.baseentities.ReferenceEntity;

/**
 * Сущность-справочник циклов дисциплин, поступающих из освтньо-професійних програм
 * (перелік нормативних дисциплін)
 * @author kgp
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class EPPCycle extends ReferenceEntity<EPPCycle> {

    private static final long serialVersionUID = 1L;
    //Циклы ОПП "Программная инженерия" в других ОПП могут отличаться!!!
    /**
     * Цикл соціально — гуманітарної підготовки
     */
    public static final String SOCIALHUMANITARY_KEY = "SOCIALHUMANITARY";
    /**
     * Цикл фундаментальної, природничо-наукової та загальноекономічної підготовки
     */
    public static final String NATUREFUNDAMENTALECONOMICAL_KEY = "NATUREFUNDAMENTALECONOMICAL";
    /**
     * Цикл професійної та практичної підготовки
     */
    public static final String PROFESSIONALPRACTICAL_KEY = "PROFESSIONALPRACTICAL";
    /**
     * Цикл професійної та практичної підготовки
     */
    public static final String SOCIALHUMANITARYECONOMIC_KEY = "SOCIALHUMANITARYECONOMIC";
    /**
     * Цикл професійної та практичної підготовки
     */
    public static final String NATURESCIENCE_KEY = "NATURESCIENCE";
    /**
     * Цикл професійної та практичної підготовки
     */
    public static final String PROFESSIONALNORMATIVE_KEY = "PROFESSIONALNORMATIVE";

    public EPPCycle() {
    }

    public EPPCycle(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }

    @Override
    public String toString() {
        return getName();
    }
}
