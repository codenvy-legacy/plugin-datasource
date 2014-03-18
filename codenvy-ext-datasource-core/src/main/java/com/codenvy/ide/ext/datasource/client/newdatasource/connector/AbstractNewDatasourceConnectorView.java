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

import com.codenvy.ide.api.mvp.View;

/**
 * The view of datasource wizard connectors.
 */
public interface AbstractNewDatasourceConnectorView extends View<AbstractNewDatasourceConnectorView.ActionDelegate> {

    /** Required for delegating functions in view. */
    public interface ActionDelegate {
        /** Action launched when asked to test the configured connection. */
        void onClickTestConnectionButton();
    }

    /**
     * Returns the database name (the SQL exiting database).
     * 
     * @return the database name
     */
    String getDatabaseName();

    /**
     * Returns the configured username used for the connection.
     * 
     * @return the username
     */
    String getUsername();

    /**
     * Returns the configured password used for the connection.
     * 
     * @return the password
     */
    String getPassword();
}
