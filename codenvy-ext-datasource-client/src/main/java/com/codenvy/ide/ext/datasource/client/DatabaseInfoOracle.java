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
package com.codenvy.ide.ext.datasource.client;

import java.util.Collection;

/**
 * High level service that provides schemas, tables and columns of a datasource as {@link String}.
 */
public interface DatabaseInfoOracle {

    Collection<String> getSchemasFor(String datasourceId);

    Collection<String> getTablesFor(String datasourceId, String schema);

    Collection<String> getColumnsFor(String datasourceId, String schema, String table);
}
