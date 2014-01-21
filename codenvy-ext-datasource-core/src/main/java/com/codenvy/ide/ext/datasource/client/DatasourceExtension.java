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

import static com.codenvy.ide.api.ui.action.Anchor.BEFORE;
import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_MAIN_MENU;
import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_WINDOW;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.Constraints;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.workspace.PartStackType;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerPartPresenter;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceAction;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardPagePresenter;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardQualifier;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.NewDatasourceConnectorAgent;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.mysql.MysqlDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.postgres.PostgresDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorAction;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Extension definition for the datasource plugin.
 */
@Singleton
@Extension(title = "Datasource Extension", version = "1.0.0")
public class DatasourceExtension {
    public static boolean SHOW_ITEM          = true;

    public static String  DS_GROUP_MAIN_MENU = "DatasourceMainMenu";

    @Inject
    public DatasourceExtension(WorkspaceAgent workspaceAgent,
                               DatasourceWelcomePresenter howToPresenter,
                               DatasourceExplorerPartPresenter dsExplorer,
                               ActionManager actionManager,
                               NewDatasourceAction newDSConnectionAction,
                               SqlEditorAction sqlEditorAction,
                               Provider<NewDatasourceWizardPagePresenter> newDatasourcePageProvider,
                               @NewDatasourceWizardQualifier DefaultWizard wizard,
                               NewDatasourceConnectorAgent connectorAgent,
                               Resources resources, Provider<PostgresDatasourceConnectorPage> pgConnectorPageProvider,
                               Provider<MysqlDatasourceConnectorPage> mysqlConnectorPageProvider) {
        workspaceAgent.openPart(howToPresenter, PartStackType.EDITING);
        workspaceAgent.openPart(dsExplorer, PartStackType.NAVIGATION);

        // create de "Datasource" menu in menubar and insert it
        DefaultActionGroup mainMenu = (DefaultActionGroup)actionManager
                                                                       .getAction(GROUP_MAIN_MENU);
        DefaultActionGroup defaultDatasourceMainGroup = new DefaultActionGroup(
                                                                               "Datasource", true, actionManager);
        actionManager.registerAction(DS_GROUP_MAIN_MENU,
                                     defaultDatasourceMainGroup);
        Constraints beforeWindow = new Constraints(BEFORE, GROUP_WINDOW);
        mainMenu.add(defaultDatasourceMainGroup, beforeWindow);

        // add submenu "New datasource" to Datasource menu
        actionManager.registerAction("NewDSConnection", newDSConnectionAction);
        defaultDatasourceMainGroup.add(newDSConnectionAction);

        wizard.addPage(newDatasourcePageProvider);

        // add submenu "SQL editor" to datasource menu
        actionManager.registerAction("SqlEditor", sqlEditorAction);
        defaultDatasourceMainGroup.add(sqlEditorAction);

        // add a new postgres connector
        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> pgWizardPages = Collections.createArray();
        pgWizardPages.add(pgConnectorPageProvider);
        connectorAgent.register(PostgresDatasourceConnectorPage.PG_DB_ID, "PostgreSQL", resources.getPostgreSqlLogo(), pgWizardPages);

        Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> mysqlWizardPages = Collections.createArray();
        mysqlWizardPages.add(mysqlConnectorPageProvider);
        connectorAgent.register(MysqlDatasourceConnectorPage.MYSQL_DB_ID, "MySQL", resources.getMySqlLogo(), mysqlWizardPages);
    }

}
