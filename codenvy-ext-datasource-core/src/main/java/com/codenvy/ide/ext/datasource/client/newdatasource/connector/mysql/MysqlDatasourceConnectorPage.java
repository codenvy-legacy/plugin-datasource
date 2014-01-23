package com.codenvy.ide.ext.datasource.client.newdatasource.connector.mysql;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerView;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPageView;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatasourceConfigPreferences;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import java.util.Map;

/**
 * Created by Wafa on 20/01/14.
 */
public class MysqlDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage implements
        MysqlDatasourceConnectorView.ActionDelegate {

    protected MysqlDatasourceConnectorView view;
    protected DatasourceClientService service;
    protected DtoFactory dtoFactory;
    protected NotificationManager notificationManager;
    protected DatasourceExplorerView datasourceExplorerView;
    protected PreferencesManager preferencesManager;
    protected NewDatasourceWizardPageView dsView;

    @Inject
    public MysqlDatasourceConnectorPage(   NewDatasourceWizardPageView dsView, MysqlDatasourceConnectorView view,
                                           DatasourceClientService service,
                                           DtoFactory dtoFactory,
                                           NotificationManager notificationManager,
                                           DatasourceExplorerView datasourceExplorerView,
                                           PreferencesManager preferencesManager) {
        super("mySQL", null, "mysql");
        this.dsView = dsView;
        this.view = view;
        this.service = service;
        this.dtoFactory = dtoFactory;
        this.notificationManager = notificationManager;
        this.datasourceExplorerView = datasourceExplorerView;
        this.preferencesManager = preferencesManager;
    }


    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void commit(final CommitCallback callback) {

        // maybe this logic should be implemented in a service.
        String datasourcesJson = preferencesManager.getValue("datasources");
        DatasourceConfigPreferences datasourcesPreferences;
        if (datasourcesJson == null) {
            datasourcesPreferences = dtoFactory.createDto(DatasourceConfigPreferences.class);
        } else {
            datasourcesPreferences =
                    dtoFactory.createDtoFromJson(datasourcesJson,
                            DatasourceConfigPreferences.class);
        }
        Map<String, DatabaseConfigurationDTO> datasourcesMap = datasourcesPreferences.getDatasources();
        datasourcesMap.put(wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME),
                dtoFactory.createDto(DatabaseConfigurationDTO.class)
                        .withDatabaseName(view.getDatabaseName())
                        .withHostname(view.getHostname()).withPort(view.getPort()).withUsername(view.getUsername())
                        .withPassword(view.getPassword()));
        preferencesManager.setPreference("datasources", dtoFactory.toJson(datasourcesPreferences));

        datasourceExplorerView.refreshDatasourceList();
    }

}
