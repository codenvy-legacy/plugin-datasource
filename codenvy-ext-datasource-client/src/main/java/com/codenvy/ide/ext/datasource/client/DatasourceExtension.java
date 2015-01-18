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
package com.codenvy.ide.ext.datasource.client;


import static com.codenvy.ide.api.action.IdeActions.GROUP_MAIN_MENU;
import static com.codenvy.ide.api.action.IdeActions.GROUP_WINDOW;
import static com.codenvy.ide.api.constraints.Anchor.BEFORE;

import com.codenvy.ide.api.action.ActionManager;
import com.codenvy.ide.api.action.DefaultActionGroup;
import com.codenvy.ide.api.constraints.Constraints;
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.keybinding.KeyBindingAgent;
import com.codenvy.ide.api.parts.PartStackType;
import com.codenvy.ide.api.parts.WorkspaceAgent;
import com.codenvy.ide.ext.datasource.client.action.EditDatasourcesAction;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerPartPresenter;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardAction;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Extension definition for the datasource plugin.
 */
@Singleton
@Extension(title = "Datasource", version = "1.0.0")
public class DatasourceExtension {

    public static boolean       SHOW_ITEM                  = true;
    public static final String  DS_GROUP_MAIN_MENU         = "datasourceMainMenu";

    @Inject
    public DatasourceExtension(WorkspaceAgent workspaceAgent,
                               DatasourceExplorerPartPresenter dsExplorer,
                               ActionManager actionManager,
                               NewDatasourceWizardAction newDSConnectionAction, 
                               ConnectorsInitializer connectorsInitializer,
                               NewDatasourceConnectorAgent connectorAgent,
                               DatasourceUiResources resources,
                               AvailableJdbcDriversService availableJdbcDrivers,
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
        actionManager.registerAction("newDSConnection", newDSConnectionAction);
        defaultDatasourceMainGroup.add(newDSConnectionAction);

        // do after adding new datasource page provider to keep page order
        connectorsInitializer.initConnectors();

        // add submenu "Edit datasource" to Datasource menu
        actionManager.registerAction("editDSConnections", editDatasourcesAction);
        defaultDatasourceMainGroup.add(editDatasourcesAction);

        // fetching available drivers list from the server
        availableJdbcDrivers.fetch();

        // inject CSS
        resources.datasourceUiCSS().ensureInjected();
    }
}
