package com.rd.query;

import com.rd.persistence.database.Transaction;
import com.rd.persistence.database.exception.TransactionException;
import com.rd.persistence.model.AbstractEntity;

import javax.persistence.EntityManager;

public class CreateTransaction<T extends AbstractEntity> implements Transaction<T> {

    private final T t;

    public CreateTransaction(final T entity) {
        t = entity;
    }

    @Override
    public final T execute(final EntityManager entityManager) throws TransactionException {
        entityManager.persist(t);
        return t;
    }
}
