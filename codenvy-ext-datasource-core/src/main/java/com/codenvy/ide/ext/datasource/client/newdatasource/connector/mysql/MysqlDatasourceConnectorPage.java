package com.codenvy.ide.ext.datasource.client.newdatasource.connector.mysql;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
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
 * Created by Wafa on 20/01/14.
 */
public class MysqlDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage implements
                                                                                    JdbcDatasourceConnectorView.ActionDelegate {
    final public static String MYSQL_DB_ID = "mysql";
    
    protected JdbcDatasourceConnectorView view;
    protected DtoFactory                  dtoFactory;
    protected NotificationManager         notificationManager;

    @Inject
    public MysqlDatasourceConnectorPage(final JdbcDatasourceConnectorView view,
                                        final NotificationManager notificationManager,
                                        final DtoFactory dtoFactory,
                                        final DatasourceManager datasourceManager,
                                        final EventBus eventBus) {
        super("mySQL", null, MYSQL_DB_ID, datasourceManager, eventBus);
        this.view = view;
        this.notificationManager = notificationManager;
        this.dtoFactory = dtoFactory;
    }


    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }


    protected DatabaseConfigurationDTO getConfiguredDatabase() {
        String datasourceId = wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME);
        DatabaseConfigurationDTO result =
                                          dtoFactory.createDto(DatabaseConfigurationDTO.class)
                                                    .withDatabaseName(view.getDatabaseName())
                                                    .withHostname(view.getHostname()).withPort(view.getPort())
                                                    .withUsername(view.getUsername())
                                                    .withPassword(view.getPassword())
                                                    .withDatabaseType(DatabaseType.MYSQL)
                                                    .withDatasourceId(datasourceId);
        return result;
    }
}
