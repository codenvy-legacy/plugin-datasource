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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.google.cloud.sql;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


public class GoogleCloudSqlConnectorViewImpl extends Composite
                                                                    implements GoogleCloudSqlConnectorView {
    interface NewDatasourceViewImplUiBinder extends UiBinder<Widget, GoogleCloudSqlConnectorViewImpl> {
    }

    @UiField
    TextBox                instanceField;

    @UiField
    TextBox                dbName;

    @UiField
    TextBox                usernameField;

    @UiField
    PasswordTextBox        passwordField;

    @UiField
    Button                 testConnectionButton;

    @UiField
    CheckBox               useSSL;

    @UiField
    CheckBox               verifyServerCertificate;

    private ActionDelegate delegate;


    @Inject
    public GoogleCloudSqlConnectorViewImpl(NewDatasourceViewImplUiBinder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setDelegate(GoogleCloudSqlConnectorView.ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getDatabaseName() {
        return dbName.getText();
    }

        
    @Override
    public String getInstanceName() {
        return instanceField.getText();
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
    public void setInstanceName(final String instanceName) {
        this.instanceField.setValue(instanceName);
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
}
