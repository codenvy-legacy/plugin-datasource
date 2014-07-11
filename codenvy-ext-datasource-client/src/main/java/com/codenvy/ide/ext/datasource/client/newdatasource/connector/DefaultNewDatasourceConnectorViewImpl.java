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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import javax.annotation.Nullable;

import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


public class DefaultNewDatasourceConnectorViewImpl extends Composite
                                                                    implements DefaultNewDatasourceConnectorView {
    interface NewDatasourceViewImplUiBinder extends UiBinder<Widget, DefaultNewDatasourceConnectorViewImpl> {
    }

    @UiField
    Label                  configureTitleCaption;

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

    @UiField
    Label                  testConnectionErrorMessage;

    @UiField
    RadioButton            radioUserPref;

    @UiField
    RadioButton            radioProject;

    @UiField
    ListBox                projectsList;

    @UiField
    CheckBox               useSSL;

    @UiField
    CheckBox               verifyServerCertificate;

    @UiField
    DatasourceUiResources  datasourceUiResources;

    private ActionDelegate delegate;


    @Inject
    public DefaultNewDatasourceConnectorViewImpl(NewDatasourceViewImplUiBinder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        hostField.setText("localhost");

        radioUserPref.setValue(true);
        radioProject.setEnabled(false);
        projectsList.setEnabled(false);
        projectsList.setWidth("100px");
        
        configureTitleCaption.setText("Settings");
    }

    public void setDelegate(DefaultNewDatasourceConnectorView.ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setImage(@Nullable ImageResource image) {

    }

    @Override
    public void setDatasourceName(@Nullable String dsName) {

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

    @Override
    public boolean getUseSSL() {
        if (useSSL.getValue() != null) {
            return useSSL.getValue();
        } else {
            return false;
        }
    }

    @Override
    public boolean getVerifyServerCertificate() {
        if (verifyServerCertificate.getValue() != null) {
            return verifyServerCertificate.getValue();
        } else {
            return false;
        }
    }

    @Override
    public void setDatabaseName(final String databaseName) {
        this.dbName.setValue(databaseName);
    }

    @Override
    public void setHostName(final String hostName) {
        this.hostField.setValue(hostName);
    }

    @Override
    public void setUseSSL(final boolean useSSL) {
        this.useSSL.setValue(useSSL);
    }

    @Override
    public void setVerifyServerCertificate(final boolean verifyServerCertificate) {
        this.verifyServerCertificate.setValue(verifyServerCertificate);
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
    void handleClick(ClickEvent e) {
        delegate.onClickTestConnectionButton();
    }

    @Override
    public void onTestConnectionSuccess() {
        // turn button green
        testConnectionButton.setStyleName(datasourceUiResources.datasourceUiCSS().datasourceWizardTestConnectionOK());
        // clear error messages
        testConnectionErrorMessage.setText("");
    }

    @Override
    public void onTestConnectionFailure(String errorMessage) {
        // turn test button red
        testConnectionButton.setStyleName(datasourceUiResources.datasourceUiCSS().datasourceWizardTestConnectionKO());
        // set message
        testConnectionErrorMessage.setText(errorMessage);

    }

    // @UiHandler("radioUserPref")
    // void handleRadioUserPrefClick(ClickEvent e) {
    // delegate.onClickRadioUserPref(); TODO
    // }

    // @UiHandler("radioProject")
    // void handleRadioProjectClick(ClickEvent e) {
    // delegate.onClickRadioProject(); TODO
    // }

    // @Override
    // public void setRadioUserPrefValue(final boolean value) {
    // radioUserPref.setValue(value);
    // }

    // @Override
    // public void setRadioProjectValue(final boolean value) {
    // radioProject.setValue(value);
    // }
}
