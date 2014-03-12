/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.datasource.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
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

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

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
import com.google.common.base.Charsets;
import com.google.common.net.HttpHeaders;
import com.google.inject.Inject;

@Path("{ws-name}/" + ServicePaths.BASE_DATASOURCE_PATH)
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
            LOG.info("postgresql driver not present");
            LOG.debug("postgresql driver not present", e);
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.info("MySQL driver not present");
            LOG.debug("MySQL driver not present", e);
        }
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            LOG.info("Oracle driver not present");
            LOG.debug("Oracle driver not present", e);
        }
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.info("JTDS driver not present");
            LOG.debug("JTDS driver not present", e);
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
        LOG.info(msg);
        return msg;
    }

    @Path(ServicePaths.DATABASE_METADATA_PATH)
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String getDatabase(final DatabaseConfigurationDTO databaseConfig) throws Exception {

        Database database = null;

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

            final SchemaCrawler schemaCrawler = new SchemaCrawler(connection);
            database = schemaCrawler.crawl(options);
        }

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

        return DtoFactory.getInstance().toJson(databaseDTO);
    }


    @Path(ServicePaths.EXECUTE_SQL_REQUEST_PATH)
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String executeSqlRequest(final RequestParameterDTO request) throws SQLException, DatabaseDefinitionException {
        final Connection connection = getDatabaseConnection(request.getDatabase());

        MultipleRequestExecutionMode mode = SqlRequestService.DEFAULT_MODE;
        if (request.getMultipleRequestExecutionMode() != null) {
            mode = request.getMultipleRequestExecutionMode();
        }

        final RequestResultGroupDTO resultGroup = this.sqlRequestService.executeSqlRequest(request, connection, mode);

        String json = DtoFactory.getInstance().toJson(resultGroup);
        LOG.debug("Return " + json);
        return json;
    }

    @Path(ServicePaths.RESULT_CSV_PATH + "/{data}")
    @GET
    @Produces({TEXT_CSV + TEXT_CSV_CHARSET_UTF8_OPTION + TEXT_CSV_HEADER_OPTION, MediaType.TEXT_PLAIN})
    public Response exportAsCSV(@PathParam("data") final String encodedRequestResult) {
        if (encodedRequestResult == null) {
            throw new IllegalArgumentException("Missing data parameter for exportAsCSV");
        }
        String jsonRequestResult;
        try {
            jsonRequestResult = URLDecoder.decode(encodedRequestResult, Charsets.UTF_8.name());
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Incorrect encoding in parameter string " + e.getMessage());
        }
        final RequestResultDTO requestResult = DtoFactory.getInstance().createDtoFromJson(jsonRequestResult,
                                                                                          RequestResultDTO.class);
        if (requestResult == null) {
            throw new IllegalArgumentException("The parameter doesn't contain a result request");
        }
        if (requestResult.getResultType() == UpdateResultDTO.TYPE) {
            throw new IllegalArgumentException("Only request results for select can be converted to CSV");
        }

        final ResponseBuilder response = Response.ok();
        response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv");
        response.entity(convertDataToCsv(requestResult, true));

        return response.build();
    }

    private String convertDataToCsv(final RequestResultDTO requestResult, boolean withHeader) {
        LOG.info("convertDataToCsv - called for {}, withHeader={}", requestResult, withHeader);

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
        Driver[] drivers = Collections.list(DriverManager.getDrivers()).toArray(new Driver[0]);


        LOG.info("Available jdbc drivers : {}", Arrays.toString(drivers));
        Connection connection = DriverManager.getConnection(this.jdbcUrlBuilder.getJdbcUrl(configuration),
                                                            configuration.getUsername(),
                                                            configuration.getPassword());


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
            testResult.withTestResult(Status.FAILURE).withFailureMessage(e.getLocalizedMessage());
        }
        return DtoFactory.getInstance().toJson(testResult);
    }
}
