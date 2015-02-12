/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.codenvy.ide.ext.datasource.shared.DefaultDatasourceDefinitionDTO;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.validation.constraints.NotNull;

public class DefaultNewDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage
        implements InitializableWizardPage, DefaultNewDatasourceConnectorView.ActionDelegate {

    private final int             defaultPort;
    private final DatabaseType    databaseType;
    private final DtoFactory      dtoFactory;

    public DefaultNewDatasourceConnectorPage(@NotNull final DefaultNewDatasourceConnectorView view,
                                             @NotNull final DatasourceClientService service,
                                             @NotNull final NotificationManager notificationManager,
                                             @NotNull final DtoFactory dtoFactory,
                                             @NotNull final NewDatasourceWizardMessages messages,
                                             final int defaultPort,
                                             final DatabaseType databaseType) {
        super(view, service, notificationManager, dtoFactory, messages);
        this.defaultPort = defaultPort;
        this.databaseType = databaseType;
        this.dtoFactory = dtoFactory;
        getView().setPort(getDefaultPort());
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(getView());
        updateView();
    }

    @Override
    public DefaultNewDatasourceConnectorView getView() {
        return (DefaultNewDatasourceConnectorView)super.getView();
    }

    /**
     * Returns the currently configured database.
     *
     * @return the database
     */
    @Override
    protected DatabaseConfigurationDTO getConfiguredDatabase() {
        String datasourceId = context.get(NewDatasourceWizard.DATASOURCE_NAME_KEY);
        DatabaseConfigurationDTO result = dtoFactory.createDto(DefaultDatasourceDefinitionDTO.class)
                                                    .withDatabaseName(getView().getDatabaseName())
                                                    .withDatabaseType(getDatabaseType())
                                                    .withDatasourceId(datasourceId)
                                                    .withHostName(getView().getHostname())
                                                    .withPort(getView().getPort())
                                                    .withUseSSL(getView().getUseSSL())
                                                    .withVerifyServerCertificate(getView().getVerifyServerCertificate())
                                                    .withRunnerProcessId(getView().getRunnerProcessId());

        result.withUsername(getView().getUsername())
              .withPassword(getView().getEncryptedPassword());

        result.withConfigurationConnectorId(dataObject.getConfigurationConnectorId());
        return result;
    }

    @Override
    public Integer getDefaultPort() {
        return defaultPort;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    @Override
    public void initPage(final Object data) {
        // should set exactly the same fields as those read in getConfiguredDatabase except those configured in first page
        if (!(data instanceof DatabaseConfigurationDTO)) {
            clearPage();
            return;
        }
        DatabaseConfigurationDTO initData = (DatabaseConfigurationDTO)data;
        dataObject.setDatabaseName(initData.getDatabaseName());
        dataObject.setHostName(initData.getHostName());
        dataObject.setPort(initData.getPort());
        dataObject.setUseSSL(initData.getUseSSL());
        dataObject.setVerifyServerCertificate(initData.getVerifyServerCertificate());
        dataObject.setUsername(initData.getUsername());
        dataObject.setRunnerProcessId(initData.getRunnerProcessId());
    }

    @Override
    public void clearPage() {
        getView().setDatabaseName("");
        getView().setHostName("");
        getView().setPort(getDefaultPort());
        getView().setUseSSL(false);
        getView().setVerifyServerCertificate(false);
        getView().setUsername("");
        getView().setPassword("");
    }

    public void updateView() {
        getView().setDatabaseName(dataObject.getDatabaseName());
        getView().setHostName(dataObject.getHostName());
        getView().setPort(dataObject.getPort());
        getView().setUseSSL(dataObject.getUseSSL());
        getView().setVerifyServerCertificate(dataObject.getVerifyServerCertificate());
        getView().setUsername(dataObject.getUsername());
        getView().setEncryptedPassword(dataObject.getPassword(), true);
        getView().setRunnerProcessId(dataObject.getRunnerProcessId());
    }

    @Override
    public void hostNameChanged(String name) {
        dataObject.setHostName(name);
        updateDelegate.updateControls();
    }

    @Override
    public void portChanged(int port) {
        dataObject.setPort(port);
        updateDelegate.updateControls();
    }

    @Override
    public void useSSLChanged(boolean useSSL) {
        dataObject.setUseSSL(useSSL);
        updateDelegate.updateControls();
    }

    @Override
    public void verifyServerCertificateChanged(boolean verifyServerCertificate) {
        dataObject.setVerifyServerCertificate(verifyServerCertificate);
        updateDelegate.updateControls();
    }
}
