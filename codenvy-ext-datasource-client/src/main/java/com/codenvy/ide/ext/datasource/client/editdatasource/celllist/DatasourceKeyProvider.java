/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.editdatasource.celllist;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.view.client.ProvidesKey;

/**
 * A {@link ProvidesKey} implementation for datasource definition objects.
 * 
 * @author "Mickaël Leduque"
 */
public class DatasourceKeyProvider implements ProvidesKey<DatabaseConfigurationDTO> {

    public static final String NAME = "datasourceKeyProvider";

    @Override
    public Object getKey(final DatabaseConfigurationDTO item) {
        return item.getDatasourceId();
    }

}
