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
package com.codenvy.ide.ext.datasource.client.newdatasource.presenter;

import com.codenvy.ide.api.wizard.AbstractWizardPage;
import com.codenvy.ide.ext.datasource.client.AvailableJdbcDriversService;
import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEvent;
import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEventHandler;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.newdatasource.view.NewDatasourceWizardMainPageView;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NewDatasourceWizardMainPagePresenter extends AbstractWizardPage<DatabaseConfigurationDTO>
        implements NewDatasourceWizardMainPageView.ActionDelegate {

    protected NewDatasourceWizardMainPageView    view;
    protected NewDatasourceConnectorAgent        connectorAgent;
    protected Collection<NewDatasourceConnector> dbConnectors;
    protected AvailableJdbcDriversService        jdbcDriversService;
    protected EventBus                           eventBus;
    protected ArrayList<String>                  enabledConnectorsId;
    private   ConnectorSelectedListener          connectorSelectedListener;
    private   NewDatasourceConnector             selectedConnector;

    @Inject
    public NewDatasourceWizardMainPagePresenter(NewDatasourceWizardMainPageView view,
                                                NewDatasourceConnectorAgent connectorAgent,
                                                AvailableJdbcDriversService jdbcDriversService,
                                                EventBus eventBus) {
        super();
        this.view = view;
        this.connectorAgent = connectorAgent;
        this.jdbcDriversService = jdbcDriversService;
        this.eventBus = eventBus;
        this.view.setDelegate(this);
        enabledConnectorsId = new ArrayList<>();
    }

    @Override
    public boolean isCompleted() {
        return selectedConnector != null;
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

        updateView();
    }

    /** Update view from data-object. */
    private void updateView() {
        for (NewDatasourceConnector connector : dbConnectors) {
            if (dataObject.getConfigurationConnectorId().equals(connector.getId())) {
                view.selectConnector(connector);
                break;
            }
        }
    }

    @Override
    public void onConnectorSelected(String id) {
        dataObject.setConfigurationConnectorId(id);

        for (NewDatasourceConnector connector : dbConnectors) {
            if (id.equals(connector.getId())) {
                selectedConnector = connector;
                break;
            }
        }
        
        connectorSelectedListener.onConnectorSelected(id);
        updateDelegate.updateControls();
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
    public void onCategorySelected() {
        selectedConnector = null;
        connectorSelectedListener.onConnectorSelected(null);
        updateDelegate.updateControls();
    }

    void setConnectorSelectedListener(ConnectorSelectedListener connectorSelectedListener) {
        this.connectorSelectedListener = connectorSelectedListener;
    }

    interface ConnectorSelectedListener {
        void onConnectorSelected(@Nullable String id);
    }
}
