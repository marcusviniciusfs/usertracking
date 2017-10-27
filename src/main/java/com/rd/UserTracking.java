package com.rd;

import com.rd.persistence.database.DatabaseService;

import java.util.concurrent.Future;

public interface UserTracking {

    enum State {
        STARTING,
        STARTED,
        RESTARTING,
        STOPPING,
        STOPPED
    }

    DatabaseService getDatabaseService();

    State getState();

    String getVersion();

    Future<Boolean> start();

    Future<Boolean> restart();

    Future<Boolean> stop();
}
