package com.codenvy.ide.ext.datasource.server.drivers;

import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.inject.Inject;

/**
 * initialization of the driver manager and the driver mapping instances. Should disappear once the mechanism to discover extensions exists.
 * 
 * @author "Mickaël Leduque"
 */
public class DriverInitializer {

    @Inject
    public DriverInitializer(final DriverRegistry driverManager,
                             final DriverAffinityManager driverAffinityManager) {
        PostgresJdbcDriver postgresJdbcDriver = new PostgresJdbcDriver();
        OracleJdbcDriver oracleJdbcDriver = new OracleJdbcDriver();
        JtdsJdbcDriver jtdsJdbcDriver = new JtdsJdbcDriver();
        MySQLJdbcDriver mySQLJdbcDriver = new MySQLJdbcDriver();
        NuoDBJdbcDriver nuoDBJdbcDriver = new NuoDBJdbcDriver();
        DrizzleJdbcDriver drizzleJdbcDriver = new DrizzleJdbcDriver();

        driverManager.registerDriver(postgresJdbcDriver);
        driverManager.registerDriver(oracleJdbcDriver);
        driverManager.registerDriver(jtdsJdbcDriver);
        driverManager.registerDriver(mySQLJdbcDriver);
        driverManager.registerDriver(nuoDBJdbcDriver);
        driverManager.registerDriver(drizzleJdbcDriver);

        driverAffinityManager.declareDriverAffinity(DatabaseType.POSTGRES, postgresJdbcDriver);
        driverAffinityManager.declareDriverAffinity(DatabaseType.ORACLE, oracleJdbcDriver);
        driverAffinityManager.declareDriverAffinity(DatabaseType.JTDS, jtdsJdbcDriver);
        driverAffinityManager.declareDriverAffinity(DatabaseType.NUODB, nuoDBJdbcDriver);
        driverAffinityManager.declareDriverAffinity(DatabaseType.MYSQL, mySQLJdbcDriver);
        driverAffinityManager.declareDriverAffinity(DatabaseType.MYSQL, drizzleJdbcDriver);
        driverAffinityManager.declareDriverAffinity(DatabaseType.DRIZZLE, drizzleJdbcDriver);
    }
}
