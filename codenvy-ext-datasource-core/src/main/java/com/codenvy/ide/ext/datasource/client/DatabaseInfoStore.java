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

import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;

/**
 * Keep the link between a datasource and its database content structure. Default implementation is using it as a cache: when
 * {@link DatabaseDTO} of a datasource is retrieved from server side, {@link DatabaseDTO} is stored in memory through this class.
 */
public interface DatabaseInfoStore {

    void setDatabaseInfo(String datasourceId, DatabaseDTO info);

    DatabaseDTO getDatabaseInfo(String datasourceId);

}
