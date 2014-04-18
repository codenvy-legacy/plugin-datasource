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
package com.codenvy.ide.ext.datasource.client.action;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.editdatasource.EditDatasourceMessages;
import com.codenvy.ide.ext.datasource.client.editdatasource.EditDatasourcesPresenter;
import com.codenvy.ide.ext.datasource.client.editdatasource.EditDatasourcesPresenterFactory;
import com.google.inject.Inject;

/**
 * IDE action to edit and delete datasources.
 * 
 * @author "Mickaël Leduque"
 */
public class EditDatasourcesAction extends Action {

    /** The factory to instanciate the dialg presenter. */
    private final EditDatasourcesPresenterFactory dialogFactory;

    @Inject
    public EditDatasourcesAction(@NotNull final EditDatasourcesPresenterFactory dialogFactory,
                                 @NotNull final EditDatasourceMessages messages,
                                 @NotNull DatasourceUiResources resources) {
        super(messages.editDatasourcesMenuText(), messages.editDatasourcesMenuDescription(), null,
              resources.manageDatasourceMenuIcon());
        this.dialogFactory = dialogFactory;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final EditDatasourcesPresenter dialogPresenter = this.dialogFactory.createEditDatasourcesPresenter();
        dialogPresenter.showDialog();
    }
}
