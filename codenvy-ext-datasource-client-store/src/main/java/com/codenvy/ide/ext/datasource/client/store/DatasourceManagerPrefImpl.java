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
package com.codenvy.ide.ext.datasource.client.store;

import java.util.Iterator;
import java.util.Set;

import com.codenvy.api.user.shared.dto.ProfileDescriptor;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatasourceConfigPreferences;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DatasourceManagerPrefImpl implements DatasourceManager {

    private static final String PREFERENCE_KEY = "datasources";

    private PreferencesManager  preferencesManager;

    private DtoFactory          dtoFactory;

    @Inject
    public DatasourceManagerPrefImpl(final PreferencesManager prefManager,
                                     final DtoFactory dtoFactory) {
        this.preferencesManager = prefManager;
        this.dtoFactory = dtoFactory;
    }

    @Override
    public Iterator<DatabaseConfigurationDTO> getDatasources() {
        return getDatasourceConfigPreferences().getDatasources().values().iterator();
    }

    @Override
    public void add(final DatabaseConfigurationDTO configuration) {
        DatasourceConfigPreferences datasourcesPreferences = getDatasourceConfigPreferences();
        datasourcesPreferences.getDatasources().put(configuration.getDatasourceId(), configuration);
        saveDatasourceConfigPreferences(datasourcesPreferences);
    }

    @Override
    public void remove(final DatabaseConfigurationDTO configuration) {
        DatasourceConfigPreferences datasourcesPreferences = getDatasourceConfigPreferences();
        datasourcesPreferences.getDatasources().remove(configuration.getDatasourceId());
        saveDatasourceConfigPreferences(datasourcesPreferences);
    }

    @Override
    public DatabaseConfigurationDTO getByName(final String name) {
        DatasourceConfigPreferences datasourcesPreferences = getDatasourceConfigPreferences();
        return datasourcesPreferences.getDatasources().get(name);
    }

    @Override
    public void persist(final AsyncCallback<ProfileDescriptor> callback) {
        this.preferencesManager.flushPreferences(callback);
    }

    public Set<String> getNames() {
        DatasourceConfigPreferences datasourcesPreferences = getDatasourceConfigPreferences();
        return datasourcesPreferences.getDatasources().keySet();
    }

    private DatasourceConfigPreferences getDatasourceConfigPreferences() {
        String datasourcesJson = this.preferencesManager.getValue(PREFERENCE_KEY);
        DatasourceConfigPreferences datasourcesPreferences;
        if (datasourcesJson == null) {
            datasourcesPreferences = dtoFactory.createDto(DatasourceConfigPreferences.class);
        } else {
            try {
                datasourcesPreferences = dtoFactory.createDtoFromJson(datasourcesJson, DatasourceConfigPreferences.class);
            } catch (Exception e) {
                // temporary smoothly log and keep ide working https://jira.codenvycorp.com/browse/PLGDS-214
                Log.error(DatasourceManagerPrefImpl.class, e);
                datasourcesPreferences = dtoFactory.createDto(DatasourceConfigPreferences.class);
            }
        }
        return datasourcesPreferences;
    }

    private void saveDatasourceConfigPreferences(final DatasourceConfigPreferences datasourcesPreferences) {
        preferencesManager.setPreference(PREFERENCE_KEY, dtoFactory.toJson(datasourcesPreferences));
    }

    public String toString() {
        return "DatasourceManagerPrefImpl[" + preferencesManager.getValue(PREFERENCE_KEY) + "]";
    }

    @Override
    public Iterator<DatabaseConfigurationDTO> iterator() {
        return getDatasources();
    }
}
