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
package com.codenvy.ide.ext.datasource.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

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

public class TestPostgres {

    @Ignore
    @Test
    public void testPostgres() throws Exception {
        // having a jdbc driver connected to a local jdbc driver, explore the
        // tables and column and schemas

        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "nuxeospirit");
        Connection conn = DriverManager.getConnection(
                                                      "jdbc:postgresql://localhost:5432/nuxeo", props);
        try {
            DatabaseMetaData metadata = conn.getMetaData();
            ResultSet catalogRS = metadata.getCatalogs();
            ArrayList<String> catalogs = new ArrayList<String>();
            try {
                while (catalogRS.next()) {
                    catalogs.add(catalogRS.getString(1));
                }
            } finally {
                catalogRS.close();
            }

            ResultSet schemasRS = metadata.getSchemas(catalogs.get(0), null);
            ArrayList<String> schemas = new ArrayList<String>();
            try {
                while (schemasRS.next()) {
                    schemas.add(schemasRS.getString(1));
                }
            } finally {
                schemasRS.close();
            }

            for (String schema : schemas) {
                System.out.println(schema);
            }
        } finally {
            conn.close();
        }

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

        for (final Schema schema : database.getSchemas()) {
            System.out.println(schema);
            for (final Table table : database.getTables(schema)) {
                System.out.print("o--> " + table);
                if (table instanceof View) {
                    System.out.println(" (VIEW)");
                } else {
                    System.out.println();
                }

                for (final Column column : table.getColumns()) {
                    System.out.println("     o--> " + column + " ("
                                       + column.getColumnDataType() + ")");
                }
            }
        }

    }

}
