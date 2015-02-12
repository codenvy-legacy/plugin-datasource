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
package com.codenvy.ide.ext.datasource.client.newdatasource.presenter;

import com.codenvy.ide.api.wizard.Wizard;
import com.codenvy.ide.api.wizard.WizardPage;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardDialog;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardFactory;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorViewImpl;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.newdatasource.view.NewDatasourceWizardHeadView;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.TextDTO;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.codenvy.ide.ui.dialogs.DialogFactory;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.regexp.shared.RegExp;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nullable;

public class NewDatasourceWizardPresenter implements InitializableWizardDialog<DatabaseConfigurationDTO>,
                                                     Wizard.UpdateDelegate, NewDatasourceWizardHeadView.ActionDelegate,
                                                     NewDatasourceWizardMainPagePresenter.ConnectorSelectedListener {

    private final NewDatasourceWizardFactory           wizardFactory;
    private final DtoFactory                           dtoFactory;
    private final DialogFactory                        dialogFactory;
    private final DatasourceClientService              service;
    private final NewDatasourceConnectorAgent          newDatasourceConnectorAgent;
    private       NewDatasourceWizardHeadView          view;
    private       NewDatasourceWizardMainPagePresenter categoriesListPage;
    private       AbstractNewDatasourceConnectorPage   connectorPage;

    /** The new datasource wizard template. */
    private NewDatasourceWizard wizard;

    private DatabaseConfigurationDTO configuration;

    @Inject
    public NewDatasourceWizardPresenter(NewDatasourceWizardHeadView view,
                                        NewDatasourceWizardMainPagePresenter categoriesListPage,
                                        NewDatasourceWizardFactory wizardFactory,
                                        DtoFactory dtoFactory,
                                        DialogFactory dialogFactory,
                                        DatasourceClientService service,
                                        NewDatasourceConnectorAgent newDatasourceConnectorAgent) {
        this.view = view;
        this.categoriesListPage = categoriesListPage;
        this.categoriesListPage.setConnectorSelectedListener(this);
        this.wizardFactory = wizardFactory;
        this.dtoFactory = dtoFactory;
        this.dialogFactory = dialogFactory;
        this.service = service;
        this.newDatasourceConnectorAgent = newDatasourceConnectorAgent;
        this.view.setDelegate(this);
    }

    @Override
    public void onSaveClicked() {
        if (connectorPage.getView().isPasswordFieldDirty()) {
            try {
                service.encryptText(connectorPage.getView().getPassword(), new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                    @Override
                    protected void onSuccess(final String encryptedText) {
                        TextDTO encryptedTextDTO = dtoFactory.createDtoFromJson(encryptedText, TextDTO.class);
                        connectorPage.getView().setEncryptedPassword(encryptedTextDTO.getValue(), false);

                        completeWizard();
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        Log.error(DefaultNewDatasourceConnectorViewImpl.class, exception);
                    }
                });
            } catch (RequestException e2) {
                Log.error(DefaultNewDatasourceConnectorViewImpl.class, e2);
            }
        } else {
            completeWizard();
        }
    }

    private void completeWizard() {
        wizard.complete(new Wizard.CompleteCallback() {
            @Override
            public void onCompleted() {
                view.cleanPage("settings");
                view.close();
            }

            @Override
            public void onFailure(Throwable e) {
                dialogFactory.createMessageDialog("", e.getMessage(), null);
            }
        });
    }

    @Override
    public void onCancelClicked() {
        view.cleanPage("settings");
        view.close();
    }

    @Override
    public void updateControls() {
//        view.cleanPage("settings");

        updateButtonsState();
    }

    private void updateButtonsState() {
        String name = wizard.getContext().get(NewDatasourceWizard.DATASOURCE_NAME_KEY);
        view.setFinishButtonEnabled((categoriesListPage != null && categoriesListPage.isCompleted()
                                     && connectorPage != null && connectorPage.isCompleted())
                                    && (name != null) && (name.length() > 0));
    }

    public void show() {
        wizard = wizardFactory.create(configuration != null ? configuration : dtoFactory.createDto(DatabaseConfigurationDTO.class));
        wizard.setUpdateDelegate(this);
        wizard.addPage(categoriesListPage);

        showFirstPage();
    }

    @Override
    public void datasourceNameChanged(String name) {
        wizard.getDataObject().setDatasourceId(name);
        wizard.getContext().put(NewDatasourceWizard.DATASOURCE_NAME_KEY, name);

        RegExp regExp = RegExp.compile("^\\S.*$");
        if (regExp.test(name)) {
            view.removeNameError();
        } else {
            view.showNameError();
        }
        updateButtonsState();
    }

    private void showFirstPage() {
        view.reset();

        view.showPage(wizard.navigateToFirst(), "categories");
        view.showDialog();
        view.setEnabledAnimation(true);
        
        if (configuration != null) {
            String datasourceID = configuration.getDatasourceId();
            view.setName(datasourceID);
            datasourceNameChanged(datasourceID);
        } else {
            updateButtonsState();
        }
    }

    @Override
    public void onConnectorSelected(@Nullable String id) {
        if (id == null) {
            // category selected
            view.cleanPage("settings");
            return;
        }

        NewDatasourceConnector connector = newDatasourceConnectorAgent.getConnector(id);
        if (connector != null) {
            for (Provider<? extends AbstractNewDatasourceConnectorPage> provider : connector.getWizardPages().asIterable()) {
                connectorPage = provider.get();
                connectorPage.setContext(wizard.getContext());
                connectorPage.setUpdateDelegate(this);
                connectorPage.init(wizard.getDataObject());
                if (configuration != null) {
                    initPage(connectorPage);
                }
                break;
            }
            view.showPage(connectorPage, "settings");
        }
    }

    @Override
    public void initData(DatabaseConfigurationDTO configuration) {
        this.configuration = configuration;
    }

    private void initPage(final WizardPage page) {
        Log.info(NewDatasourceWizardPresenter.class, "Initializing wizard page : " + page.getClass());
        ((InitializableWizardPage)page).initPage(configuration);
        Log.info(NewDatasourceWizardPresenter.class, "Wizard page initialization done");
    }
}
