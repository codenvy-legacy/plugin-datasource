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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import com.codenvy.ide.ext.datasource.client.editdatasource.wizard.EditDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.editdatasource.wizard.EditDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class NewDatasourceConnectorAgentImpl implements NewDatasourceConnectorAgent {

    private final NewDatasourceWizard               newDatasourceWizard;
    private final EditDatasourceWizard              editDatasourceWizard;

    private final SortedSet<NewDatasourceConnector> registeredConnectors;

    @Inject
    public NewDatasourceConnectorAgentImpl(final @NewDatasourceWizardQualifier NewDatasourceWizard newDatasourceWizard,
                                           final @EditDatasourceWizardQualifier EditDatasourceWizard editDatasourceWizard) {
        this.newDatasourceWizard = newDatasourceWizard;
        this.editDatasourceWizard = editDatasourceWizard;
        registeredConnectors = new TreeSet<NewDatasourceConnector>();
    }

    @Override
    public void register(final NewDatasourceConnector connector) {
        if (registeredConnectors.contains(connector)) {
            // TODO this shouldn't happen: notification error instead of the alert ?
            Window.alert("Datasource connector with " + connector.getId() + " id already exists");
            return;
        }

        registeredConnectors.add(connector);
        for (Provider< ? extends AbstractNewDatasourceConnectorPage> provider : connector.getWizardPages().asIterable()) {
            newDatasourceWizard.addPage(provider);
            editDatasourceWizard.addPage(provider);
        }
    }

    @Override
    public Collection<NewDatasourceConnector> getConnectors() {
        return registeredConnectors;
    }

    @Override
    public NewDatasourceConnector getConnector(final String id) {
        if (id == null) {
            return null;
        }
        for (final NewDatasourceConnector connector : registeredConnectors) {
            if (id.equals(connector.getId())) {
                return connector;
            }
        }
        return null;
    }
}
