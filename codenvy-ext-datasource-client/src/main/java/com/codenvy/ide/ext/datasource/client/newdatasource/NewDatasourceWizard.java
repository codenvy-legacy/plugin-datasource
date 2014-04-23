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
package com.codenvy.ide.ext.datasource.client.newdatasource;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardContext;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NewDatasourceWizard extends DefaultWizard {
    public static final WizardContext.Key<NewDatasourceConnector> DATASOURCE_CONNECTOR =
                                                                                         new WizardContext.Key<NewDatasourceConnector>(
                                                                                                                                       "DatasourceConnector");
    public static final WizardContext.Key<String>                 DATASOURCE_NAME      =
                                                                                         new WizardContext.Key<String>(
                                                                                                                       "DatasourceName");

    @Inject
    public NewDatasourceWizard(NotificationManager notificationManager, NewDatasourceWizardMessages locale) {
        super(notificationManager, locale.newDatasource());
    }
}
