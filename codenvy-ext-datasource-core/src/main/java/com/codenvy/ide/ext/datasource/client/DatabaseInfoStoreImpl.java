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
