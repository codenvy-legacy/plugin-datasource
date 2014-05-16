package com.codenvy.ide.ext.datasource.client.service;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.ExploreTableType;

/**
 * Service interface for datasource exploration.
 * 
 * @author "Mickaël Leduque"
 */
public interface FetchMetadataService {

    /***
     * Explore the given datasource, fetch the metadata, with ExploreTableType.STANDARD category.
     * 
     * @param configuration the datasource configuration
     */
    void fetchDatabaseInfo(@NotNull DatabaseConfigurationDTO configuration);

    /***
     * Explore the given datasource, fetch the metadata.
     * 
     * @param configuration the datasource configuration
     * @param type the table category
     */
    void fetchDatabaseInfo(@NotNull DatabaseConfigurationDTO configuration, ExploreTableType type);
}
