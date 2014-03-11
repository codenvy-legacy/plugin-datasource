package com.codenvy.ide.ext.datasource.client.newdatasource.connector.drizzle;

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
 * Connector for the Drizzle database implementation.
 * 
 * @author "Mickaël Leduque"
 */
public class DrizzleDatasourceConnectorPage extends DefaultNewDatasourceConnectorPage {

    public static final String DRIZZLE_DB_ID        = "drizzle";

    /** The default port for drizzle. */
    private static final int   DEFAULT_PORT_DRIZZLE = 4427;

    @Inject
    public DrizzleDatasourceConnectorPage(final DefaultNewDatasourceConnectorView view,
                                          final NotificationManager notificationManager,
                                          final DtoFactory dtoFactory,
                                          final DatasourceManager datasourceManager,
                                          final EventBus eventBus,
                                          final DatasourceClientService service,
                                          final DatasourceUiResources resources,
                                          final NewDatasourceWizardMessages messages) {
        super(view, "Drizzle", null, DRIZZLE_DB_ID, datasourceManager, eventBus, service, notificationManager,
              dtoFactory, messages, DEFAULT_PORT_DRIZZLE, DatabaseType.DRIZZLE);
    }
}
