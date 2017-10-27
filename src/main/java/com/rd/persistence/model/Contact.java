package com.rd.persistence.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
        @NamedQuery(
                name = Contact.FIND_ALL,
                query = "from Contact"
        )
})
@Entity
public class Contact extends AbstractEntity {

    public static final String FIND_ALL = "Contact.findAll";

    private String email;

    private String url;

    private String datetime;

    public Contact(final String theEmail, final String theUrl, final String theDateTime) {
        setEmail(theEmail);
        setUrl(theUrl);
        setDateTime(theDateTime);
    }

    protected Contact() {
    }

    public final String getEmail() {
        return email;
    }

    public final void setEmail(final String theEmail) {
        email = theEmail;
    }

    public final String getUrl() {
        return url;
    }

    public final void setUrl(final String theUrl) {
        url = theUrl;
    }

    public final String getDatetime() {
        return datetime;
    }

    public final void setDateTime(final String theDateTime) {
        datetime = theDateTime;
    }
}

