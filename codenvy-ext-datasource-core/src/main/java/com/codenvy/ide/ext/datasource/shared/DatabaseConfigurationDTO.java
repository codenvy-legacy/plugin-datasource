package com.codenvy.ide.ext.datasource.shared;

import java.util.List;

import com.codenvy.dto.shared.DTO;

@DTO
public interface DatabaseConfigurationDTO {

    String getDatasourceId();

    void setDatasourceId(String id);

    DatabaseConfigurationDTO withDatasourceId(String type);


    String getDatabaseName();

    void setDatabaseName(String databaseName);

    DatabaseConfigurationDTO withDatabaseName(String databaseName);


    DatabaseType getDatabaseType();

    void setDatabaseType(DatabaseType type);

    DatabaseConfigurationDTO withDatabaseType(DatabaseType type);


    String getUsername();

    DatabaseConfigurationDTO withUsername(String username);

    void setUsername(String username);


    String getPassword();

    void setPassword(String password);

    DatabaseConfigurationDTO withPassword(String password);


    /* should be in child classes */
    String getHostName();

    int getPort();


    void setHostName(String hostname);

    void setPort(int port);


    List<NuoDBBrokerDTO> getBrokers();

    void setBrokers(List<NuoDBBrokerDTO> brokers);

}
