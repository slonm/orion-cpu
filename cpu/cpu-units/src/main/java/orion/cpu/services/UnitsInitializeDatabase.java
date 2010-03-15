package orion.cpu.services;

import orion.cpu.security.OperationTypes;
import orion.cpu.services.impl.InitializeDatabaseSupport;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Начальная инициализация данных.
 * @author sl
 */
public class UnitsInitializeDatabase extends OperationTypes implements Runnable {

    private final InitializeDatabaseSupport initDBSupport;

    public UnitsInitializeDatabase(InitializeDatabaseSupport initializeDatabaseSupport) {
        this.initDBSupport = Defense.notNull(initializeDatabaseSupport, "initializeDatabaseSupport");
    }

    @Override
    public void run() {
    }
}
