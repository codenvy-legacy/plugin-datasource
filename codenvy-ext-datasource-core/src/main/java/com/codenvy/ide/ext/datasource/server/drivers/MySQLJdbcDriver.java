package com.codenvy.ide.ext.datasource.server.drivers;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public class MySQLJdbcDriver extends JdbcDriver {


    private static final Logger            LOG                     = LoggerFactory.getLogger(MySQLJdbcDriver.class);

    private static final String            MYSQL_DRIVER_NAME       = "mysql-connectorJ";
    private static final String            MYSQL_DRIVER_CLASSNAME  = "com.mysql.jdbc.Driver";

    private static final String            URL_TEMPLATE_MYSQL      = "jdbc:mysql://{0}:{1}/{2}";

    private static final Set<DatabaseType> SUPPORTED_DATABASETYPES = Collections.singleton(DatabaseType.MYSQL);

    private final static boolean           isActive;

    static {
        boolean success = false;
        try {
            Class.forName(MYSQL_DRIVER_CLASSNAME);
            success = true;
        } catch (ClassNotFoundException e) {
            success = false;
            LOG.info("mysql connector/J driver not present");
            LOG.debug("mysql connector/J driver not present", e);
        }
        isActive = success;
    }

    public MySQLJdbcDriver() {
        super(MYSQL_DRIVER_NAME, MYSQL_DRIVER_CLASSNAME);
    }

    @Override
    public String getJdbcURIForDatasource(final DatabaseConfigurationDTO configuration) {
        if (DatabaseType.MYSQL.equals(configuration.getDatabaseType())) {
            String url = MessageFormat.format(URL_TEMPLATE_MYSQL,
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
        return (DatabaseType.MYSQL.equals(type));
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

}
