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

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.codenvy.ide.ext.datasource.server.DatasourceService;
import com.codenvy.ide.ext.datasource.server.JdbcUrlBuilder;

public class TestDTOWithPostgres {
    @Mock
    protected DatabaseConfigurationDTO databaseConfig;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.POSTGRES);
        when(databaseConfig.getDatabaseName()).thenReturn("nuxeo");
        when(databaseConfig.getHostname()).thenReturn("localhost");
        when(databaseConfig.getPort()).thenReturn(5432);
        when(databaseConfig.getUsername()).thenReturn("postgres");
        when(databaseConfig.getPassword()).thenReturn("nuxeospirit");
    }
    
    @Ignore
    @Test
    public void testPostgresDTOgeneration() throws Exception {
        DatasourceService dsService = new DatasourceService(new JdbcUrlBuilder());
        String json = dsService.getDatabase(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"PostgreSQL\""));
    }
}
