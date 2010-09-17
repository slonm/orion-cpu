package orion.cpu.security.services;

import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authentication.entity.Role;
import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.authorization.Authorizer;
import org.hibernate.Criteria;

/**
 * Расширенная версия {@link Authorizer}.
 * The service has a per-thread scope.
 * @author sl
 */
//TODO Отвязать ExtendedAuthorizer от org.hibernate.Criteria
public interface ExtendedAuthorizer extends Authorizer {

    /**
     * Добавляет ограничения на выбор данных.
     * Часть механизма Row  Level Security. Записи, которые нельзя
     * видеть пользователю должны быть исключены добавлением условий к
     * объекту criteria.
     * @param criteria объект Criteria, который дополнится ограничениями
     */
    void addConstraintsToCriteria(Criteria criteria);

    /**
     * Показывает есть ли право у текущего пользователя с текущей ролью
     * @param permission право
     * @return true if can, overwise false
     */
    public boolean can(Permission permission);

    /**
     * Показывает есть ли право у текущего пользователя с текущей ролью
     * на указанный объект
     * @param permission право
     * @param object объект
     * @return true if can, overwise false
     */
    public boolean can(Permission permission, Object object);

    /**
     * Вызывает исключение, если у текущего пользователя с текущей ролью нет права
     * @param permission право
     * @throws br.com.arsmachina.authorization.TypeAuthorizationException или его подкласс
     */
    public void check(Permission permission);

    /**
     * Вызывает исключение, если у текущего пользователя с текущей ролью
     * нет права на указанный объект
     * @param permission право
     * @param object объект
     * @throws br.com.arsmachina.authorization.ObjectAuthorizationException или его подкласс
     */
    public void check(Permission permission, Object object);

    /**
     * Сохраняет текущего пользователя и роль в потоке
     * @param user
     * @param role
     * @return role, если user имеет такую роль, иначе null
     */
    public Role storeUserAndRole(User user, Role role);

    public Role getRole();

    public User getUser();

    /**
     * Сохраняет в стеке пользователя и роль и обнуляет их
     */
    public void pushUserAndRole();

    /**
     * Извлекает из стека пользователя и роль
     */
    public void popUserAndRole();

}
