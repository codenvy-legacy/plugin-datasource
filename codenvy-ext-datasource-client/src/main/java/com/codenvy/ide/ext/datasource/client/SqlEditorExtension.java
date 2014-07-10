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

import com.codenvy.ide.api.editor.EditorRegistry;
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.filetypes.FileType;
import com.codenvy.ide.api.filetypes.FileTypeRegistry;
import com.codenvy.ide.api.ui.Icon;
import com.codenvy.ide.api.ui.IconRegistry;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.ext.datasource.client.action.NewSqlFileAction;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlLauncherEditorProvider;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.core.client.ScriptInjector;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_FILE_NEW;
import static com.google.gwt.core.client.ScriptInjector.TOP_WINDOW;

/**
 * Extension definition for the sql editor.
 */
@Singleton
@Extension(title = "SQL Editor Extension", version = "1.0.0")
public class SqlEditorExtension {

    public static final String GENERIC_SQL_MIME_TYPE = "text/x-sql";
    public static final String ORACLE_SQL_MIME_TYPE  = "text/x-plsql";
    public static final String MYSQL_SQL_MIME_TYPE   = "text/x-mysql";
    public static final String MSSQL_SQL_MIME_TYPE   = "text/x-mssql";

    public static final String SQL_FILE_EXTENSION    = "sql";

    @Inject
    public SqlEditorExtension(final WorkspaceAgent workspaceAgent,
                              final ActionManager actionManager,
                              final SqlEditorResources sqlEditorResources,
                              final FileTypeRegistry fileTypeRegistry,
                              final EditorRegistry editorRegistry,
                              final SqlLauncherEditorProvider sqlEditorProvider,
                              final NewSqlFileAction newSqlFileAction,
                              final IconRegistry iconRegistry,
                              @Named("SQLFileType") final FileType sqlFile) {

        Log.debug(SqlEditorExtension.class, "Initialization of SQL editor extension.");
        // inject sql parser
        ScriptInjector.fromString(sqlEditorResources.sqlParserJs().getText()).setWindow(TOP_WINDOW).inject();
        // inject CSS
        sqlEditorResources.sqlCSS().ensureInjected();

        fileTypeRegistry.registerFileType(sqlFile);
        editorRegistry.register(sqlFile, sqlEditorProvider);

        // add action for creating new SQL file in "File-New" submenu
        DefaultActionGroup newGroup = (DefaultActionGroup)actionManager.getAction(GROUP_FILE_NEW);
        newGroup.addSeparator();
        actionManager.registerAction("NewSqlFileAction", newSqlFileAction);
        newGroup.add(newSqlFileAction);

        // register the sql file icon
        iconRegistry.registerIcon(new Icon("default.sqlfile.icon", "com/codenvy/ide/ext/datasource/client/sqleditor/sql-icon.png"));
    }

}
