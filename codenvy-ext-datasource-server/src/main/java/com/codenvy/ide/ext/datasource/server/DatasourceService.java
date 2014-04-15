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

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import schemacrawler.crawl.SchemaCrawler;
import schemacrawler.schema.Column;
import schemacrawler.schema.Database;
import schemacrawler.schema.IndexColumn;
import schemacrawler.schema.PrimaryKey;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import au.com.bytecode.opencsv.CSVWriter;

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.ColumnDTO;
import com.codenvy.ide.ext.datasource.shared.ConnectionTestResultDTO;
import com.codenvy.ide.ext.datasource.shared.ConnectionTestResultDTO.Status;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.DriversDTO;
import com.codenvy.ide.ext.datasource.shared.MultipleRequestExecutionMode;
import com.codenvy.ide.ext.datasource.shared.RequestParameterDTO;
import com.codenvy.ide.ext.datasource.shared.SchemaDTO;
import com.codenvy.ide.ext.datasource.shared.ServicePaths;
import com.codenvy.ide.ext.datasource.shared.TableDTO;
import com.codenvy.ide.ext.datasource.shared.exception.DatabaseDefinitionException;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultGroupDTO;
import com.codenvy.ide.ext.datasource.shared.request.UpdateResultDTO;
import com.google.common.base.Throwables;
import com.google.common.math.LongMath;
import com.google.inject.Inject;

@Path(ServicePaths.BASE_DATASOURCE_PATH)
public class DatasourceService {

    private static final Logger     LOG                          = LoggerFactory.getLogger(DatasourceService.class);

    public final static String      TEXT_CSV                     = "text/csv";
    public final static String      TEXT_CSV_HEADER_OPTION       = "; header=present";
    public final static String      TEXT_CSV_NO_HEADER_OPTION    = "; header=absent";
    public final static String      TEXT_CSV_CHARSET_UTF8_OPTION = "; charset=utf8";
    public final static MediaType   TEXT_CSV_TYPE                = new MediaType("text", "csv");

    private final JdbcUrlBuilder    jdbcUrlBuilder;

