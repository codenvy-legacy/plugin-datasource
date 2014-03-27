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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
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
                         @NotNull String jdbcClassName,
                         @NotNull Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> wizardPages) {

        NewDatasourceConnector connector = new NewDatasourceConnector(id, priority, title, image, jdbcClassName, wizardPages);
        register(connector);
    }

    @Override
    public Collection<NewDatasourceConnector> getConnectors() {
        return registeredConnectors;
    }
}
