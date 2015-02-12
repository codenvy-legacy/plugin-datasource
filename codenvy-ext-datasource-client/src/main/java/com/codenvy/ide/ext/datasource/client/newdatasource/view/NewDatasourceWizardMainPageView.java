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
package com.codenvy.ide.ext.datasource.client.newdatasource.view;

import java.util.Collection;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.google.inject.ImplementedBy;

@ImplementedBy(NewDatasourceWizardMainPageViewImpl.class)
public interface NewDatasourceWizardMainPageView extends View<NewDatasourceWizardMainPageView.ActionDelegate> {

    void reset();

    void setConnectors(Collection<NewDatasourceConnector> connectors);
    
    void selectConnector(NewDatasourceConnector data);

    /** Required for delegating functions in view. */
    public interface ActionDelegate {

        void onConnectorSelected(String id);

        void onCategorySelected();

        boolean connectorEnabled(String id);
    }
}
