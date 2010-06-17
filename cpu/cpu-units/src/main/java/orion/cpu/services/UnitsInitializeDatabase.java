package orion.cpu.services;

import br.com.arsmachina.controller.Controller;
import orion.cpu.security.OperationTypes;
import orion.cpu.services.impl.InitializeDatabaseSupport;
import ua.mihailslobodyanuk.utils.Defense;
import orion.cpu.entities.org.*;
import java.util.List;

/**
 * Начальная инициализация данных.
 * @author sl
 */
public class UnitsInitializeDatabase extends OperationTypes implements Runnable {

    private final InitializeDatabaseSupport iDBSpt;

    public UnitsInitializeDatabase(InitializeDatabaseSupport initializeDatabaseSupport) {
        this.iDBSpt = Defense.notNull(initializeDatabaseSupport, "initializeDatabaseSupport");
        //--Создание контроллера, работающего с экземплярами подразделений
    }

    @Override
    public void run() {
        iDBSpt.getPermissionsMap(Chair.class, READ_OP, REMOVE_OP, STORE_OP, UPDATE_OP, MENU_OP);
//        //Заполнение тестовых данных
        if (iDBSpt.isFillTestData()) {
//        //---Кафедры, выполняющие обучение по лицензиям----------
            Chair kafPIT = saveOrUpdateChair("кафедра програмування та інформаційних технологій", "КПІТ");
            Chair kafEICPHS = saveOrUpdateChair("кафедра управління навчальними закладами та педагогіки вищої школи", "КУНЗПВШ");
        }
    }

    public Chair saveOrUpdateChair(String name, String shortName) {
        Controller<Chair, Integer> ouController;
        ouController = iDBSpt.getControllerSource().get(Chair.class);
        //---Создание образца записи кафедры - наследника абстрактного класса Kafedra
        Chair ouSample = new Chair();
        ouSample.setName(name);
        ouSample.setShortName(shortName);

        //--Выборка списка данных записи кафедры
        List<Chair> ou = ouController.findByExample(ouSample);

        //--Переменная для работы с экземпляром элемента спискка
        Chair p;
        //--Инициализация элемента списка
        if (ou.size() == 0) {
            p = new Chair();
            p.setName(name);
            p.setShortName(shortName);

        } else {
            p = ou.get(0);
        }
        return ouController.saveOrUpdate(p);
    }
}
