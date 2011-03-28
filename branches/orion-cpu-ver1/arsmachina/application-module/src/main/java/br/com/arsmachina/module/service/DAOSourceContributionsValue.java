package br.com.arsmachina.module.service;

import br.com.arsmachina.dao.DAO;
import java.io.Serializable;

/**
 *
 * @param <T>
 * @param <K>
 * @author sl
 */
public class DAOSourceContributionsValue<T, K extends Serializable> {

    private final Class<? extends DAO<T, K>> daoImplementation;
    private final Class<? extends DAO<T, K>> daoDefinition;

    public DAOSourceContributionsValue(Class<? extends DAO<T, K>> daoImplementation, Class<? extends DAO<T, K>> daoDefinition) {
        this.daoImplementation = daoImplementation;
        this.daoDefinition = daoDefinition;
    }

    public Class<? extends DAO<T, K>> getDAODefinition() {
        return daoDefinition;
    }

    public Class<? extends DAO<T, K>> getDAOImplementation() {
        return daoImplementation;
    }
}
