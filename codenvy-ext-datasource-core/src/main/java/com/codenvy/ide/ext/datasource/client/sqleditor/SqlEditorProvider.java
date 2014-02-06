package com.codenvy.ide.ext.datasource.client.sqleditor;

import com.codenvy.ide.api.editor.CodenvyTextEditor;
import com.codenvy.ide.api.editor.DocumentProvider;
import com.codenvy.ide.api.editor.EditorProvider;
import com.codenvy.ide.api.editor.TextEditorPartPresenter;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SqlEditorProvider implements EditorProvider {

    private final DocumentProvider            documentProvider;

    private final Provider<CodenvyTextEditor> editorProvider;

    private final NotificationManager         notificationManager;

    @Inject
    public SqlEditorProvider(final DocumentProvider documentProvider,
                             final Provider<CodenvyTextEditor> editorProvider,
                             final NotificationManager notificationManager) {
        this.documentProvider = documentProvider;
        this.editorProvider = editorProvider;
        this.notificationManager = notificationManager;
    }

    @Override
    public TextEditorPartPresenter getEditor() {
        Log.info(SqlEditorProvider.class, "New instance of SQL editor requested.");
        CodenvyTextEditor textEditor = editorProvider.get();
        textEditor.initialize(new SqlEditorConfiguration(), documentProvider, notificationManager);
        return textEditor;
    }

}
