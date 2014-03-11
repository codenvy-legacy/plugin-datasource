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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class NewDatasourceConnectorAgentImpl implements NewDatasourceConnectorAgent {

    private final DefaultWizard                     datasourceWizard;

    private final SortedSet<NewDatasourceConnector> registeredConnectors;

    @Inject
    public NewDatasourceConnectorAgentImpl(final @NewDatasourceWizardQualifier DefaultWizard datasourceWizard) {
        this.datasourceWizard = datasourceWizard;
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
            datasourceWizard.addPage(provider);
        }
    }

    @Override
    public void register(@NotNull String id,
                         int priority,
                         @NotNull String title,
                         @Nullable ImageResource image,
                         @NotNull DatabaseType databaseType,
                         @NotNull Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> wizardPages) {

        NewDatasourceConnector connector = new NewDatasourceConnector(id, priority, title, image, databaseType, wizardPages);
        register(connector);
    }

    @Override
    public Collection<NewDatasourceConnector> getConnectors() {
        return registeredConnectors;
    }
}
