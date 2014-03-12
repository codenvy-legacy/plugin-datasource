package com.codenvy.ide.ext.datasource.server;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public class DriverMappingImpl implements DriverMapping {

    private static final Logger                   LOG                            = LoggerFactory.getLogger(DriverMappingImpl.class);

    public static final String                    POSTGRES_JDBC_DRIVER_CLASSNAME = "org.postgresql.Driver";
    public static final String                    MYSQL_JDBC_DRIVER_CLASSNAME    = "com.mysql.jdbc.Driver";
    public static final String                    ORACLE_JDBC_DRIVER_CLASSNAME   = "oracle.jdbc.driver.OracleDriver";
    public static final String                    JTDS_JDBC_DRIVER_CLASSNAME     = "net.sourceforge.jtds.jdbc.Driver";
    public static final String                    NUODB_JDBC_DRIVER_CLASSNAME    = "com.nuodb.jdbc.Driver";
    public static final String                    DRIZZLE_JDBC_DRIVER_CLASSNAME  = "org.drizzle.jdbc.DrizzleDriver";

    static {
        try {
            Class.forName(POSTGRES_JDBC_DRIVER_CLASSNAME);
        } catch (ClassNotFoundException e) {
            LOG.info("postgresql driver not present");
            LOG.debug("postgresql driver not present", e);
        }
        try {
            Class.forName(MYSQL_JDBC_DRIVER_CLASSNAME);
        } catch (ClassNotFoundException e) {
            LOG.info("MySQL driver not present");
            LOG.debug("MySQL driver not present", e);
        }
        try {
            Class.forName(ORACLE_JDBC_DRIVER_CLASSNAME);
        } catch (ClassNotFoundException e) {
            LOG.info("Oracle driver not present");
            LOG.debug("Oracle driver not present", e);
        }
        try {
            Class.forName(JTDS_JDBC_DRIVER_CLASSNAME);
        } catch (ClassNotFoundException e) {
            LOG.info("JTDS driver not present");
            LOG.debug("JTDS driver not present", e);
        }
        try {
            Class.forName(NUODB_JDBC_DRIVER_CLASSNAME);
        } catch (ClassNotFoundException e) {
            LOG.info("NuoDB driver not present");
            LOG.debug("NuoDB driver not present", e);
        }
        try {
            Class.forName(DRIZZLE_JDBC_DRIVER_CLASSNAME);
        } catch (ClassNotFoundException e) {
            LOG.info("Drizzle driver not present");
            LOG.debug("Drizzle driver not present", e);
        }
    }

    private final Map<String, Set<DatabaseType>>  driverSupports                 = new HashMap<>();
    private final EnumMap<DatabaseType, String[]> driverPreferences              = new EnumMap<>(DatabaseType.class);

    public DriverMappingImpl() {
        this.driverSupports.put(POSTGRES_JDBC_DRIVER_CLASSNAME, Collections.singleton(DatabaseType.POSTGRES));
        this.driverSupports.put(JTDS_JDBC_DRIVER_CLASSNAME, Collections.singleton(DatabaseType.JTDS));
        this.driverSupports.put(MYSQL_JDBC_DRIVER_CLASSNAME, Collections.singleton(DatabaseType.MYSQL));
        this.driverSupports.put(ORACLE_JDBC_DRIVER_CLASSNAME, Collections.singleton(DatabaseType.ORACLE));
        this.driverSupports.put(NUODB_JDBC_DRIVER_CLASSNAME, Collections.singleton(DatabaseType.NUODB));

        Set<DatabaseType> drizzleSupports = new HashSet<>();
        drizzleSupports.add(DatabaseType.MYSQL);
        drizzleSupports.add(DatabaseType.DRIZZLE);
        this.driverSupports.put(DRIZZLE_JDBC_DRIVER_CLASSNAME, drizzleSupports);

        this.driverPreferences.put(DatabaseType.POSTGRES, new String[]{POSTGRES_JDBC_DRIVER_CLASSNAME});
        this.driverPreferences.put(DatabaseType.ORACLE, new String[]{ORACLE_JDBC_DRIVER_CLASSNAME});
        this.driverPreferences.put(DatabaseType.JTDS, new String[]{JTDS_JDBC_DRIVER_CLASSNAME});
        this.driverPreferences.put(DatabaseType.MYSQL, new String[]{MYSQL_JDBC_DRIVER_CLASSNAME, DRIZZLE_JDBC_DRIVER_CLASSNAME});
        this.driverPreferences.put(DatabaseType.NUODB, new String[]{NUODB_JDBC_DRIVER_CLASSNAME});
        this.driverPreferences.put(DatabaseType.DRIZZLE, new String[]{DRIZZLE_JDBC_DRIVER_CLASSNAME});
    }

    @Override
    public Set<DatabaseType> getSupportedDatabaseTypes(final String jdbcClassName) {
        return this.driverSupports.get(jdbcClassName);
    }

    @Override
    public String[] getDatabasePreferredDriver(final DatabaseType dbtype) {
        return this.driverPreferences.get(dbtype);
    }
}
