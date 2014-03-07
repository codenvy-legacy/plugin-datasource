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
