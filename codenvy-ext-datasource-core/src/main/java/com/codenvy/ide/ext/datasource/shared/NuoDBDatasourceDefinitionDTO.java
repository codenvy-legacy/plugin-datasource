/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
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
