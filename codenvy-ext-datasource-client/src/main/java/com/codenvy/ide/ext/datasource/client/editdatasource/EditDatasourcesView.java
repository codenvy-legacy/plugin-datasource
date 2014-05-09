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

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SelectionModel;

/**
 * The view interface for the edit/delete datasource dialog.
 * 
 * @author "Mickaël Leduque"
 */
public interface EditDatasourcesView {

    /** Show the edit/delete datasource dialog. */
    void showDialog();

    /** Close the edit/delete datasource dialog. */
    void closeDialog();

    /** Binds the datasource widget to the datasource list model. */
    void bindDatasourceModel(AbstractDataProvider<DatabaseConfigurationDTO> provider);

    /** Bind the datasource widget to the datasource selection model. */
    void bindSelectionModel(SelectionModel<DatabaseConfigurationDTO> selectionModel);

    /** Sets the view's action delegate. */
    void setDelegate(ActionDelegate delegate);

    /** Enables/disbales the edit datasources button. */
    void setEditEnabled(boolean enabled);

    /** Enables/disbales the delete datasources button. */
    void setDeleteEnabled(boolean enabled);

    /**
     * Interface for this view's action delegate.
     * 
     * @author "Mickaël Leduque"
     */
    public interface ActionDelegate {
        /** The dialog must be closed. */
        void closeDialog();

        /** Datasource deletion requested. */
        void deleteSelectedDatasources();

        /** Datasource edition requested. */
        void editSelectedDatasource();

        /** Datasource creation requested. */
        void createDatasource();
    }
}
