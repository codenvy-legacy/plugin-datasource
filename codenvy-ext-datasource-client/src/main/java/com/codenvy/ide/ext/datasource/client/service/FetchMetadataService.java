package com.codenvy.ide.ext.datasource.client.service;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;

public interface FetchMetadataService {

    void fetchDatabaseInfo(@NotNull DatabaseConfigurationDTO configuration);
}
