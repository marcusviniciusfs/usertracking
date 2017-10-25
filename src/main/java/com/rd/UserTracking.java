package com.rd;

import com.rd.persistence.database.DatabaseService;

public interface UserTracking {

    DatabaseService getDatabaseService();

    String getVersion();
}
