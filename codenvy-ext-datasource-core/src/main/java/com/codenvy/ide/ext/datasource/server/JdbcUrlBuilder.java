package com.codenvy.ide.ext.datasource.server;

import java.text.MessageFormat;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.exception.DatabaseDefinitionException;

public class JdbcUrlBuilder {

    private static final String URL_TEMPLATE_POSTGRES = "jdbc:postgresql://{0}:{1}/{2}";
    private static final String URL_TEMPLATE_MYSQL    = "jdbc:mysql://{0}:{1}/{2}";
    private static final String URL_TEMPLATE_ORACLE   = "jdbc:oracle:thin:@{0}:{1}:{2}";
    private static final String URL_TEMPLATE_JTDS     = "jdbc:jtds:sqlserver://{0}:{1}/{2}";

    public String getJdbcUrl(final DatabaseConfigurationDTO configuration) throws DatabaseDefinitionException {
        // Should we check and sanitize input values ?
        if (configuration.getDatabaseType() == null) {
            throw new DatabaseDefinitionException("Database type is null in " + configuration.toString());
        }
        switch (configuration.getDatabaseType()) {
            case POSTGRES:
                return getPostgresJdbcUrl(configuration);
            case MYSQL:
                return getMySQLJdbcUrl(configuration);
            case ORACLE:
                return getOracleJdbcUrl(configuration);
            case JTDS:
                return getJTDSJdbcUrl(configuration);
            default:
                throw new DatabaseDefinitionException("Unknown database type "
                                                      + configuration.getDatabaseType()
                                                      + " in "
                                                      + configuration.toString());
        }
    }

    private String getPostgresJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_POSTGRES,
                                          configuration.getHostname(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    private String getMySQLJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_MYSQL,
                                          configuration.getHostname(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    private String getOracleJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_ORACLE,
                                          configuration.getHostname(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    private String getJTDSJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_JTDS,
                                          configuration.getHostname(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }
}
