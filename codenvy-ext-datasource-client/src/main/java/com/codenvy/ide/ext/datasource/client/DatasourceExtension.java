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
package com.codenvy.ide.ext.datasource.client;


import com.codenvy.ide.api.action.ActionManager;
import com.codenvy.ide.api.action.Constraints;
import com.codenvy.ide.api.action.DefaultActionGroup;
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.keybinding.KeyBindingAgent;
import com.codenvy.ide.api.keybinding.KeyBuilder;
import com.codenvy.ide.api.parts.PartStackType;
import com.codenvy.ide.api.parts.WorkspaceAgent;
import com.codenvy.ide.ext.datasource.client.action.EditDatasourcesAction;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerPartPresenter;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardAction;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.sqllauncher.ExecuteSqlAction;
import com.codenvy.ide.util.input.CharCodeWithModifiers;
import com.codenvy.ide.util.input.KeyCodeMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import static com.codenvy.ide.api.action.Anchor.BEFORE;
import static com.codenvy.ide.api.action.IdeActions.GROUP_MAIN_MENU;
import static com.codenvy.ide.api.action.IdeActions.GROUP_WINDOW;

/**
 * Extension definition for the datasource plugin.
 */
@Singleton
@Extension(title = "Datasource Extension", version = "1.0.0")
public class DatasourceExtension {

    public static boolean       SHOW_ITEM                  = true;
    public static final String  DS_GROUP_MAIN_MENU         = "DatasourceMainMenu";

    private static final String DS_ACTION_SHORTCUT_EXECUTE = "DatasourceActionExecute";

    @Inject
    public DatasourceExtension(WorkspaceAgent workspaceAgent,
                               DatasourceExplorerPartPresenter dsExplorer,
                               ActionManager actionManager,
                               NewDatasourceWizardAction newDSConnectionAction, 
                               ConnectorsInitializer connectorsInitializer,
                               NewDatasourceConnectorAgent connectorAgent,
                               DatasourceUiResources resources,
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

        // do after adding new datasource page provider to keep page order
        connectorsInitializer.initConnectors();

        // add submenu "Edit datasource" to Datasource menu
        actionManager.registerAction("EditDSConnections", editDatasourcesAction);
        defaultDatasourceMainGroup.add(editDatasourcesAction);

        // fetching available drivers list from the server
        availableJdbcDrivers.fetch();

        // inject CSS
        resources.datasourceUiCSS().ensureInjected();

        // Add execute shortcut
        actionManager.registerAction(DS_ACTION_SHORTCUT_EXECUTE, executeSqlAction);
        final CharCodeWithModifiers key = new KeyBuilder().action().charCode(KeyCodeMap.ENTER).build();
        keyBindingAgent.getGlobal().addKey(key, DS_ACTION_SHORTCUT_EXECUTE);
    }
}
