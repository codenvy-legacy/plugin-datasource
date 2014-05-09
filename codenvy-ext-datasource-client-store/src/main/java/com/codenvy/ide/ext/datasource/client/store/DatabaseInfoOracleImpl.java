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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.shared.ColumnDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.SchemaDTO;
import com.codenvy.ide.ext.datasource.shared.TableDTO;
import com.google.inject.Inject;

public class DatabaseInfoOracleImpl implements DatabaseInfoOracle {

    private DatabaseInfoStore databaseInfoStore;

    @Inject
    public DatabaseInfoOracleImpl(final @NotNull DatabaseInfoStore databaseInfoStore) {
        this.databaseInfoStore = databaseInfoStore;
    }

    private Map<String, SchemaDTO> getSchemaDtosFor(final String datasourceId) {
        final DatabaseDTO database = this.databaseInfoStore.getDatabaseInfo(datasourceId);
        if (database != null && database.getSchemas() != null) {
            return database.getSchemas();
        }
        return new HashMap<String, SchemaDTO>();
    }

    private Map<String, TableDTO> getTableDtosFor(final String datasourceId, final String schema) {
        Map<String, SchemaDTO> schemaDtos = getSchemaDtosFor(datasourceId);
        final SchemaDTO schemaDto = schemaDtos.get(schema);
        if (schemaDto != null && schemaDto.getTables() != null) {
            return schemaDto.getTables();
        }
        return new HashMap<String, TableDTO>();
    }

    private Map<String, ColumnDTO> getColumnDtosFor(final String datasourceId, final String schema, final String table) {
        Map<String, TableDTO> tableDtos = getTableDtosFor(datasourceId, schema);
        final TableDTO tableDto = tableDtos.get(table);
        if (tableDto != null && tableDto.getColumns() != null) {
            return tableDto.getColumns();
        }
        return new HashMap<String, ColumnDTO>();
    }

    @Override
    public Collection<String> getSchemasFor(final String datasourceId) {
        return getSchemaDtosFor(datasourceId).keySet();
    }

    @Override
    public Collection<String> getTablesFor(final String datasourceId, final String schema) {
        return getTableDtosFor(datasourceId, schema).keySet();
    }

    @Override
    public Collection<String> getColumnsFor(final String datasourceId, final String schema, final String table) {
        return getColumnDtosFor(datasourceId, schema, table).keySet();
    }

}
