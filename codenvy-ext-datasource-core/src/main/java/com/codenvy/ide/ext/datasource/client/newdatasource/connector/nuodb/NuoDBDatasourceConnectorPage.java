package com.codenvy.ide.ext.datasource.client.newdatasource.connector.nuodb;

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
public class NuoDBDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage
                                                                                    implements
                                                                                    JdbcDatasourceConnectorView.ActionDelegate {

    public static final String NUODB_DB_ID               = "nuodb";
    private static final int   DEFAULT_PORT_NUODB_BROKER = 48004;


    @Inject
    public NuoDBDatasourceConnectorPage(final JdbcDatasourceConnectorView view,
                                        final NotificationManager notificationManager,
                                        final DtoFactory dtoFactory,
                                        final DatasourceManager datasourceManager,
                                        final EventBus eventBus,
                                        final DatasourceClientService service,
                                        final DatasourceUiResources resources,
                                        final NewDatasourceWizardMessages messages) {
        super(view, "NuoDB", resources.getNuoDBLogo(), NUODB_DB_ID, datasourceManager, eventBus, service,
              notificationManager, dtoFactory, messages);
    }


    @Override
    public Integer getDefaultPort() {
        return DEFAULT_PORT_NUODB_BROKER;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.NUODB;
    }

}
