package com.codenvy.ide.ext.datasource.client.newdatasource.connector.mssqlserver;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


/**
 * This connector page is using JTDS JDBC Driver to connect to MS SQLserver.
 */
public class MssqlserverDatasourceConnectorPage extends DefaultNewDatasourceConnectorPage {

    public static final String SQLSERVER_DB_ID        = "sqlserver";
    private static final int   DEFAULT_PORT_SQLSERVER = 1433;

    @Inject
    public MssqlserverDatasourceConnectorPage(final DefaultNewDatasourceConnectorView view,
                                              final NotificationManager notificationManager,
                                              final DtoFactory dtoFactory,
                                              final DatasourceManager datasourceManager,
                                              final EventBus eventBus,
                                              final DatasourceClientService service,
                                              final DatasourceUiResources resources,
                                              final NewDatasourceWizardMessages messages) {
        super(view, "Microsoft SQL Server", resources.getSqlServerLogo(), SQLSERVER_DB_ID, datasourceManager, eventBus, service,
              notificationManager, dtoFactory, messages, DEFAULT_PORT_SQLSERVER, DatabaseType.JTDS);
    }
}
