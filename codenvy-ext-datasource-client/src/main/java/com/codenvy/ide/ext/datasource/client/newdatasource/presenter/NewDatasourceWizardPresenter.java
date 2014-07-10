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
package com.codenvy.ide.ext.datasource.client.newdatasource.presenter;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.ui.wizard.Wizard;
import com.codenvy.ide.api.ui.wizard.WizardContext;
import com.codenvy.ide.api.ui.wizard.WizardDialog;
import com.codenvy.ide.api.ui.wizard.WizardPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardDialog;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnector;
import com.codenvy.ide.ext.datasource.client.newdatasource.view.NewDatasourceWizardHeadView;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class NewDatasourceWizardPresenter implements WizardDialog, InitializableWizardDialog<DatabaseConfigurationDTO>,
                                         Wizard.UpdateDelegate, NewDatasourceWizardHeadView.ActionDelegate {

    private NewDatasourceWizardHeadView          view;
    private NewDatasourceWizardMainPagePresenter categoriesListPage;
    private AbstractNewDatasourceConnectorPage   connectorPage;

    private WizardContext                        wizardContext;

    /** The new datasource wizard template. */
    private final NewDatasourceWizard            wizard;

    private DatabaseConfigurationDTO             configuration;

    @Inject
    public NewDatasourceWizardPresenter(@NotNull final @NewDatasourceWizardQualifier NewDatasourceWizard wizard,
                                        NewDatasourceWizardHeadView view,
                                        NewDatasourceWizardMainPagePresenter categoriesListPage) {
        this.wizard = wizard;
        this.view = view;
        this.categoriesListPage = categoriesListPage;
        wizardContext = new WizardContext();
        categoriesListPage.setContext(wizardContext);
        categoriesListPage.setUpdateDelegate(this);
        view.setDelegate(this);
    }

    @Override
    public void onSaveClicked() {
        final NewDatasourceConnector connector = wizardContext.getData(NewDatasourceWizard.DATASOURCE_CONNECTOR);
        if (connector != null && wizard != null) {
            wizard.onFinish();
            if (connectorPage != null) {
                connectorPage.commit(null);
            }
            view.cleanPage("settings");
            view.close();
            return;
        }
    }

    @Override
    public void onCancelClicked() {
        view.cleanPage("settings");
        view.close();
    }

    @Override
    public void updateControls() {
        view.cleanPage("settings");

        NewDatasourceConnector connector = wizardContext.getData(NewDatasourceWizard.DATASOURCE_CONNECTOR);
        if (connector != null) {
            for (Provider< ? extends AbstractNewDatasourceConnectorPage> provider : connector.getWizardPages().asIterable()) {
                connectorPage = provider.get();
                connectorPage.setContext(wizardContext);
                if (configuration != null) {
                    scheduleInit(connectorPage);
                }
                break;
            }
            view.showPage(connectorPage, "settings");
        }

        updateButtonsState();
    }

    private void updateButtonsState() {
        view.setFinishButtonEnabled((categoriesListPage != null && categoriesListPage.isCompleted()
                                     && connectorPage != null && connectorPage.isCompleted())
                                    && (wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME) != null)
                                    && (wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME).length() > 0));
    }

    @Override
    public void show() {
        wizardContext.clear();
        showFirstPage();
    }

    @Override
    public void datasourceNameChanged(String name) {
        RegExp regExp = RegExp.compile("^[A-Za-z0-9_]*$");
        if (regExp.test(name)) {
            wizardContext.putData(NewDatasourceWizard.DATASOURCE_NAME, name);
            view.removeNameError();
        } else {
            wizardContext.removeData(NewDatasourceWizard.DATASOURCE_NAME);
            view.showNameError();
        }
        updateButtonsState();
    }

    private void showFirstPage() {
        view.reset();

        view.showPage(categoriesListPage, "categories");
        view.showDialog();
        view.setEnabledAnimation(true);

        if (configuration != null) {
            String datasourceID = configuration.getDatasourceId();
            view.setName(datasourceID);
            datasourceNameChanged(datasourceID);
        }
    }

    @Override
    public void initData(DatabaseConfigurationDTO configuration) {
        this.configuration = configuration;
    }

    private void scheduleInit(final WizardPage page) {
        Command command = new Command() {

            @Override
            public void execute() {
                Log.info(NewDatasourceWizardPresenter.class, "Initializing wizard page : " + page.getClass());
                ((InitializableWizardPage)page).initPage(configuration);
                Log.info(NewDatasourceWizardPresenter.class, "Wizard page initialization done");
            }
        };
        Scheduler.get().scheduleDeferred(command);
    }
}
