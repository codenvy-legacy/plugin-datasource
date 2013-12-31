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

import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.DefaultWizardFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class NewDatasourceWizardProvider implements Provider<DefaultWizard> {
    private DefaultWizardFactory wizardFactory;

    @Inject
    public NewDatasourceWizardProvider(DefaultWizardFactory wizardFactory) {
        this.wizardFactory = wizardFactory;
    }

    /** {@inheritDoc} */
    @Override
    public DefaultWizard get() {
        return wizardFactory.create("New Datasource Wizard");
    }
}
