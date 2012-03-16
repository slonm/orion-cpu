package ua.orion.cpu.core.security.services;

import java.lang.reflect.Method;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import ua.orion.core.services.EntityService;
import ua.orion.core.utils.IOCUtils;

/**
 * Перехватывает вызовы операций EntityManager над одной сущностью и проверяет
 * права на их выполнение.
 *
 * @author slobodyanuk
 */
public class SecurityAdvisor {

    private final EntityService es;
    private MethodAdviceReceiver receiver;

    public SecurityAdvisor(EntityService es) {
        this.es = es;
    }

    public void addAdvice(MethodAdviceReceiver receiver) {
        this.receiver = receiver;
        adviseModifyOp("persist", "insert", false);
        adviseModifyOp("merge", "update", true);
        adviseModifyOp("remove", "delete", true);
        adviseFindOp(IOCUtils.getMethod(EntityManager.class, "find", Class.class, Object.class));
        adviseFindOp(IOCUtils.getMethod(EntityManager.class, "find", Class.class, Object.class, Map.class));
        adviseFindOp(IOCUtils.getMethod(EntityManager.class, "find", Class.class, Object.class, LockModeType.class));
        adviseFindOp(IOCUtils.getMethod(EntityManager.class, "find", Class.class, Object.class, LockModeType.class, Map.class));
        adviseFindOp(IOCUtils.getMethod(EntityManager.class, "getReference", Class.class, Object.class));
    }

    private void adviseModifyOp(String methodName, final String operation, final boolean isInstancePermission) {
        Method method = IOCUtils.getMethod(EntityManager.class, methodName, Object.class);
        MethodAdvice advice = new MethodAdvice() {

            @Override
            public void advise(MethodInvocation invocation) {
                //Если субъект не установлен, то это, вероятнее всего, инициализацианные вызовы
                //SeedEntity. Разрешим их
                if (SecurityUtils.getSubject() != null) {
                    Object entity = invocation.getParameter(0);
                    String id = es.getPrimaryKey(entity).toString();
                    StringBuilder sb = new StringBuilder();
                    sb.append(entity.getClass().getSimpleName()).append(":").append(operation);
                    if (isInstancePermission) {
                        sb.append(":").append(id);
                    }
                    SecurityUtils.getSubject().checkPermission(sb.toString());
                }
                invocation.proceed();
            }
        };

        receiver.adviseMethod(method, advice);
    }

    private void adviseFindOp(Method method) {
        MethodAdvice advice = new MethodAdvice() {

            @Override
            public void advise(MethodInvocation invocation) {
                //Если субъект не установлен, то это, вероятнее всего, инициализацианные вызовы
                //SeedEntity. Разрешим их
                if (SecurityUtils.getSubject() != null) {
                    Class<?> entityClass = (Class<?>) invocation.getParameter(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append(entityClass.getSimpleName()).append(":read");
                    sb.append(":").append(invocation.getParameter(0).toString());
                    SecurityUtils.getSubject().checkPermission(sb.toString());
                }
                invocation.proceed();
            }
        };

        receiver.adviseMethod(method, advice);
    }
}
