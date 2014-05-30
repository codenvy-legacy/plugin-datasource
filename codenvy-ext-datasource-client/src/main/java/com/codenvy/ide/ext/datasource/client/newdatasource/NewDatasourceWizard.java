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
