package com.codenvy.ide.ext.datasource.server.drivers;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public class NuoDBJdbcDriver extends JdbcDriver {


    private static final Logger            LOG                     = LoggerFactory.getLogger(NuoDBJdbcDriver.class);

    private static final String            NUODB_DRIVER_NAME       = "nuodb";
    private static final String            NUODB_DRIVER_CLASSNAME  = "com.nuodb.jdbc.Driver";

    private static final String            URL_TEMPLATE_NUODB      = "jdbc:com.nuodb://{0}:{1}/{2}";

    private static final Set<DatabaseType> SUPPORTED_DATABASETYPES = Collections.singleton(DatabaseType.NUODB);

    private final static boolean           isActive;

    static {
        boolean success = false;
        try {
            Class.forName(NUODB_DRIVER_CLASSNAME);
            success = true;
        } catch (ClassNotFoundException e) {
            success = false;
            LOG.info("nuodb driver not present");
            LOG.debug("nuodb driver not present", e);
        }
        isActive = success;
    }

    public NuoDBJdbcDriver() {
        super(NUODB_DRIVER_NAME, NUODB_DRIVER_CLASSNAME);
    }

    @Override
    public String getJdbcURIForDatasource(final DatabaseConfigurationDTO configuration) {
        if (DatabaseType.NUODB.equals(configuration.getDatabaseType())) {
            String url = MessageFormat.format(URL_TEMPLATE_NUODB,
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
        return (DatabaseType.NUODB.equals(type));
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

}
