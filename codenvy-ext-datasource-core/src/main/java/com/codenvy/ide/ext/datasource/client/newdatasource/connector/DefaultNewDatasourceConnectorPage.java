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
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.codenvy.ide.ext.datasource.shared.DefaultDatasourceDefinitionDTO;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

public class DefaultNewDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage {

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
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(getView());
        getView().setPort(getDefaultPort());
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
        DefaultDatasourceDefinitionDTO result = dtoFactory.createDto(DefaultDatasourceDefinitionDTO.class)
                                                          .withDatabaseName(getView().getDatabaseName())
                                                          .withDatabaseType(getDatabaseType())
                                                          .withDatasourceId(datasourceId)
                                                          .withHostName(getView().getHostname())
                                                          .withPort(getView().getPort());
        result.withUsername(getView().getUsername())
              .withPassword(getView().getPassword());
        return result;
    }

    public Integer getDefaultPort() {
        return this.defaultPort;
    }

    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }
}
