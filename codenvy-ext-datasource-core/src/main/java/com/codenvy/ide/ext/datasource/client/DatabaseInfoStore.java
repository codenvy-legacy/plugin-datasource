package com.codenvy.ide.ext.datasource.client;

import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;

public interface DatabaseInfoStore {
    void setDatabaseInfo(String datasourceId, DatabaseDTO info);

    DatabaseDTO getDatabaseInfo(String datasourceId);
}
