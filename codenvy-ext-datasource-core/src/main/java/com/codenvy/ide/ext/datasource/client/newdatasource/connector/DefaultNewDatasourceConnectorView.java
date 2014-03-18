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

import com.google.inject.ImplementedBy;

/**
 * The view of datasource wizard connectors.
 */
@ImplementedBy(DefaultNewDatasourceConnectorViewImpl.class)
public interface DefaultNewDatasourceConnectorView extends AbstractNewDatasourceConnectorView {

    /**
     * Returns the database server host.
     * 
     * @return the server host
     */
    String getHostname();


    /**
     * Returns the database server port.
     * 
     * @return the server port
     */
    int getPort();

    /**
     * Sets the port in the displayed configuration.
     * 
     * @param port the new value
     */
    void setPort(int port);

}
