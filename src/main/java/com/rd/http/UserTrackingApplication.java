package com.rd.http;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class UserTrackingApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();

    public UserTrackingApplication() {
        singletons.add(new UserTrackingService());
    }

    @Override
    public final Set<Object> getSingletons() {
        return singletons;
    }
}
