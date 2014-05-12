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

import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;

/**
 * Keep the link between a datasource and its database content structure. Default implementation is using it as a cache: when
 * {@link DatabaseDTO} of a datasource is retrieved from server side, {@link DatabaseDTO} is stored in memory through this class.
 */
public interface DatabaseInfoStore {

    /** Store the metadata for the given datasource id. */
    void setDatabaseInfo(String datasourceId, DatabaseDTO info);

    /**
     * Retrieve the stored metadata for the given datasource id. Null is returned is no metadata was stored.
     * 
     * @param datasourceId the id of the datasource
     * @return the previously stored metadata (or null if there weren't any)
     */
    DatabaseDTO getDatabaseInfo(String datasourceId);

    /**
     * Clears the "fetch pending" flag for the given datasource.
     * 
     * @param datasourceId the datasource id
     */
    void clearFetchPending(String datasourceId);

    /**
     * Check if there is a "fetch pending" flag for the given datasource.
     * 
     * @param datasourceId the datasource id
     * @return true iff there is a pending fetch for the datasource
     */
    boolean isFetchPending(String datasourceId);

    /**
     * Sets a "fetch pending" flag for this datasource.
     * 
     * @param datasourceId the datasource id
     */
    void setFetchPending(String datasourceId);

}
