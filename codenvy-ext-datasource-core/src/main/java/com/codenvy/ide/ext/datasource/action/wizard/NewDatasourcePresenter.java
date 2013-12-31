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
package com.codenvy.ide.ext.datasource.action.wizard;

import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.action.NewDSConnectionWizard;
import com.codenvy.ide.ext.datasource.connector.ConnectorAgent;
import com.codenvy.ide.ext.datasource.connector.DatasourceConnector;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class NewDatasourcePresenter extends AbstractWizardPage implements NewDatasourceView.ActionDelegate {
    protected NewDatasourceView          view;
    protected ConnectorAgent             connectorAgent;
    protected Array<DatasourceConnector> dbConnectors;

    @Inject
    public NewDatasourcePresenter(NewDatasourceView view, ConnectorAgent connectorAgent) {
        super("New Datasource", null);
        this.view = view;
        this.connectorAgent = connectorAgent;
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
    }

    @Override
    public void removeOptions() {
        wizardContext.removeData(NewDSConnectionWizard.DATASOURCE_CONNECTOR);
    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void onConnectorSelected(int id) {
        DatasourceConnector connector = dbConnectors.get(id);
        wizardContext.putData(NewDSConnectionWizard.DATASOURCE_CONNECTOR, connector);
        delegate.updateControls();
    }

}
