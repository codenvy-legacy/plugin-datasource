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
package com.codenvy.ide.ext.datasource.client.newdatasource;

import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.DefaultWizardFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class NewDatasourceWizardProvider implements Provider<DefaultWizard> {
    private DefaultWizardFactory        wizardFactory;
    private NewDatasourceWizardMessages messages;

    @Inject
    public NewDatasourceWizardProvider(DefaultWizardFactory wizardFactory, NewDatasourceWizardMessages messages) {
        this.wizardFactory = wizardFactory;
        this.messages = messages;
    }

    /** {@inheritDoc} */
    @Override
    public DefaultWizard get() {
        return wizardFactory.create(messages.newDatasource());
    }
}
