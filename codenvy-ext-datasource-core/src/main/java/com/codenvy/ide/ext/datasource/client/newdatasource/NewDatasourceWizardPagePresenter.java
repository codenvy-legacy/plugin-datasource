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
package com.codenvy.ide.ext.datasource.client.newdatasource;

import java.util.List;

import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.client.AvailableJdbcDriversService;
import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEvent;
import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEventHandler;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class NewDatasourceWizardPagePresenter extends AbstractWizardPage implements NewDatasourceWizardPageView.ActionDelegate {
    protected NewDatasourceWizardPageView   view;
    protected NewDatasourceConnectorAgent   connectorAgent;
    protected Array<NewDatasourceConnector> dbConnectors;
    protected AvailableJdbcDriversService   jdbcDriversService;
    protected EventBus                      eventBus;

    @Inject
    public NewDatasourceWizardPagePresenter(NewDatasourceWizardPageView view,
                                            NewDatasourceConnectorAgent connectorAgent,
                                            AvailableJdbcDriversService jdbcDriversService,
                                            EventBus eventBus) {
        super("New Datasource", null);
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
        return true;
    }

    @Override
    public void focusComponent() {
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

    protected void updateAvailableDatabase(List<String> drivers) {
        view.disableAllDbTypeButton();
        if (drivers == null) {
            return;
        }
        for (int i = 0; i < dbConnectors.size(); i++) {
            NewDatasourceConnector connector = dbConnectors.get(i);

            if (drivers.contains(connector.getJdbcClassName())) {
                view.enableDbTypeButton(i);
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
    }

    @Override
    public void onConnectorSelected(int id) {
        NewDatasourceConnector connector = dbConnectors.get(id);
        wizardContext.putData(NewDatasourceWizard.DATASOURCE_CONNECTOR, connector);

        view.selectConnector(id);
        delegate.updateControls();
    }

    @Override
    public void storeOptions() {
        wizardContext.putData(NewDatasourceWizard.DATASOURCE_NAME, view.getDatasourceName());
    }
}
