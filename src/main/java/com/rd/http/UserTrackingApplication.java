package com.rd.http;

import com.rd.http.contact.UserTrackingService;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class UserTrackingApplication extends Application {

    /*//private final UserTracking userTracking;
    private Set<Object> singletons = new HashSet<Object>();

    public UserTrackingApplication(final UserTracking theUserTracking) {
        //userTracking = theUserTracking;
        singletons.add(new UserTrackingService(*//*userTracking.getDatabaseService()*//*));
    }

    @Override
    public final Set<Class<?>> getClasses() {
        final Set<Class<?>> set = new HashSet();
        set.add(RESTEasyJodaModuleProvider.class);
        set.add(WebApplicationExceptionMapper.class);
        return set;
    }

    @Override
    public final Set<Object> getSingletons() {
        return singletons;
    }*/

    private Set<Object> singletons = new HashSet<Object>();

    public UserTrackingApplication() {
        singletons.add(new UserTrackingService());
    }

    @Override
    public final Set<Object> getSingletons() {
        return singletons;
    }
}
