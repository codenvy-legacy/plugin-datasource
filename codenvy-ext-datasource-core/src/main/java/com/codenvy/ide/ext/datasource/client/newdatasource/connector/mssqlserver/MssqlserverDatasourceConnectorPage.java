package com.codenvy.ide.ext.datasource.client.newdatasource.connector.mssqlserver;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.JdbcDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


/**
 * This connector page is using JTDS JDBC Driver to connect to MS SQLserver.
 */
public class MssqlserverDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage
                                                                                          implements
                                                                                          JdbcDatasourceConnectorView.ActionDelegate {

    public static final String SQLSERVER_DB_ID        = "sqlserver";
    private static final int   DEFAULT_PORT_SQLSERVER = 1433;

    @Inject
    public MssqlserverDatasourceConnectorPage(final JdbcDatasourceConnectorView view,
                                              final NotificationManager notificationManager,
                                              final DtoFactory dtoFactory,
                                              final DatasourceManager datasourceManager,
                                              final EventBus eventBus,
                                              final DatasourceClientService service,
                                              final DatasourceUiResources resources,
                                              final NewDatasourceWizardMessages messages) {
        super(view, "Microsoft SQL Server", resources.getSqlServerLogo(), SQLSERVER_DB_ID, datasourceManager, eventBus, service,
              notificationManager, dtoFactory, messages);
    }

    @Override
    public Integer getDefaultPort() {
        return DEFAULT_PORT_SQLSERVER;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.JTDS;
    }

}
