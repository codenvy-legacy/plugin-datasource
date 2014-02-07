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

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.collections.Array;
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

        void onConnectorSelected(int id);

        void onDatasourceNameModified(String datasourceName);
    }

    void setConnectors(Array<NewDatasourceConnector> connectors);

    String getDatasourceName();

    void selectConnector(int buttonIndex);

    void enableDbTypeButton(int buttonIndex);

    void disableAllDbTypeButton();
}
