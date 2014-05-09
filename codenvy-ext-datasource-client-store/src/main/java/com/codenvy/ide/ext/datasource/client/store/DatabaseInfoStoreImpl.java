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
