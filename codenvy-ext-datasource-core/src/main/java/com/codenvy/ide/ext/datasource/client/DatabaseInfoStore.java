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
