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
package com.codenvy.ide.ext.datasource.client.newdatasource;

import java.util.Collection;
import java.util.List;

import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.ext.datasource.client.AvailableJdbcDriversService;
import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEvent;
import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEventHandler;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class NewDatasourceWizardPagePresenter extends AbstractWizardPage implements NewDatasourceWizardPageView.ActionDelegate,
                                                                        InitializableWizardPage {
    protected NewDatasourceWizardPageView        view;
    protected NewDatasourceConnectorAgent        connectorAgent;
    protected Collection<NewDatasourceConnector> dbConnectors;
    protected AvailableJdbcDriversService        jdbcDriversService;
    protected EventBus                           eventBus;

    @Inject
    public NewDatasourceWizardPagePresenter(NewDatasourceWizardPageView view,
                                            NewDatasourceConnectorAgent connectorAgent,
                                            AvailableJdbcDriversService jdbcDriversService,
                                            EventBus eventBus,
                                            NewDatasourceWizardMessages messages) {
        super(messages.wizardTitle(), null);
        this.view = view;
        this.connectorAgent = connectorAgent;
        this.jdbcDriversService = jdbcDriversService;
        this.eventBus = eventBus;
        this.view.setDelegate(this);
    }

    @Override
    public String getNotice() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return (this.view.getDatasourceName() != null)
               && (!"".equals(this.view.getDatasourceName()))
               && (wizardContext.getData(NewDatasourceWizard.DATASOURCE_CONNECTOR) != null);
    }

    @Override
    public void focusComponent() {
    }

    protected void updateAvailableDatabase(List<String> drivers) {
        view.disableAllDbTypeButton();
        if (drivers == null) {
            return;
        }

        for (final NewDatasourceConnector connector : dbConnectors) {
            if (drivers.contains(connector.getJdbcClassName())) {
                view.enableDbTypeButton(connector.getId());
            }
        }
    }

    @Override
    public void removeOptions() {
        wizardContext.removeData(NewDatasourceWizard.DATASOURCE_CONNECTOR);
    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);

        dbConnectors = connectorAgent.getConnectors();
        view.setConnectors(dbConnectors);

        List<String> drivers = jdbcDriversService.getDrivers();
        updateAvailableDatabase(drivers);

        eventBus.addHandler(JdbcDriversFetchedEvent.getType(), new JdbcDriversFetchedEventHandler() {
            @Override
            public void onJdbcDriversFetched(List<String> drivers) {
                updateAvailableDatabase(drivers);
            }
        });
    }

    @Override
    public void onConnectorSelected(String id) {
        NewDatasourceConnector connector = null;
        for (final NewDatasourceConnector item : dbConnectors) {
            if (item.getId().equals(id)) {
                connector = item;
                break;
            }
        }
        if (connector != null) {
            wizardContext.putData(NewDatasourceWizard.DATASOURCE_CONNECTOR, connector);

            view.selectConnector(id);
            delegate.updateControls();
        }
    }

    @Override
    public void onDatasourceNameModified(final String datasourceName) {
        delegate.updateControls();
    }

    @Override
    public void storeOptions() {
        wizardContext.putData(NewDatasourceWizard.DATASOURCE_NAME, view.getDatasourceName());
    }

    @Override
    public void initPage(final Object data) {
        if (!(data instanceof DatabaseConfigurationDTO)) {
            clearPage();
            return;
        }

        final DatabaseConfigurationDTO initData = (DatabaseConfigurationDTO)data;
        this.view.setDatasourceName(initData.getDatasourceId());

        final Collection<NewDatasourceConnector> connectors = this.connectorAgent.getConnectors();
        NewDatasourceConnector foundConnector = null;
        for (NewDatasourceConnector connector : connectors) {
            if (connector.getId() != null && connector.getId().equals(initData.getConfigurationConnectorId())) {
                foundConnector = connector;
                break;
            }
        }
        if (foundConnector != null) {
            this.view.selectConnector(foundConnector.getId());
        }
        delegate.updateControls();
    }

    @Override
    public void clearPage() {
        this.view.setDatasourceName("");
        this.view.selectConnector(null);
        delegate.updateControls();
    }
}
