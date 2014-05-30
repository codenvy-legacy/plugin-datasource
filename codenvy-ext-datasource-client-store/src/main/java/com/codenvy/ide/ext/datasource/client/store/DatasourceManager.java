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
package com.codenvy.ide.ext.datasource.client.store;

import java.util.Iterator;
import java.util.Set;

import com.codenvy.api.user.shared.dto.Profile;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DatasourceManager extends Iterable<DatabaseConfigurationDTO> {

    Iterator<DatabaseConfigurationDTO> getDatasources();

    void add(final DatabaseConfigurationDTO configuration);

    public void remove(final DatabaseConfigurationDTO configuration);

    public DatabaseConfigurationDTO getByName(final String name);

    public Set<String> getNames();

    public void persist(AsyncCallback<Profile> callback);
}
