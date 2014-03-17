/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


public class DefaultNewDatasourceConnectorViewImpl extends Composite
                                                                    implements DefaultNewDatasourceConnectorView {
    interface NewDatasourceViewImplUiBinder extends UiBinder<Widget, DefaultNewDatasourceConnectorViewImpl> {
    }

    @UiField
    TextBox                hostField;

    @UiField
    TextBox                portField;

    @UiField
    TextBox                dbName;

    @UiField
    TextBox                usernameField;

    @UiField
    PasswordTextBox        passwordField;

    @UiField
    Button                 testConnectionButton;

    private ActionDelegate delegate;


    @Inject
    public DefaultNewDatasourceConnectorViewImpl(NewDatasourceViewImplUiBinder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        hostField.setText("localhost");
    }

    public void setDelegate(DefaultNewDatasourceConnectorView.ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getDatabaseName() {
        return dbName.getText();
    }

    @Override
    public String getHostname() {
        return hostField.getText();
    }

    @Override
    public int getPort() {
        return Integer.parseInt(portField.getText());
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
    public void setPort(int port) {
        portField.setText(Integer.toString(port));
    }

    @UiHandler("testConnectionButton")
    void handleClick(ClickEvent e) {
        delegate.onClickTestConnectionButton();
    }
}
