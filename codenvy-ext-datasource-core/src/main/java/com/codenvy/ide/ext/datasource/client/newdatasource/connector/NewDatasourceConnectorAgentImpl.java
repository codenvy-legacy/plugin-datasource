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

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.collections.StringMap;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class NewDatasourceConnectorAgentImpl implements NewDatasourceConnectorAgent {

    protected DefaultWizard                        datasourceWizard;

    protected final StringMap<NewDatasourceConnector> registeredConnectors;

    @Inject
    public NewDatasourceConnectorAgentImpl(@NewDatasourceWizardQualifier DefaultWizard datasourceWizard) {
        this.datasourceWizard = datasourceWizard;
        registeredConnectors = Collections.createStringMap();
    }

    @Override
    public void register(@NotNull String id,
                         @NotNull String title,
                         @Nullable ImageResource image,
                         @NotNull Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> wizardPages) {
        if (registeredConnectors.containsKey(id)) {
            // TODO this shouldn't happen: notification error instead of the alert ?
            Window.alert("Datasource connector with " + id + " id already exists");
            return;
        }
        NewDatasourceConnector connector = new NewDatasourceConnector(id, title, image);
        registeredConnectors.put(id, connector);
        for (Provider< ? extends AbstractNewDatasourceConnectorPage> provider : wizardPages.asIterable()) {
            datasourceWizard.addPage(provider);
        }

    }

    @Override
    public Array<NewDatasourceConnector> getConnectors() {
        return registeredConnectors.getValues();
    }

}
