package com.codenvy.ide.ext.datasource.client.newdatasource.connector.drizzle;

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
 * Created by Wafa on 20/01/14.
 */
public class DrizzleDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage
                                                                                      implements JdbcDatasourceConnectorView.ActionDelegate {

    public static final String DRIZZLE_DB_ID        = "drizzle";
    private static final int   DEFAULT_PORT_DRIZZLE = 4427;


    @Inject
    public DrizzleDatasourceConnectorPage(final JdbcDatasourceConnectorView view,
                                          final NotificationManager notificationManager,
                                          final DtoFactory dtoFactory,
                                          final DatasourceManager datasourceManager,
                                          final EventBus eventBus,
                                          final DatasourceClientService service,
                                          final DatasourceUiResources resources,
                                          final NewDatasourceWizardMessages messages) {
        super(view, "mySQL", resources.getMySqlLogo(), DRIZZLE_DB_ID, datasourceManager, eventBus, service, notificationManager,
              dtoFactory, messages);
    }

    @Override
    public Integer getDefaultPort() {
        return DEFAULT_PORT_DRIZZLE;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.DRIZZLE;
    }


}
