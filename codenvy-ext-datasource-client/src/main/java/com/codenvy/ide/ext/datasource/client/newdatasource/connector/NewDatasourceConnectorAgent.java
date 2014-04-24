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

import java.util.Collection;

/**
 * Provides DB registered connectors
 */
public interface NewDatasourceConnectorAgent {

    /**
     * Register a new connector.
     * 
     * @param connector the connector to register
     */
    void register(NewDatasourceConnector connector);

    /**
     * Returns all registered connectors.
     * 
     * @return the connectors
     */
    Collection<NewDatasourceConnector> getConnectors();

    /**
     * Retrieve a connector by id.
     * 
     * @param id the id of the connector we seek
     * @return the connector if found, null otherwise
     */
    NewDatasourceConnector getConnector(String id);
}
