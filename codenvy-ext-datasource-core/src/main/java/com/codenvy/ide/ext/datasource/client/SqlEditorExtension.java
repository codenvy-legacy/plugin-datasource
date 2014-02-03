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

import static com.google.gwt.core.client.ScriptInjector.TOP_WINDOW;

import com.codenvy.ide.api.editor.EditorRegistry;
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.resources.FileType;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.wizard.newresource.NewResourceAgent;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorProvider;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlResourceProvider;
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
                              final SqlEditorProvider sqlEditorProvider,
                              final NewResourceAgent newResourceAgent,
                              final SqlResourceProvider sqlResourceProvider) {

        Log.info(SqlEditorExtension.class, "Initialization of SQL editor extension.");
        // inject sql parser
        ScriptInjector.fromString(sqlEditorResources.sqlParserJs().getText()).setWindow(TOP_WINDOW).inject();

        final FileType sqlFile = new FileType(sqlEditorResources.sqlFile(), GENERIC_SQL_MIME_TYPE, SQL_FILE_EXTENSION);
        resourceProvider.registerFileType(sqlFile);
        editorRegistry.register(sqlFile, sqlEditorProvider);
        newResourceAgent.register(sqlResourceProvider);
    }

}
