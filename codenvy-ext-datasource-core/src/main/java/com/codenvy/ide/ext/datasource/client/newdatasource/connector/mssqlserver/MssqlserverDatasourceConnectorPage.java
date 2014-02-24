package com.codenvy.ide.ext.datasource.client.newdatasource.connector.mssqlserver;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.JdbcDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


/**
 * This connector page is using JTDS JDBC Driver to connect to MS SQLserver.
 */
public class MssqlserverDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage
                                                implements JdbcDatasourceConnectorView.ActionDelegate {

    final public static String SQLSERVER_DB_ID = "sqlserver";
    protected DtoFactory dtoFactory;

    @Inject
    public MssqlserverDatasourceConnectorPage(final JdbcDatasourceConnectorView view,
                                              final NotificationManager notificationManager,
                                              final DtoFactory dtoFactory,
                                              final DatasourceManager datasourceManager,
                                              final EventBus eventBus,
                                              final DatasourceClientService service) {
        super(view,"sqlserver", null, SQLSERVER_DB_ID, datasourceManager, eventBus, service,notificationManager);
        this.dtoFactory = dtoFactory;
    }


    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(getView());
        // set the default port
        getView().setPort(1433);
    }


    protected DatabaseConfigurationDTO getConfiguredDatabase() {
        String datasourceId = wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME);
        DatabaseConfigurationDTO result =
                                          dtoFactory.createDto(DatabaseConfigurationDTO.class)
                                                    .withDatabaseName(getView().getDatabaseName())
                                                    .withHostname(getView().getHostname()).withPort(getView().getPort())
                                                    .withUsername(getView().getUsername())
                                                    .withPassword(getView().getPassword())
                                                    .withDatabaseType(DatabaseType.JTDS)
                                                    .withDatasourceId(datasourceId);
        return result;
    }

}
