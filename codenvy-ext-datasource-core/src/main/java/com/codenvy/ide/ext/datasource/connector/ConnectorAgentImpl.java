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
package com.codenvy.ide.ext.datasource.connector;

import com.codenvy.ide.annotations.NotNull;
import com.codenvy.ide.annotations.Nullable;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.collections.StringMap;
import com.codenvy.ide.ext.datasource.action.NewDatasourceWizard;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ConnectorAgentImpl implements ConnectorAgent {

    protected DefaultWizard                        datasourceWizard;

    protected final StringMap<DatasourceConnector> registeredConnectors;

    @Inject
    public ConnectorAgentImpl(@NewDatasourceWizard DefaultWizard datasourceWizard) {
        this.datasourceWizard = datasourceWizard;
        registeredConnectors = Collections.createStringMap();
    }

    @Override
    public void register(@NotNull String id,
                         @NotNull String title,
                         @Nullable ImageResource image,
                         @NotNull Array<Provider< ? extends AbstractConnectorPage>> wizardPages) {
        if (registeredConnectors.containsKey(id)) {
            // TODO this shouldn't happen: notification error instead of the alert ?
            Window.alert("Datasource connector with " + id + " id already exists");
            return;
        }
        DatasourceConnector connector = new DatasourceConnector(id, title, image);
        registeredConnectors.put(id, connector);
        for (Provider< ? extends AbstractConnectorPage> provider : wizardPages.asIterable()) {
            datasourceWizard.addPage(provider);
        }

    }

    @Override
    public Array<DatasourceConnector> getConnectors() {
        return registeredConnectors.getValues();
    }

}
