package com.rd.http;

import com.rd.UserTracking;
import com.rd.http.contact.UserTrackingService;
import com.rd.http.resource.ResourcePath;
import com.rd.persistence.model.configuration.ServerConfiguration;
import com.rd.util.RESTEasyJodaModuleProvider;
import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class UserTrackingApplication extends Application {

    private final UserTracking userTracking;

    public UserTrackingApplication(final UserTracking theUserTracking, final ServerConfiguration serverConfiguration) {

        userTracking = theUserTracking;

        final BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion(userTracking .getVersion());
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost(serverConfiguration.getHttpHostname() + ":" + serverConfiguration.getHttpPort());
        beanConfig.setBasePath(ResourcePath.ROOT);
        beanConfig.setResourcePackage(this.getClass().getPackage().getName());
        beanConfig.setScan(true);
        beanConfig.setPrettyPrint(true);
    }

    @Override
    public final Set<Class<?>> getClasses() {
        final Set<Class<?>> set = new HashSet();
        set.add(RESTEasyJodaModuleProvider.class);
        set.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        set.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        set.add(WebApplicationExceptionMapper.class);
        return set;
    }

    @Override
    public final Set<Object> getSingletons() {
        final Set<Object> set = new HashSet<>();

        set.add(new UserTrackingService(userTracking.getDatabaseService()));

        return set;
    }
}
