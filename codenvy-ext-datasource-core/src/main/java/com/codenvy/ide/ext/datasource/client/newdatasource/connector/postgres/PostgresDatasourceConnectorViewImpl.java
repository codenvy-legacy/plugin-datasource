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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.postgres;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * The implementation of {@link PostgresDatasourceConnectorView}.
 * 
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
public class PostgresDatasourceConnectorViewImpl extends Composite implements PostgresDatasourceConnectorView {
    interface NewDatasourceViewImplUiBinder extends UiBinder<Widget, PostgresDatasourceConnectorViewImpl> {
    }

    @UiField
    TextBox                  hostField;

    @UiField
    TextBox                  portField;

    @UiField
    TextBox                  dbName;

    @UiField
    TextBox                  usernameField;

    @UiField
    TextBox                  passwordField;

    protected ActionDelegate delegate;


    @Inject
    public PostgresDatasourceConnectorViewImpl(NewDatasourceViewImplUiBinder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        hostField.setText("localhost");
        portField.setText("5432"); // default postgres port
        dbName.setText("nuxeo");
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
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


}
