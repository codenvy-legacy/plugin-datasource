package com.codenvy.ide.ext.datasource.server.drivers;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public class DrizzleJdbcDriver extends JdbcDriver {


    private static final Logger     LOG                      = LoggerFactory.getLogger(DrizzleJdbcDriver.class);

    private static final String     DRIZZLE_DRIVER_NAME      = "drizzle";
    private static final String     DRIZZLE_DRIVER_CLASSNAME = "org.drizzle.jdbc.DrizzleDriver";

    private static final String     URL_TEMPLATE_DRIZZLE     = "jdbc:drizzle://{0}:{1}/{2}";
    private static final String     URL_TEMPLATE_MYSQL       = "jdbc:mysql:thin://{0}:{1}/{2}";

    private final Set<DatabaseType> supportedDatabaseTypes   = new HashSet<>();

    private final static boolean    isActive;

    static {
        boolean success = false;
        try {
            Class.forName(DRIZZLE_DRIVER_CLASSNAME);
            success = true;
        } catch (ClassNotFoundException e) {
            success = false;
            LOG.info("drizzle driver not present");
            LOG.debug("drizzle driver not present", e);
        }
        isActive = success;
    }

    public DrizzleJdbcDriver() {
        super(DRIZZLE_DRIVER_NAME, DRIZZLE_DRIVER_CLASSNAME);
        this.supportedDatabaseTypes.add(DatabaseType.DRIZZLE);
        this.supportedDatabaseTypes.add(DatabaseType.MYSQL);
    }

    @Override
    public String getJdbcURIForDatasource(final DatabaseConfigurationDTO configuration) {
        if (DatabaseType.MYSQL.equals(configuration.getDatabaseType())) {
            String url = MessageFormat.format(URL_TEMPLATE_MYSQL,
                                              configuration.getHostname(),
                                              Integer.toString(configuration.getPort()),
                                              configuration.getDatabaseName());
            return url;
        } else if (DatabaseType.DRIZZLE.equals(configuration.getDatabaseType())) {
            String url = MessageFormat.format(URL_TEMPLATE_DRIZZLE,
                                              configuration.getHostname(),
                                              Integer.toString(configuration.getPort()),
                                              configuration.getDatabaseName());
            return url;
        } else {
            throw new RuntimeException("Unsupported database type");
        }
    }

    @Override
    public Set<DatabaseType> getSupportedDatabaseTypes() {
        return this.supportedDatabaseTypes;
    }

    @Override
    public boolean supportsDatabaseType(final DatabaseType type) {
        if (type == null) {
            return false;
        }
        switch (type) {
            case DRIZZLE:
            case MYSQL:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

}
