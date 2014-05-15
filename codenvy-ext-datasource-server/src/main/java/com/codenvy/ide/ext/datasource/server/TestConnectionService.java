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

import java.sql.Connection;
import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.ConnectionTestResultDTO;
import com.codenvy.ide.ext.datasource.shared.ConnectionTestResultDTO.Status;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.ServicePaths;
import com.codenvy.ide.ext.datasource.shared.exception.DatabaseDefinitionException;
import com.google.common.base.Throwables;
import com.google.inject.Inject;

/**
 * Service for connection tests on datasources.
 * 
 * @author "Mickaël Leduque"
 */
@Path(ServicePaths.TEST_DATABASE_CONNECTIVITY_PATH)
public class TestConnectionService {

    /** The logger. */
    private static final Logger         LOG = LoggerFactory.getLogger(TestConnectionService.class);

    /** the provider for JDBC connections. */
    private final JdbcConnectionFactory jdbcConnectionFactory;

    @Inject
    public TestConnectionService(final JdbcConnectionFactory jdbcConnectionFactory) {
        this.jdbcConnectionFactory = jdbcConnectionFactory;
    }

    /**
     * Tests a datasource configuration by opening a connection.
     * 
     * @param databaseConfig the datasource configuration
     * @return true iff the connection was successfully created
     * @throws DatabaseDefinitionException is the datasource is not correctly defined
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String testDatabaseConnectivity(final DatabaseConfigurationDTO databaseConfig) throws DatabaseDefinitionException {
        if (databaseConfig == null) {
            throw new DatabaseDefinitionException("Database definition is 'null'");
        }

        final ConnectionTestResultDTO testResult = DtoFactory.getInstance().createDto(ConnectionTestResultDTO.class);

        try (final Connection connection = this.jdbcConnectionFactory.getDatabaseConnection(databaseConfig)) {
            if (connection != null) {
                testResult.setTestResult(Status.SUCCESS);
            } else {
                testResult.setTestResult(Status.FAILURE);
                // no message
            }
        } catch (final SQLException e) {
            LOG.debug("Connection test failed ; error messages : {} | {}", e.getMessage());
            if (LOG.isTraceEnabled()) {
                LOG.trace("Connection test failed ; exception : {}", Throwables.getStackTraceAsString(e));
            }
            testResult.withTestResult(Status.FAILURE).withFailureMessage(e.getLocalizedMessage());
        } catch (DatabaseDefinitionException e) {
            testResult.withTestResult(Status.FAILURE).withFailureMessage(e.getLocalizedMessage());
        }
        return DtoFactory.getInstance().toJson(testResult);
    }
}
