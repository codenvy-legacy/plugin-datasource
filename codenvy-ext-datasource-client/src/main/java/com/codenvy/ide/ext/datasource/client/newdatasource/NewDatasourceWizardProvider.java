/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.newdatasource;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class NewDatasourceWizardProvider implements Provider<NewDatasourceWizard> {
    private NewDatasourceWizardFactory  wizardFactory;
    private NewDatasourceWizardMessages messages;

    @Inject
    public NewDatasourceWizardProvider(final NewDatasourceWizardFactory wizardFactory, final NewDatasourceWizardMessages messages) {
        this.wizardFactory = wizardFactory;
        this.messages = messages;
    }

    /** {@inheritDoc} */
    @Override
    public NewDatasourceWizard get() {
        return wizardFactory.create(messages.newDatasource());
    }
}
