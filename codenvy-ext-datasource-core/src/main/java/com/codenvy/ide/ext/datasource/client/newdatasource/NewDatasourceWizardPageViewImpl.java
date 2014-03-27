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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * The implementation of {@link NewDatasourceWizardPageView}.
 * 
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
public class NewDatasourceWizardPageViewImpl extends Composite implements NewDatasourceWizardPageView {
    interface NewDatasourceViewImplUiBinder extends UiBinder<Widget, NewDatasourceWizardPageViewImpl> {
    }

    @UiField
    TextBox                      datasourceName;
    @UiField
    SimplePanel                  databasePanel;

    protected List<ToggleButton> connectorButtons;
    protected List<String>       connectorIds;

    protected ActionDelegate     delegate;


    @Inject
    public NewDatasourceWizardPageViewImpl(NewDatasourceViewImplUiBinder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setConnectors(Collection<NewDatasourceConnector> connectors) {
        connectorButtons = new ArrayList<ToggleButton>(connectors.size());
        connectorIds = new ArrayList<String>(connectors.size());

        Grid grid = new Grid(2, connectors.size());
        databasePanel.setWidget(grid);
        HTMLTable.CellFormatter formatter = grid.getCellFormatter();

        // create button for each paas
        int i = 0;
        for (final NewDatasourceConnector connector : connectors) {

            ImageResource icon = connector.getImage();
            final ToggleButton btn;
            if (icon != null) {
                btn = new ToggleButton(new Image(icon));
            } else {
                btn = new ToggleButton();
            }
            btn.setSize("48px", "48px");

            btn.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    delegate.onConnectorSelected(connector.getId());
                }
            });
            grid.setWidget(0, i, btn);
            formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);

            Label title = new Label(connector.getTitle());
            grid.setWidget(1, i, title);
            formatter.setHorizontalAlignment(1, i, HasHorizontalAlignment.ALIGN_CENTER);

            connectorButtons.add(btn);
            connectorIds.add(connector.getId());

            i++;
        }
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getDatasourceName() {
        return datasourceName.getText();
    }

    @Override
    public void selectConnector(final String id) {
        for (int i = 0; i < connectorButtons.size(); i++) {
            final String connectorId = this.connectorIds.get(i);
            final ToggleButton button = connectorButtons.get(i);
            button.setDown(connectorId.equals(id));
        }
    }

    @Override
    public void enableDbTypeButton(final String... ids) {
        for (int i = 0; i < connectorButtons.size(); i++) {
            final String connectorId = this.connectorIds.get(i);
            final ToggleButton button = connectorButtons.get(i);
            for (String id : ids) {
                if (connectorId.equals(id)) {
                    button.setEnabled(true);
                    break;
                }
            }
        }
    }

    @Override
    public void disableAllDbTypeButton() {
        for (ToggleButton button : connectorButtons) {
            button.setEnabled(false);
        }
    }

    @UiHandler("datasourceName")
    public void onDatasourceIdChanged(final KeyUpEvent event) {
        this.delegate.onDatasourceNameModified(this.datasourceName.getValue());
    }
}
