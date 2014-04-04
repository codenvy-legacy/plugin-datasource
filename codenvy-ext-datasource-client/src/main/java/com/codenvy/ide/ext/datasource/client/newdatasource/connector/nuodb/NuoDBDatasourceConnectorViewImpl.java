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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.nuodb;

import java.util.Set;

import com.codenvy.ide.ext.datasource.client.common.CellTableResourcesInvisible;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.inject.Inject;

public class NuoDBDatasourceConnectorViewImpl extends Composite implements NuoDBDatasourceConnectorView {


    private static final String TEXT_BOX_STYLE = "gwt-TextBox";

    @UiField(provided = true)
    CellTable<NuoDBBroker>      brokerList;

    @UiField
    Button                      addBrokerButton;

    @UiField
    Button                      deleteBrokersButton;

    @UiField
    TextBox                     dbName;

    @UiField
    TextBox                     usernameField;

    @UiField
    PasswordTextBox             passwordField;

    @UiField
    Button                      testConnectionButton;

    private ActionDelegate      delegate;
    private NuoActionDelegate   nuoDelegate;


    @Inject
    public NuoDBDatasourceConnectorViewImpl(final NuoDBDatasourceViewImplUiBinder uiBinder,
                                            final CellTableResourcesInvisible cellTableResources) {
        ProvidesKey<NuoDBBroker> keyProvider = new ProvidesKey<NuoDBBroker>() {
            @Override
            public Object getKey(final NuoDBBroker item) {
                return item.getId();
            }
        };
        this.brokerList = new CellTable<NuoDBBroker>(20, cellTableResources, keyProvider);
        initWidget(uiBinder.createAndBindUi(this));

        // first column : host
        final TextInputCell hostCell = new StyledTextInputCell();
        Column<NuoDBBroker, String> hostColumn = new Column<NuoDBBroker, String>(hostCell) {
            @Override
            public String getValue(final NuoDBBroker broker) {
                return broker.getHost();
            }
        };
        hostColumn.setFieldUpdater(new FieldUpdater<NuoDBBroker, String>() {
            @Override
            public void update(final int index, final NuoDBBroker broker, final String value) {
                // update host value in model
                broker.setHost(value);
            }
        });

        this.brokerList.addColumn(hostColumn, new TextHeader("Host"));

        // second column : port
        final TextInputCell portCell = new StyledTextInputCell();
        Column<NuoDBBroker, String> portColumn = new Column<NuoDBBroker, String>(portCell) {
            @Override
            public String getValue(final NuoDBBroker broker) {
                if (broker.getPort() != null) {
                    return Integer.toString(broker.getPort());
                } else {
                    return "";
                }
            }
        };
        portColumn.setFieldUpdater(new FieldUpdater<NuoDBBroker, String>() {
            @Override
            public void update(final int index, final NuoDBBroker broker, final String value) {
                try {
                    // update port value in model
                    int port = Integer.parseInt(value);
                    broker.setPort(port);
                } catch (final NumberFormatException e) {
                    // invalid input, cancel change
                    broker.setPort(null);
                    portCell.clearViewData(broker.getId());
                }
                brokerList.redraw();
            }
        });

        this.brokerList.addColumn(portColumn, new TextHeader("Port"));

        // manage selection
        final MultiSelectionModel<NuoDBBroker> selectionModel = new MultiSelectionModel<>(keyProvider);
        this.brokerList.setSelectionModel(selectionModel);
    }

    @Override
    public void setNuoDelegate(final NuoActionDelegate delegate) {
        this.nuoDelegate = delegate;
    }

    @Override
    public void setDelegate(final ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getDatabaseName() {
        return dbName.getText();
    }

    @Override
    public void bindBrokerList(final ListDataProvider<NuoDBBroker> dataProvider) {
        dataProvider.addDataDisplay(this.brokerList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<NuoDBBroker> getBrokerSelection() {
        return ((MultiSelectionModel<NuoDBBroker>)this.brokerList.getSelectionModel()).getSelectedSet();
    }

    @Override
    public String getUsername() {
        return usernameField.getText();
    }

    @Override
    public String getPassword() {
        return passwordField.getText();
    }

    @Override
    public void setDatabaseName(final String databaseName) {
        this.dbName.setValue(databaseName);
    }

    @Override
    public void setUsername(final String username) {
        this.usernameField.setValue(username);

    }

    @Override
    public void setPassword(final String password) {
        this.passwordField.setValue(password);
    }

    @UiHandler("testConnectionButton")
    void handleTestConnectionClick(final ClickEvent e) {
        this.delegate.onClickTestConnectionButton();
    }

    @UiHandler("addBrokerButton")
    void handleAddBrokerClick(final ClickEvent e) {
        this.nuoDelegate.onAddBroker();
    }

    @UiHandler("deleteBrokersButton")
    void handleDeleteBrokersClick(final ClickEvent e) {
        this.nuoDelegate.onDeleteBrokers();
    }

    private static class StyledTextInputCell extends TextInputCell {

        private static Template template = GWT.create(Template.class);

        @Override
        public void render(Context context, String value, SafeHtmlBuilder sb) {
            // Get the view data.
            Object key = context.getKey();
            ViewData viewData = getViewData(key);
            if (viewData != null && viewData.getCurrentValue().equals(value)) {
                clearViewData(key);
                viewData = null;
            }

            String s = (viewData != null) ? viewData.getCurrentValue() : value;
            if (s != null) {
                sb.append(template.input(s, TEXT_BOX_STYLE));
            } else {
                sb.appendHtmlConstant("<input type=\"text\" tabindex=\"-1\"></input>");
            }
        }
    }

    interface NuoDBDatasourceViewImplUiBinder extends UiBinder<Widget, NuoDBDatasourceConnectorViewImpl> {
    }

    interface Template extends SafeHtmlTemplates {
        @Template("<input type=\"text\" value=\"{0}\" tabindex=\"-1\" class='{1}'></input>")
        SafeHtml input(final String value, final String className);
    }
}
