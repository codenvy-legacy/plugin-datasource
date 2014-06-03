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

import com.codenvy.dto.shared.DTO;

/**
 * Transfer object for datasource exploration requests.
 * 
 * @author "Mickaël Leduque"
 */
@DTO
public interface ExploreRequestDTO {

    DatabaseConfigurationDTO getDatasourceConfig();

    void setDatasourceConfig(DatabaseConfigurationDTO config);

    ExploreRequestDTO withDatasourceConfig(DatabaseConfigurationDTO config);


    ExploreTableType getExploreTableType();

    void setExploreTableType(ExploreTableType mode);

    ExploreRequestDTO withExploreTableType(ExploreTableType mode);
}
