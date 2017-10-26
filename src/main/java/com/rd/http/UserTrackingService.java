package com.rd.http;

import org.apache.http.HttpStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/message")
public class UserTrackingService {

    @GET
    @Path("/{param}")
    public final Response printMessage(@PathParam("param") final String msg) {

        String result = "Restful example : " + msg;

        return Response.status(HttpStatus.SC_OK).entity(result).build();

    }
}
