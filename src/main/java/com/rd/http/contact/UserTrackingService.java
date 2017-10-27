package com.rd.http.contact;

import com.rd.http.resource.AbstractEntityService;
import com.rd.http.resource.ResourcePath;
import com.rd.persistence.database.DatabaseService;
import com.rd.persistence.model.Contact;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path(ResourcePath.SERVICE + "/contact")
@Consumes(MediaType.APPLICATION_JSON)
public class UserTrackingService extends AbstractEntityService<Contact> {

    public UserTrackingService(final DatabaseService service) {
        super(Contact.class, service);
    }
}
