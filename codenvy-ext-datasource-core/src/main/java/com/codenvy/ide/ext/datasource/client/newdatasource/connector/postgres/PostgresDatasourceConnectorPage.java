/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.postgres;

import java.util.Map;

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

public class PostgresDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage implements
                                                                                       PostgresDatasourceConnectorView.ActionDelegate {
    protected PostgresDatasourceConnectorView view;
    protected DatasourceClientService         service;
    protected DtoFactory                      dtoFactory;
    protected NotificationManager             notificationManager;
    protected DatasourceExplorerView          datasourceExplorerView;
    protected PreferencesManager              preferencesManager;
    protected NewDatasourceWizardPageView     dsView;

    @Inject
    public PostgresDatasourceConnectorPage(NewDatasourceWizardPageView dsView, PostgresDatasourceConnectorView view,
                                           DatasourceClientService service,
                                           DtoFactory dtoFactory,
                                           NotificationManager notificationManager,
                                           DatasourceExplorerView datasourceExplorerView,
                                           PreferencesManager preferencesManager) {
        super("PostgreSQL", null, "postgres");
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
