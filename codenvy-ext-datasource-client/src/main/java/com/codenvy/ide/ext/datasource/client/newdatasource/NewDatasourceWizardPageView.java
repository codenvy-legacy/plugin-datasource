/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
package com.codenvy.ide.ext.datasource.client.newdatasource;

import java.util.Collection;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.google.inject.ImplementedBy;

/**
 * The view of {@link NewDatasourceWizardPagePresenter}.
 * 
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
@ImplementedBy(NewDatasourceWizardPageViewImpl.class)
public interface NewDatasourceWizardPageView extends View<NewDatasourceWizardPageView.ActionDelegate> {
    /** Required for delegating functions in view. */
    public interface ActionDelegate {

        void onConnectorSelected(String id);

        void onDatasourceNameModified(String datasourceName);
    }

    void setConnectors(Collection<NewDatasourceConnector> connectors);

    /**
     * Retrieve the content of the datasource name field.
     * 
     * @return the content of the widget
     */
    String getDatasourceName();

    /**
     * Set the content of the datasource name field.
     * 
     * @param datasourceName the new value
     */
    void setDatasourceName(String datasourceName);

    void selectConnector(String id);

    void enableDbTypeButton(String... id);

    void disableAllDbTypeButton();

}
