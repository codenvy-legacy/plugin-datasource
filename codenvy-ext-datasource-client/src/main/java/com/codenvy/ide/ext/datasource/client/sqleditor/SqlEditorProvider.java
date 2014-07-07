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

import com.codenvy.ide.api.editor.DocumentProvider;
import com.codenvy.ide.api.editor.EditorProvider;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoOracle;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SqlEditorProvider implements EditorProvider {

    private final DocumentProvider                    documentProvider;

    private final Provider<ReadableContentTextEditor> editorProvider;

    private final NotificationManager                 notificationManager;

    private final SqlEditorResources                  resource;

    private final DatabaseInfoOracle                  databaseInfoOracle;

    private final EditorDatasourceOracle              editorDatasourceOracle;

    @Inject
    public SqlEditorProvider(@NotNull final DocumentProvider documentProvider,
                             @NotNull final Provider<ReadableContentTextEditor> editorProvider,
                             @NotNull final NotificationManager notificationManager,
                             @NotNull final DatabaseInfoOracle databaseInfoOracle,
                             @NotNull final EditorDatasourceOracle editorDatasourceOracle,
                             @NotNull final SqlEditorResources resource) {
        this.documentProvider = documentProvider;
        this.editorProvider = editorProvider;
        this.notificationManager = notificationManager;
        this.databaseInfoOracle = databaseInfoOracle;
        this.editorDatasourceOracle = editorDatasourceOracle;
        this.resource = resource;
    }

    @Override
    public ReadableContentTextEditor getEditor() {
        Log.debug(SqlEditorProvider.class, "New instance of SQL editor requested.");
        ReadableContentTextEditor textEditor = editorProvider.get();
        textEditor.initialize(new SqlEditorConfiguration(textEditor, resource, databaseInfoOracle, editorDatasourceOracle),
                              documentProvider, notificationManager);
        return textEditor;
    }

}
