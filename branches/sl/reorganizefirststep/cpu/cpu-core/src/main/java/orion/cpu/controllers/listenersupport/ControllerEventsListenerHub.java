package orion.cpu.controllers.listenersupport;

/**
 * Интерфейс обсервера событий контроллера
 * @author sl
 */
public interface ControllerEventsListenerHub {

    /**
     * Добавить слушателя
     * @param id идентификатор слушателя
     * @param listener слушатель
     * @param constraints ограничения на порядок обработки слушателей.
     * Синтаксис аналогичный используемому в
     * {@link org.apache.tapestry5.ioc.internal.util.Orderer},
     * {@link http://tapestry.apache.org/tapestry5.1/tapestry-ioc/order.html}
     * @author sl
     */
    void addListener(String id, ControllerEventsListener listener, String... constraints);

    /**
     * Заменить слушателя
     * @param id идентификатор слушателя
     * @param listener слушатель. Если null то удаление listener с указанным id
     * @param constraints ограничения на порядок обработки слушателей.
     * Синтаксис аналогичный используемому в
     * {@link org.apache.tapestry5.ioc.internal.util.Orderer}
     * @author sl
     */
    void overrideListener(String id, ControllerEventsListener listener, String... constraints);
}
