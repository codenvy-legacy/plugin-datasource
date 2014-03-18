package com.codenvy.ide.ext.datasource.shared;

import com.codenvy.dto.shared.DTO;

@DTO
public interface NuoDBBrokerDTO {

    String getHostName();

    NuoDBBrokerDTO withHostName(String hostname);

    void setHostName(String hostname);

    int getPort();

    NuoDBBrokerDTO withPort(int port);

    void setPort(int port);
}