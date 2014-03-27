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

    String getDatasourceName();

    void selectConnector(String id);

    void enableDbTypeButton(String... id);

    void disableAllDbTypeButton();

}
