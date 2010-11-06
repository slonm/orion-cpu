package orion.cpu.views.desktoptest;

import br.com.arsmachina.authentication.controller.PasswordEncrypter;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.services.LoggingAdvisor;
import org.slf4j.Logger;
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

    /**
     * Логгер вызовов всех сервисов
     * @param receiver
     */
//    @Match("*")
//    public static void adviseCpuMenu(final MethodAdviceReceiver receiver) {
//        MethodAdvice advice = new MethodAdvice() {
//
//            Logger LOG = LoggerFactory.getLogger("ServiceLogger");
//
//            @Override
//            public void advise(Invocation invocation) {
//                long startTime = System.currentTimeMillis();
//                String service = receiver.getInterface().getSimpleName();
//                String method = invocation.getMethodName();
//                String result = "";
//                Map<String, String> params = null;
//                try {
//                    invocation.proceed();
//                    result = invocation.getResultType().getSimpleName() + "(" + String.valueOf(invocation.getResult()) + ")";
//                    for (int i = 0; i < invocation.getParameterCount(); i++) {
//                        if (params == null) {
//                            params = new HashMap<String, String>();
//                        }
//                        params.put(String.valueOf(i), invocation.getParameterType(i).getSimpleName() + "(" + String.valueOf(invocation.getParameter(i)) + ")");
//                    }
//                } finally {
//                    long elapsed = System.currentTimeMillis() - startTime;
//
//                    LOG.info(String.format("Request time: %d ms", elapsed));
//                }
//            }
//        };
//        receiver.adviseAllMethods(advice);
//    }

    @Match("*")
    public static void adviseLogging(LoggingAdvisor loggingAdvisor, Logger logger, MethodAdviceReceiver reciever) {
        loggingAdvisor.addLoggingAdvice(logger, reciever);
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
