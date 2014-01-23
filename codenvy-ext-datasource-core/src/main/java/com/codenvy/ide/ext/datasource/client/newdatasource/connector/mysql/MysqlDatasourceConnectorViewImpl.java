package com.codenvy.ide.ext.datasource.client.newdatasource.connector.mysql;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Created by Wafa on 20/01/14.
 */
public class MysqlDatasourceConnectorViewImpl extends Composite implements MysqlDatasourceConnectorView {

    interface NewDatasourceViewImplUiBinder extends UiBinder<Widget, MysqlDatasourceConnectorViewImpl> {
    }

    @UiField
    TextBox hostField;

    @UiField
    TextBox portField;

    @UiField
    TextBox dbName;

    @UiField
    TextBox usernameField;

    @UiField
    PasswordTextBox passwordField;

    protected ActionDelegate delegate;


    @Inject
    public MysqlDatasourceConnectorViewImpl(NewDatasourceViewImplUiBinder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        hostField.setText("localhost");
        portField.setText("3306"); // default mysql port
        dbName.setText("wafa");
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
