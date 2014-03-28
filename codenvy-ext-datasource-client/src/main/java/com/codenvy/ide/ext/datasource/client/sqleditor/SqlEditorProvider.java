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
package com.codenvy.ide.ext.datasource.client.sqleditor;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.editor.DocumentProvider;
import com.codenvy.ide.api.editor.EditorProvider;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoOracle;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
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
        Log.info(SqlEditorProvider.class, "New instance of SQL editor requested.");
        ReadableContentTextEditor textEditor = editorProvider.get();
        textEditor.initialize(new SqlEditorConfiguration(textEditor, resource, databaseInfoOracle, editorDatasourceOracle),
                              documentProvider, notificationManager);
        return textEditor;
    }

}
