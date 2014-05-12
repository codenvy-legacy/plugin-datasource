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
package com.codenvy.ide.ext.datasource.client.store;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Inject;

import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Singleton;

/**
 * Implementation of the {@link DatabaseInfoStore} interface.
 * 
 * @author "Mickaël Leduque"
 */
@Singleton
public class DatabaseInfoStoreImpl implements DatabaseInfoStore {

    /** The database metadata cache. */
    private final Map<String, DatabaseDTO> data         = new HashMap<String, DatabaseDTO>();

    /** Per-datasource "fetch pending" flags. */
    private final HashSet<String>          fetchPending = new HashSet<>();

    /** Actions that are executed on database metadata before storing it. */
    private final Set<PreStoreProcessor>   preStoreProcessors;

    @Inject
    public DatabaseInfoStoreImpl(final @Nullable Set<PreStoreProcessor> preStoreProcessors) {
        this.preStoreProcessors = preStoreProcessors;
    }

    @Override
    public void setDatabaseInfo(final String datasourceId, final DatabaseDTO info) {
        final DatabaseDTO modifiedInfo = preStoreProcessing(info);
        this.data.put(datasourceId, modifiedInfo);
    }

    @Override
    public DatabaseDTO getDatabaseInfo(final String datasourceId) {
        return this.data.get(datasourceId);
    }

    @Override
    public void setFetchPending(final String datasourceId) {
        this.fetchPending.add(datasourceId);
    }

    @Override
    public boolean isFetchPending(final String datasourceId) {
        return this.fetchPending.contains(datasourceId);
    }

    @Override
    public void clearFetchPending(final String datasourceId) {
        this.fetchPending.remove(datasourceId);
    }

    /**
     * Execute all processors on the given database metadata. The order the processors are called in is unspecified.
     * 
     * @param databaseDTO the metadata to process
     * @return the modified metadata
     */
    private DatabaseDTO preStoreProcessing(final DatabaseDTO databaseDTO) {
        if (this.preStoreProcessors == null) {
            return databaseDTO;
        }
        DatabaseDTO modifiedDto = databaseDTO;
        for (final PreStoreProcessor processor : this.preStoreProcessors) {
            try {
                modifiedDto = processor.execute(modifiedDto);
            } catch (final PreStoreProcessorException e) {
                Log.error(DatabaseInfoStoreImpl.class, "Pre store processing error - " + e.getLocalizedMessage());
                // keep the last successfully processed dto
                break;
            }
        }
        return modifiedDto;
    }
}
