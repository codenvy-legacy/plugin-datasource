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

import com.google.inject.Inject;

/**
 * The presenter for the datasource edit/delete datasources dialog.
 * 
 * @author "Mickaël Leduque"
 */
public class EditDatasourcesPresenter implements EditDatasourcesView.ActionDelegate {

    /** The view component. */
    private EditDatasourcesView view;

    @Inject
    public EditDatasourcesPresenter(final EditDatasourcesView view) {
        this.view = view;
    }

    /** Show dialog. */
    public void showDialog() {
        this.view.showDialog();
    }

    @Override
    public void closeDialog() {
        this.view.closeDialog();

    }
}
