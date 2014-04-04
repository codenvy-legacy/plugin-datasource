/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.codenvy.ide.ext.datasource.shared.DefaultDatasourceDefinitionDTO;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

public class DefaultNewDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage implements InitializableWizardPage {

    private final int          defaultPort;
    private final DatabaseType databaseType;
    private final DtoFactory   dtoFactory;

    public DefaultNewDatasourceConnectorPage(@Nullable final DefaultNewDatasourceConnectorView view,
                                             @Nullable final String caption,
                                             @Nullable final ImageResource image,
                                             @NotNull final String datasourceId,
                                             @NotNull final DatasourceManager datasourceManager,
                                             @NotNull final EventBus eventBus,
                                             @NotNull final DatasourceClientService service,
                                             @NotNull final NotificationManager notificationManager,
                                             @NotNull final DtoFactory dtoFactory,
                                             @NotNull final NewDatasourceWizardMessages messages,
                                             final int defaultPort,
                                             final DatabaseType databaseType) {
        super(view, caption, image, datasourceId, datasourceManager, eventBus, service, notificationManager, dtoFactory, messages);
        this.defaultPort = defaultPort;
        this.databaseType = databaseType;
        this.dtoFactory = dtoFactory;
        getView().setPort(getDefaultPort());
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(getView());
    }

    public DefaultNewDatasourceConnectorView getView() {
        return (DefaultNewDatasourceConnectorView)super.getView();
    }

    /**
     * Returns the currently configured database.
     * 
     * @return the database
     */
    protected DatabaseConfigurationDTO getConfiguredDatabase() {
        String datasourceId = wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME);
        DatabaseConfigurationDTO result = dtoFactory.createDto(DefaultDatasourceDefinitionDTO.class)
                                                    .withDatabaseName(getView().getDatabaseName())
                                                    .withDatabaseType(getDatabaseType())
                                                    .withDatasourceId(datasourceId)
                                                    .withHostName(getView().getHostname())
                                                    .withPort(getView().getPort())
                                                    .withUseSSL(getView().getUseSSL())
                                                    .withVerifyServerCertificate(getView().getVerifyServerCertificate());

        result.withUsername(getView().getUsername())
              .withPassword(getView().getPassword());

        result.withConfigurationConnectorId(wizardContext.getData(NewDatasourceWizard.DATASOURCE_CONNECTOR).getId());
        return result;
    }

    public Integer getDefaultPort() {
        return this.defaultPort;
    }

    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }

    @Override
    public void initPage(final Object data) {
        // should set exactly the same fields as those read in getConfiguredDatabase except thos configured in first page
        if (!(data instanceof DatabaseConfigurationDTO)) {
            return;
        }
        DatabaseConfigurationDTO initData = (DatabaseConfigurationDTO)data;
        getView().setDatabaseName(initData.getDatabaseName());
        getView().setHostName(initData.getHostName());
        getView().setPort(initData.getPort());
        getView().setUseSSL(initData.getUseSSL());
        getView().setVerifyServerCertificate(initData.getVerifyServerCertificate());
        getView().setUsername(initData.getUsername());
        getView().setPassword(initData.getPassword());
    }
}
