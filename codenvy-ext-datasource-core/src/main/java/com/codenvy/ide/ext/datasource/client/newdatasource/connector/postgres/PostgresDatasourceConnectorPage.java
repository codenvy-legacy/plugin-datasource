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

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.JdbcDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class PostgresDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage
                                                                                       implements
                                                                                       JdbcDatasourceConnectorView.ActionDelegate {

    public static final String PG_DB_ID           = "postgres";
    private static final int   DEFAULT_PORT_PGSQL = 5432;

    private final DtoFactory   dtoFactory;

    @Inject
    public PostgresDatasourceConnectorPage(final JdbcDatasourceConnectorView view,
                                           final NotificationManager notificationManager,
                                           final DtoFactory dtoFactory,
                                           final DatasourceManager datasourceManager,
                                           final EventBus eventBus,
                                           final DatasourceClientService service,
                                           final DatasourceUiResources resources,
                                           final NewDatasourceWizardMessages messages) {
        super(view, "PostgreSQL", resources.getPostgreSqlLogo(), PG_DB_ID, datasourceManager, eventBus, service, notificationManager,
              dtoFactory, messages);
        this.dtoFactory = dtoFactory;
    }


    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(getView());
        getView().setPort(DEFAULT_PORT_PGSQL);
    }


    protected DatabaseConfigurationDTO getConfiguredDatabase() {
        String datasourceId = wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME);
        DatabaseConfigurationDTO result =
                                          dtoFactory.createDto(DatabaseConfigurationDTO.class)
                                                    .withDatabaseName(getView().getDatabaseName())
                                                    .withHostname(getView().getHostname()).withPort(getView().getPort())
                                                    .withUsername(getView().getUsername())
                                                    .withPassword(getView().getPassword())
                                                    .withDatabaseType(DatabaseType.POSTGRES)
                                                    .withDatasourceId(datasourceId);
        return result;
    }


}
