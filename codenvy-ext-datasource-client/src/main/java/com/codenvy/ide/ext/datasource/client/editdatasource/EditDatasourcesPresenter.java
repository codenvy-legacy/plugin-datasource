/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.ext.datasource.client.editdatasource;

import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * The presenter for the datasource edit/delete datasources dialog.
 * 
 * @author "Mickaël Leduque"
 */
public class EditDatasourcesPresenter implements EditDatasourcesView.ActionDelegate {

    /** The view component. */
    private final EditDatasourcesView                        view;

    /** The component that stores datasources. */
    private final DatasourceManager                          datasourceManager;

    /** the datasource list model component. */
    private final ListDataProvider<DatabaseConfigurationDTO> dataProvider = new ListDataProvider<>();
    private final SelectionModel<DatabaseConfigurationDTO>   selectionModel;

    @Inject
    public EditDatasourcesPresenter(final EditDatasourcesView view,
                                    final DatasourceManager datasourceManager,
                                    final @Named(DatasourceKeyProvider.NAME) DatasourceKeyProvider keyProvider) {
        this.view = view;
        this.datasourceManager = datasourceManager;
        this.view.bindDatasourceModel(dataProvider);
        this.view.setDelegate(this);
        this.selectionModel = new MultiSelectionModel<>(keyProvider);
    }

    /** Show dialog. */
    public void showDialog() {
        setupDatasourceList();
        this.view.showDialog();
    }

    @Override
    public void closeDialog() {
        this.view.closeDialog();

    }

    /** Sets the content of the datasource widget. */
    private void setupDatasourceList() {
        this.dataProvider.getList().clear();
        for (DatabaseConfigurationDTO datasource : this.datasourceManager) {
            this.dataProvider.getList().add(datasource);
        }
        this.dataProvider.flush();
    }

    @Override
    public void deleteSelectedDatasources() {
    }

    @Override
    public void editSelectedDatasource() {
    }
}
