package com.codenvy.ide.ext.datasource.server.drivers;

import java.util.List;

import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public interface DriverAffinityManager {

    void declareDriverAffinity(DatabaseType databaseType, JdbcDriver driver);

    List<JdbcDriver> getDatabasePreferredDriver(DatabaseType dbtype);
}
