package ua.orion.cpu.core.licensing.entities;

import ua.orion.cpu.core.eduprocplanning.entities.*;

/**
 * Состояние лицензии
 * @author slobodyanuk
 */
public enum LicenseState {

    /**
     * Вновь вводимая. В этом состоянии лицензию можно редактировать.
     */
    NEW,
    /**
     * Последняя выданная лицензия. Такая лицензия только одна.
     */
    FORCED,
    /**
     * Устаревшая лицензия.
     */
    OBSOLETE
}
