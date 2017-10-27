package com.rd.http.resource;


import com.rd.persistence.database.DatabaseService;
import com.rd.persistence.database.exception.TransactionException;
import com.rd.persistence.model.AbstractEntity;
import com.rd.query.CreateTransaction;
import com.rd.query.DeleteTransaction;
import com.rd.query.UpdateTransaction;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
public abstract class AbstractEntityService<T extends AbstractEntity> extends AbstractReadOnlyEntityService<T> {

    public AbstractEntityService(final Class<T> entityClass, final DatabaseService service) {
        super(entityClass, service);
    }

    @POST
    public final T create(@Valid @NotNull final T t) {
        try {
            return getDatabaseService().submit(new CreateTransaction<T>(t));
        } catch (TransactionException e) {
            final Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new WebApplicationException(rootCause, Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rootCause.getMessage()).build());
        }
    }

    @PUT
    public final T update(@Valid @NotNull final T t) {
        if (t.getId() == null) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Must set the entity id").build());
        }
        try {
            final T updatedEntity = getDatabaseService().submit(new UpdateTransaction<T>(getEntityClass(), t));
            if (null == updatedEntity) {
                throw new NotFoundException("Entity not found for id: " + t.getId());
            }
            return updatedEntity;
        } catch (TransactionException e) {
            final Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new WebApplicationException(rootCause, Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rootCause.getMessage()).build());
        }
    }

    @DELETE
    @Path("{id}")
    public final T delete(@PathParam("id") final int id) {
        try {
            final T removedEntity = getDatabaseService().submit(new DeleteTransaction<T>(getEntityClass(), id));
            if (null == removedEntity) {
                throw new NotFoundException("Entity not found for id: " + id);
            }
            return removedEntity;
        } catch (TransactionException e) {
            final Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new WebApplicationException(rootCause, Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(rootCause.getMessage()).build());
        }
    }
}
