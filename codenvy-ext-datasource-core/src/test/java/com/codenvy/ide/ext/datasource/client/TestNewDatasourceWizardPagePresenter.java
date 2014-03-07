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
