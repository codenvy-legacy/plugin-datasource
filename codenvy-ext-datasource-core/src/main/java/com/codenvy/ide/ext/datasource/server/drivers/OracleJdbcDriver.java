package com.codenvy.ide.ext.datasource.server.drivers;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public class OracleJdbcDriver extends JdbcDriver {

    private static final Logger            LOG                     = LoggerFactory.getLogger(OracleJdbcDriver.class);

    private static final String            ORACLE_DRIVER_NAME      = "oracle";
    private static final String            ORACLE_DRIVER_CLASSNAME = "oracle.jdbc.driver.OracleDriver";

    private static final String            URL_TEMPLATE_ORACLE     = "jdbc:oracle:thin:@{0}:{1}:{2}";

    private static final Set<DatabaseType> SUPPORTED_DATABASETYPES = Collections.singleton(DatabaseType.ORACLE);

    private final static boolean           isActive;

    static {
        boolean success = false;
        try {
            Class.forName(ORACLE_DRIVER_CLASSNAME);
            success = true;
        } catch (ClassNotFoundException e) {
            success = false;
            LOG.info("oracle driver not present");
            LOG.debug("oracle driver not present", e);
        }
        isActive = success;
    }

    public OracleJdbcDriver() {
        super(ORACLE_DRIVER_NAME, ORACLE_DRIVER_CLASSNAME);
    }

    @Override
    public String getJdbcURIForDatasource(final DatabaseConfigurationDTO configuration) {
        if (DatabaseType.ORACLE.equals(configuration.getDatabaseType())) {
            String url = MessageFormat.format(URL_TEMPLATE_ORACLE,
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
        return (DatabaseType.ORACLE.equals(type));
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

}
