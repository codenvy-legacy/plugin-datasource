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
package com.codenvy.ide.ext.datasource.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPagePresenter;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPageView;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTestWithMockito;

@GwtModule("com.codenvy.ide.ext.datasource.Datasource")
public class TestNewDatasourceWizardPagePresenter extends GwtTestWithMockito {
    @Mock
    NewDatasourceWizardPageView      view;
    @Mock
    NewDatasourceConnectorAgent      connectorAgent;
    @Mock
    EventBus                         eventbus;
    @Mock
    AvailableJdbcDriversService      jdbcDrivers;


    NewDatasourceWizardPagePresenter presenter;

    @Before
    public void init() {
        NewDatasourceWizardMessages messages = GWT.<NewDatasourceWizardMessages> create(NewDatasourceWizardMessages.class);
        presenter = new NewDatasourceWizardPagePresenter(view, connectorAgent, jdbcDrivers, eventbus, messages);
    }

    @Test
    public void testGo() {
        AcceptsOneWidget container = Mockito.mock(AcceptsOneWidget.class);
        presenter.go(container);
        Mockito.verify(container).setWidget(view);
    }
}
