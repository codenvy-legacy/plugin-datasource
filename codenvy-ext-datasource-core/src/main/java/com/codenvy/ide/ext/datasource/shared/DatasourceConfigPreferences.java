package com.codenvy.ide.ext.datasource.shared;

import java.util.Map;

import com.codenvy.dto.shared.DTO;

@DTO
public interface DatasourceConfigPreferences {

    Map<String, DatabaseConfigurationDTO> getDatasources();

}
