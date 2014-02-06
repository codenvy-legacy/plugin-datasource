package com.codenvy.ide.ext.datasource.client.common;

import com.codenvy.ide.api.editor.CodenvyTextEditor;

public interface ReadableContentTextEditor extends CodenvyTextEditor {

    String getEditorContent();

    String getSelectedContent();
}
