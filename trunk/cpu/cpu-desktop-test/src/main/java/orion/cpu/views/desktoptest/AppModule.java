package orion.cpu.views.desktoptest;

import br.com.arsmachina.authentication.controller.PasswordEncrypter;
import org.apache.tapestry5.ioc.*;
import orion.cpu.services.CoreIOCModule;

/**
 * Модуль конфигурирования IOC
 */
public class AppModule {

    /**
     * Регистрация и конфигурирование опций.
     * @param configuration
     */
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration) {
        //Заполнение базы данных тестовыми данными
        configuration.add(CoreIOCModule.FILL_TEST_DATA, "true");
    }

    public static PasswordEncrypter buildPasswordEncrypter() {
        return new PasswordEncrypter() {

            @Override
            public String encrypt(String password) {
                return password;
            }
        };
    }
}
