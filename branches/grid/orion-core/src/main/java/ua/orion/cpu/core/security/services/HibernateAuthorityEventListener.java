/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.security.services;

import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.hibernate.ejb.Ejb3Configuration;
//import org.hibernate.event.*;
import org.tynamo.jpa.Ejb3HibernateConfigurer;

/**
 *
 * @author sl
 */
@Scope(ScopeConstants.PERTHREAD)
public class HibernateAuthorityEventListener implements Ejb3HibernateConfigurer{//,
//        PreLoadEventListener, PreInsertEventListener, PreUpdateEventListener,
//        PreDeleteEventListener {

    private boolean insertMode = false;
    private boolean updateMode = false;
    private boolean deleteMode = false;

    @Override
    public void configure(Ejb3Configuration configuration) {
        configuration.getHibernateConfiguration().setListener("pre-load", this);
        configuration.getHibernateConfiguration().setListener("pre-insert", this);
        configuration.getHibernateConfiguration().setListener("pre-update", this);
        configuration.getHibernateConfiguration().setListener("pre-delete", this);
    }

//    @Override
//    public void onPreLoad(PreLoadEvent event) {
//        //во время изменяющих операций можно читать
//        //иначе будет ошибка при запоросе из таблиц прав
//        if (!insertMode && !updateMode && !deleteMode) {
////            authorizer.check(new Permission(event.getEntity().getClass(), OperationTypes.READ_OP), event.getEntity());
//        }
//    }
//
//    @Override
//    public boolean onPreInsert(PreInsertEvent event) {
//        insertMode = true;
//        try {
////            authorizer.check(new Permission(event.getEntity().getClass(), OperationTypes.STORE_OP), event.getEntity());
//        } finally {
//            insertMode = false;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onPreUpdate(PreUpdateEvent event) {
//        updateMode = true;
//        try {
////            authorizer.check(new Permission(event.getEntity().getClass(), OperationTypes.UPDATE_OP), event.getEntity());
//        } finally {
//            updateMode = false;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onPreDelete(PreDeleteEvent event) {
//        deleteMode = true;
//        try {
////            authorizer.check(new Permission(event.getEntity().getClass(), OperationTypes.REMOVE_OP), event.getEntity());
//        } finally {
//            deleteMode = false;
//        }
//        return false;
//    }
}
