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

import java.util.HashMap;
import java.util.Map;

import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.google.inject.Singleton;

@Singleton
public class DatabaseInfoStoreImpl implements DatabaseInfoStore {

    private final Map<String, DatabaseDTO> data = new HashMap<String, DatabaseDTO>();

    @Override
    public void setDatabaseInfo(final String datasourceId, final DatabaseDTO info) {
        this.data.put(datasourceId, info);
    }

    @Override
    public DatabaseDTO getDatabaseInfo(final String datasourceId) {
        return this.data.get(datasourceId);
    }
}
