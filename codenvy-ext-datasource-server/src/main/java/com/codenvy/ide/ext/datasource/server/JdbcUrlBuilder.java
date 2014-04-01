/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.ext.datasource.server;

import java.text.MessageFormat;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.NuoDBBrokerDTO;
import com.codenvy.ide.ext.datasource.shared.exception.DatabaseDefinitionException;

public class JdbcUrlBuilder {

    private static final String URL_TEMPLATE_POSTGRES          = "jdbc:postgresql://{0}:{1}/{2}";
    private static final String URL_TEMPLATE_MYSQL             = "jdbc:mysql://{0}:{1}/{2}";
    private static final String URL_TEMPLATE_ORACLE            = "jdbc:oracle:thin:@{0}:{1}:{2}";
    private static final String URL_TEMPLATE_JTDS              = "jdbc:jtds:sqlserver://{0}:{1}/{2}";
    private static final String URL_TEMPLATE_NUODB             = "jdbc:com.nuodb://{0}/{1}";

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
            case NUODB:
                return getNuoDBJdbcUrl(configuration);
            case GOOGLECLOUDSQL:
                return getMySQLJdbcUrl(configuration);
            case AMAZONRDS:
                return  getMySQLJdbcUrl(configuration);
            default:
                throw new DatabaseDefinitionException("Unknown database type "
                                                      + configuration.getDatabaseType()
                                                      + " in "
                                                      + configuration.toString());
        }
    }

    private String getPostgresJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_POSTGRES,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    private String getMySQLJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_MYSQL,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    private String getOracleJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_ORACLE,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    private String getJTDSJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_JTDS,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    private String getNuoDBJdbcUrl(final DatabaseConfigurationDTO configuration) throws DatabaseDefinitionException {
        if (configuration.getBrokers() == null || configuration.getBrokers().isEmpty()) {
            throw new DatabaseDefinitionException("no brokers configured");
        }
        StringBuilder hostPart = new StringBuilder();
        boolean first = true;
        for (final NuoDBBrokerDTO brokerConf : configuration.getBrokers()) {
            if (first) {
                first = false;
            } else {
                hostPart.append(",");
            }
            hostPart.append(brokerConf.getHostName())
                    .append(":")
                    .append(brokerConf.getPort());
        }
        String url = MessageFormat.format(URL_TEMPLATE_NUODB,
                                          hostPart.toString(),
                                          configuration.getDatabaseName());
        return url;
    }
}
