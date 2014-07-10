/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.inject;


import com.codenvy.ide.api.extension.ExtensionGinModule;
import com.codenvy.ide.api.filetypes.FileType;
import com.codenvy.ide.api.ui.preferences.PreferencesPagePresenter;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.ext.datasource.client.AvailableJdbcDriversService;
import com.codenvy.ide.ext.datasource.client.AvailableJdbcDriversServiceRestImpl;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceClientServiceImpl;
import com.codenvy.ide.ext.datasource.client.SqlEditorExtension;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditorPresenter;
import com.codenvy.ide.ext.datasource.client.common.interaction.DialogFactory;
import com.codenvy.ide.ext.datasource.client.common.interaction.confirm.ConfirmWindow;
import com.codenvy.ide.ext.datasource.client.common.interaction.confirm.ConfirmWindowFooter;
import com.codenvy.ide.ext.datasource.client.common.interaction.confirm.ConfirmWindowPresenter;
import com.codenvy.ide.ext.datasource.client.common.interaction.confirm.ConfirmWindowView;
import com.codenvy.ide.ext.datasource.client.common.interaction.confirm.ConfirmWindowViewImpl;
import com.codenvy.ide.ext.datasource.client.common.interaction.message.MessageWindow;
import com.codenvy.ide.ext.datasource.client.common.interaction.message.MessageWindowFooter;
import com.codenvy.ide.ext.datasource.client.common.interaction.message.MessageWindowPresenter;
import com.codenvy.ide.ext.datasource.client.common.interaction.message.MessageWindowView;
import com.codenvy.ide.ext.datasource.client.common.interaction.message.MessageWindowViewImpl;
import com.codenvy.ide.ext.datasource.client.editdatasource.EditDatasourcesPresenterFactory;
import com.codenvy.ide.ext.datasource.client.editdatasource.EditDatasourcesView;
import com.codenvy.ide.ext.datasource.client.editdatasource.EditDatasourcesViewImpl;
import com.codenvy.ide.ext.datasource.client.editdatasource.celllist.DatasourceCell;
import com.codenvy.ide.ext.datasource.client.editdatasource.celllist.DatasourceKeyProvider;
import com.codenvy.ide.ext.datasource.client.editdatasource.wizard.EditDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.editdatasource.wizard.EditDatasourceWizardFactory;
import com.codenvy.ide.ext.datasource.client.editdatasource.wizard.EditDatasourceWizardProvider;
import com.codenvy.ide.ext.datasource.client.editdatasource.wizard.EditDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTORenderer.Resources;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerView;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPageView;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPageViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardProvider;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgentImpl;
import com.codenvy.ide.ext.datasource.client.properties.DataEntityPropertiesView;
import com.codenvy.ide.ext.datasource.client.properties.DataEntityPropertiesViewImpl;
import com.codenvy.ide.ext.datasource.client.service.FetchMetadataService;
import com.codenvy.ide.ext.datasource.client.service.FetchMetadataServiceImpl;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracleImpl;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.ext.datasource.client.sqllauncher.RequestResultHeader;
import com.codenvy.ide.ext.datasource.client.sqllauncher.RequestResultHeaderFactory;
import com.codenvy.ide.ext.datasource.client.sqllauncher.RequestResultHeaderImpl;
import com.codenvy.ide.ext.datasource.client.sqllauncher.ResultItemBoxFactory;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlRequestLauncherFactory;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlRequestLauncherView;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlRequestLauncherViewImpl;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreClientService;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreClientServiceImpl;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreManagerPresenter;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreManagerView;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreManagerViewImpl;
import com.codenvy.ide.ext.datasource.client.ssl.upload.UploadSslKeyDialogView;
import com.codenvy.ide.ext.datasource.client.ssl.upload.UploadSslKeyDialogViewImpl;
import com.codenvy.ide.ext.datasource.client.ssl.upload.UploadSslTrustCertDialogView;
import com.codenvy.ide.ext.datasource.client.ssl.upload.UploadSslTrustCertDialogViewImpl;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

@ExtensionGinModule
public class DatasourceGinModule extends AbstractGinModule {

    /** The name bound to the datasource rest context. */
    public static final String DATASOURCE_CONTEXT_NAME = "datasourceRestContext";

