package com.codenvy.ide.ext.datasource.server.drivers;

import java.util.Iterator;
import java.util.Set;

import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public interface DriverRegistry extends Iterable<JdbcDriver> {

    void registerDriver(JdbcDriver driver);

    Iterator<JdbcDriver> iterator();

    Set<DatabaseType> getSupportedDatabaseTypes();
}
