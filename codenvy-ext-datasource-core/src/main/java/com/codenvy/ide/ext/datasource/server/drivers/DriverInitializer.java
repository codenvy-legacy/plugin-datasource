package com.codenvy.ide.ext.datasource.server.drivers;

import com.google.inject.Inject;

/**
 * initialization of the driver manager and the driver mapping instances. Should disappear once the mechanism to discover extensions exists.
 * 
 * @author "Mickaël Leduque"
 */
public class DriverInitializer {

    @Inject
    public DriverInitializer(final DriverManager driverManager) {
        driverManager.registerDriver(new PostgresJdbcDriver());
        driverManager.registerDriver(new OracleJdbcDriver());
        driverManager.registerDriver(new JtdsJdbcDriver());
        driverManager.registerDriver(new MySQLJdbcDriver());
        driverManager.registerDriver(new NuoDBJdbcDriver());
        driverManager.registerDriver(new DrizzleJdbcDriver());
    }
}
