package com.rd.persistence.database;

import com.rd.persistence.database.exception.TransactionException;

import javax.persistence.EntityManager;

public interface Transaction<T> {
    T execute(EntityManager entityManager) throws TransactionException;
}
