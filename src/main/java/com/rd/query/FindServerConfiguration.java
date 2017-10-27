package com.rd.query;

import com.rd.persistence.database.Transaction;
import com.rd.persistence.database.exception.TransactionException;
import com.rd.persistence.model.configuration.ServerConfiguration;

import javax.persistence.EntityManager;

public class FindServerConfiguration implements Transaction<ServerConfiguration> {

    @Override
    public final ServerConfiguration execute(final EntityManager entityManager) throws TransactionException {
        return entityManager.createNamedQuery(ServerConfiguration.FIND_SERVER_CONFIGURATION, ServerConfiguration .class).getSingleResult();
    }
}
