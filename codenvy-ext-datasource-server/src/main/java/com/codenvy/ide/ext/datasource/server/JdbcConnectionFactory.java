/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.api.core.ApiException;
import com.codenvy.api.core.rest.HttpJsonHelper;
import com.codenvy.commons.json.JsonHelper;
import com.codenvy.commons.json.JsonParseException;
import com.codenvy.ide.ext.datasource.server.ssl.CodenvySSLSocketFactory;
import com.codenvy.ide.ext.datasource.server.ssl.CodenvySSLSocketFactoryKeyStoreSettings;
import com.codenvy.ide.ext.datasource.server.ssl.KeyStoreObject;
import com.codenvy.ide.ext.datasource.server.ssl.SslKeyStoreService;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.NuoDBBrokerDTO;
import com.codenvy.ide.ext.datasource.shared.exception.DatabaseDefinitionException;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Service that builds connections for configured datasources.
 *
 * @author "Mickaël Leduque"
 */
public class JdbcConnectionFactory {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(JdbcConnectionFactory.class);

    /** URL pattern for PostgreSQL databases. */
    private static final String URL_TEMPLATE_POSTGRES = "jdbc:postgresql://{0}:{1}/{2}";

    /** URL pattern for MySQL databases. */
    private static final String URL_TEMPLATE_MYSQL = "jdbc:mysql://{0}:{1}/{2}";

    /** URL pattern for Oracle databases. */
    private static final String URL_TEMPLATE_ORACLE = "jdbc:oracle:thin:@{0}:{1}:{2}";

    /** URL pattern for SQLServer databases. */
    private static final String URL_TEMPLATE_JTDS = "jdbc:jtds:sqlserver://{0}:{1}/{2}";

    /** URL pattern for NuoDB databases. */
    private static final String URL_TEMPLATE_NUODB = "jdbc:com.nuodb://{0}/{1}";

    protected String profileApiUrl;

    protected EncryptTextService encryptTextService;

    @Inject
    public JdbcConnectionFactory(@Named("api.endpoint") String apiUrl, EncryptTextService encryptTextService) {
        this.encryptTextService = encryptTextService;
        profileApiUrl = apiUrl + "/profile";
    }

    /**
     * builds a JDBC {@link Connection} for a datasource.
     *
     * @param configuration the datasource configuration
     * @return a connection
     * @throws SQLException if the creation of the connection failed
     * @throws DatabaseDefinitionException if the configuration is incorrect
     */
    public Connection getDatabaseConnection(final DatabaseConfigurationDTO configuration) throws SQLException, DatabaseDefinitionException {
        if (LOG.isInfoEnabled()) {
            Driver[] drivers = Collections.list(DriverManager.getDrivers()).toArray(new Driver[0]);
            LOG.info("Available jdbc drivers : {}", Arrays.toString(drivers));
        }

        Properties info = new Properties();
        info.setProperty("user", configuration.getUsername());

        final String password = configuration.getPassword();
        if (password != null && !password.isEmpty()) {
            try {
                info.setProperty("password", encryptTextService.decryptText(password));
            } catch (final Exception e1) {
                LOG.error("Couldn't decrypt the password, trying by setting the password without decryption", e1);
                info.setProperty("password", password);
            }
        } else {
            info.setProperty("password", "");
        }

        try {
            Map<String, String> preferences = getPreferences();

            CodenvySSLSocketFactoryKeyStoreSettings sslSettings = new CodenvySSLSocketFactoryKeyStoreSettings();
            if (configuration.getUseSSL()) {
                info.setProperty("useSSL", Boolean.toString(configuration.getUseSSL()));
                String sslKeyStore = preferences.get(KeyStoreObject.SSL_KEY_STORE_PREF_ID);
                sslSettings.setKeyStorePassword(SslKeyStoreService.getDefaultKeystorePassword());
                if (sslKeyStore != null) {
                    sslSettings.setKeyStoreContent(Base64.decodeBase64(sslKeyStore));
                }
            }

            if (configuration.getVerifyServerCertificate()) {
                info.setProperty("verifyServerCertificate", Boolean.toString(configuration.getVerifyServerCertificate()));
                String trustStore = preferences.get(KeyStoreObject.TRUST_STORE_PREF_ID);
                sslSettings.setTrustStorePassword(SslKeyStoreService.getDefaultTrustorePassword());
                if (trustStore != null) {
                    sslSettings.setTrustStoreContent(Base64.decodeBase64(trustStore));
                }
            }

            CodenvySSLSocketFactory.keystore.set(sslSettings);


        } catch (Exception e) {
            LOG.error("An error occured while getting keystore from Codenvy Preferences, JDBC connection will be performed without SSL", e);
        }
        final Connection connection = DriverManager.getConnection(getJdbcUrl(configuration), info);

        return connection;
    }

    /**
     * Builds a JDBC URL for a datasource.
     *
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getJdbcUrl(final DatabaseConfigurationDTO configuration) throws DatabaseDefinitionException {
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
            default:
                throw new DatabaseDefinitionException("Unknown database type "
                                                      + configuration.getDatabaseType()
                                                      + " in "
                                                      + configuration.toString());
        }
    }

    /**
     * Builds a JDBC URL for a PostgreSQL datasource.
     *
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getPostgresJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_POSTGRES,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    /**
     * Builds a JDBC URL for a MySQL datasource.
     *
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getMySQLJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_MYSQL,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    /**
     * Builds a JDBC URL for an Oracle datasource.
     *
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getOracleJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_ORACLE,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    /**
     * Builds a JDBC URL for a JTDS/MsSQL datasource.
     *
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getJTDSJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_JTDS,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    /**
     * Builds a JDBC URL for a NuoDB datasource.
     *
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
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

    @SuppressWarnings("unchecked")
    private Map<String, String> getPreferences() throws IOException, ApiException {
        final String preferencesJson = HttpJsonHelper.requestString(profileApiUrl + "/prefs", "GET", null);
        try {
            return JsonHelper.fromJson(preferencesJson, Map.class, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (JsonParseException e) {
            throw new ApiException("It is not possible to get user preferences");
        }
    }
}
