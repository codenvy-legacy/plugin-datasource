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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.amazon.ws.sqlserver;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.client.store.DatasourceManager;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


/**
 * This connector page is using JTDS JDBC Driver to connect to MS SQLserver.
 */
public class AwsSqlServerConnectorPage extends DefaultNewDatasourceConnectorPage {

    public static final String AWSSQLSERVER_DB_ID        = "awsSqlserver";
    private static final int   DEFAULT_PORT_SQLSERVER = 1433;

    @Inject
    public AwsSqlServerConnectorPage(final DefaultNewDatasourceConnectorView view,
                                              final NotificationManager notificationManager,
                                              final DtoFactory dtoFactory,
                                              final DatasourceManager datasourceManager,
                                              final EventBus eventBus,
                                              final DatasourceClientService service,
                                              final DatasourceUiResources resources,
                                              final NewDatasourceWizardMessages messages) {
        super(view, messages.awssqlserver(), resources.getSqlServerLogo(), AWSSQLSERVER_DB_ID, datasourceManager, eventBus, service,
              notificationManager, dtoFactory, messages, DEFAULT_PORT_SQLSERVER, DatabaseType.JTDS);
    }
}
