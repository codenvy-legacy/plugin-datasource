package com.codenvy.ide.ext.datasource.server;

import com.codenvy.ide.ext.datasource.server.drivers.JdbcDriver;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public interface DriverMapping {

    JdbcDriver getFirstCompatibleDriver(DatabaseType dbtype);
}
