package orion.cpu.controllers.event.base;

/**
 * Маркер события контроллера
 * @author sl
 */
public interface ControllerEvent {

    /**
     * Тип операции
     * @return
     */
    String getOperationType();
}
