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

    void onTestConnectionSuccess();

    void onTestConnectionFailure(String errorMessage);
}
