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
