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
package com.codenvy.ide.ext.datasource.shared;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;

import schemacrawler.crawl.SchemaCrawler;
import schemacrawler.schema.Column;
import schemacrawler.schema.Database;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.DatabaseConnectionOptions;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;

import com.codenvy.dto.server.DtoFactory;

public class TestDTO {
    @Ignore
    @Test
    public void test() throws Exception {

        final DataSource dataSource = new DatabaseConnectionOptions(
                                                                    "org.postgresql.Driver",
                                                                    "jdbc:postgresql://localhost:5432/nuxeo");
        final Connection connection = dataSource.getConnection("postgres",
                                                               "nuxeospirit");
        // Create the options
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        // Set what details are required in the schema - this affects the
        // time taken to crawl the schema
        options.setSchemaInfoLevel(SchemaInfoLevel.standard());
        options.setRoutineInclusionRule(new ExcludeAll());
        // options.setSchemaInclusionRule(new RegularExpressionInclusionRule(
        // "PUBLIC.BOOKS"));

        // Get the schema definition

        final SchemaCrawler schemaCrawler = new SchemaCrawler(connection);
        final Database database = schemaCrawler.crawl(options);

        connection.close();

        DatabaseDTO databaseDTO = DtoFactory.getInstance()
                                            .createDto(DatabaseDTO.class).withName(database.getName());
        Map<String, SchemaDTO> schemaToInject = new HashMap<String, SchemaDTO>();
        databaseDTO = databaseDTO.withSchemas(schemaToInject);
        for (final Schema schema : database.getSchemas()) {
            SchemaDTO schemaDTO = DtoFactory.getInstance()
                                            .createDto(SchemaDTO.class).withName(schema.getName());
            Map<String, TableDTO> tables = new HashMap<String, TableDTO>();
            for (final Table table : database.getTables(schema)) {
                TableDTO tableDTO = DtoFactory.getInstance()
                                              .createDto(TableDTO.class).withName(table.getName());
                if (table instanceof View) {
                    tableDTO = tableDTO.withIsView(true);
                }

                Map<String, ColumnDTO> columns = new HashMap<String, ColumnDTO>();
                for (Column column : table.getColumns()) {
                    ColumnDTO columnDTO = DtoFactory
                                                    .getInstance()
                                                    .createDto(ColumnDTO.class)
                                                    .withName(column.getName())
                                                    .withColumnDataType(
                                                                        column.getColumnDataType().getName());
                    columns.put(columnDTO.getName(), columnDTO);
                }
                tableDTO = tableDTO.withColumns(columns);
                columns = tableDTO.getColumns();
                tables.put(tableDTO.getName(), tableDTO);
            }
            schemaDTO.withTables(tables);
            tables = schemaDTO.getTables();
            schemaToInject.put(schemaDTO.getName(), schemaDTO);
        }

        String json = DtoFactory.getInstance().toJson(databaseDTO);
        System.out.println(json);

    }
}
