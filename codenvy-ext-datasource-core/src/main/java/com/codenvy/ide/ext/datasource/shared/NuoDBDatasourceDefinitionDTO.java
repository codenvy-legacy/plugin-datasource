package com.codenvy.ide.ext.datasource.shared;

import java.util.List;

import com.codenvy.dto.shared.DTO;

@DTO
public interface NuoDBDatasourceDefinitionDTO extends DatabaseConfigurationDTO {

    // List<NuoDBBrokerDTO> getBrokers();
    //
    // void setBrokers(List<NuoDBBrokerDTO> brokers);
    //
    NuoDBDatasourceDefinitionDTO withBrokers(List<NuoDBBrokerDTO> brokers);


    /* Change type of parent with* methods */
    NuoDBDatasourceDefinitionDTO withDatabaseType(DatabaseType type);

    NuoDBDatasourceDefinitionDTO withDatabaseName(String databaseName);

    NuoDBDatasourceDefinitionDTO withDatasourceId(String type);

    NuoDBDatasourceDefinitionDTO withUsername(String username);

    NuoDBDatasourceDefinitionDTO withPassword(String password);
}
