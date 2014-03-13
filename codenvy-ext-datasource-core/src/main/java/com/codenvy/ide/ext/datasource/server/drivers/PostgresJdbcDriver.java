package com.codenvy.ide.ext.datasource.server.drivers;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public class PostgresJdbcDriver extends JdbcDriver {

    private static final Logger            LOG                       = LoggerFactory.getLogger(PostgresJdbcDriver.class);

    private static final String            POSTGRES_DRIVER_NAME      = "postgres";
    private static final String            POSTGRES_DRIVER_CLASSNAME = "org.postgresql.Driver";

    private static final String            URL_TEMPLATE_POSTGRES     = "jdbc:postgresql://{0}:{1}/{2}";

    private static final Set<DatabaseType> SUPPORTED_DATABASETYPES   = Collections.singleton(DatabaseType.POSTGRES);

    private final static boolean           isActive;

    static {
        boolean success = false;
        try {
            Class.forName(POSTGRES_DRIVER_CLASSNAME);
            success = true;
        } catch (ClassNotFoundException e) {
            success = false;
            LOG.info("postgresql driver not present");
            LOG.debug("postgresql driver not present", e);
        }
        isActive = success;
    }

    public PostgresJdbcDriver() {
        super(POSTGRES_DRIVER_NAME, POSTGRES_DRIVER_CLASSNAME);
    }

    @Override
    public String getJdbcURIForDatasource(final DatabaseConfigurationDTO configuration) {
        if (DatabaseType.POSTGRES.equals(configuration.getDatabaseType())) {
            String url = MessageFormat.format(URL_TEMPLATE_POSTGRES,
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
        return SUPPORTED_DATABASETYPES;
    }

    @Override
    public boolean supportsDatabaseType(final DatabaseType type) {
        return (DatabaseType.POSTGRES.equals(type));
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

}
