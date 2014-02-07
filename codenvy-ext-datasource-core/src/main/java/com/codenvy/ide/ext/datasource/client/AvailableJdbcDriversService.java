/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.client;

import java.util.List;

import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEvent;

public interface AvailableJdbcDriversService {

    /**
     * will retrieve the list of drivers from the server and keep it. This method is asynchronous. Viewers can get notified once the drivers
     * are fetched with {@link JdbcDriversFetchedEvent} event.
     */
    void fetch();

    /**
     * get the list of JDBC drivers that has been fetched previously
     */
    List<String> getDrivers();

}
