package com.codenvy.ide.ext.datasource.client;

import java.util.Collection;

public interface DatabaseInfoOracle {

    Collection<String> getSchemasFor(String datasourceId);

    Collection<String> getTablesFor(String datasourceId, String schema);

    Collection<String> getColumnsFor(String datasourceId, String schema, String table);
}
