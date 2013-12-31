/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
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

    @Inject
    public NewDatasourceWizard(NotificationManager notificationManager) {
        super(notificationManager, "New Datasource");
    }
}
