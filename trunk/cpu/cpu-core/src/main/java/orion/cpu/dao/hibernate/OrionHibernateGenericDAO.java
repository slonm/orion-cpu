package orion.cpu.dao.hibernate;

import br.com.arsmachina.dao.hibernate.ConcreteDAOImpl;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

/**
 * Расширяет ConcreteDAOImpl для поддержки сложного поиска по образцу
 * @param <T> the entity class related to this DAO.
 * @param <K> the type of the field that represents the entity class' primary key.
 * @author sl
 */
public class OrionHibernateGenericDAO<T, K extends Serializable> extends ConcreteDAOImpl<T, K> {

    public OrionHibernateGenericDAO(Class<T> clasz, SessionFactory sessionFactory) {
        super(clasz, sessionFactory);
    }

    /**
     * Модифицированный алгоритм поиска по образцу.
     * Поддерживает ассоциации вида один-один, один-много
     * @author sl
     */
    @Override
    public List<T> findByExample(T example) {
        Criteria criteria = createCriteria();
        addSortCriteria(criteria);
        if (example != null) {
            criteria.add(createExample(example));
        }
        addOneToConstraints(criteria, example);
        return criteria.list();
    }

    /**
     * Добавляет к criteria ограничения для
     * ассоциации вида один-один, один-много
     * @param criteria модифициремый объект Criteria
     * @param example образец
     * @author sl
     */
    //TODO Протестировать рекурсию
    protected void addOneToConstraints(Criteria criteria, Object example) {
        ClassMetadata cm = getSessionFactory().getClassMetadata(example.getClass());
        for (String name : cm.getPropertyNames()) {
            Type t = cm.getPropertyType(name);
            if (t.isAssociationType() && !t.isCollectionType()) {
                Object subObj = cm.getPropertyValue(example, name, getSession().getEntityMode());
                if (subObj != null) {
                    ClassMetadata subCm = getSessionFactory().getClassMetadata(subObj.getClass());
                    criteria.createCriteria(name).add(Restrictions.eq(subCm.getIdentifierPropertyName(), subCm.getIdentifier(subObj, getSession().getEntityMode())));
                }
            }
        }

    }
}
