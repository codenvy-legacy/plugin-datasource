/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client;

import static com.codenvy.ide.ext.datasource.client.DatabaseCategoryType.NOTCLOUD;
import static com.codenvy.ide.ext.datasource.client.DatabaseCategoryType.GOOGLE;
import static com.codenvy.ide.ext.datasource.client.DatabaseCategoryType.AMAZON;

import java.util.ArrayList;
import java.util.List;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.amazon.ws.mysql.AwsMysqlConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.amazon.ws.oracle.AwsOracleConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.amazon.ws.postgres.AwsPostgresConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.amazon.ws.sqlserver.AwsSqlServerConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.google.cloud.sql.GoogleCloudSqlConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.mssqlserver.MssqlserverDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.mysql.MysqlDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.nuodb.NuoDBDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.oracle.OracleDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.postgres.PostgresDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ConnectorsInitializer {

    private final List<NewDatasourceConnector> connectors = new ArrayList<>();
    private final NewDatasourceConnectorAgent  connectorAgent;

    @Inject
    public ConnectorsInitializer(final NewDatasourceConnectorAgent connectorAgent,
                                 final DatasourceUiResources resources,
                                 final NewDatasourceWizardMessages dsMessages,
                                 final Provider<PostgresDatasourceConnectorPage> pgConnectorPageProvider,
                                 final Provider<MysqlDatasourceConnectorPage> mysqlConnectorPageProvider,
                                 final Provider<OracleDatasourceConnectorPage> oracleConnectorPageProvider,
                                 final Provider<MssqlserverDatasourceConnectorPage> mssqlserverConnectorPageProvider,
                                 final Provider<NuoDBDatasourceConnectorPage> nuodbConnectorPageProvider,
                                 final Provider<GoogleCloudSqlConnectorPage> googleCloudSqlConnectorPageProvider,
                                 final Provider<AwsPostgresConnectorPage> awsPostgresConnectorPageProvider,
                                 final Provider<AwsMysqlConnectorPage> awsMysqlConnectorPageProvider,
                                 final Provider<AwsOracleConnectorPage> awsOracleConnectorPageProvider,
                                 final Provider<AwsSqlServerConnectorPage> awsSqlServerConnectorPageProvider) {

        this.connectorAgent = connectorAgent;

        // counter to add different priorities to all connectors - to increment after each #register(NewDatasourceConnector)
        int connectorCounter = 0;

        // add a new postgres connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> pgWizardPages = Collections.createArray();
        pgWizardPages.add(pgConnectorPageProvider);
        NewDatasourceConnector connectorPostgres = new NewDatasourceConnector(DatabaseType.POSTGRES.getConnectorId(),
                                                                              connectorCounter, dsMessages.postgresql(),
                                                                              resources.getPostgreSqlLogo(),
                                                                              "org.postgresql.Driver",
                                                                              pgWizardPages, NOTCLOUD);
        this.connectors.add(connectorPostgres);

        connectorCounter++;

        // Add a new mysql connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> mysqlWizardPages = Collections.createArray();
        mysqlWizardPages.add(mysqlConnectorPageProvider);
        NewDatasourceConnector connectorMysql = new NewDatasourceConnector(DatabaseType.MYSQL.getConnectorId(),
                                                                           connectorCounter,
                                                                           dsMessages.mysql(),
                                                                           resources.getMySqlLogo(),
                                                                           "com.mysql.jdbc.Driver",
                                                                           mysqlWizardPages, NOTCLOUD);
        this.connectors.add(connectorMysql);

        connectorCounter++;

        // add a new oracle connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> oracleWizardPages = Collections.createArray();
        oracleWizardPages.add(oracleConnectorPageProvider);
        NewDatasourceConnector connectorOracle = new NewDatasourceConnector(DatabaseType.ORACLE.getConnectorId(),
                                                                            connectorCounter,
                                                                            dsMessages.oracle(),
                                                                            resources.getOracleLogo(),
                                                                            "oracle.jdbc.OracleDriver", oracleWizardPages, NOTCLOUD);
        this.connectors.add(connectorOracle);

        connectorCounter++;

        // add a new SQLserver connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> sqlServerWizardPages = Collections.createArray();
        sqlServerWizardPages.add(mssqlserverConnectorPageProvider);
        NewDatasourceConnector connectorMs = new NewDatasourceConnector(DatabaseType.JTDS.getConnectorId(),
                                                                        connectorCounter,
                                                                        dsMessages.mssqlserver(),
                                                                        resources.getSqlServerLogo(),
                                                                        "net.sourceforge.jtds.jdbc.Driver",
                                                                        sqlServerWizardPages,
                                                                        NOTCLOUD);
        this.connectors.add(connectorMs);

        connectorCounter++;

        // add a new NuoDB connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> nuoDBWizardPages = Collections.createArray();
        nuoDBWizardPages.add(nuodbConnectorPageProvider);
        NewDatasourceConnector connectorNuoDB = new NewDatasourceConnector(DatabaseType.NUODB.getConnectorId(),
                                                                           connectorCounter,
                                                                           dsMessages.nuodb(),
                                                                           resources.getNuoDBLogo(),
                                                                           "com.nuodb.jdbc.Driver",
                                                                           nuoDBWizardPages, NOTCLOUD);
        this.connectors.add(connectorNuoDB);

        connectorCounter++;

        // add a new GoogleCloudSQL connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> googleCloudSQLWizardPages = Collections.createArray();
        googleCloudSQLWizardPages.add(googleCloudSqlConnectorPageProvider);
        NewDatasourceConnector connectorGoogle = new NewDatasourceConnector(DatabaseType.GOOGLECLOUDSQL.getConnectorId(),
                                                                            connectorCounter,
                                                                            dsMessages.googlecloudsql(),
                                                                            resources.getGoogleCloudSQLLogo(),
                                                                            "com.mysql.jdbc.Driver",
                                                                            googleCloudSQLWizardPages,
                                                                            GOOGLE);
        this.connectors.add(connectorGoogle);

        connectorCounter++;

        // add a new AmazonRDS/Postgres connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> awsPostgresWizardPages = Collections.createArray();
        awsPostgresWizardPages.add(awsPostgresConnectorPageProvider);
        NewDatasourceConnector connectorAwsPostg = new NewDatasourceConnector(AwsPostgresConnectorPage.AWSPOSTGRES_DB_ID,
                                                                              connectorCounter,
                                                                              dsMessages.awspg(),
                                                                              resources.getAwsPostgresLogo(),
                                                                              "org.postgresql.Driver",
                                                                              awsPostgresWizardPages,
                                                                              AMAZON);
        this.connectors.add(connectorAwsPostg);

        connectorCounter++;

        // add a new AmazonRDS/Mysql connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> awsMysqlWizardPages = Collections.createArray();
        awsMysqlWizardPages.add(awsMysqlConnectorPageProvider);
        NewDatasourceConnector connectorAwsMySql = new NewDatasourceConnector(AwsMysqlConnectorPage.AWSMYSQL_DB_ID,
                                                                              connectorCounter,
                                                                              dsMessages.awsmysql(),
                                                                              resources.getAwsMysqlLogo(),
                                                                              "com.mysql.jdbc.Driver",
                                                                              awsMysqlWizardPages,
                                                                              AMAZON);
        this.connectors.add(connectorAwsMySql);

        connectorCounter++;

        // add a new AmazonRDS/Oracle connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> awsOracleWizardPages = Collections.createArray();
        awsOracleWizardPages.add(awsOracleConnectorPageProvider);
        NewDatasourceConnector connectorAwsOracle = new NewDatasourceConnector(AwsOracleConnectorPage.AWSORACLE_DB_ID,
                                                                               connectorCounter,
                                                                               dsMessages.awsoracle(),
                                                                               resources.getAwsOracleLogo(),
                                                                               "oracle.jdbc.OracleDriver",
                                                                               awsOracleWizardPages,
                                                                               AMAZON);
        this.connectors.add(connectorAwsOracle);

        connectorCounter++;

        // add a new AmazonRDS/SqlServer connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> awsSqlServerWizardPages = Collections.createArray();
        awsSqlServerWizardPages.add(awsSqlServerConnectorPageProvider);
        NewDatasourceConnector connectorAwsMs = new NewDatasourceConnector(AwsSqlServerConnectorPage.AWSSQLSERVER_DB_ID,
                                                                           connectorCounter,
                                                                           dsMessages.awssqlserver(),
                                                                           resources.getAwsSqlServerLogo(),
                                                                           "net.sourceforge.jtds.jdbc.Driver",
                                                                           awsSqlServerWizardPages,
                                                                           AMAZON);
        this.connectors.add(connectorAwsMs);

        connectorCounter++;

        Log.debug(ConnectorsInitializer.class, "Connectors registered : " + connectorCounter);
    }

    public void initConnectors() {
        for (NewDatasourceConnector connector : this.connectors) {
            this.connectorAgent.register(connector);
        }
    }
}
