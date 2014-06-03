/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
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
