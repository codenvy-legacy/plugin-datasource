/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
package com.codenvy.ide.ext.datasource.server;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.codenvy.ide.ext.datasource.shared.DefaultDatasourceDefinitionDTO;
import com.codenvy.ide.ext.datasource.shared.ExploreRequestDTO;
import com.codenvy.ide.ext.datasource.shared.ExploreTableType;

/**
 * Test the datasource service getDatabase() method that is used to retrieve a database catalog information. Tests are ignored as they needs
 * exiting and running database. Customize the database configuration with yours for testing.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestDatasourceServiceDatabaseDTO {
    @Mock
    protected DefaultDatasourceDefinitionDTO databaseConfig;

    @Mock
    protected ExploreRequestDTO              exploreRequest;

    @Ignore
    @Test
    public void testPostgresDTOgeneration() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.POSTGRES);
        when(databaseConfig.getDatabaseName()).thenReturn("wafa");
        when(databaseConfig.getHostName()).thenReturn("localhost");
        when(databaseConfig.getPort()).thenReturn(5432);
        when(databaseConfig.getUsername()).thenReturn("postgres");
        when(databaseConfig.getPassword()).thenReturn("nuxeospirit");

        when(exploreRequest.getDatasourceConfig()).thenReturn(databaseConfig);
        when(exploreRequest.getExploreTableType()).thenReturn(ExploreTableType.SIMPLE);

        String json = getDatabaseJsonDTOFromDatasourceService(exploreRequest);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"PostgreSQL\""));
    }

    protected String getDatabaseJsonDTOFromDatasourceService(ExploreRequestDTO exploreRequest) throws Exception {
        DatabaseExploreService dsService = new DatabaseExploreService(new JdbcConnectionFactory());
        return dsService.getDatabase(exploreRequest);
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


        when(exploreRequest.getDatasourceConfig()).thenReturn(databaseConfig);
        when(exploreRequest.getExploreTableType()).thenReturn(ExploreTableType.SIMPLE);

        String json = getDatabaseJsonDTOFromDatasourceService(exploreRequest);
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


        when(exploreRequest.getDatasourceConfig()).thenReturn(databaseConfig);
        when(exploreRequest.getExploreTableType()).thenReturn(ExploreTableType.SIMPLE);

        String json = getDatabaseJsonDTOFromDatasourceService(exploreRequest);
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


        when(exploreRequest.getDatasourceConfig()).thenReturn(databaseConfig);
        when(exploreRequest.getExploreTableType()).thenReturn(ExploreTableType.SIMPLE);

        String json = getDatabaseJsonDTOFromDatasourceService(exploreRequest);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"Microsoft SQL Server\""));
    }

}
