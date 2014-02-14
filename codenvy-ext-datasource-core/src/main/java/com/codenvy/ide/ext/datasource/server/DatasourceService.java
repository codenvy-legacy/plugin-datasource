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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.ColumnDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.DriversDTO;
import com.codenvy.ide.ext.datasource.shared.RequestParameterDTO;
import com.codenvy.ide.ext.datasource.shared.SchemaDTO;
import com.codenvy.ide.ext.datasource.shared.TableDTO;
import com.codenvy.ide.ext.datasource.shared.exception.DatabaseDefinitionException;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultGroupDTO;
import com.codenvy.ide.ext.datasource.shared.request.SelectResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.UpdateResultDTO;
import com.google.inject.Inject;

@Path("{ws-name}/datasource")
public class DatasourceService {
    private static final Logger  LOG = LoggerFactory.getLogger(DatasourceService.class);

    private final JdbcUrlBuilder jdbcUrlBuilder;

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
    public DatasourceService(final JdbcUrlBuilder jdbcUrlBuilder) {
        this.jdbcUrlBuilder = jdbcUrlBuilder;
    }

    @Path("drivers")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String isDatabaseDriverAvailable() throws Exception {
        Enumeration<Driver> loadedDrivers = DriverManager.getDrivers();
        List<String> drivers = new ArrayList<>();
        while (loadedDrivers.hasMoreElements()) {
            Driver driver = loadedDrivers.nextElement();
            drivers.add(driver.getClass().getCanonicalName());
        }
        DriversDTO driversDTO = DtoFactory.getInstance().createDto(DriversDTO.class).withDrivers(drivers);
        return DtoFactory.getInstance().toJson(driversDTO);
    }


    @Path("database")
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


    @Path("executeSqlRequest")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String executeSqlRequest(final RequestParameterDTO request) throws SQLException, DatabaseDefinitionException {
        LOG.info("Execution request ; parameter : {}", request);
        try (
            final Connection connection = getDatabaseConnection(request.getDatabase());
            final Statement statement = connection.createStatement();) {

            final RequestResultGroupDTO resultGroup = DtoFactory.getInstance().createDto(RequestResultGroupDTO.class);
            final List<RequestResultDTO> resultList = new ArrayList<>();
            resultGroup.setResults(resultList);

            statement.setMaxRows(request.getResultLimit());
            boolean returnsRows = statement.execute(request.getSqlRequest());
            LOG.info("Request executed successfully");

            ResultSet resultSet = statement.getResultSet();
            int count = statement.getUpdateCount();
            while (resultSet != null || count != -1) {
                LOG.info("New result returned by request :");

                if (count != -1) {
                    LOG.info("   is an update count");
                    final UpdateResultDTO result = DtoFactory.getInstance().createDto(UpdateResultDTO.class);
                    resultList.add(result);
                    result.withResultType(UpdateResultDTO.TYPE).withUpdateCount(count);
                } else {
                    LOG.info("   is a result set");
                    final SelectResultDTO result = DtoFactory.getInstance().createDto(SelectResultDTO.class);
                    result.setResultType(SelectResultDTO.TYPE);
                    resultList.add(result);

                    final ResultSetMetaData metadata = resultSet.getMetaData();
                    final int columnCount = metadata.getColumnCount();

                    // header : column names
                    final List<String> columnNames = new ArrayList<>();
                    for (int i = 1; i < columnCount + 1; i++) {
                        columnNames.add(metadata.getColumnLabel(i));
                    }
                    result.setHeaderLine(columnNames);

                    final List<List<String>> lines = new ArrayList<>();

                    // result : actual data
                    while (resultSet.next()) {
                        final List<String> line = new ArrayList<>();
                        for (int i = 1; i < columnCount + 1; i++) {
                            line.add(resultSet.getString(i));
                        }
                        lines.add(line);
                    }
                    result.setResultLines(lines);
                }

                // continue the loop - next result

                // getMoreResult should close it, but just to remove the warning
                if (resultSet != null) {
                    resultSet.close();
                }
                boolean moreResults = statement.getMoreResults();
                resultSet = statement.getResultSet();
                count = statement.getUpdateCount();
            }

            String json = DtoFactory.getInstance().toJson(resultGroup);
            LOG.debug("Return " + json);
            return json;
        }
    }

    private Connection getDatabaseConnection(final DatabaseConfigurationDTO configuration) throws SQLException, DatabaseDefinitionException {
        Driver[] drivers = Collections.list(DriverManager.getDrivers()).toArray(new Driver[0]);


        LOG.info("Available jdbc drivers : {}", Arrays.toString(drivers));
        Connection connection = DriverManager.getConnection(this.jdbcUrlBuilder.getJdbcUrl(configuration),
                                                            configuration.getUsername(),
                                                            configuration.getPassword());

        return connection;
    }
}
