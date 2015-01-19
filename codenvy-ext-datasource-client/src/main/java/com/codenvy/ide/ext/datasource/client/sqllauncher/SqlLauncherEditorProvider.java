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
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.api.editor.EditorProvider;
import com.codenvy.ide.api.parts.WorkspaceAgent;
import com.codenvy.ide.jseditor.client.editoradapter.DefaultEditorAdapter;
import com.codenvy.ide.jseditor.client.texteditor.TextEditor;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SqlLauncherEditorProvider implements EditorProvider {

    private SqlRequestLauncherFactory sqlRequestLauncherFactory;

    private EventBus eventBus;

    private WorkspaceAgent workspaceAgent;

    @Inject
    public SqlLauncherEditorProvider(final SqlRequestLauncherFactory sqlRequestLauncherFactory, EventBus eventBus,
                                     WorkspaceAgent workspaceAgent) {
        this.sqlRequestLauncherFactory = sqlRequestLauncherFactory;
        this.eventBus = eventBus;
        this.workspaceAgent = workspaceAgent;
    }

    @Override
    public String getId() {
        return "sqlLauncher";
    }

    @Override
    public String getDescription() {
        return "SQL Launcher";
    }

    @Override
    public TextEditor getEditor() {
        Log.debug(SqlLauncherEditorProvider.class, "New instance of SQL launcher editor requested.");
        final SqlRequestLauncherPresenter launcher = sqlRequestLauncherFactory.createSqlRequestLauncherPresenter();
        final DefaultEditorAdapter adapter = new DefaultEditorAdapter(eventBus, workspaceAgent);
        adapter.setPresenter(launcher);
        adapter.setTextEditor(launcher.getTextEditor());
        return adapter;
    }
}
