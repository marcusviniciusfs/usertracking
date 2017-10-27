package com.rd.factory;

import com.rd.UserTracking;
import com.rd.persistence.database.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UserTrackingFactory {

    private UserTrackingFactory() {
    }

    public static UserTracking create(final DatabaseService databaseService) {
        return new UserTrackingImpl(databaseService);
    }
}

class UserTrackingImpl implements UserTracking {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTracking.class);

    private static final String MESSAGE_ERROR_STARTING = "Error starting ";

    private static final String MESSAGE_ERROR_STOPPING = "Error stopping ";

    private DatabaseService databaseService;

    UserTrackingImpl(final DatabaseService theDatabaseService) {
        LOGGER.info("Version: {}", getVersion());
        databaseService = theDatabaseService;
        //new UserTrackingApplication(this);
    }

    @Override
    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    @Override
    public String getVersion() {
        final String version = UserTracking.class.getPackage().getImplementationVersion();
        if (version == null) {
            return "unknown";
        }
        return version;
    }
}
