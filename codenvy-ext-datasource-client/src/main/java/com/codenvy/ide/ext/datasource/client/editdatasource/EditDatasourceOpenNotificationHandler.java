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
package com.codenvy.ide.ext.datasource.client.editdatasource;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.inject.Inject;

public class EditDatasourceOpenNotificationHandler implements Notification.OpenNotificationHandler {

    private final EditDatasourcesPresenterFactory dialogFactory;
    private DatabaseConfigurationDTO              configuration;

    @Inject
    public EditDatasourceOpenNotificationHandler(EditDatasourcesPresenterFactory dialogFactory) {
        super();
        this.dialogFactory = dialogFactory;
    }

    @Override
    public void onOpenClicked() {
        final EditDatasourcesPresenter dialogPresenter = this.dialogFactory.createEditDatasourcesPresenter();
        dialogPresenter.initData(this.configuration);
        dialogPresenter.showDialog();
    }

    public void setConfiguration(DatabaseConfigurationDTO configuration) {
        this.configuration = configuration;
    }
}