    private final SqlRequestService sqlRequestService;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            LOG.debug("postgresql driver not present");
            LOG.trace("postgresql driver not present", e);
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.debug("MySQL driver not present");
            LOG.trace("MySQL driver not present", e);
        }
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            LOG.debug("Oracle driver not present");
            LOG.trace("Oracle driver not present", e);
        }
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.debug("JTDS driver not present");
            LOG.trace("JTDS driver not present", e);
        }
        try {
            Class.forName("com.nuodb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.debug("NuoDB driver not present");
            LOG.trace("NuoDB driver not present", e);
        }
    }

    @Inject
    public DatasourceService(final JdbcUrlBuilder jdbcUrlBuilder,
                             final SqlRequestService sqlRequestService) {
        this.jdbcUrlBuilder = jdbcUrlBuilder;
        this.sqlRequestService = sqlRequestService;
    }

    @Path(ServicePaths.DATABASE_TYPES_PATH)
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String getAvailableDatabaseDrivers() throws Exception {
        final Enumeration<Driver> loadedDrivers = DriverManager.getDrivers();
        final List<String> drivers = new ArrayList<>();
        while (loadedDrivers.hasMoreElements()) {
            Driver driver = loadedDrivers.nextElement();
            drivers.add(driver.getClass().getCanonicalName());
        }
        final DriversDTO driversDTO = DtoFactory.getInstance().createDto(DriversDTO.class).withDrivers(drivers);
        final String msg = DtoFactory.getInstance().toJson(driversDTO);
        LOG.debug(msg);
        return msg;
    }

    @Path(ServicePaths.DATABASE_METADATA_PATH)
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String getDatabase(final DatabaseConfigurationDTO databaseConfig) throws Exception {

        Database database = null;

        final long startTime = System.currentTimeMillis();
        long endSetupTime;
        try (final Connection connection = getDatabaseConnection(databaseConfig)) {
            final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
            SchemaInfoLevel customized = SchemaInfoLevel.standard();
            customized.setRetrieveAdditionalTableAttributes(true);
            customized.setRetrieveAdditionalColumnAttributes(true);
            options.setSchemaInfoLevel(customized);
            options.setRoutineInclusionRule(new ExcludeAll());
            // options.setSchemaInclusionRule(new
            // RegularExpressionInclusionRule(
            // "PUBLIC.BOOKS"));

            endSetupTime = System.currentTimeMillis();
            final SchemaCrawler schemaCrawler = new SchemaCrawler(connection);
            database = schemaCrawler.crawl(options);
        }
        final long endCrawlingTime = System.currentTimeMillis();

        DatabaseDTO databaseDTO = DtoFactory.getInstance().createDto(DatabaseDTO.class)
                                            .withName(database.getName())
                                            .withLookupKey(database.getLookupKey())
                                            .withDatabaseProductName(database.getDatabaseInfo().getProductName())
                                            .withDatabaseProductVersion(database.getDatabaseInfo().getProductVersion())
                                            .withUserName(database.getDatabaseInfo().getUserName())
                                            .withJdbcDriverName(database.getJdbcDriverInfo().getDriverName())
                                            .withJdbcDriverVersion(database.getJdbcDriverInfo().getDriverVersion());
        Map<String, SchemaDTO> schemaToInject = new HashMap<String, SchemaDTO>();
        databaseDTO = databaseDTO.withSchemas(schemaToInject);
        for (final Schema schema : database.getSchemas()) {
            SchemaDTO schemaDTO = DtoFactory.getInstance().createDto(SchemaDTO.class)
                                            // TODO maybe clean up this (schema.getName() can be null with mysql and the json serializer has
                                            // a constraint on it)
                                            // TODO do we always want to display the fullname ? rather than the name ?
                                            .withName((schema.getName() == null) ? schema.getFullName() : schema.getName())
                                            .withLookupKey(schema.getLookupKey());
            Map<String, TableDTO> tables = new HashMap<String, TableDTO>();
            for (final Table table : database.getTables(schema)) {
                TableDTO tableDTO = DtoFactory.getInstance().createDto(TableDTO.class)
                                              .withName(table.getName())
                                              .withLookupKey(table.getLookupKey())
                                              .withType(table.getTableType().name());
                if (table instanceof View) {
                    tableDTO = tableDTO.withIsView(true);
                }

                PrimaryKey primaryKey = table.getPrimaryKey();
                if (primaryKey != null) {
                    List<IndexColumn> primaryKeyColumns = primaryKey.getColumns();
                    List<String> pkColumnNames = new ArrayList<>();
                    for (final IndexColumn indexColumn : primaryKeyColumns) {
                        pkColumnNames.add(indexColumn.getName());
                    }
                    tableDTO.setPrimaryKey(pkColumnNames);
                }


                Map<String, ColumnDTO> columns = new HashMap<String, ColumnDTO>();
                for (Column column : table.getColumns()) {
                    ColumnDTO columnDTO = DtoFactory
                                                    .getInstance()
                                                    .createDto(ColumnDTO.class)
                                                    .withName(column.getName())
                                                    .withLookupKey(column.getLookupKey())
                                                    .withColumnDataType(column.getColumnDataType().getName())
                                                    .withDefaultValue(column.getDefaultValue())
                                                    .withNullable(column.isNullable())
                                                    .withDataSize(column.getSize())
                                                    .withDecimalDigits(column.getDecimalDigits());
                    columns.put(columnDTO.getName(), columnDTO);
                }
                tableDTO = tableDTO.withColumns(columns);
                tables.put(tableDTO.getName(), tableDTO);
            }
            schemaDTO.withTables(tables);
            schemaToInject.put(schemaDTO.getName(), schemaDTO);
        }
        final long endDtoTime = System.currentTimeMillis();
        final String jsonResult = DtoFactory.getInstance().toJson(databaseDTO);
        final long endJsonTime = System.currentTimeMillis();

        try {
            LOG.debug("Schema metadata obtained - setup {}ms ; crawl {}ms ; dto conversion {}ms ; json conversion {}ms - total {}ms",
                      LongMath.checkedSubtract(endSetupTime, startTime),
                      LongMath.checkedSubtract(endCrawlingTime, endSetupTime),
                      LongMath.checkedSubtract(endDtoTime, endCrawlingTime),
                      LongMath.checkedSubtract(endJsonTime, endDtoTime),
                      LongMath.checkedSubtract(endJsonTime, startTime));
        } catch (final ArithmeticException e) {
            LOG.debug("Schema metadata obtained - no time info");
        }

        return jsonResult;
    }


    /**
     * Executes the SQL requests given as parameter.
     * 
     * @param request the requests parameters
     * @return a result object, either success (with data) or failure (with message)
     * @throws SQLException if the execution caused an error
     * @throws DatabaseDefinitionException if the datasource is not correctly defined
     */
    @Path(ServicePaths.EXECUTE_SQL_REQUEST_PATH)
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String executeSqlRequest(final RequestParameterDTO request) throws SQLException, DatabaseDefinitionException {
        try (final Connection connection = getDatabaseConnection(request.getDatabase())) {

            MultipleRequestExecutionMode mode = SqlRequestService.DEFAULT_MODE;
            if (request.getMultipleRequestExecutionMode() != null) {
                mode = request.getMultipleRequestExecutionMode();
            }

            long startTime = System.currentTimeMillis();
            final RequestResultGroupDTO resultGroup = this.sqlRequestService.executeSqlRequest(request, connection, mode);
            long endExecTime = System.currentTimeMillis();

            String json = DtoFactory.getInstance().toJson(resultGroup);
            long endJsonTime = System.currentTimeMillis();
            try {
                LOG.debug("Execution of SQL request '{}' with result limit {} - sql duration={}, json conversion duration={}",
                          request.getSqlRequest(), request.getResultLimit(),
                          LongMath.checkedSubtract(endExecTime, startTime),
                          LongMath.checkedSubtract(endJsonTime, endExecTime));
            } catch (final ArithmeticException e) {
                LOG.debug("Execution of SQL request '{}' with result limit {} - unknwown durations");
            }
            LOG.trace("Return {}", json);
            return json;
        }
    }

    @Path(ServicePaths.RESULT_CSV_PATH)
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String exportAsCSV(final RequestResultDTO requestResult) {
        if (requestResult == null) {
            throw new IllegalArgumentException("The parameter doesn't contain a result request");
        }
        if (requestResult.getResultType() == UpdateResultDTO.TYPE) {
            throw new IllegalArgumentException("Only request results for select can be converted to CSV");
        }


        String csvResult = convertDataToCsv(requestResult, true);

        byte[] byteResult = csvResult.getBytes(StandardCharsets.UTF_8);
        String encodedResult = Base64.encodeBase64String(byteResult);
        return encodedResult;
    }

    private String convertDataToCsv(final RequestResultDTO requestResult, boolean withHeader) {
        LOG.debug("convertDataToCsv - called for {}, withHeader={}", requestResult, withHeader);

        final StringBuilder sb = new StringBuilder();
        try (
            final Writer writer = new StringBuilderWriter(sb);
            final CSVWriter csvWriter = new CSVWriter(writer)) {

            // header
            if (withHeader) {
                csvWriter.writeNext(requestResult.getHeaderLine().toArray(new String[0]));
            }

            // body
            for (final List<String> line : requestResult.getResultLines()) {
                csvWriter.writeNext(line.toArray(new String[0]));
            }
        } catch (final IOException e) {
            LOG.error("Close failed on resource - expect leaks and incorrect operation", e);
        }

        return sb.toString();
    }

    private Connection getDatabaseConnection(final DatabaseConfigurationDTO configuration) throws SQLException, DatabaseDefinitionException {
        if (LOG.isInfoEnabled()) {
            Driver[] drivers = Collections.list(DriverManager.getDrivers()).toArray(new Driver[0]);
            LOG.info("Available jdbc drivers : {}", Arrays.toString(drivers));
        }

        Properties info = new Properties();
        info.setProperty("user", configuration.getUsername());
        info.setProperty("password", configuration.getPassword());
        if (configuration.getUseSSL()) {
            info.setProperty("useSSL", Boolean.toString(configuration.getUseSSL()));
        }
        if (configuration.getVerifyServerCertificate()) {
            info.setProperty("verifyServerCertificate", Boolean.toString(configuration.getVerifyServerCertificate()));
        }

        final Connection connection = DriverManager.getConnection(this.jdbcUrlBuilder.getJdbcUrl(configuration),
                                                                  info);

        return connection;
    }

    @Path(ServicePaths.TEST_DATABASE_CONNECTIVITY_PATH)
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String testDatabaseConnectivity(final DatabaseConfigurationDTO databaseConfig) throws Exception {

        final ConnectionTestResultDTO testResult = DtoFactory.getInstance().createDto(ConnectionTestResultDTO.class);

        try (final Connection connection = getDatabaseConnection(databaseConfig)) {
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
        }
        return DtoFactory.getInstance().toJson(testResult);
    }
}
