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
package com.codenvy.ide.ext.datasource.action;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardContext;
import com.codenvy.ide.ext.datasource.connector.DatasourceConnector;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NewDSConnectionWizard extends DefaultWizard {
    public static final WizardContext.Key<DatasourceConnector> DATASOURCE_CONNECTOR =
                                                                                      new WizardContext.Key<DatasourceConnector>(
                                                                                                                                 "DatasourceConnector");

    @Inject
    public NewDSConnectionWizard(NotificationManager notificationManager) {
        super(notificationManager, "New Datasource");
    }
}
