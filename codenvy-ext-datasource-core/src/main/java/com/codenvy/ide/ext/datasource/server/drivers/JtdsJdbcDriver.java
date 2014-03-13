package com.codenvy.ide.ext.datasource.server.drivers;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public class JtdsJdbcDriver extends JdbcDriver {


    private static final Logger            LOG                     = LoggerFactory.getLogger(JtdsJdbcDriver.class);

    private static final String            JTDS_DRIVER_NAME        = "jtds";
    private static final String            JTDS_DRIVER_CLASSNAME   = "net.sourceforge.jtds.jdbc.Driver";

    private static final String            URL_TEMPLATE_JTDS       = "jdbc:jtds:sqlserver://{0}:{1}/{2}";

    private static final Set<DatabaseType> SUPPORTED_DATABASETYPES = Collections.singleton(DatabaseType.JTDS);

    private final static boolean           isActive;

    static {
        boolean success = false;
        try {
            Class.forName(JTDS_DRIVER_CLASSNAME);
            success = true;
        } catch (ClassNotFoundException e) {
            success = false;
            LOG.info("jtds driver not present");
            LOG.debug("jtds driver not present", e);
        }
        isActive = success;
    }

    public JtdsJdbcDriver() {
        super(JTDS_DRIVER_NAME, JTDS_DRIVER_CLASSNAME);
    }

    @Override
    public String getJdbcURIForDatasource(final DatabaseConfigurationDTO configuration) {
        if (DatabaseType.JTDS.equals(configuration.getDatabaseType())) {
            String url = MessageFormat.format(URL_TEMPLATE_JTDS,
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
        return (DatabaseType.JTDS.equals(type));
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

}
