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
