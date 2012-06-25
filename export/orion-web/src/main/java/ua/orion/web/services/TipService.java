package ua.orion.web.services;

/**
 * Надстройка для AlertManager для удобного добавления в клиентскую страницу
 * сообщения о проделанной операции
 *
 * @author slobodyanuk
 */
public interface TipService {

    /**
     * Оборачивает вызов work в блок перехвата RuntimeException.
     * При удачном завершении операции выводит форматное информационное сообщение
     * извлеченное из каталога сообщений по ключу вида message.success.<messageFormat>.
     * При ошибке выводит форматное сообщение об ошибке
     * извлеченное из каталога сообщений по ключу вида message.error.<messageFormat>.
     * @author slobodyanuk
     * @param params параметры, подставляемые в  форматное сообщение.
     */
    void doWork(Runnable work, String messageFormat, Object... params);
}
