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

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.Constraints;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.keybinding.KeyBindingAgent;
import com.codenvy.ide.api.ui.keybinding.KeyBuilder;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.workspace.PartStackType;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.ext.datasource.client.action.EditDatasourcesAction;
import com.codenvy.ide.ext.datasource.client.common.CellTableResources;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerPartPresenter;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceAction;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPagePresenter;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.sqllauncher.ExecuteSqlAction;
import com.codenvy.ide.util.input.CharCodeWithModifiers;
import com.codenvy.ide.util.input.KeyCodeMap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import static com.codenvy.ide.api.ui.action.Anchor.BEFORE;
import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_MAIN_MENU;
import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_WINDOW;
import static com.codenvy.ide.ext.datasource.client.DatabaseCategoryType.CLOUD;
import static com.codenvy.ide.ext.datasource.client.DatabaseCategoryType.NOTCLOUD;

/**
 * Extension definition for the datasource plugin.
 */
@Singleton
@Extension(title = "Datasource Extension", version = "1.0.0")
public class DatasourceExtension {

    public static boolean SHOW_ITEM                  = true;
    public static String  DS_GROUP_MAIN_MENU         = "DatasourceMainMenu";

    private static String DS_ACTION_SHORTCUT_EXECUTE = "DatasourceActionExecute";

    @Inject
    public DatasourceExtension(WorkspaceAgent workspaceAgent,
                               DatasourceExplorerPartPresenter dsExplorer,
                               ActionManager actionManager,
                               NewDatasourceAction newDSConnectionAction,
                               Provider<NewDatasourceWizardPagePresenter> newDatasourcePageProvider,
                               @NewDatasourceWizardQualifier DefaultWizard wizard,
                               ConnectorsInitializer connectorsInitializer,
                               NewDatasourceConnectorAgent connectorAgent,
                               DatasourceUiResources resources,
                               CellTableResources celltableResources,
                               AvailableJdbcDriversService availableJdbcDrivers,
                               ExecuteSqlAction executeSqlAction,
                               EditDatasourcesAction editDatasourcesAction,
                               KeyBindingAgent keyBindingAgent) {

        workspaceAgent.openPart(dsExplorer, PartStackType.NAVIGATION);

        // create de "Datasource" menu in menubar and insert it
        DefaultActionGroup mainMenu = (DefaultActionGroup)actionManager.getAction(GROUP_MAIN_MENU);
        DefaultActionGroup defaultDatasourceMainGroup = new DefaultActionGroup("Datasource", true, actionManager);
        actionManager.registerAction(DS_GROUP_MAIN_MENU, defaultDatasourceMainGroup);
        Constraints beforeWindow = new Constraints(BEFORE, GROUP_WINDOW);
        mainMenu.add(defaultDatasourceMainGroup, beforeWindow);

        // add submenu "New datasource" to Datasource menu
        actionManager.registerAction("NewDSConnection", newDSConnectionAction);
        defaultDatasourceMainGroup.add(newDSConnectionAction);

        wizard.addPage(newDatasourcePageProvider);

        // do after adding new datasource page provider to keep page order
        connectorsInitializer.initConnectors();

        // add submenu "Edit datasource" to Datasource menu
        actionManager.registerAction("EditDSConnections", editDatasourcesAction);
        defaultDatasourceMainGroup.add(editDatasourcesAction);

        // fetching available drivers list from the server
        availableJdbcDrivers.fetch();

        // inject CSS
        resources.datasourceUiCSS().ensureInjected();
        celltableResources.cellTableStyle().ensureInjected();

        // Add execute shortcut
        actionManager.registerAction(DS_ACTION_SHORTCUT_EXECUTE, executeSqlAction);
        final CharCodeWithModifiers key = new KeyBuilder().action().charCode(KeyCodeMap.ENTER).build();
        keyBindingAgent.getGlobal().addKey(key, DS_ACTION_SHORTCUT_EXECUTE);
    }
}
