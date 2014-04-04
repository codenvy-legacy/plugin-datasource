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

import static com.google.gwt.core.client.ScriptInjector.TOP_WINDOW;

import java.util.HashMap;
import java.util.Map;

import com.codenvy.ide.api.editor.EditorRegistry;
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.resources.FileType;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.IconRegistry;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.wizard.newresource.NewResourceAgent;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlResourceProvider;
import com.codenvy.ide.ext.datasource.client.sqllauncher.SqlLauncherEditorProvider;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.core.client.ScriptInjector;
import com.google.inject.Inject;
import com.google.inject.Singleton;

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
                              final ResourceProvider resourceProvider,
                              final EditorRegistry editorRegistry,
                              final SqlLauncherEditorProvider sqlEditorProvider,
                              final NewResourceAgent newResourceAgent,
                              final SqlResourceProvider sqlResourceProvider,
                              final IconRegistry iconRegistry) {

        Log.info(SqlEditorExtension.class, "Initialization of SQL editor extension.");
        // inject sql parser
        ScriptInjector.fromString(sqlEditorResources.sqlParserJs().getText()).setWindow(TOP_WINDOW).inject();
        // inject CSS
        sqlEditorResources.sqlCSS().ensureInjected();

        final FileType sqlFile = new FileType(sqlEditorResources.sqlFile(), GENERIC_SQL_MIME_TYPE, SQL_FILE_EXTENSION);
        resourceProvider.registerFileType(sqlFile);
        editorRegistry.register(sqlFile, sqlEditorProvider);
        newResourceAgent.register(sqlResourceProvider);

        // register the sql file icon
        iconRegistry.registerIcon("default.sqlfile.icon", "com/codenvy/ide/ext/datasource/client/sqleditor/sql-icon.png");
    }

}
