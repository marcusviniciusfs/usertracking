package com.rd.query;

import com.rd.persistence.database.Transaction;
import com.rd.persistence.database.exception.TransactionException;
import com.rd.persistence.model.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class RetrieveListTransaction<T extends AbstractEntity> implements Transaction<List<T>> {

    private final Class<T> entityClass;

    public RetrieveListTransaction(final Class<T> theEntityClass) {
        entityClass = theEntityClass;
    }

    @Override
    public final List<T> execute(final EntityManager entityManager) throws TransactionException {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> query = builder.createQuery(entityClass);
        final Root<T> root = query.from(entityClass);
        query.select(root);
        query.where(new Predicate[0]);
        query.orderBy(builder.asc(root.get("id")));

        return entityManager.createQuery(query).getResultList();
    }
}
