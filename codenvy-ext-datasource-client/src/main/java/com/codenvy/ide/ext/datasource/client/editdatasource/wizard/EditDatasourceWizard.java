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
package com.codenvy.ide.ext.datasource.client.editdatasource.wizard;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.ext.datasource.client.events.DatasourceListChangeEvent;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

public class EditDatasourceWizard extends InitializableWizard<DatabaseConfigurationDTO> {

    private final EventBus eventBus;

    @Inject
    public EditDatasourceWizard(NotificationManager notificationManager,
                                final EventBus eventBus,
                                @Assisted String title) {
        super(notificationManager, title);
        this.eventBus = eventBus;
    }

    @Override
    public void onFinish() {
        super.onFinish();
        this.eventBus.fireEvent(new DatasourceListChangeEvent());
    }
}
