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
package com.codenvy.ide.ext.datasource.client.sqleditor;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.editor.EditorProvider;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoOracle;
import com.codenvy.ide.jseditor.client.defaulteditor.DefaultEditorProvider;
import com.codenvy.ide.jseditor.client.texteditor.ConfigurableTextEditor;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;

public class SqlEditorProvider implements EditorProvider {

    private final DefaultEditorProvider defaultEditorProvider;

    private final NotificationManager notificationManager;

    private final SqlEditorResources resource;

    private final DatabaseInfoOracle databaseInfoOracle;

    private final EditorDatasourceOracle editorDatasourceOracle;

    @Inject
    public SqlEditorProvider(@NotNull final DefaultEditorProvider defaultEditorProvider,
                             @NotNull final NotificationManager notificationManager,
                             @NotNull final DatabaseInfoOracle databaseInfoOracle,
                             @NotNull final EditorDatasourceOracle editorDatasourceOracle,
                             @NotNull final SqlEditorResources resource) {
        this.defaultEditorProvider = defaultEditorProvider;
        this.notificationManager = notificationManager;
        this.databaseInfoOracle = databaseInfoOracle;
        this.editorDatasourceOracle = editorDatasourceOracle;
        this.resource = resource;
    }

    @Override
    public String getId() {
        return "sqlEditor";
    }

    @Override
    public String getDescription() {
        return "SQL Editor";
    }

    @Override
    public ConfigurableTextEditor getEditor() {
        Log.debug(SqlEditorProvider.class, "New instance of SQL editor requested.");
        ConfigurableTextEditor textEditor = this.defaultEditorProvider.getEditor();
        textEditor.initialize(new SqlEditorConfiguration(textEditor, resource, databaseInfoOracle, editorDatasourceOracle),
                              notificationManager);
        return textEditor;
    }

}
