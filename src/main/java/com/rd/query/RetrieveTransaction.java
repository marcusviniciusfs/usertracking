package com.rd.query;

import com.rd.persistence.database.Transaction;
import com.rd.persistence.database.exception.TransactionException;
import com.rd.persistence.model.AbstractEntity;

import javax.persistence.EntityManager;

public class RetrieveTransaction<T extends AbstractEntity> implements Transaction<T> {

    private final Class<T> entityClass;

    private final int id;

    public RetrieveTransaction(final Class<T> theEntityClass, final int entityId) {
        entityClass = theEntityClass;
        id = entityId;
    }

    @Override
    public final T execute(final EntityManager entityManager) throws TransactionException {
        return entityManager.find(entityClass, id);
    }
}
