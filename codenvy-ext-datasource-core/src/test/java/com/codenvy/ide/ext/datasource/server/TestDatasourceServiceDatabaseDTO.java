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

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.codenvy.ide.ext.datasource.shared.DefaultDatasourceDefinitionDTO;

/**
 * Test the datasource service getDatabase() method that is used to retrieve a database catalog information. Tests are ignored as they needs
 * exiting and running database. Customize the database configuration with yours for testing.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestDatasourceServiceDatabaseDTO {
    @Mock
    protected DefaultDatasourceDefinitionDTO databaseConfig;

    @Ignore
    @Test
    public void testPostgresDTOgeneration() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.POSTGRES);
        when(databaseConfig.getDatabaseName()).thenReturn("wafa");
        when(databaseConfig.getHostName()).thenReturn("localhost");
        when(databaseConfig.getPort()).thenReturn(5432);
        when(databaseConfig.getUsername()).thenReturn("postgres");
        when(databaseConfig.getPassword()).thenReturn("nuxeospirit");

        String json = getDatabaseJsonDTOFromDatasourceService(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"PostgreSQL\""));
    }

    protected String getDatabaseJsonDTOFromDatasourceService(DatabaseConfigurationDTO databaseConfig) throws Exception {
        DatasourceService dsService = new DatasourceService(new JdbcUrlBuilder(), new SqlRequestService());
        return dsService.getDatabase(databaseConfig);
    }

    @Ignore
    @Test
    public void testMySqlDTOgeneration() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.MYSQL);
        when(databaseConfig.getDatabaseName()).thenReturn("aucoffre_db");
        when(databaseConfig.getHostName()).thenReturn("localhost");
        when(databaseConfig.getPort()).thenReturn(3306);
        when(databaseConfig.getUsername()).thenReturn("root");
        when(databaseConfig.getPassword()).thenReturn("selucreh");

        String json = getDatabaseJsonDTOFromDatasourceService(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"MySQL\""));
    }

    @Ignore
    @Test
    public void testOracleDTOgeneration() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.ORACLE);
        when(databaseConfig.getDatabaseName()).thenReturn("xe");
        when(databaseConfig.getHostName()).thenReturn("192.168.86.191");
        when(databaseConfig.getPort()).thenReturn(1521);
        when(databaseConfig.getUsername()).thenReturn("admin");
        when(databaseConfig.getPassword()).thenReturn("admin");

        String json = getDatabaseJsonDTOFromDatasourceService(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"Oracle\""));
    }

    @Ignore
    @Test
    public void testSqlserverDTOgeneration() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.JTDS);
        when(databaseConfig.getDatabaseName()).thenReturn("master");
        when(databaseConfig.getHostName()).thenReturn("192.168.56.101");
        when(databaseConfig.getPort()).thenReturn(1433);
        when(databaseConfig.getUsername()).thenReturn("sa");
        when(databaseConfig.getPassword()).thenReturn("admin");

        String json = getDatabaseJsonDTOFromDatasourceService(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"Microsoft SQL Server\""));
    }

}
