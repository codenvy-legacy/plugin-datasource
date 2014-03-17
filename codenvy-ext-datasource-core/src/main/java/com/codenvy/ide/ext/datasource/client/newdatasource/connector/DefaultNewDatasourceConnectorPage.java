/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

public class DefaultNewDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage {

    private final int          defaultPort;
    private final DatabaseType databaseType;

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
        final DatabaseConfigurationDTO result = super.getConfiguredDatabase();
        result.withHostname(getView().getHostname())
              .withPort(getView().getPort())
              .withUsername(getView().getUsername())
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
