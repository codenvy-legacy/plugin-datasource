package com.codenvy.ide.ext.datasource.client.service;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.ExploreTableType;

public interface FetchMetadataService {

    void fetchDatabaseInfo(@NotNull DatabaseConfigurationDTO configuration);

    void fetchDatabaseInfo(@NotNull DatabaseConfigurationDTO configuration, ExploreTableType type);
}
