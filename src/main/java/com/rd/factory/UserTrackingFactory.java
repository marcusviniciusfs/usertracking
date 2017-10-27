package com.rd.factory;

import com.rd.UserTracking;
import com.rd.UserTrackingException;
import com.rd.http.HTTPService;
import com.rd.http.UserTrackingApplication;
import com.rd.persistence.database.DatabaseService;
import com.rd.persistence.database.exception.TransactionException;
import com.rd.persistence.model.configuration.ServerConfiguration;
import com.rd.query.FindServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.util.concurrent.Future;

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
    private final StateMachine stateMachine;
    private HTTPService httpService;
    private ServerConfiguration serverConfiguration;

    UserTrackingImpl(final DatabaseService theDatabaseService) {
        LOGGER.info("Version: {}", getVersion());

        databaseService = theDatabaseService;
        stateMachine = new StateMachine(this);
    }

    @Override
    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    @Override
    public Future<Boolean> start() {
        return stateMachine.change(StateMachine.Command.START);
    }

    final void doStart() throws UserTrackingException {
        LOGGER.info("Starting...");

        startHTTPService();
    }

    private void startHTTPService() throws UserTrackingException {
        try {
            try {
                serverConfiguration = databaseService.submit(new FindServerConfiguration());
            } catch (TransactionException e) {
                throw new UserTrackingException("Error finding server configuration", e);
            }

            httpService = new HTTPService(
                        serverConfiguration.getHttpHostname(),
                        serverConfiguration.getHttpPort(),
                        new UserTrackingApplication(this, serverConfiguration));

        } catch (ServletException e) {
            throw new UserTrackingException(MESSAGE_ERROR_STARTING + HTTPService.class.getSimpleName(), e);
        }
        try {
            httpService.start();
        } catch (RuntimeException e) {
            throw new UserTrackingException(MESSAGE_ERROR_STARTING + HTTPService.class.getSimpleName(), e);
        }
    }

    @Override
    public Future<Boolean> restart() {
        return stateMachine.change(StateMachine.Command.RESTART);
    }

    final void doRestart() throws UserTrackingException {
        LOGGER.info("Restarting...");
        final long beginTime = System.currentTimeMillis();

        doStop();
        doStart();

        final long finishTime = System.currentTimeMillis() - beginTime;
        LOGGER.info("Restart took {} millisecs", finishTime);
    }

    public Future<Boolean> stop() {
        return stateMachine.change(StateMachine.Command.STOP);
    }

    final void doStop() {
        LOGGER.info("Stopping...");

        if (httpService != null) {
            httpService.stop();
        }

        LOGGER.info("Stopped");
    }

    @Override
    public String getVersion() {
        final String version = UserTracking.class.getPackage().getImplementationVersion();
        if (version == null) {
            return "unknown";
        }
        return version;
    }

    @Override
    public State getState() {
        return stateMachine.state();
    }
}
