package com.rd.query;

import com.rd.persistence.database.Transaction;
import com.rd.persistence.database.exception.TransactionException;
import com.rd.persistence.model.AbstractEntity;

import javax.persistence.EntityManager;

public class DeleteTransaction<T extends AbstractEntity> implements Transaction<T> {

    private final Class<T> entityClass;

    private final int id;

    public DeleteTransaction(final Class<T> theEntityClass, final int entityId) {
        entityClass = theEntityClass;
        id = entityId;
    }

    @Override
    public final T execute(final EntityManager entityManager) throws TransactionException {
        final RetrieveTransaction<T> transaction = new RetrieveTransaction<>(entityClass, id);
        final T managedEntity = transaction.execute(entityManager);
        if (managedEntity != null) {
            entityManager.remove(managedEntity);
        }
        return managedEntity;
    }
}
