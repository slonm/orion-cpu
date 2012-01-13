/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.grid.services;

import javax.persistence.EntityManager;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.datasource.DataSource;
import orion.tapestry.grid.lib.datasource.impl.JPADataSource;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorJPACriteria;

/**
 *
 * @author dobro
 */
public class CpuGridDataSourceFactoryImpl implements CpuGridDataSourceFactory {

    /**
     * Создатель моделей для CpuGrid
     */
    private GridBeanModelSource gridBeanModelSource;
    /**
     * JPA EntityManager - подключение к базе данных
     */
    private EntityManager entityManager;
    /**
     * Сообщения интерфейса
     */
    private Messages messages;

    public CpuGridDataSourceFactoryImpl(GridBeanModelSource _gridBeanModelSource, EntityManager _entityManager, Messages _messages) {
        gridBeanModelSource = _gridBeanModelSource;
        entityManager = _entityManager;
        messages = _messages;
    }

    @Override
    public DataSource createDataSource(Class entityClass) {
        GridBeanModel gridBeanModel = gridBeanModelSource.createDisplayModel(entityClass, messages);
        RestrictionEditorJPACriteria editor = new RestrictionEditorJPACriteria(entityClass, entityManager);
        JPADataSource dataSource = new JPADataSource(gridBeanModel, entityManager, editor);
        return dataSource;
    }
}
