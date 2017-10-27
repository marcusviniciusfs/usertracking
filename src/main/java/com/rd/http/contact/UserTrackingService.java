package com.rd.http.contact;

/*import com.rd.http.AbstractEntityService;
import com.rd.http.ResourcePath;
import com.rd.persistence.database.DatabaseService;
import com.rd.persistence.model.Contato;*/

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/message")
public class UserTrackingService /*extends AbstractEntityService<Contato>*/ {

    /*public UserTrackingService(final DatabaseService service) {
        super(Contato.class, service);
    }*/

    @GET
    @Path("/{param}")
    public final Response printMessage(@PathParam("param") final String msg) {

        String result = "Restful example : " + msg;

        return Response.status(Response.Status.OK).entity(result).build();

    }
}
