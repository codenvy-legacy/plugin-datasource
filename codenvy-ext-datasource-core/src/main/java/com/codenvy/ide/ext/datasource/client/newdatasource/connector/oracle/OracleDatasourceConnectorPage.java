package com.codenvy.ide.ext.datasource.client.newdatasource.connector.oracle;

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
 * Created by Wafa on 20/01/14.
 */
public class OracleDatasourceConnectorPage extends DefaultNewDatasourceConnectorPage {
    public static final String ORACLE_DB_ID        = "oracle";
    private static final int   DEFAULT_PORT_ORACLE = 1521;

    @Inject
    public OracleDatasourceConnectorPage(final DefaultNewDatasourceConnectorView view,
                                         final NotificationManager notificationManager,
                                         final DtoFactory dtoFactory,
                                         final DatasourceManager datasourceManager,
                                         final EventBus eventBus,
                                         final DatasourceClientService service,
                                         final DatasourceUiResources resources,
                                         final NewDatasourceWizardMessages messages) {
        super(view, "Oracle", resources.getOracleLogo(), ORACLE_DB_ID, datasourceManager, eventBus, service, notificationManager,
              dtoFactory, messages, DEFAULT_PORT_ORACLE, DatabaseType.ORACLE);
    }
}
