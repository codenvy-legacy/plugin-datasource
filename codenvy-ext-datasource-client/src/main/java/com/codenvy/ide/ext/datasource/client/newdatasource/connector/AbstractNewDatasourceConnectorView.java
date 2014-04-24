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
