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
package com.codenvy.ide.ext.datasource.client;

import static com.codenvy.ide.ext.datasource.client.DatabaseCategoryType.CLOUD;
import static com.codenvy.ide.ext.datasource.client.DatabaseCategoryType.NOTCLOUD;

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
        NewDatasourceConnector connectorPostgres = new NewDatasourceConnector(PostgresDatasourceConnectorPage.PG_DB_ID,
                                                                              connectorCounter, dsMessages.postgresql(),
                                                                              resources.getPostgreSqlLogo(),
                                                                              "org.postgresql.Driver",
                                                                              pgWizardPages, NOTCLOUD);
        this.connectors.add(connectorPostgres);

        connectorCounter++;

        // Add a new mysql connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> mysqlWizardPages = Collections.createArray();
        mysqlWizardPages.add(mysqlConnectorPageProvider);
        NewDatasourceConnector connectorMysql = new NewDatasourceConnector(MysqlDatasourceConnectorPage.MYSQL_DB_ID,
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
        NewDatasourceConnector connectorOracle = new NewDatasourceConnector(OracleDatasourceConnectorPage.ORACLE_DB_ID,
                                                                            connectorCounter,
                                                                            dsMessages.oracle(),
                                                                            resources.getOracleLogo(),
                                                                            "oracle.jdbc.OracleDriver", oracleWizardPages, NOTCLOUD);
        this.connectors.add(connectorOracle);

        connectorCounter++;

        // add a new SQLserver connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> sqlServerWizardPages = Collections.createArray();
        sqlServerWizardPages.add(mssqlserverConnectorPageProvider);
        NewDatasourceConnector connectorMs = new NewDatasourceConnector(MssqlserverDatasourceConnectorPage.SQLSERVER_DB_ID,
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
        NewDatasourceConnector connectorNuoDB = new NewDatasourceConnector(NuoDBDatasourceConnectorPage.NUODB_DB_ID,
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
        NewDatasourceConnector connectorGoogle = new NewDatasourceConnector(GoogleCloudSqlConnectorPage.GOOGLECLOUDSQL_DB_ID,
                                                                            connectorCounter,
                                                                            dsMessages.googlecloudsql(),
                                                                            resources.getGoogleCloudSQLLogo(),
                                                                            "com.mysql.jdbc.Driver",
                                                                            googleCloudSQLWizardPages,
                                                                            CLOUD);
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
                                                                              CLOUD);
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
                                                                              CLOUD);
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
                                                                               CLOUD);
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
                                                                           CLOUD);
        this.connectors.add(connectorAwsMs);

        connectorCounter++;

        Log.info(ConnectorsInitializer.class, "Connectors registered : " + connectorCounter);
    }

    public void initConnectors() {
        for (NewDatasourceConnector connector : this.connectors) {
            this.connectorAgent.register(connector);
        }
    }
}
