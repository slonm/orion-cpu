package orion.cpu.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.util.*;
import org.hibernate.*;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;

/**
 * Обертка для org.hibernate.Session с интерфейсом
 * org.hibernate.classic.Session
 * @author sl
 */
public class HibernateSessionWrapper implements org.hibernate.classic.Session{

    private final org.hibernate.Session session;

    public HibernateSessionWrapper(org.hibernate.Session session) {
        this.session = session;
    }

    public String toString() {
        return session.toString();
    }

    public int hashCode() {
        return session.hashCode();
    }

    public boolean equals(Object obj) {
        return session.equals(obj);
    }

    public void update(String entityName, Object object) throws HibernateException {
        session.update(entityName, object);
    }

    public void update(Object object) throws HibernateException {
        session.update(object);
    }

    public void setReadOnly(Object entity, boolean readOnly) {
        session.setReadOnly(entity, readOnly);
    }

    public void setFlushMode(FlushMode flushMode) {
        session.setFlushMode(flushMode);
    }

    public void setCacheMode(CacheMode cacheMode) {
        session.setCacheMode(cacheMode);
    }

    public void saveOrUpdate(String entityName, Object object) throws HibernateException {
        session.saveOrUpdate(entityName, object);
    }

    public void saveOrUpdate(Object object) throws HibernateException {
        session.saveOrUpdate(object);
    }

    public Serializable save(String entityName, Object object) throws HibernateException {
        return session.save(entityName, object);
    }

    public Serializable save(Object object) throws HibernateException {
        return session.save(object);
    }

    public void replicate(String entityName, Object object, ReplicationMode replicationMode) throws HibernateException {
        session.replicate(entityName, object, replicationMode);
    }

    public void replicate(Object object, ReplicationMode replicationMode) throws HibernateException {
        session.replicate(object, replicationMode);
    }

    public void refresh(Object object, LockMode lockMode) throws HibernateException {
        session.refresh(object, lockMode);
    }

    public void refresh(Object object) throws HibernateException {
        session.refresh(object);
    }

    public void reconnect(Connection connection) throws HibernateException {
        session.reconnect(connection);
    }

    public void reconnect() throws HibernateException {
        session.reconnect();
    }

    public void persist(String entityName, Object object) throws HibernateException {
        session.persist(entityName, object);
    }

    public void persist(Object object) throws HibernateException {
        session.persist(object);
    }

    public Object merge(String entityName, Object object) throws HibernateException {
        return session.merge(entityName, object);
    }

    public Object merge(Object object) throws HibernateException {
        return session.merge(object);
    }

    public void lock(String entityName, Object object, LockMode lockMode) throws HibernateException {
        session.lock(entityName, object, lockMode);
    }

    public void lock(Object object, LockMode lockMode) throws HibernateException {
        session.lock(object, lockMode);
    }

    public void load(Object object, Serializable id) throws HibernateException {
        session.load(object, id);
    }

    public Object load(String entityName, Serializable id) throws HibernateException {
        return session.load(entityName, id);
    }

    public Object load(Class theClass, Serializable id) throws HibernateException {
        return session.load(theClass, id);
    }

    public Object load(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        return session.load(entityName, id, lockMode);
    }

    public Object load(Class theClass, Serializable id, LockMode lockMode) throws HibernateException {
        return session.load(theClass, id, lockMode);
    }

    public boolean isOpen() {
        return session.isOpen();
    }

    public boolean isDirty() throws HibernateException {
        return session.isDirty();
    }

    public boolean isConnected() {
        return session.isConnected();
    }

    public Transaction getTransaction() {
        return session.getTransaction();
    }

    public SessionStatistics getStatistics() {
        return session.getStatistics();
    }

    public SessionFactory getSessionFactory() {
        return session.getSessionFactory();
    }

    public Session getSession(EntityMode entityMode) {
        return session.getSession(entityMode);
    }

    public Query getNamedQuery(String queryName) throws HibernateException {
        return session.getNamedQuery(queryName);
    }

    public Serializable getIdentifier(Object object) throws HibernateException {
        return session.getIdentifier(object);
    }

    public FlushMode getFlushMode() {
        return session.getFlushMode();
    }

    public String getEntityName(Object object) throws HibernateException {
        return session.getEntityName(object);
    }

