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
package com.codenvy.ide.ext.datasource.client.inject;

import com.codenvy.ide.api.extension.ExtensionGinModule;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceClientServiceImpl;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.DatasourceManagerPrefImpl;
import com.codenvy.ide.ext.datasource.client.DatasourceWelcomeView;
import com.codenvy.ide.ext.datasource.client.DatasourceWelcomeViewImpl;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditorPresenter;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerView;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPageView;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPageViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardProvider;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.JdbcDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.JdbcDatasourceConnectorViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgentImpl;
import com.codenvy.ide.ext.datasource.client.properties.DataEntityPropertiesView;
import com.codenvy.ide.ext.datasource.client.properties.DataEntityPropertiesViewImpl;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlResourceProvider;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlRequestLauncherFactory;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlRequestLauncherView;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlRequestLauncherViewImpl;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;

@ExtensionGinModule
public class DatasourceGinModule extends AbstractGinModule {
    /** {@inheritDoc} */
    @Override
    protected void configure() {
        bind(DatasourceWelcomeView.class).to(DatasourceWelcomeViewImpl.class)
                                         .in(Singleton.class);
        bind(DatasourceExplorerView.class).to(DatasourceExplorerViewImpl.class)
                                          .in(Singleton.class);
        bind(DatasourceClientService.class).to(DatasourceClientServiceImpl.class)
                                           .in(Singleton.class);
        bind(DefaultWizard.class).annotatedWith(NewDatasourceWizardQualifier.class)
                                 .toProvider(NewDatasourceWizardProvider.class)
                                 .in(Singleton.class);
        bind(NewDatasourceConnectorAgent.class).to(NewDatasourceConnectorAgentImpl.class).in(Singleton.class);
        bind(NewDatasourceWizardPageView.class).to(NewDatasourceWizardPageViewImpl.class);
        bind(JdbcDatasourceConnectorView.class).to(JdbcDatasourceConnectorViewImpl.class);

        bind(DatasourceManager.class).to(DatasourceManagerPrefImpl.class).in(Singleton.class);

        bind(DataEntityPropertiesView.class).to(DataEntityPropertiesViewImpl.class);

        bind(SqlRequestLauncherView.class).to(SqlRequestLauncherViewImpl.class);

        install(new GinFactoryModuleBuilder()
                                             .implement(SqlRequestLauncherView.class, SqlRequestLauncherViewImpl.class)
                                             .build(SqlRequestLauncherFactory.class));

        bind(SqlResourceProvider.class);

        bind(ReadableContentTextEditor.class).to(ReadableContentTextEditorPresenter.class);
    }
}
