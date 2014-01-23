package com.codenvy.ide.ext.datasource.core;

import org.junit.Ignore;
import org.junit.Test;
import schemacrawler.crawl.SchemaCrawler;
import schemacrawler.schema.*;
import schemacrawler.schemacrawler.DatabaseConnectionOptions;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Wafa on 22/01/14.
 */
public class TestMysql {

    @Ignore
    @Test
    public void testMysql() throws Exception {
        // having a jdbc driver connected to a local jdbc driver, explore the
        // tables and column and schemas

        Properties props = new Properties();
        props.setProperty("user", "admin");
        props.setProperty("password", "admin");
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/wafa", props);
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

        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wafa", "admin", "admin");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

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