    public EntityMode getEntityMode() {
        return session.getEntityMode();
    }

    public Filter getEnabledFilter(String filterName) {
        return session.getEnabledFilter(filterName);
    }

    public LockMode getCurrentLockMode(Object object) throws HibernateException {
        return session.getCurrentLockMode(object);
    }

    public CacheMode getCacheMode() {
        return session.getCacheMode();
    }

    public Object get(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        return session.get(entityName, id, lockMode);
    }

    public Object get(String entityName, Serializable id) throws HibernateException {
        return session.get(entityName, id);
    }

    public Object get(Class clazz, Serializable id, LockMode lockMode) throws HibernateException {
        return session.get(clazz, id, lockMode);
    }

    public Object get(Class clazz, Serializable id) throws HibernateException {
        return session.get(clazz, id);
    }

    public void flush() throws HibernateException {
        session.flush();
    }

    public void evict(Object object) throws HibernateException {
        session.evict(object);
    }

    public Filter enableFilter(String filterName) {
        return session.enableFilter(filterName);
    }

    public void doWork(Work work) throws HibernateException {
        session.doWork(work);
    }

    public Connection disconnect() throws HibernateException {
        return session.disconnect();
    }

    public void disableFilter(String filterName) {
        session.disableFilter(filterName);
    }

    public void delete(String entityName, Object object) throws HibernateException {
        session.delete(entityName, object);
    }

    public void delete(Object object) throws HibernateException {
        session.delete(object);
    }

    public SQLQuery createSQLQuery(String queryString) throws HibernateException {
        return session.createSQLQuery(queryString);
    }

    public Query createQuery(String queryString) throws HibernateException {
        return session.createQuery(queryString);
    }

    public Query createFilter(Object collection, String queryString) throws HibernateException {
        return session.createFilter(collection, queryString);
    }

    public Criteria createCriteria(String entityName, String alias) {
        return session.createCriteria(entityName, alias);
    }

    public Criteria createCriteria(String entityName) {
        return session.createCriteria(entityName);
    }

    public Criteria createCriteria(Class persistentClass, String alias) {
        return session.createCriteria(persistentClass, alias);
    }

    public Criteria createCriteria(Class persistentClass) {
        return session.createCriteria(persistentClass);
    }

    public boolean contains(Object object) {
        return session.contains(object);
    }

    public Connection connection() throws HibernateException {
        return session.connection();
    }

    public Connection close() throws HibernateException {
        return session.close();
    }

    public void clear() {
        session.clear();
    }

    public void cancelQuery() throws HibernateException {
        session.cancelQuery();
    }

    public Transaction beginTransaction() throws HibernateException {
        return session.beginTransaction();
    }

    @Override
    public Object saveOrUpdateCopy(Object object) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object saveOrUpdateCopy(Object object, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object saveOrUpdateCopy(String entityName, Object object) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object saveOrUpdateCopy(String entityName, Object object, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List find(String query) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List find(String query, Object value, Type type) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List find(String query, Object[] values, Type[] types) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator iterate(String query) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator iterate(String query, Object value, Type type) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator iterate(String query, Object[] values, Type[] types) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection filter(Object collection, String filter) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection filter(Object collection, String filter, Object value, Type type) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection filter(Object collection, String filter, Object[] values, Type[] types) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int delete(String query) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int delete(String query, Object value, Type type) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int delete(String query, Object[] values, Type[] types) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Query createSQLQuery(String sql, String returnAlias, Class returnClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Query createSQLQuery(String sql, String[] returnAliases, Class[] returnClasses) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(Object object, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(String entityName, Object object, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Object object, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(String entityName, Object object, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDefaultReadOnly() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDefaultReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object load(Class theClass, Serializable id, LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object load(String entityName, Serializable id, LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LockRequest buildLockRequest(LockOptions lockOptions) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void refresh(Object object, LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object get(Class clazz, Serializable id, LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object get(String entityName, Serializable id, LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isReadOnly(Object entityOrProxy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isFetchProfileEnabled(String name) throws UnknownProfileException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void enableFetchProfile(String name) throws UnknownProfileException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void disableFetchProfile(String name) throws UnknownProfileException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TypeHelper getTypeHelper() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LobHelper getLobHelper() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
