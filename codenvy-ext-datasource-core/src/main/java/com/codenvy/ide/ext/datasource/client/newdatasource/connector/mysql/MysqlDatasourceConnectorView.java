package com.codenvy.ide.ext.datasource.client.newdatasource.connector.mysql;

import com.codenvy.ide.api.mvp.View;
import com.google.inject.ImplementedBy;

/**
 * Created by Wafa on 20/01/14.
 */
@ImplementedBy(MysqlDatasourceConnectorViewImpl.class)
public interface MysqlDatasourceConnectorView extends View<MysqlDatasourceConnectorView.ActionDelegate> {

    /** Required for delegating functions in view. */
    public interface ActionDelegate {

    }

    String getDatabaseName();
    String getHostname();
    int getPort();
    String getUsername();
    String getPassword();

}
