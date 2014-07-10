/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.newdatasource.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.ext.datasource.client.AvailableJdbcDriversService;
import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEvent;
import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEventHandler;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.newdatasource.view.NewDatasourceWizardMainPageView;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class NewDatasourceWizardMainPagePresenter extends AbstractWizardPage implements NewDatasourceWizardMainPageView.ActionDelegate,
                                                                            InitializableWizardPage {

    protected NewDatasourceWizardMainPageView    view;
    protected NewDatasourceConnectorAgent        connectorAgent;
    protected Collection<NewDatasourceConnector> dbConnectors;
    protected AvailableJdbcDriversService        jdbcDriversService;
    protected EventBus                           eventBus;
    protected ArrayList<String>                  enabledConnectorsId;

    @Inject
    public NewDatasourceWizardMainPagePresenter(NewDatasourceWizardMainPageView view,
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
        enabledConnectorsId = new ArrayList<String>();
    }

    @Override
    public String getNotice() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return (wizardContext.getData(NewDatasourceWizard.DATASOURCE_CONNECTOR) != null);
    }

    @Override
    public void focusComponent() {
    }

    protected void updateAvailableDatabase(List<String> drivers) {
        enabledConnectorsId.clear();
        if (drivers == null) {
            return;
        }
        for (final NewDatasourceConnector connector : dbConnectors) {
            if (drivers.contains(connector.getJdbcClassName())) {
                enabledConnectorsId.add(connector.getId());
            }
        }
        view.reset();
    }

    @Override
    public void removeOptions() {
        wizardContext.removeData(NewDatasourceWizard.DATASOURCE_CONNECTOR);
    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view.asWidget());

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
        if (id != null) {
            for (final NewDatasourceConnector item : dbConnectors) {
                if (item.getId().equals(id)) {
                    connector = item;
                    break;
                }
            }
        }
        wizardContext.putData(NewDatasourceWizard.DATASOURCE_CONNECTOR, connector);
        delegate.updateControls();
    }

    @Override
    public boolean connectorEnabled(String id) {
        boolean enabled = false;
        for (String connectorId : enabledConnectorsId) {
            if (id.equals(connectorId)) {
                enabled = true;
                break;
            }
        }
        return enabled;
    }

    @Override
    public void onCategorieSelected() {
        removeOptions();
        delegate.updateControls();
    }

    @Override
    public void initPage(Object initData) {

    }

    @Override
    public void clearPage() {

    }
}