    /** {@inheritDoc} */
    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named(DATASOURCE_CONTEXT_NAME)).to("/datasource");

        bind(DatasourceExplorerView.class).to(DatasourceExplorerViewImpl.class)
                                          .in(Singleton.class);
        bind(DatasourceClientService.class).to(DatasourceClientServiceImpl.class)
                                           .in(Singleton.class);

        install(new GinFactoryModuleBuilder().build(EditDatasourceWizardFactory.class));

        bind(DefaultWizard.class).annotatedWith(NewDatasourceWizardQualifier.class)
                                 .toProvider(NewDatasourceWizardProvider.class)
                                 .in(Singleton.class);

        bind(EditDatasourceWizard.class).annotatedWith(EditDatasourceWizardQualifier.class)
                                        .toProvider(EditDatasourceWizardProvider.class)
                                        .in(Singleton.class);

        bind(NewDatasourceConnectorAgent.class).to(NewDatasourceConnectorAgentImpl.class).in(Singleton.class);
        bind(NewDatasourceWizardPageView.class).to(NewDatasourceWizardPageViewImpl.class);
        bind(DefaultNewDatasourceConnectorView.class).to(DefaultNewDatasourceConnectorViewImpl.class);

        bind(DataEntityPropertiesView.class).to(DataEntityPropertiesViewImpl.class);

        bind(SqlRequestLauncherView.class).to(SqlRequestLauncherViewImpl.class);

        install(new GinFactoryModuleBuilder()
                                             .implement(SqlRequestLauncherView.class, SqlRequestLauncherViewImpl.class)
                                             .build(SqlRequestLauncherFactory.class));

        bind(ReadableContentTextEditor.class).to(ReadableContentTextEditorPresenter.class);

        bind(AvailableJdbcDriversService.class).to(AvailableJdbcDriversServiceRestImpl.class).in(Singleton.class);

        bind(FetchMetadataService.class).to(FetchMetadataServiceImpl.class).in(Singleton.class);

        bind(EditorDatasourceOracle.class).to(EditorDatasourceOracleImpl.class).in(Singleton.class);

        bind(Resources.class).in(Singleton.class);
        bind(com.codenvy.ide.Resources.class).to(Resources.class).in(Singleton.class);

        // Add and bind ssl keystore manager preference page and views
        GinMultibinder<PreferencesPagePresenter> prefBinder = GinMultibinder.newSetBinder(binder(), PreferencesPagePresenter.class);
        prefBinder.addBinding().to(SslKeyStoreManagerPresenter.class);
        bind(SslKeyStoreClientService.class).to(SslKeyStoreClientServiceImpl.class).in(Singleton.class);
        bind(SslKeyStoreManagerView.class).to(SslKeyStoreManagerViewImpl.class).in(Singleton.class);
        bind(UploadSslKeyDialogView.class).to(UploadSslKeyDialogViewImpl.class).in(Singleton.class);
        bind(UploadSslTrustCertDialogView.class).to(UploadSslTrustCertDialogViewImpl.class).in(Singleton.class);

        bind(EditDatasourcesView.class).to(EditDatasourcesViewImpl.class);
        install(new GinFactoryModuleBuilder().build(EditDatasourcesPresenterFactory.class));
        bind(DatasourceKeyProvider.class).annotatedWith(Names.named(DatasourceKeyProvider.NAME))
                                         .to(DatasourceKeyProvider.class)
                                         .in(Singleton.class);
        bind(DatasourceCell.class).in(Singleton.class);

        /* factories for the result zone */
        install(new GinFactoryModuleBuilder().build(ResultItemBoxFactory.class));
        install(new GinFactoryModuleBuilder().implement(RequestResultHeader.class, RequestResultHeaderImpl.class)
                                             .build(RequestResultHeaderFactory.class));

        /* confirmation window */
        bind(ConfirmWindowFooter.class);
        bind(ConfirmWindowView.class).to(ConfirmWindowViewImpl.class);
        /* message window */
        bind(MessageWindowFooter.class);
        bind(MessageWindowView.class).to(MessageWindowViewImpl.class);
        /* factory for these dialogs. */
        install(new GinFactoryModuleBuilder().implement(ConfirmWindow.class, ConfirmWindowPresenter.class)
                                             .implement(MessageWindow.class, MessageWindowPresenter.class)
                                             .build(DialogFactory.class));

    }

    @Provides
    @Singleton
    @Named("SQLFileType")
    protected FileType provideSQLFile(SqlEditorResources sqlEditorResources) {
        return  new FileType(sqlEditorResources.sqlFile(), SqlEditorExtension.GENERIC_SQL_MIME_TYPE, SqlEditorExtension.SQL_FILE_EXTENSION);
    }

}
