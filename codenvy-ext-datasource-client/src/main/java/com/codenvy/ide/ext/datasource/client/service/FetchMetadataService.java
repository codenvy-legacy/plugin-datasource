/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
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
