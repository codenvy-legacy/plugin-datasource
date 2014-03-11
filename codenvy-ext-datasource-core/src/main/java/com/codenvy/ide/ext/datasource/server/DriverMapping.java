package com.codenvy.ide.ext.datasource.server;

import java.util.Set;

import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public interface DriverMapping {

    Set<DatabaseType> getSupportedDatabaseTypes(String jdbcClassName);

    String[] getDatabasePreferredDriver(DatabaseType dbtype);
}
